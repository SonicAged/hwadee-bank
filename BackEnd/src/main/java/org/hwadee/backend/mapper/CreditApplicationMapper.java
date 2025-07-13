package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.CreditApplication;

import java.util.List;

/**
 * 学分申请Mapper接口
 */
@Mapper
public interface CreditApplicationMapper {

    /**
     * 插入学分申请
     */
    int insert(CreditApplication application);

    /**
     * 根据ID查询
     */
    CreditApplication selectById(Long applicationId);

    /**
     * 根据用户ID查询申请列表
     */
    List<CreditApplication> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询所有申请（分页）
     */
    List<CreditApplication> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据状态查询申请
     */
    List<CreditApplication> selectByStatus(@Param("status") Integer status);

    /**
     * 更新申请状态
     */
    int updateStatus(@Param("applicationId") Long applicationId, @Param("status") Integer status, @Param("remark") String remark);

    /**
     * 更新申请信息
     */
    int update(CreditApplication application);

    /**
     * 删除申请
     */
    int deleteById(Long applicationId);

    /**
     * 统计申请数量
     */
    int countAll();

    /**
     * 根据条件统计
     */
    int countByStatus(@Param("status") Integer status);

    /**
     * 统计用户申请数量
     */
    int countByUserId(@Param("userId") Long userId);

    /**
     * 统计用户特定状态的申请数量
     */
    int countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 根据条件搜索申请列表
     */
    List<CreditApplication> searchByCondition(@Param("userId") Long userId, @Param("applicationType") String applicationType, @Param("achievementName") String achievementName, @Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据条件统计申请数量
     */
    long countByCondition(@Param("userId") Long userId, @Param("applicationType") String applicationType, @Param("achievementName") String achievementName, @Param("status") Integer status);
} 