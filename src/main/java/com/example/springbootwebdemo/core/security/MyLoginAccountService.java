package com.example.springbootwebdemo.core.security;

import java.util.List;

/**
 * 可被spring security管理的实体 与 业务登录实体 的过度处理
 *
  * @date 2022-01-21
 */
public interface MyLoginAccountService {

    /**
     * 获取可被spring security管理的权限字符串
     *
     * @param myLoginAccount 可被spring security管理的实体 与 业务登录实体 的连接
     * @return 可被spring security管理的权限字符串
     */
    List<String> getSpringSecurityAuthoritiesStr(MyLoginAccount myLoginAccount);

    /**
     * 生成可被spring security管理的权限字符串
     *
     * @param myLoginAccount 可被spring security管理的实体 与 业务登录实体 的连接
     */
    void generateSpringSecurityAuthoritiesStr(MyLoginAccount myLoginAccount);

    /**
     * 依据用户名登录名获取用户对象
     *
     * @param username 用户登录名
     * @return 可被spring security管理的实体
     */
    MyUserDetails loadUserByUsername(String username);

    /**
     * 依据用户名登录名获取用户登录盐值
     *
     * @param username 用户登录名
     * @return 用户登录盐值
     */
    String loadPasswordSaltByUsername(String username);

    /**
     * 发送登录提醒
     *
     * @param myLoginAccount 登录账户
     */
    void remindLogin(MyLoginAccount myLoginAccount);

    /**
     * 根据json字符串生成MyLoginAccount实现类
     *
     * @param jsonStr json字符串
     * @return MyLoginAccount实现类
     */
    MyLoginAccount createMyLoginAccountByJsonStr(String jsonStr);

}
