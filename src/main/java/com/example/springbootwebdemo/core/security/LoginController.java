package com.example.springbootwebdemo.core.security;

import cn.hutool.json.JSONUtil;
import com.example.springbootwebdemo.core.log.AppLog;
import com.example.springbootwebdemo.core.property.TokenProperties;
import com.example.springbootwebdemo.core.returnval.AppReturnValue;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 登录 前端控制器
 * </p>
 *
  * @since 2021-11-29
 */
@Api(value = "登录", tags = "登录")
@Slf4j
@RestController
@RequestMapping(value = "/token", produces = "application/json;charset=UTF-8")
public class LoginController {

    @Autowired
    private MyLoginAccountService myLoginAccountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 使用全局异常捕获时，不要在参数列表使用Errors类型的对象接收错误，不然不会抛异常出去
     * if (errors.hasErrors()) {
     * System.out.println(JSONUtil.toJsonStr(errors));
     * }
     */
    @AppLog(methodDesc = "获取token")
    @ApiOperation(value = "获取token")
    @GetMapping
    public AppReturnValue login(
            @ApiParam(value = "登录名")
            @NotBlank(message = "用户名不可为空")
            @RequestParam String loginName,
            @ApiParam(value = "登录密码")
            @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,16}", message = "密码规则：长度8~16位，需同时包含字母、数字、特殊字符")
            @RequestParam String password
    ) {
        /*执行认证，成功会返回认证成功的authentication，失败会抛异常
         * 如果需要将信息置入SecurityContext：
         *  Authentication authenticationSuccess = authenticationManager.authenticate()
         *  SecurityContextHolder.getContext().setAuthentication(authenticationSuccess)
         *  此时从authenticationSuccess取出principal会是仅包含验证时传入的authentication信息的UserDetails对象，认证时框架替换的
         *authenticate方法的执行：
         * 以用户名调用UserDetailsService::loadUserByUsername，得到UserDetails并替换用户名
         * 调用PasswordEncoder::matches(密码, UserDetails::getPassword)
         */
        //取盐值前先验证下是否存在此用户，TODO：与authenticate重复，考虑优化
        userDetailsService.loadUserByUsername(loginName);
        String salt = myLoginAccountService.loadPasswordSaltByUsername(loginName);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginName,
                        //因为自定义PasswordEncoder::matches原因，用和salt组合的密码替换用户输入的密码
                        MyPasswordEncoder.getPasswordOutSideUseBeforeEncode(password, salt)
                )
        );

        //TODO:springsecurity调authenticationManager.authenticate时会loadByName并且调用一次getAuthority。并且验证完对象就没了，下边儿还要再查，需要优化

        //把后续业务中可能用到的登录实体信息加密后存入jwt，下次请求时携带此token，过滤器会从token解析出实体置入SecurityContext
        MyLoginAccount myLoginAccount = myLoginAccountService.loadUserByUsername(loginName).getMyLoginAccount();
        //如需登录期间不关注权限变化，则在生成token前生成权限，以置入token。直至退出都会使用token里的权限。
        // 否则每次请求都会查库里的权限（MyLoginAccountService::getSpringSecurityAuthoritiesStr）
        if (!tokenProperties.isAlwaysSelectAuthority()) {
            myLoginAccountService.generateSpringSecurityAuthoritiesStr(myLoginAccount);
        }
        String token = jwtService.generateTokenFromPayload(JSONUtil.toJsonStr(myLoginAccount));

        //认证成功后若开启了登录时提醒，则向用户发出登录提醒
        if (myLoginAccount.getRemindWhenLogin()) {
            myLoginAccountService.remindLogin(myLoginAccount);
        }

        return AppReturnValue.ok(token);
    }

}
