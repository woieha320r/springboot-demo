package com.example.springbootwebdemo.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootwebdemo.core.security.MyLoginAccountService;
import com.example.springbootwebdemo.demo.entity.LoginAccountEntity;

import java.util.List;

/**
 * <p>
 * 登录账户 服务类
 * springsecurity可从此处获得可被其管理的登录实体
 * </p>
 *
  * @since 2022-01-20
 */
public interface LoginAccountService extends IService<LoginAccountEntity>, MyLoginAccountService {

    /**
     * 分页获取所有登录名
     *
     * @param pageNum  当前页号
     * @param pageSize 每页大小
     * @return 通用返回值
     */
    List<String> getAllLoginName(Integer pageNum, Integer pageSize);

}
