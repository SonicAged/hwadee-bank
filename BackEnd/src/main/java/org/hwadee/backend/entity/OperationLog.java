package org.hwadee.backend.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationLog {
    /**
     * 日志ID
     */
    private Long logId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 操作名称
     */
    private String operationName;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求IP
     */
    private String requestIp;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 执行时间(毫秒)
     */
    private Long executionTime;

    /**
     * 状态：0-失败，1-成功
     */
    private Integer status;

    public static final int FAIL = 0;
    public static final int SUCCESS = 1;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
} 