package top.cattycat.mapper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.cattycat.common.pojo.entity.Article;

/**
 * @author 王金义
 * @date 2021/11/9
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
