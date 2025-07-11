package org.hwadee.backend.service.serviceImpl;

import org.hwadee.backend.entity.CreditApplication;
import org.hwadee.backend.mapper.CreditApplicationMapper;
import org.hwadee.backend.service.CreditApplicationService;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学分申请服务实现类
 */
@Service
public class CreditApplicationServiceImpl implements CreditApplicationService {

    @Autowired
    private CreditApplicationMapper applicationMapper;

    @Override
    @Transactional
    public Result<String> submitApplication(CreditApplication application) {
        try {
            // 验证参数
            if (application.getUserId() == null) {
                return Result.error("用户ID不能为空");
            }
            if (application.getCreditType() == null || application.getCreditType().trim().isEmpty()) {
                return Result.error("学分类型不能为空");
            }
            if (application.getCreditSource() == null || application.getCreditSource().trim().isEmpty()) {
                return Result.error("学分来源不能为空");
            }
            if (application.getCreditAmount() == null || application.getCreditAmount().doubleValue() <= 0) {
                return Result.error("学分数量必须大于0");
            }

            // 设置默认值
            application.setStatus(0); // 待审核
            application.setCreateTime(LocalDateTime.now());
            application.setUpdateTime(LocalDateTime.now());

            int result = applicationMapper.insert(application);
            if (result > 0) {
                return Result.success("申请提交成功");
            } else {
                return Result.error("申请提交失败");
            }
        } catch (Exception e) {
            return Result.error("提交申请失败：" + e.getMessage());
        }
    }

    @Override
    public Result<CreditApplication> getApplicationById(Long applicationId) {
        try {
            if (applicationId == null) {
                return Result.error("申请ID不能为空");
            }

            CreditApplication application = applicationMapper.selectById(applicationId);
            if (application == null) {
                return Result.error("申请不存在");
            }

            return Result.success(application);
        } catch (Exception e) {
            return Result.error("查询申请失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<CreditApplication>> getUserApplications(Long userId, int page, int size) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            if (page < 1) page = 1;
            if (size < 1) size = 10;
            int offset = (page - 1) * size;

            List<CreditApplication> applications = applicationMapper.selectByUserId(userId);
            return Result.success(applications);
        } catch (Exception e) {
            return Result.error("查询用户申请失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<CreditApplication>> getAllApplications(int page, int size) {
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            int offset = (page - 1) * size;

            List<CreditApplication> applications = applicationMapper.selectAll(offset, size);
            return Result.success(applications);
        } catch (Exception e) {
            return Result.error("查询申请列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<CreditApplication>> getApplicationsByStatus(Integer status, int page, int size) {
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;

            List<CreditApplication> applications = applicationMapper.selectByStatus(status);
            return Result.success(applications);
        } catch (Exception e) {
            return Result.error("查询申请列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> reviewApplication(Long applicationId, Integer status, String reviewComment, Long reviewerId) {
        try {
            if (applicationId == null) {
                return Result.error("申请ID不能为空");
            }
            if (status == null) {
                return Result.error("审核状态不能为空");
            }

            // 检查申请是否存在
            CreditApplication application = applicationMapper.selectById(applicationId);
            if (application == null) {
                return Result.error("申请不存在");
            }

            // 检查申请状态
            if (application.getStatus() != 0) {
                return Result.error("申请已被审核，无法重复操作");
            }

            int result = applicationMapper.updateStatus(applicationId, status, reviewComment);
            if (result > 0) {
                String statusText = status == 1 ? "通过" : "拒绝";
                return Result.success("审核" + statusText + "成功");
            } else {
                return Result.error("审核失败");
            }
        } catch (Exception e) {
            return Result.error("审核申请失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> updateApplication(CreditApplication application) {
        try {
            if (application.getApplicationId() == null) {
                return Result.error("申请ID不能为空");
            }

            // 检查申请是否存在
            CreditApplication existingApplication = applicationMapper.selectById(application.getApplicationId());
            if (existingApplication == null) {
                return Result.error("申请不存在");
            }

            // 只有待审核状态的申请才能修改
            if (existingApplication.getStatus() != 0) {
                return Result.error("只有待审核状态的申请才能修改");
            }

            application.setUpdateTime(LocalDateTime.now());
            int result = applicationMapper.update(application);
            if (result > 0) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error("更新申请失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> deleteApplication(Long applicationId) {
        try {
            if (applicationId == null) {
                return Result.error("申请ID不能为空");
            }

            // 检查申请是否存在
            CreditApplication application = applicationMapper.selectById(applicationId);
            if (application == null) {
                return Result.error("申请不存在");
            }

            // 只有待审核状态的申请才能删除
            if (application.getStatus() != 0) {
                return Result.error("只有待审核状态的申请才能删除");
            }

            int result = applicationMapper.deleteById(applicationId);
            if (result > 0) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除申请失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Integer> getApplicationCount() {
        try {
            int count = applicationMapper.countAll();
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计申请数量失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Integer> getApplicationCountByStatus(Integer status) {
        try {
            int count = applicationMapper.countByStatus(status);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计申请数量失败：" + e.getMessage());
        }
    }
} 