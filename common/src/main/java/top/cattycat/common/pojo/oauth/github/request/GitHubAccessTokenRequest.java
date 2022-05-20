package top.cattycat.common.pojo.oauth.github.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * GitHub OAuth get access token request body
 * @author 王金义
 * @date 2022/1/10
 */
@Data
@Accessors(chain = true)
public class GitHubAccessTokenRequest {
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    private String code;
    @JsonProperty("redirect_uri")
    private String redirectUri;
}
