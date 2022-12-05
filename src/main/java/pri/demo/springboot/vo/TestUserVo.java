package pri.demo.springboot.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import pri.demo.springboot.enums.Gender;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 用户信息Vo
 * </p>
 *
 * @author woieha320r
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "用户信息")
public class TestUserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "昵称", required = true)
    private String nickname;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "性别")
    private Gender gender;

    @ApiModelProperty(value = "测试Date")
    private Date date;

    @ApiModelProperty(value = "测试LocalDateTime")
    private LocalDateTime localDateTime;

}
