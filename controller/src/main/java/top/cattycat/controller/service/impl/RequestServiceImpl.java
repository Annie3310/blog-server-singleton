package top.cattycat.controller.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.cattycat.common.config.BlogConfig;
import top.cattycat.common.pojo.Converter;
import top.cattycat.common.pojo.dto.LabelsForArticlesDTO;
import top.cattycat.common.pojo.dto.SearchDTO;
import top.cattycat.common.pojo.entity.Article;
import top.cattycat.common.pojo.entity.Blog;
import top.cattycat.common.pojo.entity.Label;
import top.cattycat.common.pojo.entity.LabelsForArticles;
import top.cattycat.common.pojo.request.BlogSearchRequest;
import top.cattycat.common.pojo.request.PageParam;
import top.cattycat.common.pojo.request.SetCoverRequest;
import top.cattycat.common.pojo.vo.BlogVO;
import top.cattycat.common.pojo.vo.LabelVO;
import top.cattycat.common.pojo.vo.SearchVO;
import top.cattycat.common.util.JwtUtils;
import top.cattycat.controller.constant.Constant;
import top.cattycat.controller.service.RequestService;
import top.cattycat.service.impl.ArticleServiceImpl;
import top.cattycat.service.impl.BlogServiceImpl;
import top.cattycat.service.impl.LabelServiceImpl;
import top.cattycat.service.impl.LabelsForArticlesServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 王金义
 * @date 2021/11/9
 */
@Service
public class RequestServiceImpl implements RequestService {
    private final BlogConfig blogConfig;

    private final static String BLOG_STATE_CLOSED = "closed";
    private final static String BLOG_STATE_OPEN = "open";
    private final static String SEARCH_URL = "https://api.github.com/search/issues?q=repo:Annie3310/blog+author:Annie3310+%s in:title,body&per_page=%d&page=%d&order=asc";

    private final BlogServiceImpl blogService;
    private final ArticleServiceImpl articleService;
    private final LabelServiceImpl labelService;
    private final LabelsForArticlesServiceImpl labelsForArticlesService;
    private final RestTemplate restTemplate;
    private final HttpServletRequest request;
    private final RedisTemplate<String, String> redisTemplate;

    public RequestServiceImpl(BlogConfig blogConfig, BlogServiceImpl blogService, ArticleServiceImpl articleService, LabelServiceImpl labelService, LabelsForArticlesServiceImpl labelsForArticlesService, RestTemplate restTemplate, HttpServletRequest request, RedisTemplate<String, String> redisTemplate) {
        this.blogConfig = blogConfig;
        this.blogService = blogService;
        this.articleService = articleService;
        this.labelService = labelService;
        this.labelsForArticlesService = labelsForArticlesService;
        this.restTemplate = restTemplate;
        this.request = request;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<BlogVO> listBlogs(PageParam page) {
        return this.listBlogs(page, BLOG_STATE_OPEN);
    }

    @Override
    public BlogVO getBlog(String number) {
        // 通过 number 获取博客
        final Blog blog = this.getBlogMapper(number);
        if (Objects.isNull(blog)) {
            return null;
        }

        // 获取博文
        final Article article = this.getArticleMapper(number);

        // 组装
        final BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);
        blogVO.setBody(article.getBody());
        blogVO.setToc(article.getToc());

        final LabelsForArticlesDTO lfa = this.getLfaMapper(number);

        // 如果有标签则加入标签关系
        if (Objects.nonNull(lfa)) {
            final List<Label> labels = this.getLabelsMapper(lfa.getLId());
            final List<LabelVO> labelVOs = labels.stream().map(Converter::labelVO).collect(Collectors.toList());
            blogVO.setLabels(labelVOs);
        }
        return blogVO;
    }

    @Override
    public List<LabelVO> listLabels() {
        final Map<Long, Label> labelMap = this.listLabelsMapper();

        if (labelMap.isEmpty()) {
            return null;
        }

        return labelMap.values().stream().map(Converter::labelVO).collect(Collectors.toList());
    }

    @Override
    public List<LabelVO> listLabelsForBlog(String number) {
        final LabelsForArticlesDTO lfa = this.getLfaMapper(number);
        if (Objects.isNull(lfa)) {
            return new ArrayList<>();
        }
        final List<Label> labels = this.getLabelsMapper(lfa.getLId());
        return labels.stream().map(Converter::labelVO).collect(Collectors.toList());
    }

    @Override
    public List<BlogVO> listBlogsByLabel(Long id, PageParam page) {
        final List<String> numbers = this.listNumbersByLabelId(id);

        if (numbers.isEmpty()) {
            return null;
        }

        final List<Blog> blogs = this.listBlogsMapper(new Page<>(page.getPage(), page.getLimit()), numbers);

        final Map<String, List<Long>> lfaMap = this.listLfaMapper(numbers);

        return this.listBlogs(blogs, lfaMap);
    }

    @Override
    public LabelVO getLabelById(Long id) {
        return this.getLabelMapper(id);
    }

    @Override
    public List<BlogVO> listArchive(PageParam page) {
        return this.listBlogs(page, BLOG_STATE_CLOSED);
    }

    @Override
    public List<SearchVO> search(BlogSearchRequest request) {
        final String formattedUrl = String.format(SEARCH_URL, request.getKeyword(), request.getLimit(), request.getPage());
        final HttpHeaders headers = new HttpHeaders();
        final String authorization = this.request.getHeader("Authorization");
        final String id = JwtUtils.parseToken(authorization).get("id", String.class);
        final String accessToken = String.valueOf(this.redisTemplate.opsForValue().get(String.format(Constant.USER_ACCESS_TOKEN_TEMPLATE, id)));

        headers.add("Authorization", accessToken);
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final SearchDTO result = this.restTemplate.exchange(formattedUrl, HttpMethod.GET, httpEntity, SearchDTO.class).getBody();
        if (Objects.isNull(result)) {
            return null;
        }
        final List<SearchVO> items = result.getItems();
        if (Objects.isNull(items)) {
            return null;
        }

        final List<String> numbersList = items.stream().map(SearchVO::getNumber).collect(Collectors.toList());
        final Map<String, String> covers = this.getCovers(numbersList);
        items.forEach(o -> {
            final List<LabelVO> labelVOS = this.setWellNumber(Converter.setFontColor(o.getLabels()));
            o.setLabels(labelVOS);
            final String cover = covers.get(o.getNumber());
            if (StringUtils.isEmpty(cover)) {
                o.setCover(null);
            } else {
                o.setCover(cover);
            }
        });
        return items;
    }

    @Override
    public Boolean setCover(SetCoverRequest request) {
        return this.setCoverMapper(request);
    }

    private List<BlogVO> listBlogs(PageParam page, String state) {
        final PageParam pageParam = Optional.ofNullable(page).orElseGet(() -> new PageParam(1, this.blogConfig.getPage().getLimit()));

        // 获取博客
        final List<Blog> blogs = this.listBlogsMapper(new Page<>(pageParam.getPage(), pageParam.getLimit()), state);
        if (CollectionUtils.isEmpty(blogs)) {
            return new ArrayList<>();
        }
        // 获取标签博客映射关系
        final Map<String, List<Long>> lfaMap = this.listLfaMapper(null);

        return this.listBlogs(blogs, lfaMap);
    }

    private List<Blog> listBlogsMapper(Page<Blog> page, String state) {
        final LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .select(Blog::getNumber, Blog::getTitle, Blog::getCreatedAt, Blog::getCover)
                .eq(Blog::getState, state)
                .orderByDesc(Blog::getCreatedAt);
        return this.blogService.page(page, wrapper).getRecords();
    }

    private List<Blog> listBlogsMapper(Page<Blog> page, List<String> numbers) {
        final LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .select(Blog::getNumber, Blog::getTitle, Blog::getCreatedAt, Blog::getCover)
                .in(Blog::getNumber, numbers);
        return this.blogService.page(page, wrapper).getRecords();
    }

    private Map<String, List<Long>> listLfaMapper(List<String> numbers) {
        final List<LabelsForArticlesDTO> labelsForArticlesDTOS = this.labelsForArticlesService.getBaseMapper().listLfa(numbers);
        return labelsForArticlesDTOS.stream().collect(Collectors.toMap(LabelsForArticlesDTO::getBNumber, LabelsForArticlesDTO::getLId, (origin, replacement) -> replacement));
    }

    private Map<Long, Label> listLabelsMapper() {
        final LambdaQueryWrapper<Label> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Label::getId, Label::getName, Label::getColor, Label::getDescription);
        return this.labelService.list(wrapper).stream().collect(Collectors.toMap(Label::getId, Function.identity(), (origin, replacement) -> replacement, LinkedHashMap::new));
    }

    private Blog getBlogMapper(String number) {
        final LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .select(Blog::getNumber, Blog::getTitle, Blog::getCreatedAt, Blog::getUpdatedAt, Blog::getCover, Blog::getState)
                .eq(Blog::getNumber, number);
        return this.blogService.getOne(wrapper);
    }

    private Article getArticleMapper(String number) {
        final LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .select(Article::getBNumber, Article::getBody, Article::getToc)
                .eq(Article::getBNumber, number);
        return this.articleService.getOne(wrapper);
    }

    private LabelsForArticlesDTO getLfaMapper(String number) {
        final List<String> numberWrapper = Collections.singletonList(number);
        final List<LabelsForArticlesDTO> labelsForArticlesDTOS = this.labelsForArticlesService.getBaseMapper().listLfa(numberWrapper);
        if (CollectionUtils.isNotEmpty(labelsForArticlesDTOS)) {
            return labelsForArticlesDTOS.get(0);
        }
        return null;
    }

    private List<Label> getLabelsMapper(List<Long> lIds) {
        final LambdaQueryWrapper<Label> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .select(Label::getId, Label::getName, Label::getColor, Label::getDescription)
                .in(Label::getId, lIds);
        return this.labelService.list(wrapper);
    }

    private LabelVO getLabelMapper(Long id) {
        final LambdaQueryWrapper<Label> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Label::getId, Label::getName, Label::getColor, Label::getDescription).eq(Label::getId, id);
        return Converter.labelVO(this.labelService.getOne(wrapper));
    }

    private List<String> listNumbersByLabelId(Long id) {
        final LambdaQueryWrapper<LabelsForArticles> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(LabelsForArticles::getBNumber).eq(LabelsForArticles::getLId, id);
        final List<LabelsForArticles> labelsForArticlesList = this.labelsForArticlesService.list(wrapper);
        return labelsForArticlesList.stream().map(LabelsForArticles::getBNumber).collect(Collectors.toList());
    }

    /**
     * 使用获取到的博客列表和标签关系组装成列表返回
     *
     * @param blogs  博客列表
     * @param lfaMap 博客标签关联关系
     * @return 组装了标签的博客列表
     */
    private List<BlogVO> listBlogs(List<Blog> blogs, Map<String, List<Long>> lfaMap) {
        return blogs.stream().map(o -> {
            final String number = o.getNumber();
            final BlogVO blogVO = new BlogVO();
            BeanUtils.copyProperties(o, blogVO);

            final List<Long> lfaLIds = lfaMap.get(number);
            if (CollectionUtils.isNotEmpty(lfaMap.get(number))) {
                final List<LabelVO> labelVOList = lfaLIds.stream().map(this::getLabelMapper).collect(Collectors.toList());
                blogVO.setLabels(labelVOList);
            }
            return blogVO;
        }).collect(Collectors.toList());
    }

    /**
     * 设置井号
     */
    private List<LabelVO> setWellNumber(List<LabelVO> labelVOS) {
        for (LabelVO labelVO : labelVOS) {
            labelVO.setColor("#" + labelVO.getColor());
        }
        return labelVOS;
    }

    private Map<String, String> getCovers(List<String> numbers) {
        final LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Blog::getNumber, Blog::getCover).in(Blog::getNumber, numbers);
        final List<Blog> blogListWithCover = this.blogService.list(wrapper);
        return blogListWithCover.stream().collect(Collectors.toMap(Blog::getNumber, v -> Optional.ofNullable(v.getCover()).orElse(""), (origin, replacement) -> replacement));
    }

    private boolean setCoverMapper(SetCoverRequest request) {
        final String number = request.getNumber();
        final String src = request.getSrc();
        final LambdaUpdateWrapper<Blog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Blog::getCover, src).eq(Blog::getNumber, number);

        return this.blogService.update(wrapper);
    }
}
