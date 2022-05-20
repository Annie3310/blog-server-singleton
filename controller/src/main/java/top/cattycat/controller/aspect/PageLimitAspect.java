package top.cattycat.controller.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.cattycat.common.config.BlogConfig;
import top.cattycat.common.pojo.request.PageParam;

import java.util.Arrays;
import java.util.Objects;

/**
 * 自动分页 limit 切面
 * @author 王金义
 * @date 2021/11/15
 */
@Aspect
@Order(2)
@Component
public class PageLimitAspect {
    private final BlogConfig blogConfig;

    public PageLimitAspect(BlogConfig blogConfig) {
        this.blogConfig = blogConfig;
    }

    @Pointcut("args(top.cattycat.common.pojo.request.PageParam, ..)")
    public void pageParam() {}
    @Pointcut("execution(* top.cattycat.controller.controller.BlogController.*(..))")
    public void method() {}
    @Pointcut("pageParam() && method()")
    public void point() {}

    @Before("point()")
    public void setLimit(JoinPoint joinPoint) {
        final Integer limit = this.blogConfig.getPage().getLimit();
        final Object[] args = joinPoint.getArgs();
        Arrays.stream(args).forEach(o -> {
            // 参数类型为 PageParam
            if (o instanceof PageParam) {
                // 传入的 limit 为 null
                if (Objects.isNull(((PageParam) o).getLimit())) {
                    // 配置文件中的 limit 不为 null
                    if (Objects.nonNull(limit)) {
                        ((PageParam) o).setLimit(limit);
                    } else {
                        // 如果为 null, 则强行传入 20
                        ((PageParam) o).setLimit(20);
                    }
                }
            }
        });
    }

}
