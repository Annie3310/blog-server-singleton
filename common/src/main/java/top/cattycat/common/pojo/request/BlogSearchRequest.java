package top.cattycat.common.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 王金义
 * @date 2021/10/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogSearchRequest extends PageParam{
    @NotNull(message = "关键词不能为空")
    private String keyword;
}
