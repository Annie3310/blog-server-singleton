package top.cattycat.mapper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import top.cattycat.common.pojo.dto.LabelsForArticlesDTO;
import top.cattycat.common.pojo.entity.LabelsForArticles;

import java.util.List;

/**
 * @author 王金义
 * @date 2021/11/9
 */
@Mapper
public interface LabelsForArticlesMapper extends BaseMapper<LabelsForArticles> {
    /**
     * 获取所有文章与标签关联关系
     * @param numbers 可能传入的文章 numbers
     * @return 关联关系
     */
    List<LabelsForArticlesDTO> listLfa(@Param("numbers") List<String> numbers);
}
