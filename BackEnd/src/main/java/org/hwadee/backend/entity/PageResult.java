package org.hwadee.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    /**
     * 数据列表
     */
    private List<T> list;
    
    /**
     * 总记录数
     */
    private long total;
    
    /**
     * 当前页码
     */
    private int page;
    
    /**
     * 每页大小
     */
    private int size;

    /**
     * 当前页码
     */
    public static <T> PageResult<T> of(List<T> list, long total, int page, int size) {
        return new PageResult<T>(list, total, page, size);
    }
} 