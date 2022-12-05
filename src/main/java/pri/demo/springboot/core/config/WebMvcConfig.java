package pri.demo.springboot.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC配置
 *
 * @author woieha320r
 */
@Configuration(proxyBeanMethods = false)
public class WebMvcConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer(
            @Value("${server.servlet.context-path}") String contextPath,
            @Autowired HandlerInterceptor[] interceptors
    ) {
        return new WebMvcConfigurer() {

            //跨域能否成功，取决于被访问站是否返回"Access-Control-Allow-Origin: 协议://来源站"响应头。
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //当启用allowCredentials时，allOrigins不能是*，默认值就可以，所有这里没再配置
                registry.addMapping(contextPath + "/**");
            }

            //注册所有拦截器，否则都不会生效
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                for (HandlerInterceptor interceptor : interceptors) {
                    registry.addInterceptor(interceptor);
                }
            }

            //枚举code -> 枚举
            // @Override
            // public void addFormatters(FormatterRegistry registry) {
            //     registry.addConverterFactory(new ConverterFactory<Integer, BaseEnum>() {
            //         private final Map<Class<? extends BaseEnum>, Converter> map = new HashMap<>();
            //
            //         @Override
            //         public <T extends BaseEnum> Converter<Integer, T> getConverter(Class<T> targetType) {
            //             Converter<Integer, T> returnVal = map.get(targetType);
            //             if (Objects.isNull(returnVal)) {
            //                 returnVal = new CodeToEnumConverter<>(targetType);
            //                 map.put(targetType, returnVal);
            //             }
            //             return returnVal;
            //         }
            //     });
            // }
        };
    }

}
