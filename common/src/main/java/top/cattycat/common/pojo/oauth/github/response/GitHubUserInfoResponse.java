package top.cattycat.common.pojo.oauth.github.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 王金义
 * @date 2022/1/10
 */
@Data
@Accessors(chain = true)
public class GitHubUserInfoResponse {
    private String login;
    private Long id;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String email;
}
