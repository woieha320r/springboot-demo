package com.example.springbootwebdemo.core.log;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 打在需要记录日志的方法上
 *
  * @date 2021-11-24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Validated
public @interface AppLog {

    @NotNull
    String methodDesc();

}
