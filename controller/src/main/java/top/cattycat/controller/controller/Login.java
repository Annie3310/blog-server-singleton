package top.cattycat.controller.controller;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import top.cattycat.common.enums.ExceptionEnum;
import top.cattycat.common.exception.ConnectToGitHubFailedException;
import top.cattycat.common.pojo.oauth.github.request.ValidateRequest;
import top.cattycat.common.pojo.oauth.github.response.GitHubAuthorizationResponse;
import top.cattycat.common.pojo.oauth.github.response.GitHubGetAccessTokenResponse;
import top.cattycat.common.pojo.oauth.github.response.GitHubUserInfoResponse;
import top.cattycat.common.pojo.oauth.github.response.LoggedInResponse;
import top.cattycat.common.pojo.response.ResponseResult;
import top.cattycat.common.pojo.vo.UserVO;
import top.cattycat.common.util.JwtUtils;
import top.cattycat.controller.constant.Constant;
import top.cattycat.service.UserService;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 王金义
 * @date 2022/1/7
 */
@RestController
@RequestMapping("login/oauth/github")
public class Login {
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;

    public Login(UserService userService, RedisTemplate<String, String> redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/authorize")
    public void authorize(GitHubAuthorizationResponse response) {
        final String state = response.getState();
        final GitHubGetAccessTokenResponse accessTokenResponse = this.userService.getAccessToken(response);
        final String accessToken = accessTokenResponse.getAccessToken();
        final GitHubUserInfoResponse userInformation = this.userService.getUserInformation(accessToken);
        final Long id = userInformation.getId();

        if (Objects.isNull(id)) {
            throw new ConnectToGitHubFailedException();
        }
        // Whether user has registered
        final String jwtToken = JwtUtils.getToken(userInformation);
        if (this.userService.isRegistered(id)) {
            this.userService.updateLastLoginTime(id);
            this.setAccessTokenInRedis(userInformation, accessToken, state, jwtToken);
        } else {
            // It will register if user has not registered
            if (this.userService.register(userInformation)) {
                this.setAccessTokenInRedis(userInformation, accessToken, state, jwtToken);
            }
        }
    }

    @PostMapping("/validate")
    public ResponseResult<LoggedInResponse> validate(@RequestBody ValidateRequest state) {
        final ValueOperations<String, String> opsForValue = this.redisTemplate.opsForValue();
        final String jwtTokenKey = String.format(Constant.USER_JWT_TOKEN_TEMPLATE, state.getState());
        final String jwtToken = opsForValue.get(jwtTokenKey);
        if (StringUtils.isEmpty(jwtToken)) {
            return ResponseResult.error(ExceptionEnum.LOGIN_IS_NOT_COMPLETE);
        } else {
            final Claims claims = JwtUtils.parseToken(jwtToken);
            final String login = String.valueOf(claims.get("login"));
            final String avatar = String.valueOf(claims.get("avatar"));
            final UserVO userVO = new UserVO().setLogin(login).setAvatarUrl(avatar);
            LoggedInResponse result = new LoggedInResponse().setAccessToken(avatar).setUserInfo(userVO);
            this.redisTemplate.delete(jwtTokenKey);
            return ResponseResult.success(result);
        }
    }
    /**
     * Set user's access token and jwt token into redis
     * @param userInformation User information
     * @param accessToken access token
     */
    private void setAccessTokenInRedis(GitHubUserInfoResponse userInformation, String accessToken, String state, String jwtToken) {
        final String accessTokenKey = String.format(Constant.USER_ACCESS_TOKEN_TEMPLATE, userInformation.getId());
        final String jwtTokenKey = String.format(Constant.USER_JWT_TOKEN_TEMPLATE, state);
        final ValueOperations<String, String> opsForValue = this.redisTemplate.opsForValue();
        opsForValue.set(accessTokenKey, accessToken, 7, TimeUnit.DAYS);
        opsForValue.set(jwtTokenKey, jwtToken);
    }
}
