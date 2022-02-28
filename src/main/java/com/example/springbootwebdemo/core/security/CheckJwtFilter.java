package com.example.springbootwebdemo.core.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.springbootwebdemo.core.exception.ResponseWrite;
import com.example.springbootwebdemo.core.property.TokenProperties;
import com.example.springbootwebdemo.core.returnval.AppReturnValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 处理jwt的过滤器
 * 负责将token解析为可被spring security管理的实体并置入spring security容器由其管理
 * 继承自一次一个请求过滤器，所有过滤器都默认继承于此，请求达到controller前要先过过滤器
 *
  * @date 2021-11-12
 */
@Component
@Slf4j
public class CheckJwtFilter extends OncePerRequestFilter {

    @Autowired
    private MyLoginAccountService myLoginAccountService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenProperties tokenProperties;

    /**
     * token在传输中有个'Bearer '前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    //过滤器异常由切面处理
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeader = getHttpAuthHeader(request);
        String requestPath = request.getRequestURI();
        if (Objects.nonNull(authHeader) && authHeader.startsWith(TOKEN_PREFIX)) {
            MyUserDetails myUserDetails;
            //从token解析出置入的对象，并转为MyUserDetails
            try {
                myUserDetails = parseUserDetailsFromHttpAuthHeader(authHeader);
            } catch (TokenExpiredException | IllegalStateException | JWTDecodeException e) {
                //token过期、错误
                //执行顺序：过滤器 -> 拦截器 -> 切面 -> 业务代码。所以切面不能处理过滤器的异常，直接回写
                log.warn("出现携带异常token的请求，Authorization Header：{}", authHeader);
                ResponseWrite.okUtf8Json(response, AppReturnValue.authError().toString());
                return;
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //每次收到带token的请求，若能正常解析token但缓存没有登录实体，就把登录实体放到缓存里，表示已经认证。
            //框架在执行完SecurityContextPersistenceFilter和FilterChainProxy后会清空上下文，所以每次请求都会执行下方代码
            if (Objects.isNull(authentication)) {
                //用户的权限需要单独入参置入（userDetails.getAuthorities()），框架鉴权时使用。密码不需要
                //框架获取登录实体会取SecurityContext中置入的authentication的principal，因为框架认证成功后会在此放入（见登录验证时的注释）
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities())
                );
            }
        }
        //单独验证websocket请求在路径中携带的token参数
        //websocket不能被代理，所以不能用切面验证token，此样例在过滤器中实现
        //websocket的token验证逻辑：所有websocket连接的路径配置于配置文件，并在连接时在路径参数中传入token（需要有固定前缀，如token=，配置于配置文件）
        //若是websocket连接路径，则截取token路径参数，验证通过则放行。
        if (isWebSocketRequest(requestPath)) {
            String tokenPrefix = tokenProperties.getPathPrefix();
            boolean tokenError = true;
            if (requestPath.contains(tokenPrefix)) {
                String token = requestPath.split(tokenPrefix)[1].split("/")[0];
                try {
                    jwtService.parsePayloadFromToken(token);
                    tokenError = false;
                } catch (TokenExpiredException | IllegalStateException | JWTDecodeException ignore) {
                }
            }
            if (tokenError) {
                log.warn("出现携带异常token的websocket建立请求，请求路径：{}", requestPath);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 从请求中获取携带token的header值
     */
    private String getHttpAuthHeader(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    /**
     * 根据请求的Authority头中的token解析出MyUserDetails
     */
    private MyUserDetails parseUserDetailsFromHttpAuthHeader(String authHeader) {
        return new MyUserDetails(myLoginAccountService, jwtService.parsePayloadFromToken(authHeader.substring(TOKEN_PREFIX.length())));
    }

    /**
     * 是否是websocket请求路径
     */
    private boolean isWebSocketRequest(String requestPath) {
        return tokenProperties.getWebsocketPaths().parallelStream().anyMatch(requestPath::contains);
    }

}