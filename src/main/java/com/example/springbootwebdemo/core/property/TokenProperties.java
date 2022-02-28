package com.example.springbootwebdemo.core.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 读取配置文件中有关token的配置项
 *
  * @version 1.0
 * @date 2022/1/25 14:12
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "app.token")
public class TokenProperties extends GlobalPropertySourceInclude {

    /**
     * token过期时间，单位：秒
     */
    private long timeToLive;

    /**
     * 无需token认证的接口
     */
    private List<String> ignorePath;

    /**
     * 是否每次登录都查询最新权限，否则权限更新需要重新登录才能生效
     */
    private boolean alwaysSelectAuthority;

    /**
     * 后端重启后，token是否继续有效
     */
    private boolean usefulWhenRestart;

    /**
     * 路径传递token时的前缀
     */
    private String pathPrefix;

    /**
     * 包含websocket请求的路径
     */
    private List<String> websocketPaths;

}
