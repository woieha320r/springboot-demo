package pri.demo.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import pri.demo.springboot.enums.LoginType;
import pri.demo.springboot.query.SelfLoginQuery;

import java.time.LocalDateTime;

/**
 * 登录通知消息
 *
 * @author woieha320r
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginJmsDto {

    private String identifier;
    private LoginType loginType;
    private LocalDateTime loginTime;

    public LoginJmsDto(SelfLoginQuery selfLoginQuery) {
        this.identifier = selfLoginQuery.getIdentifier();
        this.loginType = selfLoginQuery.getLoginType();
        this.loginTime = LocalDateTime.now();
    }

}
