package com.example.springbootwebdemo.core.security;

/**
 * 可被spring security管理的实体 与 业务登录实体 的过度实体接口
 *
  * @date 2022-01-21
 */
public interface MyLoginAccount {

    /**
     * 获取唯一标识id
     *
     * @return 唯一标识id
     */
    Long getId();

    /**
     * 获取登录密码
     *
     * @return 登录密码
     */
    String getLoginPassword();

    /**
     * 获取登录名
     *
     * @return 登录名
     */
    String getLoginName();

    /**
     * 是否开启
     *
     * @return 账户是否可用
     */
    Boolean getEnable();

    /**
     * 是否在登录时发送提醒信息（邮件、短信）
     *
     * @return 是否在登录时发送提醒信息（邮件、短信）
     */
    Boolean getRemindWhenLogin();

    /**
     * 获取安全验证邮箱
     *
     * @return 安全验证邮箱
     */
    String getSecurityEmail();

}
