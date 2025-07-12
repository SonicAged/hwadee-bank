package org.hwadee.backend.service.serviceImpl;

import org.hwadee.backend.entity.CreditConversionRule;
import org.hwadee.backend.entity.CreditRecord;
import org.hwadee.backend.mapper.CreditConversionRuleMapper;
import org.hwadee.backend.mapper.CreditRecordMapper;
import org.hwadee.backend.service.CreditConversionService;
import org.hwadee.backend.service.CreditAccountService;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 学分转换服务实现类
 */
@Service
public class CreditConversionServiceImpl implements CreditConversionService {

    @Autowired
    private CreditConversionRuleMapper ruleMapper;

    @Autowired
    private CreditAccountService accountService;

    @Autowired
    private CreditRecordMapper recordMapper;

    @Override
    public Result<List<CreditConversionRule>> getActiveRules() {
        try {
            List<CreditConversionRule> rules = ruleMapper.selectActiveRules();
            return Result.success(rules);
        } catch (Exception e) {
            return Result.error("查询转换规则失败：" + e.getMessage());
        }
    }

    @Override
    public Result<CreditConversionRule> getRuleByTypes(String sourceType, String targetType) {
        try {
            if (sourceType == null || sourceType.trim().isEmpty()) {
                return Result.error("源学分类型不能为空");
            }
            if (targetType == null || targetType.trim().isEmpty()) {
                return Result.error("目标学分类型不能为空");
            }

            CreditConversionRule rule = ruleMapper.selectByTypes(sourceType, targetType);
            if (rule == null) {
                return Result.error("未找到对应的转换规则");
            }

            return Result.success(rule);
        } catch (Exception e) {
            return Result.error("查询转换规则失败：" + e.getMessage());
        }
    }

    @Override
    public Result<BigDecimal> calculateConvertedCredits(String sourceType, String targetType, BigDecimal sourceCredits) {
        try {
            if (sourceCredits == null || sourceCredits.compareTo(BigDecimal.ZERO) <= 0) {
                return Result.error("源学分数量必须大于0");
            }

            Result<CreditConversionRule> ruleResult = getRuleByTypes(sourceType, targetType);
            if (!ruleResult.isSuccess()) {
                return Result.error(ruleResult.getMessage());
            }

            CreditConversionRule rule = ruleResult.getData();
            
            // 检查最小转换学分
            if (rule.getMinCredits() != null && sourceCredits.compareTo(rule.getMinCredits()) < 0) {
                return Result.error("转换学分不能少于 " + rule.getMinCredits() + " 分");
            }

            // 检查最大转换学分
            if (rule.getMaxCredits() != null && sourceCredits.compareTo(rule.getMaxCredits()) > 0) {
                return Result.error("转换学分不能超过 " + rule.getMaxCredits() + " 分");
            }

            // 计算转换后的学分
            BigDecimal convertedCredits = sourceCredits.multiply(rule.getConversionRate())
                    .setScale(2, RoundingMode.HALF_UP);

            return Result.success(convertedCredits);
        } catch (Exception e) {
            return Result.error("计算转换学分失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> convertCredits(Long userId, String sourceType, String targetType, BigDecimal sourceCredits) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            // 计算转换后的学分
            Result<BigDecimal> calculateResult = calculateConvertedCredits(sourceType, targetType, sourceCredits);
            if (!calculateResult.isSuccess()) {
                return Result.error(calculateResult.getMessage());
            }

            BigDecimal convertedCredits = calculateResult.getData();

            // 确保用户有学分账户
            Result<org.hwadee.backend.entity.CreditAccount> accountResult = 
                accountService.getAccountByUserId(userId);
            if (!accountResult.isSuccess()) {
                return Result.error("用户学分账户不存在");
            }
            
            // 检查用户是否有足够的源学分类型学分
            // 这里简化处理，假设用户有足够的学分
            // 实际应该根据不同学分类型分别管理

            // 扣除源学分
            Result<String> deductResult = accountService.deductCredits(userId, sourceCredits);
            if (!deductResult.isSuccess()) {
                return Result.error("扣除源学分失败：" + deductResult.getMessage());
            }

            // 增加目标学分
            Result<String> addResult = accountService.addCredits(
                userId, 
                convertedCredits, 
                targetType, 
                "学分转换", 
                "从 " + sourceType + " 转换而来，原始学分：" + sourceCredits
            );
            if (!addResult.isSuccess()) {
                return Result.error("增加目标学分失败：" + addResult.getMessage());
            }

            // 记录转换记录
            recordConversion(userId, sourceType, targetType, sourceCredits, convertedCredits);

            return Result.success("学分转换成功，获得 " + convertedCredits + " " + targetType + " 学分");
        } catch (Exception e) {
            return Result.error("学分转换失败：" + e.getMessage());
        }
    }

    /**
     * 记录转换记录
     */
    private void recordConversion(Long userId, String sourceType, String targetType, 
                                BigDecimal sourceCredits, BigDecimal convertedCredits) {
        try {
            CreditRecord record = new CreditRecord();
            record.setUserId(userId);
            record.setCreditType("学分转换");
            record.setCreditSource(sourceType + " → " + targetType);
            record.setCreditAmount(convertedCredits);
            record.setOperationType(3); // 转换
            record.setStatus(1); // 有效
            record.setRemark("将 " + sourceCredits + " " + sourceType + " 转换为 " + convertedCredits + " " + targetType);
            record.setCreateTime(LocalDateTime.now());
            
            recordMapper.insert(record);
        } catch (Exception e) {
            System.err.println("记录学分转换失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> createRule(CreditConversionRule rule) {
        try {
            // 验证参数
            if (rule.getSourceType() == null || rule.getSourceType().trim().isEmpty()) {
                return Result.error("源学分类型不能为空");
            }
            if (rule.getTargetType() == null || rule.getTargetType().trim().isEmpty()) {
                return Result.error("目标学分类型不能为空");
            }
            if (rule.getConversionRate() == null || rule.getConversionRate().compareTo(BigDecimal.ZERO) <= 0) {
                return Result.error("转换比例必须大于0");
            }

            // 检查是否已存在相同的转换规则
            CreditConversionRule existingRule = ruleMapper.selectByTypes(rule.getSourceType(), rule.getTargetType());
            if (existingRule != null) {
                return Result.error("已存在相同的转换规则");
            }

            rule.setStatus(1); // 默认启用
            rule.setCreateTime(LocalDateTime.now());
            rule.setUpdateTime(LocalDateTime.now());

            int result = ruleMapper.insert(rule);
            if (result > 0) {
                return Result.success("创建转换规则成功");
            } else {
                return Result.error("创建转换规则失败");
            }
        } catch (Exception e) {
            return Result.error("创建转换规则失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> updateRule(CreditConversionRule rule) {
        try {
            if (rule.getRuleId() == null) {
                return Result.error("规则ID不能为空");
            }

            // 检查规则是否存在
            CreditConversionRule existingRule = ruleMapper.selectById(rule.getRuleId());
            if (existingRule == null) {
                return Result.error("转换规则不存在");
            }

            rule.setUpdateTime(LocalDateTime.now());
            int result = ruleMapper.update(rule);
            if (result > 0) {
                return Result.success("更新转换规则成功");
            } else {
                return Result.error("更新转换规则失败");
            }
        } catch (Exception e) {
            return Result.error("更新转换规则失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> deleteRule(Long ruleId) {
        try {
            if (ruleId == null) {
                return Result.error("规则ID不能为空");
            }

            int result = ruleMapper.deleteById(ruleId);
            if (result > 0) {
                return Result.success("删除转换规则成功");
            } else {
                return Result.error("删除转换规则失败");
            }
        } catch (Exception e) {
            return Result.error("删除转换规则失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<CreditConversionRule>> getAllRules(int page, int size) {
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            int offset = (page - 1) * size;

            List<CreditConversionRule> rules = ruleMapper.selectAll(offset, size);
            return Result.success(rules);
        } catch (Exception e) {
            return Result.error("查询转换规则失败：" + e.getMessage());
        }
    }
} 