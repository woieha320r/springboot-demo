package com.example.springbootwebdemo.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootwebdemo.core.log.GlobalLogService;
import com.example.springbootwebdemo.demo.entity.GlobalLogEntity;

/**
 * <p>
 * 全局日志 服务类
 * </p>
 *
  * @since 2022-01-21
 */
public interface IGlobalLogService extends IService<GlobalLogEntity>, GlobalLogService {

}
