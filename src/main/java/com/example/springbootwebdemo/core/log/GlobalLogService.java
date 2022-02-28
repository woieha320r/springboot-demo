package com.example.springbootwebdemo.core.log;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局日志对象服务接口
 *
  * @date 2022-01-21
 */
@Service
public interface GlobalLogService {

    /**
     * 获取全局日志对象
     *
     * @return 全局日志对象
     */
    GlobalLog getLogObj();

    /**
     * 根据方法反射获取class路径
     *
     * @param methodClass 执行的方法反射类路径
     * @param methodDesc  方法描述
     * @param globalLog   全局日志对象
     */
    void setMethodInfo(String methodClass, String methodDesc, GlobalLog globalLog);

    /**
     * 设置请求信息
     *
     * @param request   http请求对象
     * @param params    入参
     * @param globalLog 全局日志对象
     */
    void setRequestInfo(HttpServletRequest request, String params, GlobalLog globalLog);

    /**
     * 设置响应和异常
     *
     * @param returnVal 返回值
     * @param e         异常
     * @param globalLog 全局日志对象
     */
    void setResponseAndExceptionInfo(String returnVal, Throwable e, GlobalLog globalLog);

    /**
     * 保存日志
     *
     * @param globalLog     待保存的全局日志对象
     * @param waitBatchSave 是否交由缓存并由其他任务负责批量存库，否则立即存库
     */
    void saveLog(GlobalLog globalLog, boolean waitBatchSave);

    /**
     * 批量保存日志
     *
     * @param list 待保存的列表
     */
    void batchSaveLog(List<GlobalLog> list);

}
