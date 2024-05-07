package com.minzheng.blog.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.minzheng.blog.constant.ArticleConst;
import com.minzheng.blog.dao.ElasticsearchDao;
import com.minzheng.blog.dto.ArticleSearchDTO;
import com.minzheng.blog.entity.Article;
import com.minzheng.blog.service.ArticleService;
import com.minzheng.blog.utils.HTMLUtil;
import com.minzheng.blog.vo.ArticleVO;
import com.minzheng.blog.vo.DeleteVO;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 同步es数据
 *
 */
@Component
@RabbitListener(queues = "article")
public class MaxWellReceiver {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @RabbitHandler
    public void process(String messageJson) throws IOException {
        //初始化
        Article article;
        //获取更改信息
        Map<String, Object> map = JSON.parseObject(messageJson, Map.class);
        //判断操作类型
        String type = map.get("type").toString();
        switch (type) {
            case "insert":
                //发布文章后更新es文章
                //获取文章数据
                article = JSONObject.toJavaObject((JSONObject) map.get("data"), Article.class);
                //过滤markdown标签
                article.setArticleContent(HTMLUtil.deleteArticleTag(article.getArticleContent()));

                IndexRequest request = new IndexRequest("article").id(article.getId().toString());
                request.source(JSON.toJSONString(new ArticleSearchDTO(article)), XContentType.JSON);
                restHighLevelClient.index(request, RequestOptions.DEFAULT);
                break;
            case "delete":
                //物理删除文章
                DeleteVO deleteVO = JSONObject.toJavaObject((JSONObject) map.get("data"), DeleteVO.class);
                List<Integer> articleIDList = deleteVO.getIdList();
                for (Integer articleID : articleIDList) {
                    DeleteRequest deleteRequest = new DeleteRequest("article",articleID.toString());
                    restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
                }
                break;
            case "logic":
                //逻辑删除或恢复
                DeleteVO deleteVO2 = JSONObject.toJavaObject((JSONObject) map.get("data"), DeleteVO.class);
                Integer isDelete = deleteVO2.getIsDelete();
                List<Integer> idList = deleteVO2.getIdList();
                for (Integer articleId : idList) {
                    ArticleVO articleBackById = articleService.getArticleBackById(articleId);
                    if (articleBackById.getIsDraft() == ArticleConst.PUBLISH) {
                        UpdateRequest updateRequest2 = new UpdateRequest("article",articleId.toString());
                        updateRequest2.doc("isDelete", isDelete);
                        restHighLevelClient.update(updateRequest2,RequestOptions.DEFAULT);
                    }
                }
            default:
                break;
        }
    }
}