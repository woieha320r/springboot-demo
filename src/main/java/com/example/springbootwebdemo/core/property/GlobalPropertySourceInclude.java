package com.example.springbootwebdemo.core.property;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 引入所有可能被maven打包的配置文件；
 * 这里引入后，就可通过@Value或@ConfigurationProperties或Environment读取了
 * 多配置文件key重名时取application.properties
 * 整个项目使用属性上的@Value或类上的@ConfigurationProperties+属性名来读取配置文件时需要继承这个类；
 * Environment和@Scheduled无需继承此类，但同样依赖@PropertySource，使用Environment需要给这个类加@Component以被spring加载
 * 最后决定选用@ConfigurationProperties
 * 当配置文件改名，这里也要改；
 *
  * @date 2022-01-20
 */
@Component
@PropertySource(value = {
        "classpath:application.properties",
        "classpath:application-dev.properties",
        "classpath:application-test.properties",
        "classpath:application-prod.properties"
}, encoding = "UTF-8", ignoreResourceNotFound = true)
@SuppressWarnings(value = "unused")
public class GlobalPropertySourceInclude {
}
