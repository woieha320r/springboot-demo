package com.example.springbootwebdemo.core.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 异常相关配置
 *
  * @version 1.0
 * @date 2022/1/25 14:12
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "exception")
public class ExceptionProperty extends GlobalPropertySourceInclude {

    /**
     * 日志记录异常信息的长度限制
     */
    private int dbLength;

}
