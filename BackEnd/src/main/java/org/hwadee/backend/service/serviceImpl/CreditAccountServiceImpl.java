package org.hwadee.backend.service.serviceImpl;

import org.hwadee.backend.entity.CreditAccount;
import org.hwadee.backend.entity.CreditRecord;
import org.hwadee.backend.mapper.CreditAccountMapper;
import org.hwadee.backend.mapper.CreditRecordMapper;
import org.hwadee.backend.service.CreditAccountService;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学分账户服务实现类
 */
@Service
public class CreditAccountServiceImpl implements CreditAccountService {

    @Autowired
    private CreditAccountMapper accountMapper;

    @Autowired
    private CreditRecordMapper recordMapper;

    @Override
    @Transactional
    public Result<String> createAccount(Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            // 检查是否已存在账户
            CreditAccount existingAccount = accountMapper.selectByUserId(userId);
            if (existingAccount != null) {
                return Result.error("用户账户已存在");
            }

            // 创建新账户
            CreditAccount account = new CreditAccount();
            account.setUserId(userId);
            account.setTotalCredits(BigDecimal.ZERO);
            account.setAvailableCredits(BigDecimal.ZERO);
            account.setFrozenCredits(BigDecimal.ZERO);
            account.setCreateTime(LocalDateTime.now());
            account.setUpdateTime(LocalDateTime.now());

            int result = accountMapper.insert(account);
            if (result > 0) {
                return Result.success("账户创建成功");
            } else {
                return Result.error("账户创建失败");
            }
        } catch (Exception e) {
            return Result.error("创建账户失败：" + e.getMessage());
        }
    }

    @Override
    public Result<CreditAccount> getAccountByUserId(Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            CreditAccount account = accountMapper.selectByUserId(userId);
            if (account == null) {
                return Result.error("账户不存在");
            }

            return Result.success(account);
        } catch (Exception e) {
            return Result.error("查询账户失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> updateAccount(CreditAccount account) {
        try {
            if (account.getAccountId() == null) {
                return Result.error("账户ID不能为空");
            }

            account.setUpdateTime(LocalDateTime.now());
            int result = accountMapper.update(account);
            if (result > 0) {
                return Result.success("账户更新成功");
            } else {
                return Result.error("账户更新失败");
            }
        } catch (Exception e) {
            return Result.error("更新账户失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> addCredits(Long userId, BigDecimal credits) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            if (credits == null || credits.compareTo(BigDecimal.ZERO) <= 0) {
                return Result.error("学分数量必须大于0");
            }

            CreditAccount account = accountMapper.selectByUserId(userId);
            if (account == null) {
                return Result.error("账户不存在");
            }

            // 更新学分
            account.setTotalCredits(account.getTotalCredits().add(credits));
            account.setAvailableCredits(account.getAvailableCredits().add(credits));
            account.setUpdateTime(LocalDateTime.now());

            int result = accountMapper.update(account);
            if (result > 0) {
                // 记录学分变动
                recordCreditChange(userId, credits, 1, "学分获得", "系统增加", "学分账户增加");
                return Result.success("学分添加成功");
            } else {
                return Result.error("学分添加失败");
            }
        } catch (Exception e) {
            return Result.error("添加学分失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> addCredits(Long userId, BigDecimal credits, String creditType, String creditSource, String remark) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            if (credits == null || credits.compareTo(BigDecimal.ZERO) <= 0) {
                return Result.error("学分数量必须大于0");
            }

            CreditAccount account = accountMapper.selectByUserId(userId);
            if (account == null) {
                return Result.error("账户不存在");
            }

            // 更新学分
            account.setTotalCredits(account.getTotalCredits().add(credits));
            account.setAvailableCredits(account.getAvailableCredits().add(credits));
            account.setUpdateTime(LocalDateTime.now());

            int result = accountMapper.update(account);
            if (result > 0) {
                // 记录学分变动
                recordCreditChange(userId, credits, 1, creditType, creditSource, remark);
                return Result.success("学分添加成功");
            } else {
                return Result.error("学分添加失败");
            }
        } catch (Exception e) {
            return Result.error("添加学分失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> deductCredits(Long userId, BigDecimal credits) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            if (credits == null || credits.compareTo(BigDecimal.ZERO) <= 0) {
                return Result.error("学分数量必须大于0");
            }

            CreditAccount account = accountMapper.selectByUserId(userId);
            if (account == null) {
                return Result.error("账户不存在");
            }

            // 检查可用学分是否足够
            if (account.getAvailableCredits().compareTo(credits) < 0) {
                return Result.error("可用学分不足");
            }

            // 扣除学分
            account.setAvailableCredits(account.getAvailableCredits().subtract(credits));
            account.setUpdateTime(LocalDateTime.now());

            int result = accountMapper.update(account);
            if (result > 0) {
                // 记录学分变动
                recordCreditChange(userId, credits, 2, "学分消费", "系统扣减", "学分账户扣减");
                return Result.success("学分消费成功");
            } else {
                return Result.error("学分消费失败");
            }
        } catch (Exception e) {
            return Result.error("消费学分失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> freezeCredits(Long userId, BigDecimal credits) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            if (credits == null || credits.compareTo(BigDecimal.ZERO) <= 0) {
                return Result.error("学分数量必须大于0");
            }

            CreditAccount account = accountMapper.selectByUserId(userId);
            if (account == null) {
                return Result.error("账户不存在");
            }

            // 检查可用学分是否足够
            if (account.getAvailableCredits().compareTo(credits) < 0) {
                return Result.error("可用学分不足");
            }

            // 冻结学分
            account.setAvailableCredits(account.getAvailableCredits().subtract(credits));
            account.setFrozenCredits(account.getFrozenCredits().add(credits));
            account.setUpdateTime(LocalDateTime.now());

            int result = accountMapper.update(account);
            if (result > 0) {
                return Result.success("学分冻结成功");
            } else {
                return Result.error("学分冻结失败");
            }
        } catch (Exception e) {
            return Result.error("冻结学分失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> unfreezeCredits(Long userId, BigDecimal credits) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            if (credits == null || credits.compareTo(BigDecimal.ZERO) <= 0) {
                return Result.error("学分数量必须大于0");
            }

            CreditAccount account = accountMapper.selectByUserId(userId);
            if (account == null) {
                return Result.error("账户不存在");
            }

            // 检查冻结学分是否足够
            if (account.getFrozenCredits().compareTo(credits) < 0) {
                return Result.error("冻结学分不足");
            }

            // 解冻学分
            account.setFrozenCredits(account.getFrozenCredits().subtract(credits));
            account.setAvailableCredits(account.getAvailableCredits().add(credits));
            account.setUpdateTime(LocalDateTime.now());

            int result = accountMapper.update(account);
            if (result > 0) {
                return Result.success("学分解冻成功");
            } else {
                return Result.error("学分解冻失败");
            }
        } catch (Exception e) {
            return Result.error("解冻学分失败：" + e.getMessage());
        }
    }

    /**
     * 记录学分变动
     */
    private void recordCreditChange(Long userId, BigDecimal amount, Integer operationType, String creditType, String creditSource, String remark) {
        try {
            CreditRecord record = new CreditRecord();
            record.setUserId(userId);
            record.setCreditAmount(amount);
            record.setOperationType(operationType);
            record.setCreditType(creditType != null ? creditType : "系统操作");
            record.setCreditSource(creditSource != null ? creditSource : "系统自动");
            record.setStatus(1); // 有效
            record.setRemark(remark);
            record.setCreateTime(LocalDateTime.now());
            
            recordMapper.insert(record);
        } catch (Exception e) {
            // 记录失败不影响主业务，只记录日志
            System.err.println("记录学分变动失败：" + e.getMessage());
        }
    }
} 