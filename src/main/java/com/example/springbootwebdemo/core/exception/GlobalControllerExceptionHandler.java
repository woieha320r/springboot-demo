package com.example.springbootwebdemo.core.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.springbootwebdemo.core.returnval.AppReturnValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局controller异常处理
 * 依据controller中抛出的异常类型，决定向客户端的返回内容。
 * 此处的异常处理不负责记录日志，日志记录在全局日志处理类中。
 * 此处无法捕获过滤器异常，过滤器异常处理在全局过滤器异常处理类中。
 *
  * @date 2021-11-22
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * 参数绑定验证失败
     */
    @ExceptionHandler(value = {BindException.class})
    public AppReturnValue bindExceptionHandle(BindException exception) {
        StringBuilder errorMsg = new StringBuilder();
        exception.getBindingResult().getFieldErrors().forEach(error -> errorMsg.append(error.getDefaultMessage()).append("；"));
        return AppReturnValue.clientErr(errorMsg.toString());
    }

    /**
     * 权限校验异常
     * 捕获不到过滤器中的异常（JWT解析）
     */
    @ExceptionHandler(value = {JWTVerificationException.class, AccessDeniedException.class, BadCredentialsException.class, UsernameNotFoundException.class})
    public AppReturnValue tokenExceptionHandle() {
        return AppReturnValue.authError();
    }

    /**
     * 预料之外的运行时异常
     */
    @ExceptionHandler(value = {Throwable.class})
    public AppReturnValue unknownExceptionHandler(Throwable e) {
        log.error("异常：{}", ExceptionUtil.stacktraceToString(e));
        return AppReturnValue.serverErr();
    }

}