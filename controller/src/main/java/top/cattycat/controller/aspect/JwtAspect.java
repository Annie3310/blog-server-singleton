package top.cattycat.controller.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.cattycat.common.enums.ExceptionEnum;
import top.cattycat.common.exception.IllegalAuthorizationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * verify token aspect
 * @author 王金义
 * @date 2022/1/10
 */
@Aspect
@Component
@Order(1)
public class JwtAspect {

    @Pointcut("execution(* top.cattycat.controller.controller.BlogController.search(..))")
    void search() {}

    /**
     * Restrict searches to logged-in users
     * @param joinPoint 切入点
     * @return 返回值
     * @throws Throwable 可能发生的错误
     */
    @Around("search()")
    public Object verify(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取request
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            throw new RuntimeException(ExceptionEnum.SERVER_ERROR.getMessage());
        }
        final HttpServletRequest request = requestAttributes.getRequest();

        final String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            throw new IllegalAuthorizationException();
        } else {
            return joinPoint.proceed();
        }
    }
}
