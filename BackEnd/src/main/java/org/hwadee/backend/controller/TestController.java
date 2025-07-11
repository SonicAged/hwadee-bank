package org.hwadee.backend.controller;

import org.hwadee.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

/**
 * 测试控制器 - 用于验证后端连接
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("后端服务正常运行");
    }

    /**
     * 测试接口
     */
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello from Backend!");
    }
} 