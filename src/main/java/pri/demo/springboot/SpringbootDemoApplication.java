package pri.demo.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pri.demo.springboot.core.security.ThirdPartyLoginProperties;
import pri.demo.springboot.core.security.TokenProperties;

/**
 * SpringBoot启动类
 *
 * @author woieha320r
 */
@SpringBootApplication
@EnableConfigurationProperties(value = {TokenProperties.class, ThirdPartyLoginProperties.class})
public class SpringbootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemoApplication.class, args);
    }

}
