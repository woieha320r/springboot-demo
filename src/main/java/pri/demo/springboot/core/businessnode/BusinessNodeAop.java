package pri.demo.springboot.core.businessnode;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pri.demo.springboot.core.config.Constants;
import pri.demo.springboot.core.config.JmsJsonTemplate;
import pri.demo.springboot.entity.SysBusinessTrackEntity;
import pri.demo.springboot.entity.SysUserEntity;
import pri.demo.springboot.core.security.JwtAuthenticationToken;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 业务节点切面
 *
 * @author woieha320r6
 */
@Aspect
@Component
@Slf4j
public class BusinessNodeAop {

    private final JmsJsonTemplate jmsJsonTemplate;

    @Autowired
    public BusinessNodeAop(JmsJsonTemplate jmsJsonTemplate) {
        this.jmsJsonTemplate = jmsJsonTemplate;
    }

    private SysBusinessTrackEntity businessTrack;

    @Pointcut(value = "@annotation(pri.demo.springboot.core.businessnode.BusinessNodeAnno)")
    public void pointCut() {
    }

    @Before(value = "@annotation(businessNodeAnno)")
    public void before(BusinessNodeAnno businessNodeAnno) {
        log.debug("切面-开始:业务节点");
        LocalDateTime now = LocalDateTime.now();
        SysUserEntity loginEntity = JwtAuthenticationToken.getCurrentLoginEntity();
        businessTrack = new SysBusinessTrackEntity()
                .setNode(businessNodeAnno.node().getNode())
                .setUserId(Objects.isNull(loginEntity) ? null : loginEntity.getId())
                .setCreateTime(now)
                .setUpdateTime(now)
        ;
    }

    @AfterReturning(value = "pointCut()")
    public void success() {
        publish(businessTrack.setSuccess(true));
    }

    @AfterThrowing(value = "pointCut()")
    public void exception() {
        publish(businessTrack.setSuccess(false));
    }

    private void publish(SysBusinessTrackEntity businessTrack) {
        log.debug("切面-结束:业务节点");
        jmsJsonTemplate.send(Constants.JMS_BUSINESS_NODE, businessTrack);
    }

}
