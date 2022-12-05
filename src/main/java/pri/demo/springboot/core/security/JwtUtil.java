package pri.demo.springboot.core.security;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * JWT工具类
 *
 * @author woieha320r
 */
@Component
public class JwtUtil {

    private final TokenProperties tokenProperties;
    private final byte[] HMAC256Key;
    private String aesPassword = "6Jwg5^_~mndg49@$%zct=kery08rz0";
    private String aesSalt = "6J_~my086Jwt=@wndg4z09w86Jwt=@";
    private final String CLAIM_KEY = "PAYLOAD";

    @Autowired
    public JwtUtil(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
        String hmac256KeyStr = "crzkerg5^_~my086Jwt=@$%zndg490";
        if (!tokenProperties.isLiveAfterRestart()) {
            hmac256KeyStr = RandomUtil.randomString(32);
            aesPassword = RandomUtil.randomString(32);
            aesSalt = RandomUtil.randomString(32);
        }
        aesPassword = HexUtil.encodeHexStr(aesPassword, StandardCharsets.UTF_8);
        aesSalt = HexUtil.encodeHexStr(aesSalt, StandardCharsets.UTF_8);
        HMAC256Key = hmac256KeyStr.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 将负载置入token
     */
    public String token(String payload) {
        return JWT.create()
                .withClaim(CLAIM_KEY, encryptPayload(payload))
                .withExpiresAt(expireDate())
                .sign(Algorithm.HMAC256(HMAC256Key));
    }

    /**
     * 从token提取负载
     */
    public String payload(String token) {
        return decryptPayload(
                JWT.require(Algorithm.HMAC256(HMAC256Key))
                        .build()
                        .verify(token)
                        .getClaim(CLAIM_KEY)
                        .as(String.class)
        );
    }

    /**
     * 加密payload
     */
    private String encryptPayload(String payload) {
        return HexUtil.encodeHexStr(new AesBytesEncryptor(aesPassword, aesSalt).encrypt(payload.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 解密payload
     */
    private String decryptPayload(String encryptPayload) {
        return new String(new AesBytesEncryptor(aesPassword, aesSalt).decrypt(HexUtil.decodeHex(encryptPayload)), StandardCharsets.UTF_8);
    }

    /**
     * 过期时间
     */
    private Date expireDate() {
        return Date.from(LocalDateTime.now().plusSeconds(tokenProperties.getTimeToLive()).toInstant(ZoneOffset.ofHours(8)));
    }

}
