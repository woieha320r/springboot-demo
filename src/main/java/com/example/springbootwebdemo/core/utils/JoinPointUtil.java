package com.example.springbootwebdemo.core.utils;

import cn.hutool.json.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 处理ProceedingJoinPoint的工具
 *
  * @date 2022-02-24
 */
public class JoinPointUtil {

    /**
     * 解析出方法
     */
    public static Method getMethod(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod();
    }

    /**
     * 解析出入参map
     */
    public static Map<String, Object> getParamMap(ProceedingJoinPoint joinPoint) {
        Object[] paramVals = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Map<String, Object> methodParamsMap = new HashMap<>(3);
        if (Objects.nonNull(paramNames)) {
            IntStream.range(0, paramNames.length).forEach(i -> methodParamsMap.put(paramNames[i], paramVals[i]));
        }
        return methodParamsMap;
    }

    /**
     * 根据类型返回对应的入参
     */
    public static <T> List<Object> getParamValsByType(ProceedingJoinPoint joinPoint, Class<T> cls) {
        return getParamMap(joinPoint).values()
                .parallelStream()
                .filter(obj -> cls.isAssignableFrom(obj.getClass()))
                .collect(Collectors.toList());
    }

    /**
     * 解析出入参json字符串
     */
    public static String getParamJsonStr(ProceedingJoinPoint joinPoint) {
        return JSONUtil.toJsonStr(getParamMap(joinPoint));
    }

}
