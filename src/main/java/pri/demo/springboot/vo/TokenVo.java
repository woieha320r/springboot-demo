package pri.demo.springboot.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * tokenVo
 *
 * @author woieha320r
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class TokenVo {

    @ApiModelProperty(value = "token", example = "e67y.9ij8.00ok", required = true)
    private String token;

    @ApiModelProperty(value = "请求API式应携带在哪个headers上", example = "token", required = true)
    private String httpHeader;

    @ApiModelProperty(value = "过期unix时间戳", required = true)
    private Long deathTime;

}
