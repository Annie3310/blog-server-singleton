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
@TableName("label")
public class Label {
    @JsonProperty("l_id")
    @TableId("l_id")
    private Long lId;

    private Long id;

    @JsonProperty("node_id")
    private String nodeId;
    private String url;
    private String name;
    private String color;
    private String description;
}
