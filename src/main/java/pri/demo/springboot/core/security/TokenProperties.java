package pri.demo.springboot.core.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于读取自定义配置的token相关配置
 *
 * @author woieha320r
 */
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "app.token")
@Validated
public class TokenProperties {

    /**
     * 客户端http请求头中携带token的key，默认:"token"
     */
    private String headerKey = "token";

    /**
     * 客户端http url中携带token的key，默认:"token"
     */
    private String urlKey = "token";

    /**
     * token过期时间，单位：秒，默认:86400（24小时）
     */
    @Min(value = 0)
    private long timeToLive = 86400;

    /**
     * 无需token认证的servlet路径前缀匹配，不支持特殊扩展符，已全部添加了${server.servlet.context-path}，无需再加
     * 多环境配置文件下，List将被按优先级后者覆盖，Map则是不同key合并
     */
    private List<String> ignoreServletPaths = new ArrayList<>();

    /**
     * 重启后，签发过的未过期token是否继续有效，默认:false
     */
    private boolean liveAfterRestart = false;
}
