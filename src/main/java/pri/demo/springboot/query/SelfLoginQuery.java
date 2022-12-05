package pri.demo.springboot.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import pri.demo.springboot.enums.LoginType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 己方系统登录
 *
 * @author woieha320r
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SelfLoginQuery {

    @ApiModelProperty(value = "用户名/邮箱", required = true)
    @NotEmpty
    @Size(min = 1, max = 20)
    private String identifier;

    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty
    private String credential;

    @ApiModelProperty(value = "登录方式（用户名/邮箱）", required = true)
    @NotNull
    private LoginType loginType;

}
