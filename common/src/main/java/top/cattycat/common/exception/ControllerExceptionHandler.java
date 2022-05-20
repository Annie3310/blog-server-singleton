package top.cattycat.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import top.cattycat.common.enums.ExceptionEnum;
import top.cattycat.common.pojo.response.ResponseResult;

import java.util.Arrays;

/**
 * 异常捕获器
 *
 * @author 王金义
 * @date 2021/8/30
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Object> handleRestClientException(RestClientException e) {
        logger.warn(e.getMessage());
        logger.warn(Arrays.toString(e.getStackTrace()));
        return ResponseResult.error(ExceptionEnum.HTTP_REQUEST_EXCEPTION);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Object> handleHttpClientErrorException(HttpClientErrorException e) {
        logger.warn(e.getMessage());
        logger.warn(Arrays.toString(e.getStackTrace()));
        return ResponseResult.error(ExceptionEnum.HTTP_REQUEST_EXCEPTION);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.warn(e.getAllErrors().get(0).getDefaultMessage());
        logger.warn(Arrays.toString(e.getStackTrace()));
        return ResponseResult.error(e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        final String message = e.getMessage();
        logger.warn(message);
        logger.warn(Arrays.toString(e.getStackTrace()));
        return ResponseResult.error(message);
    }

    @ExceptionHandler(IllegalAuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseResult<Object> handleIllegalAuthorizationException(IllegalAuthorizationException e) {
        final String message = e.getMessage();
        logger.warn(message);
        logger.warn(Arrays.toString(e.getStackTrace()));
        return ResponseResult.error(ExceptionEnum.FORBIDDEN);
    }

    @ExceptionHandler(ConnectToGitHubFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Object> handleConnectToGitHubFailedException(ConnectToGitHubFailedException e) {
        logger.warn(ExceptionEnum.CONNECT_TO_GITHUB_FAILED.getMessage());
        logger.warn(Arrays.toString(e.getStackTrace()));
        return ResponseResult.error(ExceptionEnum.CONNECT_TO_GITHUB_FAILED);
    }
}
