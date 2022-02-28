package com.example.springbootwebdemo.property;

import com.example.springbootwebdemo.core.property.GlobalPropertySourceInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Environment之后执行
 * 跳转：配置文件 -> 代码；
 * 不配置PropertySource能否读取其他激活的配置文件：否；
 * 影响配置文件显示：否；
 * 不存在时异常：否；
 *
  * @date 2022-01-20
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "app.test.read")
public class ByConfigurationProperties extends GlobalPropertySourceInclude {

    private String value;

}
