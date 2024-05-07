package com.minzheng.blog.dto;

import lombok.Data;

/**
 * 文章排行
 */
@Data
public class ArticleRankDTO {

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 浏览量
     */
    private Integer viewsCount;


    public ArticleRankDTO(String articleTitle, Integer viewsCount) {
        this.articleTitle = articleTitle;
        this.viewsCount = viewsCount;
    }

}
