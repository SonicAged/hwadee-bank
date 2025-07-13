package org.hwadee.backend.service;

import org.hwadee.backend.entity.PageResult;
import org.hwadee.backend.entity.TrainingProgram;
import org.hwadee.backend.entity.TrainingParticipant;

import java.util.List;
import java.util.Map;

/**
 * 培训项目服务接口
 */
public interface TrainingProgramService {
    
    /**
     * 分页查询培训项目列表
     */
    PageResult<TrainingProgram> getProgramList(Integer page, Integer size, String programName, Integer programType, Integer status);
    
    /**
     * 根据ID查询培训项目详情
     */
    TrainingProgram getProgramById(Long programId);
    
    /**
     * 创建培训项目
     */
    TrainingProgram createProgram(TrainingProgram program);
    
    /**
     * 更新培训项目
     */
    TrainingProgram updateProgram(TrainingProgram program);
    
    /**
     * 删除培训项目
     */
    void deleteProgram(Long programId);
    
    /**
     * 报名参加培训项目
     */
    void enrollProgram(Long programId, Long userId);
    
    /**
     * 取消报名
     */
    void cancelEnrollment(Long programId, Long userId);
    
    /**
     * 确认参与培训
     */
    void confirmParticipation(Long participantId);
    
    /**
     * 完成培训
     */
    void completeTraining(Long participantId);
    
    /**
     * 查询用户参与的培训项目
     */
    PageResult<Map<String, Object>> getUserPrograms(Long userId, Integer page, Integer size, Integer status);
    
    /**
     * 查询培训项目参与者
     */
    PageResult<Map<String, Object>> getProgramParticipants(Long programId, Integer page, Integer size, Integer status);
    
    /**
     * 检查用户是否已经参与培训项目
     */
    boolean checkUserInProgram(Long programId, Long userId);
    
    /**
     * 获取培训项目统计信息
     */
    Map<String, Object> getProgramStatistics(Long programId);
    
    /**
     * 获取最新培训项目
     */
    List<TrainingProgram> getLatestPrograms(Integer limit);
} 