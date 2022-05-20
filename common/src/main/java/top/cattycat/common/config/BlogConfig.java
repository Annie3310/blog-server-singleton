package top.cattycat.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author 王金义
 * @date 2021/11/18
 */
@ConfigurationProperties("blog")
@Getter
@Setter
public class BlogConfig {
    private Set<String> username;
    private Page page;
    private SearchLimit searchLimit;

    @Getter
    @Setter
    public static class Page{
        private Integer limit;
    }

    @Getter
    @Setter
    public static class SearchLimit {
        private Long limit;
    }
}
