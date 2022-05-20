package top.cattycat.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 王金义
 * @date 2022/1/5
 */
@Data
public class SearchVO {
    private String number;
    private String title;
    private String state;
    private List<LabelVO> labels;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    private String cover;
}
