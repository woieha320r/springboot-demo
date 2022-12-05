package pri.demo.springboot;

import cn.hutool.json.JSONUtil;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pri.demo.springboot.core.login.LoginNoticeJms;
import pri.demo.springboot.dto.LoginJmsDto;
import pri.demo.springboot.enums.LoginType;
import pri.demo.springboot.query.SelfLoginQuery;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;

@SpringBootTest
public class MailSend {

    @Autowired
    LoginNoticeJms loginNoticeJms;

    @Test
    public void test() throws MessagingException, IOException, TemplateException {
        SelfLoginQuery selfLoginQuery = new SelfLoginQuery()
                .setLoginType(LoginType.EMAIL)
                .setIdentifier("目标邮箱");
        LoginJmsDto loginJmsDto = new LoginJmsDto(selfLoginQuery).setLoginTime(LocalDateTime.now());
        String jmsDtoStr = JSONUtil.toJsonStr(loginJmsDto);
        loginNoticeJms.businessNodeRecord(jmsDtoStr);
    }

}
