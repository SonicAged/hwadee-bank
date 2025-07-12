package org.hwadee.backend.controller;

import org.hwadee.backend.entity.CreditRecord;
import org.hwadee.backend.entity.PageResult;
import org.hwadee.backend.mapper.CreditRecordMapper;
import org.hwadee.backend.utils.JwtUtil;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 学分记录控制器
 */
@RestController
@RequestMapping("/credit/record")
public class CreditRecordController {

    private static final Logger logger = LoggerFactory.getLogger(CreditRecordController.class);

    @Autowired
    private CreditRecordMapper recordMapper;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取当前用户的学分记录
     */
    @GetMapping("/my")
    public Result<PageResult<CreditRecord>> getMyRecords(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // 从token获取用户ID
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的认证令牌");
            }

            String token = authHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                return Result.error("无效的认证令牌");
            }

            if (page < 1) page = 1;
            if (size < 1) size = 10;
            int offset = (page - 1) * size;

            // 获取总数
            long total = recordMapper.countByUserId(userId);
            
            // 获取分页数据
            List<CreditRecord> records = recordMapper.selectByUserId(userId, offset, size);
            
            // 返回分页结果
            return Result.success(PageResult.of(records, total, page, size));
        } catch (Exception e) {
            logger.error("查询学分记录时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 获取指定用户的学分记录（管理员功能）
     */
    @GetMapping("/user/{userId}")
    public Result<PageResult<CreditRecord>> getUserRecords(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            int offset = (page - 1) * size;

            // 获取总数
            long total = recordMapper.countByUserId(userId);
            
            // 获取分页数据
            List<CreditRecord> records = recordMapper.selectByUserId(userId, offset, size);
            
            // 返回分页结果
            return Result.success(PageResult.of(records, total, page, size));
        } catch (Exception e) {
            logger.error("查询用户学分记录时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 根据条件查询学分记录
     */
    @GetMapping("/search")
    public Result<PageResult<CreditRecord>> searchRecords(
            HttpServletRequest request,
            @RequestParam(required = false) String creditType,
            @RequestParam(required = false) Integer operationType,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // 从token获取用户ID
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的认证令牌");
            }

            String token = authHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                return Result.error("无效的认证令牌");
            }

            if (page < 1) page = 1;
            if (size < 1) size = 10;
            int offset = (page - 1) * size;

            // 获取总数
            long total = recordMapper.countByCondition(userId, creditType, operationType, status);
            
            // 获取分页数据
            List<CreditRecord> records = recordMapper.selectByCondition(userId, creditType, operationType, status, offset, size);
            
            // 返回分页结果
            return Result.success(PageResult.of(records, total, page, size));
        } catch (Exception e) {
            logger.error("搜索学分记录时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 统计当前用户的学分记录数量
     */
    @GetMapping("/count")
    public Result<Long> getMyRecordCount(HttpServletRequest request) {
        try {
            // 从token获取用户ID
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的认证令牌");
            }

            String token = authHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                return Result.error("无效的认证令牌");
            }

            long count = recordMapper.countByUserId(userId);
            return Result.success(count);
        } catch (Exception e) {
            logger.error("统计学分记录数量时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }
} 