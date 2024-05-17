package com.minzheng.blog.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    @Bean
    public RestHighLevelClient buildRestHighLevelClient(){
        RestHighLevelClient client= new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.231.128:9200")
        ));
        return client;
    }

}
