package pri.demo.springboot.service;

import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

/**
 * 邮件服务
 *
 * @author woieha320r
 */
public interface MailService {

    /**
     * 文本邮件
     *
     * @param to      收件人列表
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String subject, String content, String... to);

    /**
     * html邮件
     *
     * @param to      收件人列表
     * @param subject 主题
     * @param content 内容
     * @throws MessagingException 邮件消息异常
     */
    void sendHtmlMail(String subject, String content, String... to) throws MessagingException;

    /**
     * html邮件
     *
     * @param to             收件人列表
     * @param subject        主题
     * @param freemarkerName freemarker模版文件名
     * @param freemarkerMap  freemarker数据集，将使用mail模版
     * @throws IOException        freemarker异常
     * @throws TemplateException  freemarker异常
     * @throws MessagingException 邮件消息异常
     */
    void sendHtmlMail(String subject, String freemarkerName, Map<String, Object> freemarkerMap, String... to) throws MessagingException, IOException, TemplateException;

    /**
     * 附件邮件
     *
     * @param to       收件人列表
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件地址
     * @throws MessagingException 邮件消息异常
     */
    void sendAttachmentsMail(String subject, String content, String filePath, String... to) throws MessagingException;

}