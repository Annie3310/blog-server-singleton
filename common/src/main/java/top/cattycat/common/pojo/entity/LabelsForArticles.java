package top.cattycat.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 王金义
 * @date 2021/11/9
 */
@Data
@TableName("labels_for_articles")
public class LabelsForArticles {
    @JsonProperty("lfb_id")
    @TableId("lfa_id")
    private Long lfbId;
    @JsonProperty("l_id")
    private Long lId;
    @JsonProperty("b_number")
    private String bNumber;
}
