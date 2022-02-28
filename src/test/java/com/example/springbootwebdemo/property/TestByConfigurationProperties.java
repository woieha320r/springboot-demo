package com.example.springbootwebdemo.property;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//不加此注解，导致@Autowired引入Null
@RunWith(SpringRunner.class)
//不加web环境的话，websocket会报错javax.websocket.server.ServerContainer not available
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestByConfigurationProperties {

    @Autowired
    private ByConfigurationProperties byConfigurationProperties;

    @Test
    public void test() {
        System.out.println(byConfigurationProperties.getValue());
    }

}
