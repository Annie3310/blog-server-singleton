package top.cattycat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.cattycat.common.pojo.entity.User;
import top.cattycat.common.pojo.oauth.github.response.GitHubAuthorizationResponse;
import top.cattycat.common.pojo.oauth.github.response.GitHubGetAccessTokenResponse;
import top.cattycat.common.pojo.oauth.github.response.GitHubUserInfoResponse;

/**
 * @author 王金义
 * @date 2022/1/10
 */
public interface UserService extends IService<User> {
    /**
     * Exchange access token from github authorization server
     * @param response Response from user authorization
     * @return Access token
     */
    GitHubGetAccessTokenResponse getAccessToken(GitHubAuthorizationResponse response);

    /**
     * get user's information from api.github.com
     * @param accessToken
     * @return
     */
    GitHubUserInfoResponse getUserInformation(String accessToken);

    /**
     * Whether the user is already registered
     * @param id GitHub id
     * @return result
     */
    boolean isRegistered(Long id);

    /**
     * Register
     * @param userInfoResponse
     * @return
     */
    boolean register(GitHubUserInfoResponse userInfoResponse);

    /**
     * Update last login time
     * @param id User id
     * @return Whether it updated successfully
     */
    boolean updateLastLoginTime(Long id);
}
