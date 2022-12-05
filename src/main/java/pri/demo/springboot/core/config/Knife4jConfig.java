package pri.demo.springboot.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Knife4j配置
 *
 * @author woieha320r
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("springboot-demo")
                .description("springboot-demo APIs")
                .termsOfServiceUrl("https://www.springboot.demo.pri/")
                .contact(new Contact("springboot-demo", "https://www.springboot.demo.pri", "springboot@demo.pri"))
                .version("1.0")
                .build();
    }

    /**
     * 默认组
     */
    @Bean
    public Docket defaultDocket(@Autowired ApiInfo apiInfo) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                //不显示这个类型的入参
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                //分组名称
                .groupName("默认组")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("pri.demo.springboot.controller"))
                //不显示/error开头路径的api
                // .paths((String path) -> !path.startsWith("/error"))
                .paths(PathSelectors.any())
                .build();
    }

}
