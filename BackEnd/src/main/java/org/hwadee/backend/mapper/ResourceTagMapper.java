package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.ResourceTag;

import java.util.List;

/**
 * 资源标签Mapper接口
 */
@Mapper
public interface ResourceTagMapper {

    /**
     * 插入资源标签
     */
    int insert(ResourceTag tag);

    /**
     * 根据标签ID查询资源标签
     */
    ResourceTag selectById(@Param("tagId") Long tagId);

    /**
     * 更新资源标签
     */
    int update(ResourceTag tag);

    /**
     * 根据标签ID删除资源标签
     */
    int deleteById(@Param("tagId") Long tagId);

    /**
     * 查询所有资源标签
     */
    List<ResourceTag> selectAll();

    /**
     * 根据标签名称查询
     */
    ResourceTag selectByName(@Param("tagName") String tagName);

    /**
     * 根据状态查询标签
     */
    List<ResourceTag> selectByStatus(@Param("status") Integer status);

    /**
     * 获取热门标签
     */
    List<ResourceTag> selectPopularTags(@Param("limit") int limit);

    /**
     * 更新标签使用次数
     */
    int updateUseCount(@Param("tagId") Long tagId);

    /**
     * 根据资源ID查询标签
     */
    List<ResourceTag> selectByResourceId(@Param("resourceId") Long resourceId);

    /**
     * 为资源添加标签
     */
    int addResourceTag(@Param("resourceId") Long resourceId, 
                      @Param("tagId") Long tagId);

    /**
     * 移除资源标签
     */
    int removeResourceTag(@Param("resourceId") Long resourceId, 
                         @Param("tagId") Long tagId);

    /**
     * 批量为资源添加标签
     */
    int batchAddResourceTags(@Param("resourceId") Long resourceId, 
                            @Param("tagIds") List<Long> tagIds);

    /**
     * 清空资源的所有标签
     */
    int clearResourceTags(@Param("resourceId") Long resourceId);
} 