package com.minzheng.blog.controller;


import com.minzheng.blog.constant.PathConst;
import com.minzheng.blog.constant.StatusConst;
import com.minzheng.blog.dto.*;
import com.minzheng.blog.service.ArticleService;
import com.minzheng.blog.service.FileStorageService;
import com.minzheng.blog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Api(tags = "文章模块")
@RestController
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private FileStorageService fileStorageService;

    @ApiOperation(value = "查看文章归档")
    @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "Long")
    @GetMapping("/articles/archives")
    private Result<PageDTO<ArchiveDTO>> listArchives(Long current) {
        return new Result(true, StatusConst.OK, "查询成功", articleService.listArchives(current));
    }

    @ApiOperation(value = "查看首页文章")
    @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "Long")
    @GetMapping("/articles")
    private Result<List<ArticleHomeDTO>> listArticles(Long current) {
        return new Result(true, StatusConst.OK, "查询成功", articleService.listArticles(current));
    }

    @ApiOperation(value = "查看后台文章")
    @GetMapping("/admin/articles")
    private Result<PageDTO<ArticleBackDTO>> listArticleBackDTO(ConditionVO conditionVO) {
        return new Result(true, StatusConst.OK, "查询成功", articleService.listArticleBackDTO(conditionVO));
    }

    @ApiOperation(value = "查看文章选项")
    @GetMapping("/admin/articles/options")
    private Result<ArticleOptionDTO> listArticleOptionDTO() {
        return new Result(true, StatusConst.OK, "查询成功", articleService.listArticleOptionDTO());
    }

    @ApiOperation(value = "添加或修改文章")
    @PostMapping("/admin/articles")
    private Result saveArticle(@Valid @RequestBody ArticleVO articleVO) {
        articleService.saveOrUpdateArticle(articleVO);
        return new Result(true, StatusConst.OK, "操作成功");
    }

    @ApiOperation(value = "修改文章置顶")
    @PutMapping("/admin/articles/top/{articleId}")
    private Result updateArticleTop(@PathVariable("articleId") Integer articleId, Integer isTop) {
        articleService.updateArticleTop(articleId, isTop);
        return new Result(true, StatusConst.OK, "修改成功");
    }

    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/articles/images")
    private Result<String> saveArticleImages(MultipartFile file) throws IOException {
        //参数为 文件，图片路径
//        return new Result(true, StatusConst.OK, "上传成功", OSSUtil.upload(file, PathConst.ARTICLE));
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = file.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));//.jpg
        return new Result(true, StatusConst.OK, "上传成功", fileStorageService.uploadImgFile(PathConst.ARTICLE,fileName+postfix,file.getInputStream()));
    }

    @ApiOperation(value = "恢复或删除文章")
    @PutMapping("/admin/articles")
    private Result updateArticleDelete(DeleteVO deleteVO) {
        articleService.updateArticleDelete(deleteVO);
        return new Result(true, StatusConst.OK, "操作成功");
    }

    @ApiOperation(value = "物理删除文章")
    @DeleteMapping("/admin/articles")
    private Result deleteArticles(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticles(articleIdList);
        return new Result(true, StatusConst.OK, "操作成功！");
    }

    @ApiOperation(value = "根据id查看后台文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/admin/articles/{articleId}")
    private Result<ArticleVO> getArticleBackById(@PathVariable("articleId") Integer articleId) {
        return new Result(true, StatusConst.OK, "查询成功", articleService.getArticleBackById(articleId));
    }

    @ApiOperation(value = "根据id查看文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/articles/{articleId}")
    private Result<ArticleDTO> getArticleById(@PathVariable("articleId") Integer articleId) {
        return new Result(true, StatusConst.OK, "查询成功", articleService.getArticleById(articleId));
    }

    @ApiOperation(value = "搜索文章")
    @GetMapping("/articles/search")
    private Result<List<ArticleSearchDTO>> listArticlesBySearch(ConditionVO condition) {
        return new Result(true, StatusConst.OK, "查询成功", articleService.listArticlesBySearch(condition));
    }

    @ApiOperation(value = "点赞文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @PostMapping("/articles/like")
    private Result saveArticleLike(Integer articleId) {
        articleService.saveArticleLike(articleId);
        return new Result(true, StatusConst.OK, "点赞成功");
    }

}

