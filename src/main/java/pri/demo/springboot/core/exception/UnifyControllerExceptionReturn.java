package pri.demo.springboot.core.exception;

import cn.hutool.http.ContentType;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import pri.demo.springboot.core.appreturn.AppReturn;
import pri.demo.springboot.core.appreturn.NotAppReturn;

/**
 * 全局controller/interceptor异常处理，返回值封装。
 * 此处捕获后，Interceptor中不会再有异常，过滤器异常由其自身负责响应
 * <p>
 * 用HandlerInterceptor.afterCompletion()处理异常并置null后同线程还会再进入一次，而且会报两次error。
 * * 第一次由o.a.c.c.C.[.[.[.[dispatcherServlet]
 * * 第二次由s.e.ErrorMvcAutoConfiguration$StaticView
 *
 * @author woieha320r
 */
@RestControllerAdvice
@Slf4j
public class UnifyControllerExceptionReturn implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Autowired
    public UnifyControllerExceptionReturn(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * SpringMvc参数绑定验证失败
     */
    @ExceptionHandler(value = {BindException.class})
    public AppReturn<String> bindParamError(BindException exception) {
        log.error("", exception);
        StringBuilder errorMsg = new StringBuilder();
        exception.getBindingResult().getFieldErrors().forEach(
                error -> errorMsg.append(error.getField()).append(error.getDefaultMessage()).append(";")
        );
        return AppReturn.clientErr(errorMsg.toString());
    }

    /**
     * 自定义参数异常
     */
    @ExceptionHandler(value = {ParamException.class})
    public AppReturn<String> paramError(ParamException exception) {
        log.error("", exception);
        return AppReturn.clientErr(exception.getMessage());
    }

    /**
     * 登录异常
     */
    @ExceptionHandler(value = {LoginException.class})
    public AppReturn<String> paramException(LoginException exception) {
        log.error("", exception);
        return AppReturn.unAuth(exception.getMessage());
    }

    /**
     * jwt token无效
     */
    @ExceptionHandler(value = {JWTVerificationException.class})
    public AppReturn<String> tokenError(JWTVerificationException exception) {
        log.error("", exception);
        return AppReturn.unAuth(null);
    }

    /**
     * SpringSecurity鉴权失败
     */
    @ExceptionHandler(value = {AccessDeniedException.class, AuthenticationException.class})
    public AppReturn<String> permissionError(RuntimeException exception) {
        log.error("", exception);
        return AppReturn.forbidden();
    }

    /**
     * 后端要求必填的参数没填
     */
    @ExceptionHandler(value = {ServletRequestBindingException.class})
    public AppReturn<String> mustParamMissing(RuntimeException exception) {
        log.error("", exception);
        return AppReturn.clientErr("请求中缺少必填参数");
    }

    /**
     * 其他异常
     */
    @ExceptionHandler(value = {Throwable.class})
    public AppReturn<String> unknownError(Throwable exception) {
        log.error("", exception);
        return AppReturn.serverErr();
    }

    /**
     * 何时执行beforeBodyWrite
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        boolean declare = returnType.hasMethodAnnotation(NotAppReturn.class);
        boolean isSwagger = returnType.getDeclaringClass().getName().contains(".swagger");
        boolean handle = !(declare || isSwagger);
        log.debug("统一返回值-controller/拦截器:" + handle);
        return handle;
    }

    /**
     * 统一封装返回值
     */
    @Override
    public Object beforeBodyWrite(
            Object body, MethodParameter returnType, MediaType selectedContentType
            , Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request
            , ServerHttpResponse response
    ) {
        //如果返回String，会调用StringHttpMessageConvert处理，而我们已将其改为AppReturn，会异常
        if (body instanceof String) {
            try {
                response.getHeaders().set(HttpHeaders.CONTENT_TYPE, ContentType.JSON.getValue());
                return objectMapper.writeValueAsString(AppReturn.ok(body));
            } catch (JsonProcessingException e) {
                log.error("", e);
            }
        }
        //此时已被@ExceptionHandler处理了，处理后就是AppReturn了，如果已经是AppReturn，放行
        if (body instanceof AppReturn) {
            return body;
        }
        return AppReturn.ok(body);
    }
}
