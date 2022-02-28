package com.example.springbootwebdemo.core.security;

import org.springframework.stereotype.Service;

/**
 * jwt工具
 * 负责实体和token的转换
 * jwt包含：base64(头部).base64(负载).签名
 * 头部包含加密算法名称；负载包含自定义存储信息和过期时间；签名用于放置前二者被篡改；jwt本身不负责对负载的加密，需自行加密
 *
  * @date 2021-11-11
 */
@Service
public interface JwtService {

    /**
     * 从token解析出有效负载
     *
     * @param token token
     * @return 负载对象的json字符串
     */
    String parsePayloadFromToken(String token);

    /**
     * 根据有效负载生成token
     *
     * @param payload 负载对象的json字符串
     * @return token
     */
    String generateTokenFromPayload(String payload);

}
