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

/**
 * 第三方登录
 *
 * @author woieha320r
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OthersLoginQuery {

    @ApiModelProperty(value = "在第三方平台申请的client_id")
    @NotEmpty
    private String clientId;

    @ApiModelProperty(value = "临时授权码")
    @NotEmpty
    private String code;

    @ApiModelProperty(value = "登录方式（用户名/邮箱）", required = true)
    @NotNull
    private LoginType loginType;

}
