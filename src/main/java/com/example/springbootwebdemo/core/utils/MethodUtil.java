package com.example.springbootwebdemo.core.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理Method的工具
 *
  * @date 2022-02-24
 */
public class MethodUtil {

    /**
     * 获取名称（class名称#方法名）
     */
    public static String getPathName(Method method) {
        return method.getDeclaringClass().getName() + "#" + method.getName();
    }

    /**
     * 获取参数名
     */
    public static List<String> getParamNameList(Method method) {
        return Arrays.stream(method.getParameters()).map(Parameter::getName).collect(Collectors.toList());
    }

}
