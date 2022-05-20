package top.cattycat.common.enums;

/**
 * 返回信息
 *
 * @author 王金义
 * @date 2021/8/30
 */
public enum ResponseEnum {
    SUCCESS("00000", "Success"),

    SEARCH_NO_RESULT("00000", "没有搜索到结果"),
    NO_LABELS("0000", "该仓库没有标签"),
    NO_BLOGS_IN_THE_LABEL("00000", "该博客没有标签");

    final String code;
    final String message;

    ResponseEnum(String code, String message) {
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
