package top.cattycat.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author 王金义
 * @date 2021/9/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogVO {
    private String number;
    private String title;
    private String state;

    private String body;

    private String toc;

    private List<LabelVO> labels;

    private Date createdAt;
    private Date updatedAt;

    private String cover;
}
