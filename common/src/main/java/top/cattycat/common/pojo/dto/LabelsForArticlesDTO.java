package top.cattycat.common.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ηιδΉ
 * @date 2021/11/9
 */
@Data
public class LabelsForArticlesDTO {
    @JsonProperty("b_number")
    private String bNumber;

    @JsonProperty("l_id")
    private List<Long> lId;
}
