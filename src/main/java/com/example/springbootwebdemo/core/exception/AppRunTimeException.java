package com.example.springbootwebdemo.core.exception;

import cn.hutool.core.util.StrUtil;

/**
 * 主动抛出的不符合预期的异常
 *
 * 没有需要的地方，已弃用
 *
  * @date 2022-01-19
 */
@Deprecated
public class AppRunTimeException extends RuntimeException {

    public AppRunTimeException(String msg) {
        super(StrUtil.isBlank(msg) ? "程序运行不符合预期" : msg);
    }

}
