package pri.demo.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pri.demo.springboot.core.config.Constants;
import pri.demo.springboot.core.config.JmsJsonTemplate;
import pri.demo.springboot.core.exception.LoginException;
import pri.demo.springboot.core.exception.ParamException;
import pri.demo.springboot.core.security.JwtAuthenticationToken;
import pri.demo.springboot.core.security.JwtUtil;
import pri.demo.springboot.core.security.ThirdPartyLoginProperties;
import pri.demo.springboot.core.security.TokenProperties;
import pri.demo.springboot.dto.GithubUserDto;
import pri.demo.springboot.dto.LoginJmsDto;
import pri.demo.springboot.entity.SysLoginAccountEntity;
import pri.demo.springboot.entity.SysUserEntity;
import pri.demo.springboot.enums.LoginType;
import pri.demo.springboot.mapper.SysLoginAccountMapper;
import pri.demo.springboot.mapper.SysUserMapper;
import pri.demo.springboot.query.OthersLoginQuery;
import pri.demo.springboot.query.SelfLoginQuery;
import pri.demo.springboot.service.SysLoginAccountService;
import pri.demo.springboot.vo.TokenVo;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * 登录账户 服务实现类
 * </p>
 *
 * @author woieha320r
 */
@Service
@Slf4j
public class SysLoginAccountServiceImpl extends ServiceImpl<SysLoginAccountMapper, SysLoginAccountEntity> implements SysLoginAccountService {

    private final SysLoginAccountMapper accountMapper;
    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenProperties tokenProperties;
    private final ThirdPartyLoginProperties thirdPartyLoginProperties;
    private final ConcurrentMap<String, Map<String, String>> thirdPartyCodeTokenHeader = new ConcurrentHashMap<>();
    private final JmsJsonTemplate jmsTemplate;

    @Autowired
    public SysLoginAccountServiceImpl(SysLoginAccountMapper accountMapper, SysUserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, TokenProperties tokenProperties, ThirdPartyLoginProperties thirdPartyLoginProperties, JmsJsonTemplate jmsTemplate) {
        this.accountMapper = accountMapper;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenProperties = tokenProperties;
        this.thirdPartyLoginProperties = thirdPartyLoginProperties;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public TokenVo usernameLogin(SelfLoginQuery query) {
        return tokenVo(Constants.ROOT_NAME.equals(query.getIdentifier()) ? rootLogin(query) : passwordLogin(query));
    }

    @Override
    public TokenVo emailLogin(SelfLoginQuery query) {
        return tokenVo(passwordLogin(query));
    }

    @Override
    public String loginEmail(LoginType loginType, String identifier) {
        return accountMapper.loginEmail(loginType, identifier, LoginType.EMAIL);
    }

    @Override
    public TokenVo githubLoginAndJms(OthersLoginQuery query) {
        githubToken(query.getClientId(), query.getCode());
        GithubUserDto githubUser = githubUser(query.getCode());
        Long githubUserId = githubUser.getId();
        Long userId = null;
        // TODO:githubUserId存在login_account.identifier，token存在credential，如果没有需要补充用户信息注册，填充userId
        jmsTemplate.send(Constants.JMS_LOGIN, new LoginJmsDto()
                .setIdentifier(githubUserId.toString())
                .setLoginType(LoginType.GITHUB)
        );
        return tokenVo(new JwtAuthenticationToken(userMapper.selectById(userId)));
    }

    @Override
    public String nickname(LoginType loginType, String identifier) {
        return accountMapper.nickname(loginType, identifier);
    }

    private Authentication rootLogin(SelfLoginQuery query) {
        //TODO:root密码机制
        if (!Objects.equals(query.getCredential(), "123456")) {
            //用户名密码都匹配才是root，不防止用户注册root登录名。用户密码是加密过的，root密码是明文的，可认为不会相同。token鉴权用ROOT_ID判断
            return passwordLogin(query);
        }
        return new JwtAuthenticationToken(new SysUserEntity()
                .setId(Constants.ROOT_ID)
                .setNickname(query.getIdentifier())
        );
    }

    private Authentication passwordLogin(SelfLoginQuery query) {
        SysLoginAccountEntity accountEntity = Optional.ofNullable(
                accountMapper.selectOne(Wrappers.lambdaQuery(SysLoginAccountEntity.class)
                        .select(SysLoginAccountEntity::getUserId, SysLoginAccountEntity::getCredential)
                        .eq(SysLoginAccountEntity::getIdentifier, query.getIdentifier())
                        .eq(SysLoginAccountEntity::getLoginType, query.getLoginType())
                )
        ).orElse(new SysLoginAccountEntity().setCredential("ACCOUNT_NOT_FOUND"));
        if (!passwordEncoder.matches(query.getCredential(), accountEntity.getCredential())) {
            throw new LoginException(
                    StrUtil.format("{}或密码错误", LoginType.USERNAME.equals(query.getLoginType()) ? "用户名" : "邮箱")
            );
        }
        return new JwtAuthenticationToken(userMapper.selectById(accountEntity.getUserId()));
    }

    private TokenVo tokenVo(Authentication authentication) {
        return new TokenVo()
                .setToken(jwtUtil.token(JSONUtil.toJsonStr(authentication)))
                .setHttpHeader(tokenProperties.getHeaderKey())
                .setDeathTime(LocalDateTime.now()
                        //token产生到执行到这里已花时间
                        .plusSeconds(tokenProperties.getTimeToLive() - 1)
                        .toInstant(ZoneOffset.of("+8"))
                        .toEpochMilli()
                );
    }

    private void githubToken(String clientId, String code) {
        String clientSecret = thirdPartyLoginProperties.getGithub().get(clientId);
        if (Objects.isNull(clientSecret)) {
            throw new ParamException("client_id无效");
        }
        HttpRequest request = HttpUtil.createPost("https://github.com/login/oauth/access_token")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name())
                .form("client_id", clientId)
                .form("client_secret", clientSecret)
                .form("code", code);
        String responseBodyStr = request.executeAsync().body();
        if (!JSONUtil.isTypeJSON(responseBodyStr)) {
            log.error("向GitHub交换token失败，响应体：{}", responseBodyStr);
            throw new RuntimeException("向GitHub交换token失败");
        }
        JSONObject githubToken = JSONUtil.parseObj(responseBodyStr);
        Map<String, String> githubVal = new HashMap<>();
        githubVal.put(githubToken.getStr("access_token"), githubToken.getStr("token_type"));
        thirdPartyCodeTokenHeader.put(code, githubVal);
    }

    private GithubUserDto githubUser(String code) {
        Map<String, String> tokenWithHeader = thirdPartyCodeTokenHeader.get(code);
        String token = null;
        String header = null;
        for (Map.Entry<String, String> stringStringEntry : tokenWithHeader.entrySet()) {
            token = stringStringEntry.getKey();
            header = stringStringEntry.getValue();
            break;
        }
        if (Objects.isNull(token) || Objects.isNull(header)) {
            log.error("未获取到GitHub token");
            throw new RuntimeException("未获取到GitHub token");
        }
        Map<String, Object> requestParam = new HashMap<>(3);
        requestParam.put(header, token);
        String url = HttpUtil.urlWithForm("https://api.github.com/user", requestParam, StandardCharsets.UTF_8, false);
        HttpRequest request = HttpUtil.createGet(url)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        // .auth("token " + tokenWithHeader);
        String responseBodyStr = request.executeAsync().body();
        if (!JSONUtil.isTypeJSON(responseBodyStr)) {
            log.error("向GitHub请求用户信息失败，响应体：{}", responseBodyStr);
            throw new RuntimeException("向GitHub请求用户信息失败");
        }
        return JSONUtil.toBean(responseBodyStr, GithubUserDto.class);
    }

}
