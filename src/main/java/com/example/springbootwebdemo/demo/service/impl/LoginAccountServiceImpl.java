package com.example.springbootwebdemo.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootwebdemo.core.jms.JmsOrderSender;
import com.example.springbootwebdemo.core.security.MyLoginAccount;
import com.example.springbootwebdemo.core.security.MyUserDetails;
import com.example.springbootwebdemo.demo.entity.*;
import com.example.springbootwebdemo.demo.mapper.*;
import com.example.springbootwebdemo.demo.service.LoginAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 登录账户 服务实现类
 * </p>
 *
  * @since 2022-01-20
 */
@Service
public class LoginAccountServiceImpl extends ServiceImpl<LoginAccountMapper, LoginAccountEntity> implements LoginAccountService {

    @Autowired
    private LoginAccountMapper loginAccountMapper;

    @Autowired
    private LoginAccountRoleMapper loginAccountRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private JmsOrderSender jmsOrderSender;

    @Autowired
    private Environment environment;

    @Override
    public List<String> getSpringSecurityAuthoritiesStr(MyLoginAccount myLoginAccount) {
        LoginAccountEntity loginAccount = (LoginAccountEntity) myLoginAccount;
        if (Objects.isNull(loginAccount.getAuthoritiesStr())) {
            this.generateSpringSecurityAuthoritiesStr(loginAccount);
        }
        return loginAccount.getAuthoritiesStr();
    }

    @Override
    public void generateSpringSecurityAuthoritiesStr(MyLoginAccount myLoginAccount) {
        LoginAccountEntity loginAccount = (LoginAccountEntity) myLoginAccount;
        List<Long> roleIds = new ArrayList<>();
        if (Objects.isNull(loginAccount.getRoles())) {
            roleIds = loginAccountRoleMapper.selectList(
                    Wrappers.lambdaQuery(LoginAccountRoleEntity.class)
                            .select(LoginAccountRoleEntity::getRole)
                            .eq(LoginAccountRoleEntity::getLoginAccount, loginAccount.getId())
            ).parallelStream().map(LoginAccountRoleEntity::getRole).collect(Collectors.toList());
            if (!roleIds.isEmpty()) {
                loginAccount.setRoles(
                        roleMapper.selectList(
                                Wrappers.lambdaQuery(RoleEntity.class)
                                        .in(RoleEntity::getId, roleIds)
                        )
                );
            }
        }
        if (Objects.isNull(loginAccount.getPermissions()) && !roleIds.isEmpty()) {
            List<Long> permissionIds = rolePermissionMapper.selectList(
                    Wrappers.lambdaQuery(RolePermissionEntity.class)
                            .select(RolePermissionEntity::getPermission)
                            .in(RolePermissionEntity::getRole, roleIds)
            ).parallelStream().map(RolePermissionEntity::getPermission).collect(Collectors.toList());
            if (!permissionIds.isEmpty()) {
                loginAccount.setPermissions(
                        permissionMapper.selectList(
                                Wrappers.lambdaQuery(PermissionEntity.class)
                                        .in(PermissionEntity::getId, permissionIds)
                        )
                );
            }
        }
        List<String> authoritiesStr = loginAccount.getRoles().parallelStream().map(role -> "ROLE_" + role.getName()).collect(Collectors.toList());
        authoritiesStr.addAll(loginAccount.getPermissions().parallelStream().map(PermissionEntity::getName).collect(Collectors.toList()));
        loginAccount.setAuthoritiesStr(authoritiesStr);
    }

    @Override
    public MyUserDetails loadUserByUsername(String username) {
        LoginAccountEntity loginAccount = loginAccountMapper.selectOne(
                Wrappers.lambdaQuery(LoginAccountEntity.class)
                        .eq(LoginAccountEntity::getLoginName, username)
        );
        return new MyUserDetails(this, loginAccount);
    }

    @Override
    public String loadPasswordSaltByUsername(String username) {
        return loginAccountMapper.selectOne(
                Wrappers.lambdaQuery(LoginAccountEntity.class)
                        .select(LoginAccountEntity::getLoginPasswordSalt)
                        .eq(LoginAccountEntity::getLoginName, username)
        ).getLoginPasswordSalt();
    }

    @Override
    public void remindLogin(MyLoginAccount myLoginAccount) {
        if (StrUtil.isNotBlank(myLoginAccount.getSecurityEmail())) {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            //读取配置文件配置邮件的登录名
            simpleMailMessage.setFrom(environment.getProperty("spring.mail.username", StrUtil.EMPTY));
            simpleMailMessage.setTo(myLoginAccount.getSecurityEmail());
            simpleMailMessage.setSubject("来自sbwd的提醒");
            simpleMailMessage.setText("登录提醒邮件");
            jmsOrderSender.sendOrder("sbwd-mail", JSONUtil.toJsonStr(
                    simpleMailMessage,
                    JSONConfig.create().setIgnoreNullValue(true)
            ));
        }
    }

    @Override
    public MyLoginAccount createMyLoginAccountByJsonStr(String jsonStr) {
        return JSONUtil.toBean(jsonStr, LoginAccountEntity.class);
    }

    @Override
    public List<String> getAllLoginName(Integer pageNum, Integer pageSize) {
        IPage<LoginAccountEntity> iPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<LoginAccountEntity> wrappers = Wrappers.lambdaQuery(LoginAccountEntity.class).select(LoginAccountEntity::getLoginName);
        return loginAccountMapper.selectPage(iPage, wrappers).getRecords().parallelStream().map(LoginAccountEntity::getLoginName).collect(Collectors.toList());
    }
}
