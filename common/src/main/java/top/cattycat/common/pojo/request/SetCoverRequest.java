package top.cattycat.common.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 王金义
 * @date 2021/10/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetCoverRequest {
    @NotNull(message = "博客 number 不能为空")
    private String number;
    @NotNull(message = "封面地址不能为空")
    private String src;
}
