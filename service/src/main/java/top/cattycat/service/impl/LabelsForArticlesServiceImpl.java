package top.cattycat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.cattycat.common.pojo.entity.LabelsForArticles;
import top.cattycat.mapper.mapper.LabelsForArticlesMapper;
import top.cattycat.service.LabelsForArticlesService;

/**
 * @author 王金义
 * @date 2021/11/9
 */
@Service
public class LabelsForArticlesServiceImpl extends ServiceImpl<LabelsForArticlesMapper, LabelsForArticles> implements LabelsForArticlesService {
}
