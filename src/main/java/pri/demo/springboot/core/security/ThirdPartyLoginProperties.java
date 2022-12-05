package pri.demo.springboot.core.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 用于读取自定义配置的token相关配置
 *
 * @author woieha320r
 */
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "third-party-secret")
public class ThirdPartyLoginProperties {

    /**
     * github的client_id、client_secret
     */
    private Map<String, String> github;

}
