package top.cattycat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.cattycat.common.pojo.entity.Blog;
import top.cattycat.mapper.mapper.BlogMapper;
import top.cattycat.service.BlogService;

/**
 * @author 王金义
 * @date 2021/11/9
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
}
