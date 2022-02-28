package com.example.springbootwebdemo.core.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * websocket配置类
 *
  * @date 2021-12-13
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**websocket每次请求都是一个实例，@Autowired注入null
     * 在此处将service实例注入MyWebSocket
     * 或在MyWebSocket中手动将实例从容器取出
     * */
    /*
    @Autowired
    public void setUserService(UserService userService) {
        MyWebSocket.userService = userService;
    }
    */

}
