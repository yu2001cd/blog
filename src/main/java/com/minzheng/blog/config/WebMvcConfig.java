package com.minzheng.blog.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * mvc配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

}
/**
 * 这段代码是一个Spring Boot应用程序中的配置类，用于自定义Web MVC的配置。这个类实现了WebMvcConfigurer接口，允许开发者通过重写接口中的方法来配置Spring MVC的各个方面。下面是对这段代码的详细解释：
 *
 * @Configuration注解：这表明这个类是一个Spring配置类，Spring容器会在启动时加载这个类，并执行其中定义的Bean配置。
 *
 * WebMvcConfig类：这是自定义的配置类名称。
 *
 * implements WebMvcConfigurer：这表明WebMvcConfig类实现了WebMvcConfigurer接口，从而可以自定义Spring MVC的配置。
 *
 * addResourceHandlers(ResourceHandlerRegistry registry)方法：这个方法用于添加资源处理器，告诉Spring MVC如何处理特定的资源请求。在这个例子中，它配置了对于/swagger-ui.html路径的请求，资源应该从classpath:/META-INF/resources/目录中获取。这通常用于将静态资源（如HTML、CSS、JavaScript文件）映射到Web应用程序的URL路径上。
 *
 * addCorsMappings(CorsRegistry registry)方法：这个方法用于配置跨源资源共享（CORS）的规则。CORS是一种机制，它允许Web应用程序服务器指示任何来源的Web页面都可以安全地访问它的API。在这个例子中，它配置了对于所有路径（"/**"）的CORS规则，允许任何来源（allowedOrigins("*")）、任何HTTP方法（allowedMethods("*")）和任何HTTP头（allowedHeaders("*")），并且允许携带凭证（如cookies和HTTP认证信息，通过allowCredentials(true)）。
 *
 * 这种配置通常用于开发环境中，因为它允许任何域都可以访问你的API，这在开发和测试时很有用。然而，在生产环境中，你应该限制allowedOrigins到特定的域名，以增强安全性。
 *
 * 通过这样的配置，你的Spring Boot应用程序将能够提供静态资源，并且支持跨域请求，这对于构建RESTful API和单页应用程序（SPA）特别有用。
 */