package com.example.springbootwebdemo.demo.service.impl;

import com.example.springbootwebdemo.demo.entity.PermissionEntity;
import com.example.springbootwebdemo.demo.mapper.PermissionMapper;
import com.example.springbootwebdemo.demo.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
  * @since 2022-01-20
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {

}
