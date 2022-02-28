package com.example.springbootwebdemo.core.returnval;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 统一返回值
 *
  * @date 2021-11-24
 */
@Api(value = "响应体对象")
@Accessors(chain = true)
@Data
//值为null时不序列化
@JsonInclude(JsonInclude.Include.NON_NULL)
//缓存反序列化时需要
@NoArgsConstructor
public class AppReturnValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否成功", required = true)
    private boolean success;
    @ApiModelProperty(value = "状态编码", required = true)
    private int code;
    @ApiModelProperty(value = "提示信息")
    private String msg;
    @ApiModelProperty(value = "响应数据")
    private Object data;

    private AppReturnValue(AppReturnValueMsgEnum returnValueMsgEnum, Object data) {
        this.success = returnValueMsgEnum.getSuccess();
        this.code = returnValueMsgEnum.getCode();
        this.msg = returnValueMsgEnum.getMsg();
        this.data = data;
    }

    public static AppReturnValue get(AppReturnValueMsgEnum returnValueMsgEnum, Object data) {
        return new AppReturnValue(returnValueMsgEnum, data);
    }

    public static AppReturnValue ok(Object data) {
        return AppReturnValue.get(AppReturnValueMsgEnum.OK, data);
    }

    public static AppReturnValue clientErr(Object data) {
        return AppReturnValue.get(AppReturnValueMsgEnum.BAD_REQUEST, data);
    }

    public static AppReturnValue serverErr(Object data) {
        return AppReturnValue.get(AppReturnValueMsgEnum.INTERNAL_SERVER_ERROR, data);
    }

    public static AppReturnValue serverErr() {
        return AppReturnValue.serverErr(null);
    }

    public static AppReturnValue authError(Object data) {
        return AppReturnValue.get(AppReturnValueMsgEnum.FORBIDDEN, data);
    }

    public static AppReturnValue authError() {
        return AppReturnValue.authError(null);
    }

    public static String getString(AppReturnValueMsgEnum returnValueMsgEnum, Object data) {
        return AppReturnValue.get(returnValueMsgEnum, data).toString();
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this, JSONConfig.create().setIgnoreNullValue(true));
    }
}
