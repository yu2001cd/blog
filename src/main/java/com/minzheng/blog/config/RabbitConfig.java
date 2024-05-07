package com.minzheng.blog.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbitmq配置类
 *
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue insertDirectQueue() {
        return new Queue("article", true);
    }

    @Bean
    FanoutExchange maxWellExchange() {
        return new FanoutExchange("maxwell", false, false);
    }

    @Bean
    Binding bindingArticleDirect() {
        return BindingBuilder.bind(insertDirectQueue()).to(maxWellExchange());
    }

    //消息转换器
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory myRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

}
