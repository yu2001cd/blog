package com.minzheng.blog.dto;

import lombok.Data;

/**
 * 后台分类列表
 */
@Data
public class CategoryBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;
}
