package org.hwadee.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hwadee.backend.entity.ResourceCategory;

import java.util.List;

/**
 * 资源分类Mapper接口
 */
@Mapper
public interface ResourceCategoryMapper {

    /**
     * 插入资源分类
     */
    int insert(ResourceCategory category);

    /**
     * 根据分类ID查询资源分类
     */
    ResourceCategory selectById(@Param("categoryId") Long categoryId);

    /**
     * 更新资源分类
     */
    int update(ResourceCategory category);

    /**
     * 根据分类ID删除资源分类
     */
    int deleteById(@Param("categoryId") Long categoryId);

    /**
     * 查询所有资源分类
     */
    List<ResourceCategory> selectAll();

    /**
     * 根据父级ID查询子分类
     */
    List<ResourceCategory> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 查询所有顶级分类
     */
    List<ResourceCategory> selectTopLevel();

    /**
     * 查询分类树结构
     */
    List<ResourceCategory> selectTree();

    /**
     * 根据分类名称查询
     */
    List<ResourceCategory> selectByName(@Param("categoryName") String categoryName);

    /**
     * 根据状态查询分类
     */
    List<ResourceCategory> selectByStatus(@Param("status") Integer status);

    /**
     * 统计分类下的资源数量
     */
    int countResourcesByCategory(@Param("categoryId") Long categoryId);
} 