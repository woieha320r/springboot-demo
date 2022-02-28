package com.example.springbootwebdemo.demo.controller;

import com.example.springbootwebdemo.core.log.AppLog;
import com.example.springbootwebdemo.core.returnval.AppReturnValue;
import com.example.springbootwebdemo.demo.property.PropertyDemo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 外部获取配置文件项
 *
  * @date 2022-02-23
 */
@Api(value = "读取配置文件", tags = "读取配置文件")
@Slf4j
@RestController
@RequestMapping(value = "/test", produces = "application/json;charset=UTF-8")
public class PropertyController {

    @Autowired
    private PropertyDemo propertyDemo;

    @AppLog(methodDesc = "获取配置文件项")
    @ApiOperation(value = "获取配置文件项")
    @GetMapping(value = "/getProperty")
    public AppReturnValue getAppTestValue() {
        return AppReturnValue.ok(propertyDemo.getValue());
    }

}
