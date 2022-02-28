package com.example.springbootwebdemo.core.security;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 可被spring security管理的实体类
 *
  * @date 2021-11-30
 */
//lombok的@RequiredArgsConstructor注解所说的参数是指final且未初始化和@NotNull的
public class MyUserDetails implements UserDetails, Serializable {

    @Getter(AccessLevel.NONE)
    private final MyLoginAccountService myLoginAccountService;

    @Getter
    private final MyLoginAccount myLoginAccount;

    public MyUserDetails(MyLoginAccountService myLoginAccountService, MyLoginAccount myLoginAccount) {
        this.myLoginAccountService = myLoginAccountService;
        this.myLoginAccount = myLoginAccount;
    }

    public MyUserDetails(MyLoginAccountService myLoginAccountService, String myLoginAccountJsonStr) {
        this.myLoginAccountService = myLoginAccountService;
        this.myLoginAccount = myLoginAccountService.createMyLoginAccountByJsonStr(myLoginAccountJsonStr);
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return myLoginAccountService.getSpringSecurityAuthoritiesStr(myLoginAccount)
                .parallelStream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return myLoginAccount.getLoginPassword();
    }

    @Override
    public String getUsername() {
        return myLoginAccount.getLoginName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return myLoginAccount.getEnable();
    }

}
