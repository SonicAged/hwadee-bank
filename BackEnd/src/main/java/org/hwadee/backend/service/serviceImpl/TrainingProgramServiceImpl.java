package org.hwadee.backend.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.hwadee.backend.entity.PageResult;
import org.hwadee.backend.entity.TrainingParticipant;
import org.hwadee.backend.entity.TrainingProgram;
import org.hwadee.backend.mapper.TrainingProgramMapper;
import org.hwadee.backend.service.TrainingProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 培训项目服务实现类
 */
@Service
@Slf4j
public class TrainingProgramServiceImpl implements TrainingProgramService {

    @Autowired
    private TrainingProgramMapper trainingProgramMapper;

    @Override
    public PageResult<TrainingProgram> getProgramList(Integer page, Integer size, String programName, Integer programType, Integer status) {
        // 计算分页参数
        int offset = (page - 1) * size;
        
        // 查询列表
        List<TrainingProgram> list = trainingProgramMapper.selectProgramList(offset, size, programName, programType, status);
        
        // 查询总数
        int total = trainingProgramMapper.countPrograms(programName, programType, status);
        
        // 返回分页结果
        return new PageResult<>(list, total, page, size);
    }

    @Override
    public TrainingProgram getProgramById(Long programId) {
        return trainingProgramMapper.selectProgramById(programId);
    }

    @Override
    @Transactional
    public TrainingProgram createProgram(TrainingProgram program) {
        // 设置初始参与人数
        program.setCurrentParticipants(0);
        
        // 设置创建时间
        program.setCreateTime(LocalDateTime.now());
        program.setUpdateTime(LocalDateTime.now());
        
        // 插入数据
        trainingProgramMapper.insertProgram(program);
        return program;
    }

    @Override
    @Transactional
    public TrainingProgram updateProgram(TrainingProgram program) {
        // 设置更新时间
        program.setUpdateTime(LocalDateTime.now());
        
        // 更新数据
        trainingProgramMapper.updateProgram(program);
        return program;
    }

    @Override
    @Transactional
    public void deleteProgram(Long programId) {
        trainingProgramMapper.deleteProgram(programId);
    }

    @Override
    @Transactional
    public void enrollProgram(Long programId, Long userId) {
        // 检查用户是否已经报名
        Integer status = trainingProgramMapper.checkUserInProgram(programId, userId);
        if (status != null) {
            throw new RuntimeException("您已经报名了该培训项目");
        }
        
        // 检查项目是否存在且可以报名
        TrainingProgram program = trainingProgramMapper.selectProgramById(programId);
        if (program == null) {
            throw new RuntimeException("培训项目不存在");
        }
        
        // 检查是否已经超过报名截止时间
        if (program.getEnrollDeadline() != null && 
            program.getEnrollDeadline().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("已经超过报名截止时间");
        }
        
        // 检查是否已满员
        if (program.getMaxParticipants() != null && 
            program.getCurrentParticipants() >= program.getMaxParticipants()) {
            throw new RuntimeException("培训项目已满员");
        }
        
        // 创建参与记录
        TrainingParticipant participant = new TrainingParticipant();
        participant.setProgramId(programId);
        participant.setUserId(userId);
        participant.setStatus(TrainingParticipant.STATUS_ENROLLED); // 已报名
        participant.setEnrollTime(LocalDateTime.now());
        participant.setCreateTime(LocalDateTime.now());
        participant.setUpdateTime(LocalDateTime.now());
        
        // 保存参与记录
        trainingProgramMapper.insertParticipant(participant);
        
        // 更新参与人数
        trainingProgramMapper.updateParticipantCount(programId);
    }

    @Override
    @Transactional
    public void cancelEnrollment(Long programId, Long userId) {
        // 检查用户是否已经报名
        TrainingParticipant participant = trainingProgramMapper.selectParticipant(programId, userId);
        if (participant == null) {
            throw new RuntimeException("您尚未报名该培训项目");
        }
        
        // 检查状态是否允许取消
        if (participant.getStatus() > TrainingParticipant.STATUS_CONFIRMED) {
            throw new RuntimeException("当前状态不允许取消报名");
        }
        
        // 更新状态为已取消
        trainingProgramMapper.updateParticipantStatus(participant.getId(), TrainingParticipant.STATUS_CANCELLED);
        
        // 更新参与人数
        trainingProgramMapper.updateParticipantCount(programId);
        
        // 确保参与人数更新成功
        log.info("用户 {} 取消报名培训项目 {}, 参与记录ID: {}", userId, programId, participant.getId());
    }

    @Override
    @Transactional
    public void confirmParticipation(Long participantId) {
        // 更新状态为已确认
        trainingProgramMapper.updateParticipantStatus(participantId, TrainingParticipant.STATUS_CONFIRMED);
    }

    @Override
    @Transactional
    public void completeTraining(Long participantId) {
        // 更新状态为已完成
        trainingProgramMapper.updateParticipantStatus(participantId, TrainingParticipant.STATUS_COMPLETED);
    }

    @Override
    public PageResult<Map<String, Object>> getUserPrograms(Long userId, Integer page, Integer size, Integer status) {
        // 计算分页参数
        int offset = (page - 1) * size;
        
        // 查询列表
        List<Map<String, Object>> list = trainingProgramMapper.selectUserPrograms(userId, offset, size, status);
        
        // 查询总数
        int total = trainingProgramMapper.countUserPrograms(userId, status);
        
        // 返回分页结果
        return new PageResult<>(list, total, page, size);
    }

    @Override
    public PageResult<Map<String, Object>> getProgramParticipants(Long programId, Integer page, Integer size, Integer status) {
        // 计算分页参数
        int offset = (page - 1) * size;
        
        // 查询列表
        List<Map<String, Object>> list = trainingProgramMapper.selectProgramParticipants(programId, offset, size, status);
        
        // 查询总数
        int total = trainingProgramMapper.countProgramParticipants(programId, status);
        
        // 返回分页结果
        return new PageResult<>(list, total, page, size);
    }

    @Override
    public boolean checkUserInProgram(Long programId, Long userId) {
        Integer status = trainingProgramMapper.checkUserInProgram(programId, userId);
        return status != null;
    }

    @Override
    public Map<String, Object> getProgramStatistics(Long programId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取项目信息
        TrainingProgram program = trainingProgramMapper.selectProgramById(programId);
        if (program != null) {
            result.put("programId", program.getProgramId());
            result.put("programName", program.getProgramName());
            result.put("currentParticipants", program.getCurrentParticipants());
            result.put("maxParticipants", program.getMaxParticipants());
            
            // 获取各状态参与者数量
            int enrolledCount = trainingProgramMapper.countProgramParticipants(programId, TrainingParticipant.STATUS_ENROLLED);
            int confirmedCount = trainingProgramMapper.countProgramParticipants(programId, TrainingParticipant.STATUS_CONFIRMED);
            int completedCount = trainingProgramMapper.countProgramParticipants(programId, TrainingParticipant.STATUS_COMPLETED);
            int cancelledCount = trainingProgramMapper.countProgramParticipants(programId, TrainingParticipant.STATUS_CANCELLED);
            
            result.put("enrolledCount", enrolledCount);
            result.put("confirmedCount", confirmedCount);
            result.put("completedCount", completedCount);
            result.put("cancelledCount", cancelledCount);
            
            // 计算完成率
            double completionRate = 0;
            if (program.getCurrentParticipants() > 0) {
                completionRate = (double) completedCount / program.getCurrentParticipants() * 100;
            }
            result.put("completionRate", String.format("%.2f", completionRate) + "%");
        }
        
        return result;
    }

    @Override
    public List<TrainingProgram> getLatestPrograms(Integer limit) {
        // 获取最新培训项目（默认按创建时间倒序）
        return trainingProgramMapper.selectProgramList(0, limit, null, null, null);
    }
} 