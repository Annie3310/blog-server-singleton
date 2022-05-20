package top.cattycat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.cattycat.common.config.GitHubConfig;
import top.cattycat.common.pojo.entity.User;
import top.cattycat.common.pojo.oauth.github.request.GitHubAccessTokenRequest;
import top.cattycat.common.pojo.oauth.github.response.GitHubAuthorizationResponse;
import top.cattycat.common.pojo.oauth.github.response.GitHubGetAccessTokenResponse;
import top.cattycat.common.pojo.oauth.github.response.GitHubUserInfoResponse;
import top.cattycat.mapper.mapper.UserMapper;
import top.cattycat.service.UserService;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Date;
import java.util.Objects;

/**
 * @author 王金义
 * @date 2022/1/10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final RestTemplate restTemplate;
    private final GitHubConfig gitHubConfig;
    private final static String GET_ACCESS_TOKEN_URI = "https://github.com/login/oauth/access_token";
    private final static String GET_USER_INFORMATION_URI = "https://api.github.com/user";
    private final static String AUTHORIZATION_TOKEN = "token %s";

    public UserServiceImpl(RestTemplate restTemplate, GitHubConfig gitHubConfig) {
        this.restTemplate = restTemplate;
        this.gitHubConfig = gitHubConfig;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(
                new Proxy(
                        Proxy.Type.HTTP,
                        //设置代理服务
                        new InetSocketAddress("127.0.0.1", 4780)
                )
        );
        this.restTemplate.setRequestFactory(requestFactory);
    }

    @Override
    public GitHubGetAccessTokenResponse getAccessToken(GitHubAuthorizationResponse response) {

        final GitHubAccessTokenRequest accessTokenRequestBody = this.getAccessTokenRequestBody(response);
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<GitHubAccessTokenRequest> httpEntity = new HttpEntity<>(accessTokenRequestBody, headers);
        GitHubGetAccessTokenResponse accessTokenResponse;
        accessTokenResponse = this.restTemplate.postForObject(GET_ACCESS_TOKEN_URI, httpEntity, GitHubGetAccessTokenResponse.class);
        return accessTokenResponse;
    }

    @Override
    public boolean isRegistered(Long id) {
        final LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getGId).eq(User::getGId, id);
        final User one = this.getOne(wrapper);
        return Objects.nonNull(one);
    }

    @Override
    public GitHubUserInfoResponse getUserInformation(String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format(AUTHORIZATION_TOKEN, accessToken));
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<GitHubUserInfoResponse> response = this.restTemplate.exchange(GET_USER_INFORMATION_URI, HttpMethod.GET, httpEntity, GitHubUserInfoResponse.class);
        return response.getBody();
    }

    @Override
    public boolean register(GitHubUserInfoResponse userInfoResponse) {
        final User user = new User();
        user
                .setGId(userInfoResponse.getId())
                .setNickname(userInfoResponse.getLogin())
                .setRegistrationTime(new Date())
                .setAvatar(userInfoResponse.getAvatarUrl());
        return this.save(user);
    }

    @Override
    public boolean updateLastLoginTime(Long id) {
        final LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getLastLoginTime, new Date());
        return this.update(wrapper);
    }

    private GitHubAccessTokenRequest getAccessTokenRequestBody(GitHubAuthorizationResponse response) {
        return new GitHubAccessTokenRequest()
                .setCode(response.getCode())
                .setClientId(gitHubConfig.getOauth().getClientId())
                .setClientSecret(gitHubConfig.getOauth().getSecret());
//                .setRedirectUri("http://localhost:6002/blog");
    }
}
