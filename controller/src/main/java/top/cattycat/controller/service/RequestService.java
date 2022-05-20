package top.cattycat.controller.service;

import top.cattycat.common.pojo.request.BlogSearchRequest;
import top.cattycat.common.pojo.request.PageParam;
import top.cattycat.common.pojo.request.SetCoverRequest;
import top.cattycat.common.pojo.vo.BlogVO;
import top.cattycat.common.pojo.vo.LabelVO;
import top.cattycat.common.pojo.vo.SearchVO;

import java.util.List;

/**
 * @author 王金义
 * @date 2021/11/9
 */
public interface RequestService {
    /**
     * 获取仓库的所有 state = open, creator = Annie3310 的 issues
     *
     * @param page 页码
     * @return 所有文章及用户信息
     */
    List<BlogVO> listBlogs(PageParam page);

    /**
     * 获取 number 的 blog
     *
     * @param number blog number
     * @return 查找到的 blog
     */
    BlogVO getBlog(String number);

    /**
     * 获取所有的 labels
     *
     * @return 所有的 labels
     */
    List<LabelVO> listLabels();

    /**
     * 获取一个 blog 的所有 labels
     *
     * @param number blog number
     * @return labels
     */
    List<LabelVO> listLabelsForBlog(String number);

    /**
     * 获取一个 label 的所有 blogs
     *
     * @param id label id
     * @param page 页码
     * @return 查找到的所有 blogs
     */
    List<BlogVO> listBlogsByLabel(Long id, PageParam page);

    /**
     * 依据 id 获取标签详情
     * @param id 标签 id
     * @return 标签详情
     */
    LabelVO getLabelById(Long id);

    /**
     * 获取状态为 Close 的文章
     * @param page 页码
     * @return 状态为 Close 的文章
     */
    List<BlogVO> listArchive(PageParam page);

    /**
     * 标题, 正文搜索
     * @param request 关键词
     * @return 搜索到的结果
     */
    List<SearchVO> search(BlogSearchRequest request);

    /**
     * 为 blog 设置封面
     * @param request blog number 和 封面地址
     * @return 执行信息
     */
    Boolean setCover(SetCoverRequest request);
}
