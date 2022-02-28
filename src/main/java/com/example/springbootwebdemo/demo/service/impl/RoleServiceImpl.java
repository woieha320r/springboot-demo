package com.example.springbootwebdemo.demo.service.impl;

import com.example.springbootwebdemo.demo.entity.RoleEntity;
import com.example.springbootwebdemo.demo.mapper.RoleMapper;
import com.example.springbootwebdemo.demo.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
  * @since 2022-01-20
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

}
