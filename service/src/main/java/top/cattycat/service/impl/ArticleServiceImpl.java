package top.cattycat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.cattycat.common.pojo.entity.Article;
import top.cattycat.mapper.mapper.ArticleMapper;
import top.cattycat.service.ArticleService;

/**
 * @author 王金义
 * @date 2021/11/9
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}
