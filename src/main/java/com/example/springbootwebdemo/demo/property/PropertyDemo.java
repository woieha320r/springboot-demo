package com.example.springbootwebdemo.demo.property;

import com.example.springbootwebdemo.core.property.GlobalPropertySourceInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取配置文件样例
 * 不可被两个类重复读取
 *
  * @version 1.0
 * @date 2022/1/25 14:12
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "app.test.other.read")
public class PropertyDemo extends GlobalPropertySourceInclude {

    private String value;

}
