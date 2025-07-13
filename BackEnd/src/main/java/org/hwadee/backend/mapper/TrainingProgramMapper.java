package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.TrainingProgram;
import org.hwadee.backend.entity.TrainingParticipant;

import java.util.List;
import java.util.Map;

@Mapper
public interface TrainingProgramMapper {
    
    /**
     * 查询培训项目列表
     */
    List<TrainingProgram> selectProgramList(@Param("offset") int offset, 
                                           @Param("limit") int limit,
                                           @Param("programName") String programName,
                                           @Param("programType") Integer programType,
                                           @Param("status") Integer status);
    
    /**
     * 统计培训项目总数
     */
    int countPrograms(@Param("programName") String programName,
                     @Param("programType") Integer programType,
                     @Param("status") Integer status);
    
    /**
     * 根据ID查询培训项目
     */
    TrainingProgram selectProgramById(Long programId);
    
    /**
     * 创建培训项目
     */
    int insertProgram(TrainingProgram program);
    
    /**
     * 更新培训项目
     */
    int updateProgram(TrainingProgram program);
    
    /**
     * 删除培训项目
     */
    int deleteProgram(Long programId);
    
    /**
     * 添加培训参与者
     */
    int insertParticipant(TrainingParticipant participant);
    
    /**
     * 更新参与者状态
     */
    int updateParticipantStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 查询用户参与的培训项目
     */
    List<Map<String, Object>> selectUserPrograms(@Param("userId") Long userId,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit,
                                               @Param("status") Integer status);
    
    /**
     * 统计用户参与的培训项目总数
     */
    int countUserPrograms(@Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 查询培训项目参与者列表
     */
    List<Map<String, Object>> selectProgramParticipants(@Param("programId") Long programId,
                                                      @Param("offset") int offset,
                                                      @Param("limit") int limit,
                                                      @Param("status") Integer status);
    
    /**
     * 统计培训项目参与者总数
     */
    int countProgramParticipants(@Param("programId") Long programId, @Param("status") Integer status);
    
    /**
     * 检查用户是否已经参与培训项目
     */
    Integer checkUserInProgram(@Param("programId") Long programId, @Param("userId") Long userId);
    
    /**
     * 根据项目ID和用户ID查询参与记录
     */
    TrainingParticipant selectParticipant(@Param("programId") Long programId, @Param("userId") Long userId);
    
    /**
     * 更新培训项目参与人数
     */
    int updateParticipantCount(Long programId);
} 