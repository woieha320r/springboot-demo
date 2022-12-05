package pri.demo.springboot.core.appreturn;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 统一返回值
 *
 * @author woieha320r
 */
@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "返回值")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppReturn<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否成功", required = true, example = "true")
    private boolean success;
    @ApiModelProperty(value = "状态编码", required = true, example = "200")
    private int code;
    @ApiModelProperty(value = "状态提示", example = "执行成功")
    private String msg;
    @ApiModelProperty(value = "json数据")
    private T data;

    private AppReturn(AppReturnMsgEnum returnValueMsgEnum, String msg, T data) {
        this.success = returnValueMsgEnum.getSuccess();
        this.code = returnValueMsgEnum.getCode();
        this.msg = Objects.isNull(msg) ? returnValueMsgEnum.getMsg() : msg;
        this.data = data;
    }

    public static <T> AppReturn<T> of(AppReturnMsgEnum returnValueMsgEnum, String msg, T data) {
        return new AppReturn<>(returnValueMsgEnum, msg, data);
    }

    public static <T> AppReturn<T> of(AppReturnMsgEnum returnValueMsgEnum, T data) {
        return new AppReturn<>(returnValueMsgEnum, null, data);
    }

    public static <T> AppReturn<T> ok(T data) {
        return new AppReturn<>(AppReturnMsgEnum.OK, null, data);
    }

    public static <T> AppReturn<T> clientErr(@NotNull String msg) {
        return new AppReturn<>(AppReturnMsgEnum.BAD_REQUEST, msg, null);
    }

    public static <T> AppReturn<T> serverErr() {
        return new AppReturn<>(AppReturnMsgEnum.INTERNAL_SERVER_ERROR, null, null);
    }

    public static <T> AppReturn<T> unAuth(String msg) {
        return new AppReturn<>(AppReturnMsgEnum.UNAUTHORIZED, msg, null);
    }

    public static <T> AppReturn<T> forbidden() {
        return new AppReturn<>(AppReturnMsgEnum.FORBIDDEN, null, null);
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
