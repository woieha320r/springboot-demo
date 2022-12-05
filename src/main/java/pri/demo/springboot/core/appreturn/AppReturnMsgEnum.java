package pri.demo.springboot.core.appreturn;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * 统一返回值code、msg枚举
 * HTTP状态码语义：
 * * 100–199：信息，服务器收到请求，需要请求者继续执行操作
 * * 200–299：成功，操作被成功接收并处理
 * * 300–399：重定向，需要进一步的操作以完成请求
 * * 400–499：客户端错误，请求包含语法错误或无法完成请求
 * * 500–599：服务器错误，服务器在处理请求的过程中发生了错误
 *
 * @author woieha320r
 */
@Getter
public enum AppReturnMsgEnum {

    /**
     * 请求成功，该操作是幂等的。
     */
    OK(HttpStatus.OK, null, ""),
    /**
     * 用户新建或修改数据成功。
     */
    CREATED(HttpStatus.CREATED, null, ""),
    /**
     * 已经接受请求，但未处理完成（异步任务）
     */
    ACCEPTED(HttpStatus.ACCEPTED, null, ""),
    /**
     * 服务器成功处理，但未返回内容（删除成功或无数据）
     */
    NO_CONTENT(HttpStatus.NO_CONTENT, null, ""),
    /**
     * 用户发出的请求有错误，服务器没有进行操作，该操作是幂等的。
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, null, "请求有误"),
    /**
     * 用户未被授权
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, null, "未登录"),
    /**
     * 用户得到授权，但服务端拒绝为此用户服务
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, null, "无权访问"),
    /**
     * 用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, null, "资源不存在"),
    /**
     * 用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
     */
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, null, "请求格式不可得"),
    /**
     * 服务器发生错误，用户将无法判断发出的请求是否成功。
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, null, "服务器错误");

    /**
     * lombok会对boolean生成is方法，使用Boolean
     */
    private final Boolean success;
    @Getter(AccessLevel.NONE)
    private final Integer httpStatusCode;
    @Getter(AccessLevel.NONE)
    private final HttpStatus httpStatus;
    private final Integer code;
    private final String msg;

    AppReturnMsgEnum(HttpStatus httpStatus, Integer code, String msg) {
        int httpStatusCode = httpStatus.value();
        this.success = httpStatus.is2xxSuccessful();
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.code = Objects.isNull(code) ? httpStatusCode : code;
        this.msg = msg;
    }

}
