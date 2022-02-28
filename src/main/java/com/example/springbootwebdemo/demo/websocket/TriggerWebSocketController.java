package com.example.springbootwebdemo.demo.websocket;

import com.example.springbootwebdemo.core.log.AppLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调用websocket广播样例
 *
  * @date 2022-02-23
 */
@Api(value = "广播", tags = "广播")
@RestController
@RequestMapping(value = "/websocket")
public class TriggerWebSocketController {

    /**
     * 向拥有指定特征的websocket发送消息
     */
    @AppLog(methodDesc = "向符合特征的前端页面广播消息")
    @ApiOperation(value = "向符合特征的前端页面广播消息")
    @GetMapping(value = "/send")
    public void sendMsg(@RequestParam String characteristic) {
        MyWebSocket.broadcast("这是来自后端的广播消息，特征码：" + characteristic, characteristic);
    }

}
