package com.minzheng.blog.config;

import com.minzheng.blog.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security配置类
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;
    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailHandlerImpl authenticationFailHandler;
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * 密码加密
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置权限
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailHandler).and()
                .logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler).and()
                .authorizeRequests()
                //开放测试账号权限
                .antMatchers(HttpMethod.GET,"/admin/**").hasAnyRole("test","admin")
                //管理员页面需要权限
                .antMatchers("/admin/**").hasRole("admin")
                //用户操作，发表评论需要登录
                .antMatchers("/users/info","/users/avatar").authenticated()
                .antMatchers(HttpMethod.POST, "/comments/**").authenticated()
                .anyRequest().permitAll()
                .and()
                //关闭跨站请求防护
                .csrf().disable().exceptionHandling()
                //未登录处理
                .authenticationEntryPoint(authenticationEntryPoint)
                //权限不足处理
                .accessDeniedHandler(accessDeniedHandler).and()
                //开启嵌入
                .headers().frameOptions().disable();
    }

}
/**
 *这段代码是一个Spring Boot应用程序中的安全配置类，使用了Spring Security框架来提供认证和授权功能。`WebSecurityConfigurerAdapter`是一个方便的基类，它提供了默认的配置，并且允许开发者通过重写方法来自定义安全性配置。下面是对这段代码的详细解释：
 *
 * 1. `@Configuration`注解：表明这个类是一个Spring配置类，Spring容器会在启动时加载这个类，并执行其中定义的Bean配置。
 *
 * 2. `@EnableWebSecurity`注解：启用Spring Security的Web安全性配置。
 *
 * 3. `WebSecurityConfig`类：这是自定义的配置类名称，它继承自`WebSecurityConfigurerAdapter`。
 *
 * 4. `@Autowired`注解：自动注入自定义的`AuthenticationEntryPoint`、`AccessDeniedHandler`、`AuthenticationSuccessHandler`、`AuthenticationFailHandler`和`LogoutSuccessHandler`的实例。这些都是Spring Security中用于处理认证过程中不同事件的组件。
 *
 * 5. `PasswordEncoder`接口的实现：`BCryptPasswordEncoder`是一个密码编码器，用于将明文密码转换为加密后的密码。这是为了安全起见，防止密码以明文形式存储。
 *
 * 6. `configure(HttpSecurity http)`方法：这是`WebSecurityConfigurerAdapter`中的一个方法，用于配置HTTP安全性。在这个例子中，配置了以下内容：
 *    - 表单登录（`formLogin`），指定登录处理URL为`/login`，并设置了成功和失败处理器。
 *    - 退出登录（`logout`），指定退出URL为`/logout`，并设置了退出成功后的处理器。
 *    - 授权请求（`authorizeRequests`），定义了不同URL路径需要的权限：
 *      - 对于`/admin/**`路径的GET请求，用户必须拥有`test`或`admin`角色之一。
 *      - 对于`/admin/**`路径的所有请求，用户必须拥有`admin`角色。
 *      - 对于`/users/info`、`/users/avatar`和`/comments`路径的所有请求，用户必须已经认证（即登录）。
 *      - 其他所有请求都允许访问。
 *    - 禁用了跨站请求防护（`csrf().disable()`），这在某些情况下可能不安全，但可能对于不需要CSRF保护的API是必要的。
 *    - 异常处理，设置了未登录时的`AuthenticationEntryPoint`和权限不足时的`AccessDeniedHandler`。
 *    - 允许嵌入，禁用了`X-Frame-Options`头部的默认行为，这通常用于防止点击劫持攻击。
 *
 * 通过这样的配置，你的应用程序将能够对用户进行认证和授权，确保只有拥有适当权限的用户才能访问特定的资源。这对于构建安全的Web应用程序来说是非常重要的。然而，需要注意的是，在生产环境中，应该启用CSRF保护，并且应该根据应用程序的具体需求来配置权限。
 */
