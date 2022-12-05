package pri.demo.springboot.core.login;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pri.demo.springboot.core.config.Constants;
import pri.demo.springboot.dto.LoginJmsDto;
import pri.demo.springboot.enums.LoginType;
import pri.demo.springboot.service.MailService;
import pri.demo.springboot.service.SysLoginAccountService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 登录通知
 *
 * @author woieha320r
 */
@Component
@Slf4j
public class LoginNoticeJms {

    private final MailService mailService;
    private final SysLoginAccountService accountService;

    @Autowired
    public LoginNoticeJms(MailService mailService, SysLoginAccountService accountService) {
        this.mailService = mailService;
        this.accountService = accountService;
    }

    @JmsListener(destination = Constants.JMS_LOGIN)
    public void businessNodeRecord(String msg) throws MessagingException, IOException, TemplateException {
        log.debug("消息队列-接收:登录");
        LoginJmsDto loginJmsDto = JSONUtil.toBean(msg, LoginJmsDto.class);
        String email = email(loginJmsDto);
        if (Objects.isNull(email) || email.isEmpty()) {
            return;
        }
        String subject = "SpringBoot-Demo 登录提醒";
        Map<String, Object> freemarkerMap = new HashMap<>();
        freemarkerMap.put("subject", subject);
        freemarkerMap.put("nickname", nickname(loginJmsDto));
        freemarkerMap.put("loginTime", loginJmsDto.getLoginTime());
        mailService.sendHtmlMail(subject, "loginNotice.ftl", freemarkerMap, email);
    }

    private String email(LoginJmsDto jmsDto) {
        return Objects.equals(jmsDto.getLoginType(), LoginType.EMAIL)
                ? jmsDto.getIdentifier()
                : accountService.loginEmail(jmsDto.getLoginType(), jmsDto.getIdentifier());
    }

    private String nickname(LoginJmsDto jmsDto) {
        return Objects.equals(jmsDto.getLoginType(), LoginType.USERNAME)
                ? jmsDto.getIdentifier()
                : accountService.nickname(jmsDto.getLoginType(), jmsDto.getIdentifier());
    }

}
