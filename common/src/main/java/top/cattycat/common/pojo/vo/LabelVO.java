package top.cattycat.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王金义
 * @date 2021/9/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabelVO {
    private Long id;
    private String name;
    private String color;
    private String description;
    private String fontColor;
}
