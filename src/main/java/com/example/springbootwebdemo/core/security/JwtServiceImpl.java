package com.example.springbootwebdemo.core.security;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springbootwebdemo.core.property.TokenProperties;
import com.example.springbootwebdemo.core.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
  */
@Service
public class JwtServiceImpl implements JwtService {

    /**
     * JWT相关
     */
    private static final String JWT_SECRET = "^_~my086Jwt=@$%z";
    private static final String JWT_CLAIM_KEY = "payload";

    @Autowired
    private TokenProperties tokenProperties;

    /**
     * AES加密相关，只能是16进制可表示的数字字母
     */
    private static String AES_PASSWORD;
    private static String AES_SALT;

    // 需要晚于@Autowired，不能用构造方法
    @PostConstruct
    void init() {
        if (tokenProperties.isUsefulWhenRestart()) {
            AES_PASSWORD = "0936fa186ade";
            AES_SALT = "cc876aed";
        } else {
            AES_PASSWORD = HexUtil.encodeHexStr(RandomUtil.randomString(32), StandardCharsets.UTF_8);
            AES_SALT = HexUtil.encodeHexStr(RandomUtil.randomString(32), StandardCharsets.UTF_8);
        }
    }

    @Override
    public String generateTokenFromPayload(String payload) {
        return JWT.create()
                .withClaim(JWT_CLAIM_KEY, encryptPayload(payload))
                .withExpiresAt(getExpireDate())
                .sign(Algorithm.HMAC256(JWT_SECRET));
    }

    @Override
    public String parsePayloadFromToken(String token) {
        return decryptionPayload(
                JWT.require(Algorithm.HMAC256(JWT_SECRET))
                        .build()
                        .verify(token)
                        .getClaim(JWT_CLAIM_KEY)
                        .as(String.class)
        );
    }

    /**
     * 加密payload
     */
    private String encryptPayload(String payload) {
        return HexUtil.encodeHexStr(new AesBytesEncryptor(AES_PASSWORD, AES_SALT).encrypt(payload.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 解密payload
     */
    private String decryptionPayload(String encryptPayload) {
        return new String(new AesBytesEncryptor(AES_PASSWORD, AES_SALT).decrypt(HexUtil.decodeHex(encryptPayload)), StandardCharsets.UTF_8);
    }

    /**
     * N秒后过期，使用UTC+8时区
     */
    private Date getExpireDate() {
        return Date.from(LocalDateTime.now().plusSeconds(tokenProperties.getTimeToLive()).toInstant(ZoneOffset.ofHours(8)));
    }

}
