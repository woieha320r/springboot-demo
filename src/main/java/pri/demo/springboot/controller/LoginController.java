package pri.demo.springboot.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pri.demo.springboot.core.config.Constants;
import pri.demo.springboot.core.config.JmsJsonTemplate;
import pri.demo.springboot.core.exception.ParamException;
import pri.demo.springboot.core.security.ThirdPartyLoginProperties;
import pri.demo.springboot.dto.LoginJmsDto;
import pri.demo.springboot.query.OthersLoginQuery;
import pri.demo.springboot.query.SelfLoginQuery;
import pri.demo.springboot.service.SysLoginAccountService;
import pri.demo.springboot.vo.TokenVo;

import java.util.Map;

/**
 * 登录控制器
 *
 * @author woieha320r
 */
@Api(tags = "token/登录")
@ApiSupport(author = "woieha320r")
@RestController
@RequestMapping(value = "/token")
@Slf4j
public class LoginController {

    private final SysLoginAccountService accountService;
    private final JmsJsonTemplate jmsTemplate;
    private final ThirdPartyLoginProperties thirdPartyLoginProperties;

    @Autowired
    public LoginController(SysLoginAccountService accountService, JmsJsonTemplate jmsTemplate, ThirdPartyLoginProperties thirdPartyLoginProperties) {
        this.accountService = accountService;
        this.jmsTemplate = jmsTemplate;
        this.thirdPartyLoginProperties = thirdPartyLoginProperties;
    }

    @ApiOperation(value = "己方系统（用户名/邮箱）")
    @GetMapping
    public TokenVo self(@Validated @RequestBody SelfLoginQuery query) {
        TokenVo returnVal;
        switch (query.getLoginType()) {
            case USERNAME:
                returnVal = accountService.usernameLogin(query);
                break;
            case EMAIL:
                returnVal = accountService.emailLogin(query);
                break;
            default:
                throw new ParamException("只允许用户名/邮箱登录方式");
        }
        jmsTemplate.send(Constants.JMS_LOGIN, new LoginJmsDto(query));
        return returnVal;
    }

    @ApiOperation(value = "第三方系统（GitHub）")
    @GetMapping("/github")
    public TokenVo others(@ApiParam(value = "GitHub临时授权码") @RequestBody OthersLoginQuery query) {
        return accountService.githubLoginAndJms(query);
    }

    @ApiOperation(value = "获取GitHub client_id")
    @GetMapping("/github/client_id")
    public String githubClientId() {
        for (Map.Entry<String, String> stringStringEntry : thirdPartyLoginProperties.getGithub().entrySet()) {
            return stringStringEntry.getKey();
        }
        throw new RuntimeException("未获取到GitHub client_id");
    }

}
