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
     * 更新学分账户
     */
    Result<String> updateAccount(CreditAccount account);

    /**
     * 增加学分
     */
    Result<String> addCredits(Long userId, BigDecimal credits);

    /**
     * 增加学分（带详细信息）
     */
    Result<String> addCredits(Long userId, BigDecimal credits, String creditType, String creditSource, String remark);

    /**
     * 消费学分
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
} 