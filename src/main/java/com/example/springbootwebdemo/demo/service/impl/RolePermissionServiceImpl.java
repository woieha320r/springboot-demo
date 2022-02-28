package com.example.springbootwebdemo.demo.service.impl;

import com.example.springbootwebdemo.demo.entity.RolePermissionEntity;
import com.example.springbootwebdemo.demo.mapper.RolePermissionMapper;
import com.example.springbootwebdemo.demo.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色拥有的权限 服务实现类
 * </p>
 *
  * @since 2022-01-20
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {

}
