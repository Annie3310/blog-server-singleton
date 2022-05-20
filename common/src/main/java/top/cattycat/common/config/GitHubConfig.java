package top.cattycat.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 王金义
 * @date 2021/11/18
 */
@ConfigurationProperties("github")
@Getter
@Setter
public class GitHubConfig {
    private String secret;
    private String token;
    private Oauth oauth;

    @Getter
    @Setter
    public static class Oauth {
        private String clientId;
        private String secret;
    }
}
