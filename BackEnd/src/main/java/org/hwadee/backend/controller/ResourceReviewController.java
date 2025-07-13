package org.hwadee.backend.controller;

import org.hwadee.backend.entity.ResourceReview;
import org.hwadee.backend.entity.LearningResource;
import org.hwadee.backend.mapper.ResourceReviewMapper;
import org.hwadee.backend.mapper.LearningResourceMapper;
import org.hwadee.backend.service.ResourceService;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 资源评价控制器
 */
@RestController
@RequestMapping("/resource-reviews")
public class ResourceReviewController {

    @Autowired
    private ResourceReviewMapper resourceReviewMapper;
    
    @Autowired
    private LearningResourceMapper learningResourceMapper;
    
    @Autowired
    private ResourceService resourceService;

    /**
     * 根据资源ID获取评价列表
     */
    @GetMapping("/resource/{resourceId}")
    public Result<List<ResourceReview>> getReviewsByResource(
            @PathVariable Long resourceId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            int offset = (page - 1) * size;
            List<ResourceReview> reviews = resourceReviewMapper.selectByResourceId(resourceId, offset, size);
            return Result.success("获取成功", reviews);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户对资源的评分
     */
    @GetMapping("/{resourceId}/user/{userId}")
    public Result<ResourceReview> getUserReview(
            @PathVariable Long resourceId,
            @PathVariable Long userId) {
        try {
            ResourceReview review = resourceReviewMapper.selectByUserAndResource(userId, resourceId);
            if (review == null) {
                return Result.success("用户未评价", null);
            }
            return Result.success("获取成功", review);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 添加或更新评分
     */
    @PostMapping("/rate")
    public Result<LearningResource> rateResource(
            @RequestBody ResourceReview review,
            HttpServletRequest request) {
        try {
            // 获取当前用户ID
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return Result.error("用户未登录");
            }
            
            // 检查资源是否存在
            LearningResource resource = resourceService.getResourceById(review.getResourceId());
            if (resource == null) {
                return Result.error("资源不存在");
            }
            
            // 设置评价信息
            review.setUserId(userId);
            review.setCreateTime(LocalDateTime.now());
            review.setUpdateTime(LocalDateTime.now());
            review.setStatus(ResourceReview.STATUS_VISIBLE);
            review.setHelpfulCount(0);
            
            // 检查用户是否已评价过该资源
            ResourceReview existingReview = resourceReviewMapper.selectByUserAndResource(userId, review.getResourceId());
            
            if (existingReview != null) {
                // 更新评价
                review.setReviewId(existingReview.getReviewId());
                resourceReviewMapper.update(review);
            } else {
                // 新增评价
                resourceReviewMapper.insert(review);
            }
            
            // 重新计算资源的平均评分
            Double avgRating = resourceReviewMapper.calculateAverageRating(review.getResourceId());
            int ratingCount = resourceReviewMapper.countByResourceId(review.getResourceId());
            
            // 更新资源评分信息
            if (avgRating != null) {
                resource.setRating(new BigDecimal(avgRating).setScale(1, RoundingMode.HALF_UP));
            } else {
                resource.setRating(BigDecimal.ZERO);
            }
            resource.setRatingCount(ratingCount);
            
            // 直接使用Mapper更新评分，避免可能的类型转换问题
            learningResourceMapper.updateRating(
                resource.getResourceId(),
                resource.getRating().doubleValue(),
                resource.getRatingCount()
            );
            
            // 重新获取完整的资源信息
            resource = resourceService.getResourceById(resource.getResourceId());
            
            // 返回更新后的资源信息
            return Result.success("评价成功", resource);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("评价失败：" + e.getMessage());
        }
    }

    /**
     * 删除评价
     */
    @DeleteMapping("/{reviewId}")
    public Result<String> deleteReview(
            @PathVariable Long reviewId,
            HttpServletRequest request) {
        try {
            // 获取当前用户ID
            Long userId = getUserIdFromRequest(request);
            if (userId == null) {
                return Result.error("用户未登录");
            }
            
            // 获取评价信息
            ResourceReview review = resourceReviewMapper.selectById(reviewId);
            if (review == null) {
                return Result.error("评价不存在");
            }
            
            // 检查是否为评价作者
            if (!review.getUserId().equals(userId)) {
                return Result.error("无权删除他人评价");
            }
            
            // 删除评价
            resourceReviewMapper.deleteById(reviewId);
            
            // 重新计算资源的平均评分
            Double avgRating = resourceReviewMapper.calculateAverageRating(review.getResourceId());
            int ratingCount = resourceReviewMapper.countByResourceId(review.getResourceId());
            
            // 获取资源信息
            LearningResource resource = resourceService.getResourceById(review.getResourceId());
            
            // 更新资源评分信息
            if (avgRating != null) {
                resource.setRating(new BigDecimal(avgRating).setScale(1, RoundingMode.HALF_UP));
            } else {
                resource.setRating(BigDecimal.ZERO);
            }
            resource.setRatingCount(ratingCount);
            
            // 直接使用Mapper更新评分，避免可能的类型转换问题
            learningResourceMapper.updateRating(
                resource.getResourceId(),
                resource.getRating().doubleValue(),
                resource.getRatingCount()
            );
            
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 获取资源平均评分
     */
    @GetMapping("/{resourceId}/average")
    public Result<Double> getAverageRating(@PathVariable Long resourceId) {
        try {
            Double avgRating = resourceReviewMapper.calculateAverageRating(resourceId);
            if (avgRating == null) {
                avgRating = 0.0;
            }
            return Result.success("获取成功", avgRating);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 从请求中获取用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        // 这里假设有一个TokenUtil工具类来解析token获取用户ID
        // 实际实现需要根据项目的认证机制来获取
        String userId = request.getHeader("userId");
        if (userId != null) {
            return Long.valueOf(userId);
        }
        return 1L; // 测试时默认返回1
    }
} 