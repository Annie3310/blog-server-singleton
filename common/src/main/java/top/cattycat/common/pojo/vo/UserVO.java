package top.cattycat.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 王金义
 * @date 2022/1/12
 */
@Data
@Accessors(chain = true)
public class UserVO {
    private String login;
    @JsonProperty("avatar_url")
    private String avatarUrl;
}
