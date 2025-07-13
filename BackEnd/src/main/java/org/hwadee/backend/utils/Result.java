package org.hwadee.backend.utils;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果类
 * 提供了 success, error, unauthorize, forbiden 的接口
 * @Param code
 * @Param message
 * @Param data
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 错误标志
     */
    private boolean error;
    
    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null, false, true);
    }

    /**
     * 成功响应带数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data, false, true);
    }

    /**
     * 成功响应带消息和数据
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data, false, true);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败", null, true, false);
    }

    /**
     * 失败响应带消息
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null, true, false);
    }

    /**
     * 失败响应带错误码和消息
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null, true, false);
    }

    /**
     * 未授权
     */
    public static <T> Result<T> unauthorized() {
        return new Result<>(401, "未授权", null, true, false);
    }

    /**
     * 未授权带消息
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null, true, false);
    }

    /**
     * 禁止访问
     */
    public static <T> Result<T> forbidden() {
        return new Result<>(403, "禁止访问", null, true, false);
    }

    /**
     * 禁止访问带消息
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null, true, false);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * 判断是否失败
     */
    public boolean isError() {
        return this.error;
    }
} 