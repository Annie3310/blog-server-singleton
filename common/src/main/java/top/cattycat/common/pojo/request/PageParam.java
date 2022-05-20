package top.cattycat.common.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author 王金义
 * @date 2021/11/9
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {
    @NotNull(message = "页码不能为空")
    private Integer page = 1;
    private Integer limit;
}
