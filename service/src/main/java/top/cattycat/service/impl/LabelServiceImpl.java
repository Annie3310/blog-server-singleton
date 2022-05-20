package top.cattycat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.cattycat.common.pojo.entity.Label;
import top.cattycat.mapper.mapper.LabelMapper;
import top.cattycat.service.LabelService;

/**
 * @author 王金义
 * @date 2021/11/9
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements LabelService {
}
