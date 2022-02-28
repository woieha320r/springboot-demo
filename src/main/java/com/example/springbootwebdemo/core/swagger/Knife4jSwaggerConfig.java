package com.example.springbootwebdemo.core.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * knife4j增强swagger配置类
 * 访问knife4j的路径：/doc.html，需要在鉴权时放行/v2/api-docs,/swagger-resources
 *
  * @date 2022-02-22
 */
@Configuration
@EnableSwagger2
//开启增强
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class Knife4jSwaggerConfig {

    /**
     * TODO:不知道为啥必须有一个组名叫SpringBootWebDemo的，一改名儿就报Knife4j文档请求异常。而且按照官方文档排查不出问题
     */
    @Bean
    public Docket allDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("SpringBootWebDemo")
                .select()
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * core分组
     */
    @Bean
    public Docket coreDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //不显示这个类型的入参
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                //分组名称
                .groupName("核心")
                .select()
                //指定扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.springbootwebdemo.core"))
                //不显示/error开头路径的api
                .paths((String path) -> !path.startsWith("/error"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * demo分组
     */
    @Bean
    public Docket demoDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .groupName("样例")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.springbootwebdemo.demo"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBootWebDemo APIs")
                .description("使用spring boot开发的web样例的API")
                .termsOfServiceUrl("无")
                .contact(new Contact("无", "无", "无"))
                .version("1.0.0")
                .build();
    }

}
