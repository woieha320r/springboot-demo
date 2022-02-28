package com.example.springbootwebdemo.core.log;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.example.springbootwebdemo.core.returnval.AppReturnValue;
import com.example.springbootwebdemo.core.utils.JoinPointUtil;
import com.example.springbootwebdemo.core.utils.MethodUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 切面记录日志
 * <p>
 * 不要使用IDEA的Aspect类型创建！！！不然会失效
 *
  * @date 2021-11-24
 */
@Aspect
@Component
@Slf4j
public class LogRecordAspect {

    @Autowired
    private GlobalLogService globalLogService;

    /**
     * 切入所有名称以Controller结尾的类的所有公共方法
     */
    @Pointcut(value = "execution(public * *..*.*Controller.*(..))")
    public void allController() {
    }

    /**
     * 切入持有AppLog注解的方法
     */
    @Pointcut(value = "@annotation(com.example.springbootwebdemo.core.log.AppLog)")
    public void appLogPointCut() {
    }

    /**
     * 环绕持有ApiOperation注解的切入点
     */
    @Around(value = "appLogPointCut()")
    public AppReturnValue appLogPointCutAround(ProceedingJoinPoint joinPoint) throws Throwable {
        GlobalLog globalLog = globalLogService.getLogObj();
        //解析出执行方法相关的信息并记录进日志实体
        Method method = JoinPointUtil.getMethod(joinPoint);
        String methodDesc = method.getAnnotation(AppLog.class).methodDesc();
        String requestParam = JoinPointUtil.getParamJsonStr(joinPoint);
        globalLogService.setMethodInfo(MethodUtil.getPathName(method), methodDesc, globalLog);
        //不要在多线程中使用
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        globalLogService.setRequestInfo(request, requestParam, globalLog);
        log.info("{} >>> 即将执行 >>> 入参：{}", methodDesc, requestParam);
        //本方法返回值需要能转成controller实际的返回类型，本样例全部为AppReturnValue
        //这里捕获异常为了记录日志入库。异常具体应该打印什么信息和如何处理，由全局异常处理决定。
        AppReturnValue methodReturnObj;
        String responseBody = null;
        Throwable exception = null;
        try {
            LocalDateTime startTime = LocalDateTime.now();
            methodReturnObj = (AppReturnValue) joinPoint.proceed();
            responseBody = Objects.isNull(methodReturnObj) ? "void" : methodReturnObj.toString();
            log.info("{} >>> 执行完成（耗时{}秒） >>> 返回值：{}", methodDesc, LocalDateTimeUtil.between(startTime, LocalDateTime.now()).getSeconds(), responseBody);
        } catch (Throwable throwable) {
            //仅负责日志打印请求失败，然后抛出去，异常的打印和处理由全局异常负责，异常栈中引到这里，因为过程中由此抛出
            exception = throwable;
            log.error("{} >>> 执行中断 >>> 异常：{}", methodDesc, exception.getMessage());
            throw exception;
        } finally {
            globalLogService.setResponseAndExceptionInfo(responseBody, exception, globalLog);
            globalLogService.saveLog(globalLog, true);
        }
        return methodReturnObj;
    }

}
