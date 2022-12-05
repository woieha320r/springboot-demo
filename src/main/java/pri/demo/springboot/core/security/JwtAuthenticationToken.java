package pri.demo.springboot.core.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import pri.demo.springboot.entity.SysUserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 业务用户与SpringSecurity验证实体的桥梁，后续使用SpringSecurity鉴权注解的基础
 *
 * @author woieha320r
 */
@NoArgsConstructor
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtAuthenticationToken implements Authentication {

    /**
     * 将token转为Authentication存入SecurityContext时要置true以告知SpringSecurity当前登录实体已验证通过
     */
    private boolean authenticated = false;

    /**
     * \@PreAuthorize鉴权使用
     * 设置权限时，如果是角色，需加"ROLE_"前缀
     * 鉴权权限用@PreAuthorize("hasAuthority('权限名')")
     * 鉴权角色用@PreAuthorize("hasRole('[ROLE_]角色名')")、@PreAuthorize("hasAuthority('ROLE_test_role')")
     */
    private List<SimpleGrantedAuthority> authorities;

    /**
     * \@AuthenticationPrincipal声明在方法参数上获取登录实体使用
     * xxxController(@AuthenticationPrincipal 实际类型 参数名)
     */
    private SysUserEntity principal;

    public JwtAuthenticationToken(SysUserEntity principal) {
        this.principal = principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return null;
    }

    public static SysUserEntity getCurrentLoginEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return null;
        }
        return (SysUserEntity) authentication.getPrincipal();
    }

}
