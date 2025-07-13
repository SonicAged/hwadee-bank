package org.hwadee.backend.service.serviceImpl;

import org.hwadee.backend.entity.CreditRecord;
import org.hwadee.backend.mapper.CreditRecordMapper;
import org.hwadee.backend.service.CreditRecordService;
import org.hwadee.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学分记录服务实现类
 */
@Service
public class CreditRecordServiceImpl implements CreditRecordService {

    @Autowired
    private CreditRecordMapper recordMapper;

    @Override
    public Result<List<CreditRecord>> getUserRecords(Long userId, int page, int size) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            if (page < 1) page = 1;
            if (size < 1) size = 10;
            int offset = (page - 1) * size;

            List<CreditRecord> records = recordMapper.selectByUserId(userId, offset, size);
            return Result.success(records);
        } catch (Exception e) {
            return Result.error("查询学分记录失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<CreditRecord>> searchRecords(String creditType, Integer operationType, Integer status, int page, int size) {
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            int offset = (page - 1) * size;

            List<CreditRecord> records = recordMapper.searchByCondition(creditType, operationType, status, offset, size);
            return Result.success(records);
        } catch (Exception e) {
            return Result.error("搜索学分记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<String> createRecord(CreditRecord record) {
        try {
            if (record.getUserId() == null) {
                return Result.error("用户ID不能为空");
            }
            if (record.getCreditType() == null || record.getCreditType().trim().isEmpty()) {
                return Result.error("学分类型不能为空");
            }
            if (record.getCreditAmount() == null) {
                return Result.error("学分数量不能为空");
            }
            
            record.setCreateTime(LocalDateTime.now());
            if (record.getStatus() == null) {
                record.setStatus(1); // 默认有效
            }

            int result = recordMapper.insert(record);
            if (result > 0) {
                return Result.success("创建学分记录成功");
            } else {
                return Result.error("创建学分记录失败");
            }
        } catch (Exception e) {
            return Result.error("创建学分记录失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Integer> getRecordCount() {
        try {
            long count = recordMapper.countAllRecords();
            return Result.success((int) count);
        } catch (Exception e) {
            return Result.error("获取记录总数失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Integer> getRecordCountByUserId(Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }

            long count = recordMapper.countByUserId(userId);
            return Result.success((int) count);
        } catch (Exception e) {
            return Result.error("获取用户记录总数失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Integer> getRecordCountByOperationType(Integer operationType) {
        try {
            if (operationType == null) {
                return Result.error("操作类型不能为空");
            }

            long count = recordMapper.countByOperationType(operationType);
            return Result.success((int) count);
        } catch (Exception e) {
            return Result.error("获取记录统计失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Integer> getRecordCountByUserIdAndOperationType(Long userId, Integer operationType) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            if (operationType == null) {
                return Result.error("操作类型不能为空");
            }

            long count = recordMapper.countByUserIdAndOperationType(userId, operationType);
            return Result.success((int) count);
        } catch (Exception e) {
            return Result.error("获取用户操作记录统计失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getCreditTypeDistribution(Long userId) {
        try {
            Map<String, Object> distribution = new HashMap<>();
            
            if (userId != null) {
                // 获取用户的学分类型分布
                Map<String, Integer> typeDistribution = recordMapper.getCreditTypeDistributionByUserId(userId);
                
                // 计算百分比
                int total = 0;
                for (Integer value : typeDistribution.values()) {
                    total += value;
                }
                
                Map<String, Integer> percentMap = new HashMap<>();
                if (total > 0) {
                    for (Map.Entry<String, Integer> entry : typeDistribution.entrySet()) {
                        int percent = (int) Math.round((entry.getValue() * 100.0) / total);
                        percentMap.put(entry.getKey(), percent);
                    }
                }
                
                distribution.put("typeDistribution", percentMap);
            } else {
                // 获取系统整体学分类型分布
                Map<String, Integer> typeDistribution = recordMapper.getCreditTypeDistribution();
                
                // 计算百分比
                int total = 0;
                for (Integer value : typeDistribution.values()) {
                    total += value;
                }
                
                Map<String, Integer> percentMap = new HashMap<>();
                if (total > 0) {
                    for (Map.Entry<String, Integer> entry : typeDistribution.entrySet()) {
                        int percent = (int) Math.round((entry.getValue() * 100.0) / total);
                        percentMap.put(entry.getKey(), percent);
                    }
                }
                
                distribution.put("typeDistribution", percentMap);
            }
            
            return Result.success(distribution);
        } catch (Exception e) {
            return Result.error("获取学分类型分布失败：" + e.getMessage());
        }
    }
} 