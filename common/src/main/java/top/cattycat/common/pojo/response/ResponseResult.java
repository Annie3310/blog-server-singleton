package top.cattycat.common.pojo.response;

import lombok.Data;
import lombok.experimental.Accessors;
import top.cattycat.common.enums.ExceptionEnum;
import top.cattycat.common.enums.ResponseEnum;

/**
 * 通用返回体
 *
 * @author 王金义
 * @date 2021/8/30
 */
@Data
@Accessors(chain = true)
public class ResponseResult<T> {
    private String code;
    private String message;
    private T result;

    public static <T> ResponseResult<T> success(T o) {
        return new ResponseResult<T>()
                .setCode(ResponseEnum.SUCCESS.getCode())
                .setMessage(ResponseEnum.SUCCESS.getMessage())
                .setResult(o);
    }

    public static ResponseResult<Object> success() {
        return new ResponseResult<Object>()
                .setCode(ResponseEnum.SUCCESS.getCode())
                .setMessage(ResponseEnum.SUCCESS.getMessage());
    }


    public static <T> ResponseResult<T> error(ExceptionEnum error) {
        return new ResponseResult<T>()
                .setCode(error.getCode())
                .setMessage(error.getMessage());
    }

    public static <T> ResponseResult<T> error(ResponseEnum error) {
        return new ResponseResult<T>()
                .setCode(error.getCode())
                .setMessage(error.getMessage());
    }

    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<T>()
                .setCode(ExceptionEnum.ERROR.getCode())
                .setMessage(message);
    }
}
