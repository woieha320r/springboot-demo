package pri.demo.springboot.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity配置类
 *
 * @author woieha320r
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 修改此值以使得验证时间在1秒左右
        return new BCryptPasswordEncoder(10);
    }

    /**
     * 忽略所有路径，不使用SpringSecurity过滤器链，由jwt拦截器负责
     */
    @Bean
    public WebSecurityCustomizer webSecurity() {
        return web -> web.ignoring().anyRequest();
    }

}
