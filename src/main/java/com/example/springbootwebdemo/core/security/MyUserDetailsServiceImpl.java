package com.example.springbootwebdemo.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * spring security登录实体服务实现类
 *
  * @date 2021-11-30
 */
@Service
//当有多个实现时，IDEA会对@Autowired报错，但并不影响运行
@Primary
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MyLoginAccountService myLoginAccountService;

    @Override
    public MyUserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        MyUserDetails myUserDetails = myLoginAccountService.loadUserByUsername(loginName);
        if (Objects.isNull(myUserDetails.getMyLoginAccount())) {
            throw new UsernameNotFoundException("不存在此用户");
        }
        return myUserDetails;
    }

}
