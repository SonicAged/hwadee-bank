package org.hwadee.backend.service.serviceImpl;

import org.hwadee.backend.entity.CreditApplication;
import org.hwadee.backend.entity.PageResult;
import org.hwadee.backend.mapper.CreditApplicationMapper;
import org.hwadee.backend.service.CreditApplicationService;
import org.hwadee.backend.service.CreditAccountService;
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

    @Autowired
    private CreditAccountService creditAccountService;

    @Override
    @Transactional
    public Result<String> submitApplication(CreditApplication application) {
        try {
            // 验证必填字段
            if (application.getUserId() == null) {
                return Result.error("用户ID不能为空");
            }
            if (application.getApplicationType() == null || application.getApplicationType().trim().isEmpty()) {
                return Result.error("申请类型不能为空");
            }
            if (application.getAchievementName() == null || application.getAchievementName().trim().isEmpty()) {
                return Result.error("成果名称不能为空");
            }
            if (application.getAppliedCredits() == null) {
                return Result.error("申请学分不能为空");
            }
            
            // 处理证明材料文件字段 - 确保是有效的 JSON 格式
            if (application.getEvidenceFiles() == null || application.getEvidenceFiles().trim().isEmpty()) {
                application.setEvidenceFiles(null); // 数据库允许 null
            } else {
                // 如果不是 JSON 格式，将其转换为 JSON 数组
                String evidenceFiles = application.getEvidenceFiles().trim();
                if (!evidenceFiles.startsWith("[") && !evidenceFiles.startsWith("{")) {
                    // 单个文件路径，转换为 JSON 数组
                    application.setEvidenceFiles("[\"" + evidenceFiles + "\"]");
                }
            }
            
            // 设置默认值
            application.setCurrentStep(1);
            application.setStatus(1); // 待审核
            application.setApplyTime(LocalDateTime.now());
            
            applicationMapper.insert(application);
            return Result.success("学分申请提交成功");
        } catch (Exception e) {
            return Result.error("提交学分申请失败: " + e.getMessage());
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

            // 检查申请状态 - 只有待审核状态(1)才能审核
            if (application.getStatus() != 1) {
                return Result.error("申请已被审核，无法重复操作");
            }

            int result = applicationMapper.updateStatus(applicationId, status, reviewComment);
            if (result > 0) {
                // 如果审核通过，自动增加学分到用户账户
                if (status == 3) {
                    try {
                        // 确保用户有学分账户
                        Result<org.hwadee.backend.entity.CreditAccount> accountResult = 
                            creditAccountService.getAccountByUserId(application.getUserId());
                        if (!accountResult.isSuccess()) {
                            // 如果没有账户，先创建
                            creditAccountService.createAccount(application.getUserId());
                        }
                        
                        // 增加学分
                        creditAccountService.addCredits(
                            application.getUserId(), 
                            application.getAppliedCredits(),
                            application.getApplicationType(),
                            application.getAchievementName(),
                            "学分申请审核通过，申请ID: " + applicationId
                        );
                    } catch (Exception e) {
                        // 学分增加失败不影响审核，只记录日志
                        System.err.println("自动增加学分失败，申请ID: " + applicationId + 
                                         ", 用户ID: " + application.getUserId() + 
                                         ", 学分: " + application.getAppliedCredits() + 
                                         ", 错误: " + e.getMessage());
                    }
                }
                
                String statusText = status == 3 ? "通过" : "拒绝";
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

            // 只有待审核状态(1)的申请才能修改
            if (existingApplication.getStatus() != 1) {
                return Result.error("只有待审核状态的申请才能修改");
            }

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

            // 只有待审核状态(1)的申请才能删除
            if (application.getStatus() != 1) {
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

    @Override
    public Result<List<CreditApplication>> searchApplications(Long userId, String applicationType, String achievementName, Integer status, int page, int size) {
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            int offset = (page - 1) * size;

            List<CreditApplication> applications = applicationMapper.searchByCondition(userId, applicationType, achievementName, status, offset, size);
            return Result.success(applications);
        } catch (Exception e) {
            return Result.error("搜索申请列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PageResult<CreditApplication>> getApplicationListWithPaging(Integer status, String applicationType, String achievementName, int page, int size) {
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            int offset = (page - 1) * size;

            // 查询申请列表
            List<CreditApplication> applications = applicationMapper.searchByCondition(null, applicationType, achievementName, status, offset, size);
            
            // 查询总数
            long total = applicationMapper.countByCondition(null, applicationType, achievementName, status);
            
            // 构造分页结果
            PageResult<CreditApplication> pageResult = PageResult.of(applications, total, page, size);
            
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("查询申请列表失败：" + e.getMessage());
        }
    }
} 