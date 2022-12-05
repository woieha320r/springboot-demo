package pri.demo.springboot.core.exception;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import org.springframework.http.HttpStatus;
import pri.demo.springboot.core.appreturn.AppReturn;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 把异常写入HttpServletResponse，如过滤器环境
 *
 * @author woieha320r
 */
public class ExceptionWriteResponse {

    public static void write(AppReturn<Object> appReturn, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(ContentType.JSON.getValue());
        response.getWriter().write(JSONUtil.toJsonStr(appReturn));
        response.flushBuffer();
    }

}
