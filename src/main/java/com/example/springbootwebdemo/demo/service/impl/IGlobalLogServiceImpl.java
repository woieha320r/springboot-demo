package com.example.springbootwebdemo.demo.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootwebdemo.core.log.GlobalLog;
import com.example.springbootwebdemo.core.property.ExceptionProperty;
import com.example.springbootwebdemo.core.security.MyUserDetails;
import com.example.springbootwebdemo.core.utils.MyBeanUtil;
import com.example.springbootwebdemo.demo.entity.GlobalLogEntity;
import com.example.springbootwebdemo.demo.mapper.GlobalLogMapper;
import com.example.springbootwebdemo.demo.property.BatchSaveCacheLogProperty;
import com.example.springbootwebdemo.demo.service.IGlobalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 全局日志 服务实现类
 * </p>
 *
  * @since 2022-01-21
 */
@Service
public class IGlobalLogServiceImpl extends ServiceImpl<GlobalLogMapper, GlobalLogEntity> implements IGlobalLogService {

    @Autowired
    private ExceptionProperty exceptionProperty;

    @Autowired
    private RedisTemplate<String, Object> strObjRedisTemplate;

    @Autowired
    private BatchSaveCacheLogProperty batchSaveCacheKey;

    @Autowired
    private GlobalLogMapper globalLogMapper;

    @Override
    public GlobalLog getLogObj() {
        return new GlobalLogEntity().setRequestTime(LocalDateTime.now());
    }

    @Override
    public void setRequestInfo(HttpServletRequest request, String params, GlobalLog globalLog) {
        if (Objects.nonNull(request)) {
            //获取登录主体信息：spring security容器或重新解析token
            //若使用spring security容器方式，当配置类中配置了此路径无需认证时，容器中是匿名用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.nonNull(authentication)) {
                Object principal = authentication.getPrincipal();
                if (!String.class.isAssignableFrom(principal.getClass())) {
                    globalLog.setLoginAccount(((MyUserDetails) principal).getMyLoginAccount().getId());
                }
            }
            /*
            利用解析Jwt的过滤器中的方法重新解析token
            @Autowired
            private CheckJwtFilter jwtFilter;
            String authHeader = jwtFilter.getAuthHeader(request);
            if (Objects.nonNull(token)) {
                MyUserDetails userDetails = jwtFilter.parseUserDetailsFromHttpAuthHeader(authHeader);
            }
            */
            globalLog.setHttpMethod(request.getMethod())
                    .setRequestUrl(request.getRequestURI())
                    .setRemoteAddress(request.getRemoteAddr())
                    .setRemoteHost(request.getRemoteHost())
                    .setRemotePort(request.getRemotePort())
                    .setLocalAddress(request.getLocalAddr())
                    .setLocalName(request.getLocalName())
                    .setLocalPort(request.getLocalPort());
        }
        globalLog.setRequestParam(params);
    }

    @Override
    public void setResponseAndExceptionInfo(String returnVal, Throwable e, GlobalLog globalLog) {
        //依据com.example.springbootwebdemo.core.returnval.AppReturnValue的success字段判断成败
        globalLog.setResponseBody(returnVal)
                .setSuccess(!Objects.isNull(returnVal) && JSONUtil.parseObj(returnVal).getBool("success"));
        if (Objects.nonNull(e)) {
            globalLog.setExceptionMessage(ExceptionUtil.stacktraceToOneLineString(e, exceptionProperty.getDbLength()));
        }
    }

    @Override
    public void setMethodInfo(String methodClass, String methodDesc, GlobalLog globalLog) {
        globalLog.setClassPath(methodClass).setMethodDescription(methodDesc);
    }

    @Override
    public void saveLog(GlobalLog globalLog, boolean waitBatchSave) {
        if (waitBatchSave) {
            //缓存设计：一边只管放，一边只管取，不要依赖是否已取出的状态或是否成功，这样要加锁
            //从缓存取出并保存入库的定时任务位于：com.example.springbootwebdemo.demo.task.BatchSaveDbTask::log
            strObjRedisTemplate.opsForSet().add(batchSaveCacheKey.getKey(), globalLog);
            return;
        }
        globalLogMapper.insert((GlobalLogEntity) globalLog);
    }

    @Override
    public void batchSaveLog(List<GlobalLog> list) {
        MyBeanUtil.nullFieldToDefault(list);
        globalLogMapper.batchSaveLog(
                list.parallelStream()
                        .map(globalLog -> (GlobalLogEntity) globalLog)
                        .collect(Collectors.toList())
        );
    }

}
