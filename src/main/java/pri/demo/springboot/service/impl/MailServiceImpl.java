package pri.demo.springboot.service.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import pri.demo.springboot.service.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * 邮件服务实现
 *
 * @author woieha320r
 */
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private MimeMessage message;
    private MimeMessageHelper messageHelper;
    private final Configuration cfg;

    @Value(value = "${spring.mail.username}")
    private String mailFrom;

    @Autowired
    //JavaMailSender的@Autowired报红不用管，运行后会根据配置文件注册到Spring Bean容器的
    public MailServiceImpl(JavaMailSender mailSender, Configuration cfg) {
        this.mailSender = mailSender;
        this.cfg = cfg;
    }

    @Override
    public void sendSimpleMail(String subject, String content, String... to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setSentDate(new Date());
        mailSender.send(message);
    }

    @Override
    public void sendHtmlMail(String subject, String content, String... to) throws MessagingException {
        createMimeMessage(subject, content, to);
        mailSender.send(message);
    }

    @Override
    public void sendHtmlMail(String subject, String freemarkerName, Map<String, Object> freemarkerMap, String... to) throws MessagingException, IOException, TemplateException {
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(cfg.getTemplate(freemarkerName), freemarkerMap);
        createMimeMessage(subject, content, to);
        mailSender.send(message);
    }

    @Override
    public void sendAttachmentsMail(String subject, String content, String filePath, String... to) throws MessagingException {
        createMimeMessage(subject, content, to);
        File file = new File(filePath);
        messageHelper.addAttachment(file.getName(), new FileSystemResource(file));
        mailSender.send(message);
    }

    private void createMimeMessage(String subject, String content, String... to) throws MessagingException {
        message = mailSender.createMimeMessage();
        messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(mailFrom);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        messageHelper.setSentDate(new Date());
    }

}
