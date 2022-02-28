package com.example.springbootwebdemo.core.log;

import java.time.LocalDateTime;

/**
 * 全局日志对象接口
 *
  * @date 2022-01-21
 */
@SuppressWarnings("unused")
public interface GlobalLog {

    /**
     * 设置 HTTP 请求消息中的请求方式（如 GET、POST 等）
     *
     * @param param HTTP请求方式
     * @return 全局日志对象
     */
    GlobalLog setHttpMethod(String param);

    /**
     * 设置请求参数
     *
     * @param param 请求路径中的参数
     * @return 全局日志对象
     */
    GlobalLog setRequestParam(String param);

    /**
     * 设置方法描述
     *
     * @param methodDescription 方法描述
     * @return 全局日志对象
     */
    GlobalLog setMethodDescription(String methodDescription);

    /**
     * 获取方法描述
     *
     * @return 方法描述
     */
    String getMethodDescription();

    /**
     * 设置请求客户端的 IP 地址，其格式类似于 192.168.0.3
     *
     * @param param 请求客户端的 IP 地址
     * @return 全局日志对象
     */
    GlobalLog setRemoteAddress(String param);

    /**
     * 设置请求客户端的完整主机名，其格式类似于 pcl.mengma.com。需要注意的是，如果无法解析出客户机的完整主机名，那么该方法将会返回客户端的 IP 地址
     *
     * @param param 请求客户端的完整主机名
     * @return 全局日志对象
     */
    GlobalLog setRemoteHost(String param);

    /**
     * 设置请求客户端网络连接的端口号
     *
     * @param param 请求客户端网络连接的端口号
     * @return 全局日志对象
     */
    GlobalLog setRemotePort(Integer param);

    /**
     * 设置 Web 服务器上接收当前请求网络连接的 IP 地址
     *
     * @param param Web 服务器上接收当前请求网络连接的 IP 地址
     * @return 全局日志对象
     */
    GlobalLog setLocalAddress(String param);

    /**
     * 设置 Web 服务器上接收当前网络连接 IP 所对应的主机名
     *
     * @param param Web 服务器上接收当前网络连接 IP 所对应的主机名
     * @return 全局日志对象
     */
    GlobalLog setLocalName(String param);

    /**
     * 设置 Web 服务器上接收当前网络连接的端口号
     *
     * @param param Web 服务器上接收当前网络连接的端口号
     * @return 全局日志对象
     */
    GlobalLog setLocalPort(Integer param);

    /**
     * 设置客户端发出请求时的完整 URL，包括协议、服务器名、端口号、 资源路径等信息，但不包括后面的査询参数部分
     *
     * @param param 客户端发出请求时的完整 URL，包括协议、服务器名、端口号、 资源路径等信息，但不包括后面的査询参数部分。
     * @return 全局日志对象
     */
    GlobalLog setRequestUrl(String param);

    /**
     * 请求是否成功
     *
     * @param param 请求是否成功
     * @return 全局日志对象
     */
    GlobalLog setSuccess(Boolean param);

    /**
     * 获取 请求是否成功
     *
     * @return 请求是否成功
     */
    Boolean getSuccess();

    /**
     * 设置响应体
     *
     * @param param 响应体
     * @return 全局日志对象
     */
    GlobalLog setResponseBody(String param);

    /**
     * 设置异常信息
     *
     * @param param 异常信息
     * @return 全局日志对象
     */
    GlobalLog setExceptionMessage(String param);

    /**
     * 设置请求时间
     *
     * @param param 请求时间
     * @return 全局日志对象
     */
    GlobalLog setRequestTime(LocalDateTime param);

    /**
     * 设置方法class路径
     *
     * @param param 方法class路径
     * @return 全局日志对象
     */
    GlobalLog setClassPath(String param);

    /**
     * 设置登录实体的id标识
     *
     * @param id 登录实体的id标识
     * @return 全局日志对象
     */
    GlobalLog setLoginAccount(Long id);

    /**
     * 获取登录实体的id标识
     *
     * @return 登录实体的id标识
     */
    Long getLoginAccount();

    /**
     * 获取 HTTP 请求消息中的请求方式（如 GET、POST 等）
     *
     * @return HTTP请求方式
     */
    String getHttpMethod();

    /**
     * 获取请求参数
     *
     * @return 请求路径中的参数
     */
    String getRequestParam();

    /**
     * 获取请求客户端的 IP 地址，其格式类似于 192.168.0.3
     *
     * @return 请求客户端的 IP 地址
     */
    String getRemoteAddress();

    /**
     * 获取请求客户端的完整主机名，其格式类似于 pcl.mengma.com。需要注意的是，如果无法解析出客户机的完整主机名，那么该方法将会返回客户端的 IP 地址
     *
     * @return 请求客户端的完整主机名
     */
    String getRemoteHost();

    /**
     * 获取请求客户端网络连接的端口号
     *
     * @return 请求客户端网络连接的端口号
     */
    Integer getRemotePort();

    /**
     * 获取 Web 服务器上接收当前请求网络连接的 IP 地址
     *
     * @return 获取 Web 服务器上接收当前请求网络连接的 IP 地址
     */
    String getLocalAddress();

    /**
     * 获取 Web 服务器上接收当前网络连接 IP 所对应的主机名
     *
     * @return 获取 Web 服务器上接收当前网络连接 IP 所对应的主机名
     */
    String getLocalName();

    /**
     * 获取 Web 服务器上接收当前网络连接的端口号
     *
     * @return 获取 Web 服务器上接收当前网络连接的端口号
     */
    Integer getLocalPort();

    /**
     * 获取客户端发出请求时的完整 URL，包括协议、服务器名、端口号、 资源路径等信息，但不包括后面的査询参数部分
     *
     * @return 获取客户端发出请求时的完整 URL，包括协议、服务器名、端口号、 资源路径等信息，但不包括后面的査询参数部分。
     */
    String getRequestUrl();

    /**
     * 获取响应体
     *
     * @return 响应体
     */
    String getResponseBody();

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    String getExceptionMessage();

    /**
     * 获取请求时间
     *
     * @return 请求时间
     */
    LocalDateTime getRequestTime();

    /**
     * 获取方法class路径
     *
     * @return 方法class路径
     */
    String getClassPath();

}
