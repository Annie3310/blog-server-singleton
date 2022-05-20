package top.cattycat.common.enums;

/**
 * 错误返回信息
 *
 * @author 王金义
 * @date 2021/8/30
 */
public enum ExceptionEnum {
    ERROR("B0001", "Error"),
    SERVER_ERROR("B0001", "服务端错误"),
    NO_LABELS("0000", "该仓库没有标签"),
    HTTP_REQUEST_EXCEPTION("C0001", "HTTP 请求异常"),
    CONNECT_TO_GITHUB_FAILED("C0001 ", "连接到 GitHub 服务器时失败"),
    FORBIDDEN("A0220", "认证失败"),
    LOGIN_IS_NOT_COMPLETE("B0001", "尚未认证");

    final String code;
    final String message;

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
