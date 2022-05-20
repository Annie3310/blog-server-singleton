package top.cattycat.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 王金义
 * @date 2022/1/5
 */
@ConfigurationProperties("spring")
@Getter
@Setter
public class SpringConfig {
    private Datasource datasource;
    private Redis redis;

    @Getter
    @Setter
    public static class Datasource {
        private String url;
        private String username;
        private String password;
    }
    @Getter
    @Setter
    public static class Redis {
        private String host;
        private String port;
        private String password;
    }
}
