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
@TableName("article")
public class Article {
    @JsonProperty("a_id")
    @TableId("a_id")
    private Long aId;

    private String body;

    @JsonProperty("b_number")
    private String bNumber;

    private String toc;
}
