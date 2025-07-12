package org.hwadee.backend.controller;

import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    // 从配置文件读取上传路径，如果没有配置则使用默认路径
    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    // 从配置文件读取访问URL前缀
    @Value("${file.access.url:/api/file/}")
    private String accessUrlPrefix;

    /**
     * 上传头像
     */
    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件是否为空
            if (file.isEmpty()) {
                return Result.error("上传文件不能为空");
            }

            // 获取原始文件名
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                return Result.error("文件名不能为空");
            }

            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error("只支持图片文件上传");
            }

            // 验证文件大小（限制为5MB）
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

            // 获取项目根目录
            String projectDir = System.getProperty("user.dir");
            
            // 创建上传目录（绝对路径）
            Path uploadDir = Paths.get(projectDir, uploadPath, "avatars");
            logger.info("项目根目录: {}", projectDir);
            logger.info("上传目录: {}", uploadDir.toString());
            
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                logger.info("创建上传目录: {}", uploadDir.toString());
            }

            // 保存文件
            Path filePath = uploadDir.resolve(newFileName);
            file.transferTo(filePath.toFile());

            // 构造访问URL
            String accessUrl = accessUrlPrefix + "avatars/" + newFileName;

            logger.info("头像上传成功，文件名: {}, 访问URL: {}", newFileName, accessUrl);
            
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
    @GetMapping("/avatars/{filename}")
    public void getAvatar(@PathVariable String filename, 
                         @RequestParam(defaultValue = "false") boolean download,
                         jakarta.servlet.http.HttpServletResponse response) {
        try {
            // 获取项目根目录并构造文件路径
            String projectDir = System.getProperty("user.dir");
            Path filePath = Paths.get(projectDir, uploadPath, "avatars", filename);
            
            // 检查文件是否存在
            if (!Files.exists(filePath)) {
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
            
            if (download) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            } else {
                response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
            }

            // 输出文件内容
            Files.copy(filePath, response.getOutputStream());
            response.flushBuffer();
            
        } catch (IOException e) {
            logger.error("获取头像文件失败", e);
            response.setStatus(500);
        }
    }

    /**
     * 删除头像文件
     */
    @DeleteMapping("/avatars/{filename}")
    public Result<String> deleteAvatar(@PathVariable String filename) {
        try {
            // 获取项目根目录并构造文件路径
            String projectDir = System.getProperty("user.dir");
            Path filePath = Paths.get(projectDir, uploadPath, "avatars", filename);
            
            // 检查文件是否存在
            if (!Files.exists(filePath)) {
                return Result.error("文件不存在");
            }

            // 删除文件
            Files.delete(filePath);
            
            logger.info("头像删除成功，文件名: {}", filename);
            return Result.success("头像删除成功");
            
        } catch (IOException e) {
            logger.error("头像删除失败", e);
            return Result.error("头像删除失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("头像删除发生异常", e);
            return Result.error("服务器内部错误: " + e.getMessage());
        }
    }
} 