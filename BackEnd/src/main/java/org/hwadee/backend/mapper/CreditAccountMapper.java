package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.CreditAccount;

import java.math.BigDecimal;
import java.util.List;

/**
 * 学分账户Mapper接口
 */
@Mapper
public interface CreditAccountMapper {

    /**
     * 根据用户ID查询学分账户
     */
    CreditAccount selectByUserId(@Param("userId") Long userId);

    /**
     * 插入学分账户
     */
    int insert(CreditAccount account);

    /**
     * 更新学分账户
     */
    int update(CreditAccount account);

    /**
     * 根据用户ID删除学分账户
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 统计所有账户数量
     */
    long countAllAccounts();

    /**
     * 获取学分排行榜
     */
    List<CreditAccount> selectTopUsersByCredits(@Param("limit") int limit);
    
    /**
     * 获取系统总学分
     */
    BigDecimal sumTotalCredits();
    
    /**
     * 获取系统可用学分总额
     */
    BigDecimal sumAvailableCredits();
    
    /**
     * 获取系统冻结学分总额
     */
    BigDecimal sumFrozenCredits();
} 