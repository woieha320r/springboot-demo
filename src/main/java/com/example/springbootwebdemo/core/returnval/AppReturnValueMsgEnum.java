package com.example.springbootwebdemo.core.returnval;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * 统一返回值code、msg枚举
 *
  * @version 1.0
 * @date 2022/1/25 20:49
 */
@Getter
public enum AppReturnValueMsgEnum {

    /**
     * 服务器成功返回用户请求的数据，该操作是幂等的。
     */
    OK(HttpStatus.OK, null, "成功"),
    /**
     * 用户新建或修改数据成功。
     */
    CREATED(HttpStatus.CREATED, null, "新建或更新成功"),
    /**
     * 表示一个请求已经进入后台排队（异步任务）
     */
    ACCEPTED(HttpStatus.ACCEPTED, null, "已受理"),
    /**
     * 用户删除数据成功。
     */
    NO_CONTENT(HttpStatus.NO_CONTENT, null, "删除成功"),
    /**
     * 用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, null, "入参有误"),
    /**
     * 表示用户没有权限（令牌、用户名、密码错误）。
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, null, "无权操作或登录失败"),
    /**
     * 表示用户得到授权（与401错误相对），但是访问是被禁止的。
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, null, "身份验证失败"),
    /**
     * 用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, null, "请求的资源不存在"),
    /**
     * 用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
     */
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, null, "请求格式不可得"),
    /**
     * 用户请求的资源被永久删除，且不会再得到的。
     */
    GONE(HttpStatus.GONE, null, "资源已被永久删除"),
    /**
     * 当创建一个对象时，发生一个验证错误。
     */
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, null, "不知道啥意思"),
    /**
     * 服务器发生错误，用户将无法判断发出的请求是否成功。
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, null, "未知错误");

    private final Boolean success;
    private final int httpStatusCode;
    @Getter(AccessLevel.NONE)
    private final HttpStatus httpStatus;
    private final int code;
    private final String msg;

    AppReturnValueMsgEnum(HttpStatus httpStatus, Integer code, String msg) {
        this.success = httpStatus.is2xxSuccessful();
        this.httpStatusCode = httpStatus.value();
        this.httpStatus = httpStatus;
        this.code = Objects.isNull(code) ? httpStatus.value() : code;
        this.msg = msg;
    }

}
