package pri.demo.springboot.core.security;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import pri.demo.springboot.entity.SysUserEntity;
import pri.demo.springboot.service.SysPermissionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * jwt拦截器，负责校验token并解析出实体置入SecurityContext
 * <p>
 * 拦截器包围controller执行，由spring管理。过滤器范围更大，由servlet管理
 * 所有拦截器需要在WebMvcConfigurer中注册，否则不生效
 *
 * @author woieha320r
 */
@Slf4j
@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final TokenProperties tokenProperties;
    private final SysPermissionService permissionService;
    private final List<AntPathRequestMatcher> antMatchers;

    @Autowired
    public JwtAuthenticationInterceptor(JwtUtil jwtUtil, TokenProperties tokenProperties, SysPermissionService permissionService) {
        this.jwtUtil = jwtUtil;
        this.tokenProperties = tokenProperties;
        this.permissionService = permissionService;
        antMatchers = tokenProperties.getIgnoreServletPaths().stream()
                .map(ignoreServlet -> new AntPathRequestMatcher(
                        ignoreServlet + (ignoreServlet.contains(".") ? "" : "/**")
                )).collect(Collectors.toList());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("拦截器-开始:jwt");
        if (!canUnAuth(request)) {
            JwtAuthenticationToken authentication = JSONUtil.toBean(
                    jwtUtil.payload(Optional.ofNullable(request.getHeader(tokenProperties.getHeaderKey())).orElse("NO_FOUND"))
                    , JwtAuthenticationToken.class
            );
            //填充用户权限，对用户的角色权限修改立即生效
            authentication.setAuthorities(permissionService.grantedAuthoritiesByUser(
                    ((SysUserEntity) authentication.getPrincipal()).getId()
            ));
            authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.debug("拦截器-结束:jwt");
        SecurityContextHolder.clearContext();
    }

    private boolean canUnAuth(HttpServletRequest request) {
        for (AntPathRequestMatcher antMatcher : antMatchers) {
            if (antMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

}
