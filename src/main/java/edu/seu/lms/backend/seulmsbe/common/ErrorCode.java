package edu.seu.lms.backend.seulmsbe.common;

/**
 * 错误码
 */
public enum ErrorCode {
    SUCCESS(0,"ok",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求数据为空",""),
    NOT_LOGIN(401,"账号未登录",""),
    NO_AUTH(40101, "没有权限",""),
    SYSTEM_ERROR(50000,"系统异常",""),
    FILE_EMPTY(50505,"文件为空","");


    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
