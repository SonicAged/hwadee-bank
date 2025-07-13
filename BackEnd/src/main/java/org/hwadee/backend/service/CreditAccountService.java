package org.hwadee.backend.service;

import org.hwadee.backend.entity.CreditAccount;
import org.hwadee.backend.utils.Result;

import java.math.BigDecimal;

/**
 * 学分账户服务接口
 */
public interface CreditAccountService {

    /**
     * 创建学分账户
     */
    Result<String> createAccount(Long userId);

    /**
     * 根据用户ID获取学分账户
     */
    Result<CreditAccount> getAccountByUserId(Long userId);

    /**
     * 增加学分
     */
    Result<String> addCredits(Long userId, BigDecimal credits, String creditType, String source, String remark);

    /**
     * 扣除学分
     */
    Result<String> deductCredits(Long userId, BigDecimal credits);

    /**
     * 冻结学分
     */
    Result<String> freezeCredits(Long userId, BigDecimal credits);

    /**
     * 解冻学分
     */
    Result<String> unfreezeCredits(Long userId, BigDecimal credits);
    
    /**
     * 获取系统中的总学分
     */
    Result<BigDecimal> getTotalCreditsInSystem();
    
    /**
     * 获取系统中的可用学分总和
     */
    Result<BigDecimal> getTotalAvailableCredits();
    
    /**
     * 获取系统中的冻结学分总和
     */
    Result<BigDecimal> getTotalFrozenCredits();
    
    /**
     * 获取系统中的学分账户总数
     */
    Result<Integer> getTotalAccountCount();
} 