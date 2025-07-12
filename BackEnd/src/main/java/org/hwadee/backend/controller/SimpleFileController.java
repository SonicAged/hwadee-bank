package org.hwadee.backend.controller;

import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 简化的文件上传控制器
 */
@RestController
@RequestMapping("/simple-file")
public class SimpleFileController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleFileController.class);

    /**
     * 简单的头像上传接口
     */
    @PostMapping("/upload-avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("开始处理头像上传，文件名: {}, 大小: {} bytes", file.getOriginalFilename(), file.getSize());

            // 验证文件
            if (file.isEmpty()) {
                return Result.error("上传文件不能为空");
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                return Result.error("文件名不能为空");
            }

            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error("只支持图片文件上传");
            }

            // 验证文件大小（5MB）
            if (file.getSize() > 5 * 1024 * 1024) {
                return Result.error("文件大小不能超过5MB");
            }

            // 获取文件扩展名
            String fileExtension = "";
            int lastIndexOf = originalFileName.lastIndexOf(".");
            if (lastIndexOf != -1) {
                fileExtension = originalFileName.substring(lastIndexOf);
            }

            // 生成唯一文件名
            String newFileName = "avatar_" + UUID.randomUUID().toString() + fileExtension;

            // 使用系统临时目录
            String tempDir = System.getProperty("java.io.tmpdir");
            Path uploadDir = Paths.get(tempDir, "bank-analysis", "avatars");
            
            logger.info("系统临时目录: {}", tempDir);
            logger.info("目标上传目录: {}", uploadDir.toString());

            // 创建目录
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                logger.info("创建目录: {}", uploadDir.toString());
            }

            // 保存文件
            Path filePath = uploadDir.resolve(newFileName);
            file.transferTo(filePath.toFile());

            logger.info("文件保存成功: {}", filePath.toString());

            // 构造访问URL（返回完整的URL）
            String accessUrl = "http://localhost:8080/simple-file/avatar/" + newFileName;

            return Result.success("头像上传成功", accessUrl);

        } catch (IOException e) {
            logger.error("头像上传失败", e);
            return Result.error("头像上传失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("头像上传发生异常", e);
            return Result.error("服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 获取头像文件
     */
    @GetMapping("/avatar/{filename}")
    public void getAvatar(@PathVariable String filename, 
                         jakarta.servlet.http.HttpServletResponse response) {
        try {
            logger.info("请求头像文件: {}", filename);

            // 构造文件路径
            String tempDir = System.getProperty("java.io.tmpdir");
            Path filePath = Paths.get(tempDir, "bank-analysis", "avatars", filename);
            
            logger.info("查找文件: {}", filePath.toString());

            // 检查文件是否存在
            if (!Files.exists(filePath)) {
                logger.warn("文件不存在: {}", filePath.toString());
                response.setStatus(404);
                return;
            }

            // 获取文件内容类型
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 设置响应头
            response.setContentType(contentType);
            response.setContentLengthLong(Files.size(filePath));
            response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");

            // 输出文件内容
            Files.copy(filePath, response.getOutputStream());
            response.flushBuffer();

            logger.info("成功返回头像文件: {}", filename);

        } catch (IOException e) {
            logger.error("获取头像文件失败", e);
            response.setStatus(500);
        }
    }

    /**
     * 测试接口
     */
    @GetMapping("/test")
    public Result<String> test() {
        String tempDir = System.getProperty("java.io.tmpdir");
        String userDir = System.getProperty("user.dir");
        
        String info = String.format("临时目录: %s, 用户目录: %s", tempDir, userDir);
        logger.info(info);
        
        return Result.success(info);
    }
} 