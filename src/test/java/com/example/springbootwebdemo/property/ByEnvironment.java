package com.example.springbootwebdemo.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 比@Value后执行，比@ConfigurationProperties先执行
 * 跳转：配置文件 <-> 代码；
 * 不配置PropertySource能否读取其他激活的配置文件：否；
 * 不存在时异常：否；
 * 多配置文件的key重名时取application.properties；
 *
  * @date 2022-01-20
 */
@Deprecated
@Data
@Component
public class ByEnvironment {

    @Autowired
    private Environment environment;

    private List<String> testValue;

    //执行顺序：Constructor > @Autowired > @PostConstruct
    @PostConstruct
    void readProperty() {
        //不用时改成一个配置文件一定有的，不然报错
        testValue = environment.getProperty("app.token.ignore", List.class);
    }

}
