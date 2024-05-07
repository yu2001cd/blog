package com.minzheng.blog.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
/**
这段代码是一个Spring Boot应用程序中的一个配置类，用于设置`RedisTemplate`的序列化器。`RedisTemplate`是Spring Data Redis提供的一个高级抽象，它使得Redis的操作变得更加简单。下面是对这段代码的详细解释：

1. `@Configuration`注解：这表明这个类是一个Spring配置类，Spring容器会在启动时加载这个类，并执行其中定义的Bean。

2. `RedisConfig`类：这是自定义的配置类名称。

3. `redisTemplate(RedisConnectionFactory factory)`方法：这是一个工厂方法，用于创建`RedisTemplate<String, Object>`类型的Bean。`RedisConnectionFactory`是Redis连接的工厂，它提供了创建连接的方法。

4. `Jackson2JsonRedisSerializer`：这是一个由Jackson库提供的序列化器，用于将对象序列化为JSON格式的字符串。这里创建了一个`Jackson2JsonRedisSerializer`实例，并设置了其对象映射器`ObjectMapper`。

5. `ObjectMapper`：这是Jackson库中用于配置JSON序列化和反序列化的类。在这段代码中，`ObjectMapper`被配置为允许访问所有属性，并且启用了非最终类型的默认类型识别。

6. `StringRedisSerializer`：这是Redis的默认序列化器，用于将对象序列化为字符串。这里创建了一个`StringRedisSerializer`实例。

7. `setKeySerializer`和`setHashKeySerializer`：这些方法用于设置键（key）的序列化方式。在这个例子中，键的序列化方式被设置为`StringRedisSerializer`。

8. `setValueSerializer`和`setHashValueSerializer`：这些方法用于设置值（value）的序列化方式。在这个例子中，值的序列化方式被设置为`Jackson2JsonRedisSerializer`，这意味着所有的值（包括存储在Redis中的普通值和Hash值）都将使用Jackson进行序列化。

9. `redisTemplate.afterPropertiesSet()`：这是一个生命周期方法，它在所有属性设置完成后被调用，允许`RedisTemplate`执行任何必要的初始化。

通过这样的配置，你的应用程序将能够使用JSON格式来序列化和反序列化存储在Redis中的对象，同时使用字符串作为键。这是一种常见的做法，因为它提供了良好的灵活性和可读性，同时也能够处理复杂的对象结构。
 */