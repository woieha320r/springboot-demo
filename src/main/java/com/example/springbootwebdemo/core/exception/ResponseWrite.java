package com.example.springbootwebdemo.core.exception;

import cn.hutool.http.ContentType;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 回写响应
 *
  * @date 2022-02-22
 */
public class ResponseWrite {

    /**
     * 客户端问题，响应内容为UTF-8编码的JSON
     */
    public static void cliErrUtf8Json(HttpServletResponse response, String data) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        utf8Json(response, data);
    }

    /**
     * 正常
     */
    public static void okUtf8Json(HttpServletResponse response, String data) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        utf8Json(response, data);
    }

    private static void utf8Json(HttpServletResponse response, String data) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(ContentType.JSON.getValue());
        response.getWriter().write(data);
        response.flushBuffer();
    }

}
