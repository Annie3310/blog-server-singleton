package top.cattycat.common.pojo.oauth.github.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Get access token from github response body
 * @author 王金义
 * @date 2022/1/10
 */
@Data
public class GitHubGetAccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    private String scope;
    @JsonProperty("token_type")
    private String tokenType;
}
