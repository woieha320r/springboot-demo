package com.example.springbootwebdemo.demo.controller;

import com.example.springbootwebdemo.core.log.AppLog;
import com.example.springbootwebdemo.core.returnval.AppReturnValue;
import com.example.springbootwebdemo.core.security.MyUserDetails;
import com.example.springbootwebdemo.demo.service.LoginAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试spring security+jwt可用性
 *
  * @date 2022-02-23
 */
@Api(value = "token", tags = "token")
@Slf4j
@RestController
@RequestMapping(value = "/token", produces = "application/json;charset=UTF-8")
public class TokenController {

    @Autowired
    private LoginAccountService loginAccountService;

    @AppLog(methodDesc = "登录者获取登录名")
    @ApiOperation(value = "登录者获取登录名")
    @GetMapping(value = "/myloginname")
    //允许认证过的用户访问
    @PreAuthorize(value = "isAuthenticated()")
    //不在swagger中显示AuthenticationPrincipal类型的入参，是在配置类中设定的全局的
    public AppReturnValue getLoginName(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return AppReturnValue.ok(myUserDetails.getUsername());
    }

    /**
     * 角色是权限的另一种表达方式。被添加了ROLE_前缀的将被视为角色
     * 对于test_role来说
     * 若置入authorities时加了ROLE_前缀，则可以使用hasRole。hasRole('ROLE_test_role') = hasRole('test_role') = hasAuthority('ROLE_test_role')
     * 不加ROLE_前缀则不能通过控制角色来限制访问权，只能使用hasAuthority('test_role')来做权限限制
     */
    @AppLog(methodDesc = "分页获取所有登陆账户名（需要扮演默认角色）")
    @ApiOperation(value = "分页获取所有登陆账户名（需要扮演默认角色）")
    @GetMapping(value = "/loginnames")
    @PreAuthorize("hasRole('default_role')")
    //@PreAuthorize("hasAuthority('test1')")
    //@PreAuthorize("hasAuthority('test')")
    public AppReturnValue allLoginNames(Integer pageNum, Integer pageSize) {
        return AppReturnValue.ok(loginAccountService.getAllLoginName(pageNum, pageSize));
    }

    /**
     * Api是否可以正常访问
     */
    //value是cacheName，key是key，全局前缀在配置文件配置。组合方式在配置类配置。指定缓存触发条件：condition="#user.id%2==0"
    //貌似先于Aspect，会导致Aspect无法切入
    //SpringCache利用代理实现，同类方法间调用不会触发缓存
    @Cacheable(value = "testUseful", key = "#root.methodName")
    @AppLog(methodDesc = "测试api可用性，无需token")
    @ApiOperation(value = "测试api可用性，无需token")
    @GetMapping(value = "/useful")
    public AppReturnValue testUseful() {
        log.debug("测试SpringCache，已进入方法。若缓存生效，知道缓存失效前，此后的方法调用不会再次执行方法。");
        return AppReturnValue.ok(null);
    }

}
