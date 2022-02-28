package com.example.springbootwebdemo.core.security;

import cn.hutool.core.util.ArrayUtil;
import com.example.springbootwebdemo.core.property.TokenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security配置类
 *
  * @date 2021-11-12
 */
@Configuration
//开启全局方法执行前检查权限，否则方法的@PreAuthorize拦截无效
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsServiceImpl userDetailsService;

    @Autowired
    private MyPasswordEncoder passwordEncoder;

    @Autowired
    private CheckJwtFilter checkJwtFilter;

    @Autowired
    private TokenProperties tokenProperties;

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(this.userDetailsService)
                .passwordEncoder(this.passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 使用的是JWT，不需要csrf
                .csrf().disable()
                // 基于token，不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                // 跨域前的OPTION，放行
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                // 配置文件配置的无需token认证的接口，放行
                .antMatchers(ArrayUtil.toArray(tokenProperties.getIgnorePath(), String.class)).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                // 在用户名密码验证过滤器前先执行用于验证jwt-token的过滤器
                .and().addFilterBefore(this.checkJwtFilter, UsernamePasswordAuthenticationFilter.class)
                // 禁用缓存
                .headers().cacheControl();
    }

}
