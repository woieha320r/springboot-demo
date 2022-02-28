package com.example.springbootwebdemo.demo.websocket;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket样例
 * 配置文件中屏蔽token验证，并定义websocket路径。在过滤器中另行解析此路径下的token，须有一个前缀，比如token=111
 *
  * @date 2021-12-10
 */
@Slf4j
@ServerEndpoint(value = "/websocket/{characteristic}/{token}")
@Component
public class MyWebSocket {

    /**
     * websocket每次请求都是一个实例，@Autowired注入null
     * 在WebSocketConfig中将service实例注入此处
     * 或手动从容器取出实例：SpringUtil.getBean(UserServiceImpl.class)
     */
    //public static UserService userService = SpringUtil.getBean(UserServiceImpl.class);

    /**
     * 保存所有连接，WebSocket不是单例的
     */
    private static final ConcurrentHashMap<String, MyWebSocket> WEB_SOCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 保存自己在连接容器中的的key，便于操作容器
     */
    private String key;

    /**
     * 当前连接（每个websocket连入都会创建一个MyWebSocket实例），关闭时需要
     */
    private Session session;

    /**
     * 建立连接时执行
     * 参数只能以PathParam传入
     * 连接时传入特征码，广播时按照特征码选择向谁广播
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("characteristic") String characteristic, @PathParam("token") String token) {
        this.session = session;
        this.key = session.getId() + "+" + characteristic;
        if (!WEB_SOCKET_MAP.containsKey(key)) {
            WEB_SOCKET_MAP.put(key, this);
            log.debug("websocket >>> 建立新连接 >>> key：{}", key);
        } else {
            log.warn("websocket >>> 连接已存在，重复请求建立 >>> key：{}", key);
        }
    }

    /**
     * 接受消息时执行
     */
    @OnMessage
    public void onMessage(String message) {
        log.debug("websocket >>> 收到来自连接{}的消息 >>> {}", key, message);
        try {
            this.sendMessage("已收到消息：" + message);
        } catch (Exception e) {
            log.error("websocket >>> 发送消息异常 >>> {}", ExceptionUtil.stacktraceToString(e));
        }
    }

    /**
     * 发生错误时执行
     */
    @OnError
    public void onError(Throwable error) {
        log.error("websocket >>> 连接{}发生错误 >>> {}", key, ExceptionUtil.stacktraceToString(error));
    }

    /**
     * 连接关闭时执行
     * TODO:不知道需不需要处理session（关闭、移除）
     */
    @OnClose
    public void onClose() {
        log.debug("websocket >>> 连接{}即将关闭", key);
        WEB_SOCKET_MAP.remove(key);
    }

    /**
     * 发送消息，外部调用
     */
    public void sendMessage(String message) {
        try {
            log.debug("websocket >>> 向连接{}发送消息 >>> {}", key, message);
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("websocket >>> 向连接{}发送消息异常 >>> {}", key, ExceptionUtil.stacktraceToString(e));
        }
    }

    /**
     * 广播消息，外部调用
     */
    public static void broadcast(String msg, String characteristic) {
        MyWebSocket.WEB_SOCKET_MAP.forEach((key, value) -> {
            if (key.contains("+" + characteristic)) {
                value.sendMessage(msg);
            }
        });
    }

    /**
     * 获取在线连接数目
     */
    public static int getCount() {
        return WEB_SOCKET_MAP.size();
    }

}
