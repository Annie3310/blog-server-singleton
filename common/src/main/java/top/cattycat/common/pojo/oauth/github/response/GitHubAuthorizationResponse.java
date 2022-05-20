package top.cattycat.common.pojo.oauth.github.response;

import lombok.Data;

/**
 * GitHub OAuth get authorization response body
 * @author 王金义
 * @date 2022/1/10
 */
@Data
public class GitHubAuthorizationResponse {
    /**
     * Authorization code from last authorization
     */
    private String code;

    /**
     * The same random string as the authorization request
     */
    private String state;
}
