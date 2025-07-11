package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.Certificate;

import java.util.List;

/**
 * 证书Mapper接口
 */
@Mapper
public interface CertificateMapper {

    /**
     * 插入证书
     */
    int insert(Certificate certificate);

    /**
     * 根据ID查询
     */
    Certificate selectById(Long certificateId);

    /**
     * 根据用户ID查询证书列表
     */
    List<Certificate> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据证书编号查询
     */
    Certificate selectByCertificateNumber(@Param("certificateNumber") String certificateNumber);

    /**
     * 查询所有证书（分页）
     */
    List<Certificate> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 更新证书信息
     */
    int update(Certificate certificate);

    /**
     * 删除证书
     */
    int deleteById(Long certificateId);

    /**
     * 统计证书数量
     */
    int countAll();

    /**
     * 根据用户ID统计证书数量
     */
    int countByUserId(@Param("userId") Long userId);
} 