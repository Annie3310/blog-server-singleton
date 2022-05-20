package top.cattycat.controller.controller;

import org.springframework.web.bind.annotation.*;
import top.cattycat.common.config.BlogConfig;
import top.cattycat.common.enums.ExceptionEnum;
import top.cattycat.common.enums.ResponseEnum;
import top.cattycat.common.pojo.request.BlogSearchRequest;
import top.cattycat.common.pojo.request.PageParam;
import top.cattycat.common.pojo.request.SetCoverRequest;
import top.cattycat.common.pojo.response.ResponseResult;
import top.cattycat.common.pojo.vo.BlogVO;
import top.cattycat.common.pojo.vo.LabelVO;
import top.cattycat.common.pojo.vo.SearchVO;
import top.cattycat.controller.service.RequestService;
import top.cattycat.controller.service.impl.RequestServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 博客后端接口
 *
 * @author 王金义
 * @date 2021/8/30
 */
@RestController
public class BlogController {
    private final RequestService service;
    private final BlogConfig blogConfig;

    public BlogController(RequestServiceImpl service, BlogConfig blogConfig) {
        this.service = service;
        this.blogConfig = blogConfig;
    }

    @GetMapping("/blog")
    public Object blog() {
        return blogConfig.getUsername();
    }

    @GetMapping("/list/blogs")
    public ResponseResult<List<BlogVO>> listBlogs(@Valid PageParam page) {
        final List<BlogVO> result = this.service.listBlogs(page);
        return ResponseResult.success(result);
    }

    @GetMapping("/get/blog/{number}")
    public ResponseResult<BlogVO> getBlog(@PathVariable String number) {
        final BlogVO result = this.service.getBlog(number);
        return ResponseResult.success(result);
    }

    @GetMapping("/list/labels")
    public ResponseResult<List<LabelVO>> listLabels() {
        final List<LabelVO> result = this.service.listLabels();
        if (Objects.isNull(result)) {
            return ResponseResult.error(ExceptionEnum.NO_LABELS);
        }
        return ResponseResult.success(result);
    }

    @GetMapping("/list/labels/blog/{number}")
    public ResponseResult<List<LabelVO>> getLabelsByBlogNumber(@PathVariable String number) {
        final List<LabelVO> result = this.service.listLabelsForBlog(number);
        return ResponseResult.success(result);
    }

    @GetMapping("/list/blogs/label/{id}")
    public ResponseResult<List<BlogVO>> listBlogsByLabelId(@Valid PageParam page, @PathVariable Long id) {
        final List<BlogVO> result = this.service.listBlogsByLabel(id, page);
        if (Objects.isNull(result)) {
            return ResponseResult.error(ResponseEnum.NO_BLOGS_IN_THE_LABEL);
        }
        return ResponseResult.success(result);
    }

    @GetMapping("/get/label/{id}")
    public ResponseResult<LabelVO> getLabel(@PathVariable Long id) {
        final LabelVO result = this.service.getLabelById(id);
        return ResponseResult.success(result);
    }

    @GetMapping("/list/archive")
    public ResponseResult<List<BlogVO>> listArchive(@Valid PageParam page) {
        final List<BlogVO> result = this.service.listArchive(page);
        return ResponseResult.success(result);
    }

    @GetMapping("/search/blogs")
    public ResponseResult<List<SearchVO>> search(@Valid BlogSearchRequest request) {
        final List<SearchVO> result = this.service.search(request);
        if (Objects.isNull(request)) {
            return ResponseResult.error(ResponseEnum.SEARCH_NO_RESULT);
        }
        return ResponseResult.success(result);
    }

    @PutMapping("/set/cover")
    public ResponseResult<Boolean> setCover(@Valid @RequestBody SetCoverRequest request) {
        final Boolean result = this.service.setCover(request);
        return ResponseResult.success(result);
    }
}
