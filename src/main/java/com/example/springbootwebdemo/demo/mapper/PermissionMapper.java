package com.example.springbootwebdemo.demo.mapper;

import com.example.springbootwebdemo.demo.entity.PermissionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
  * @since 2022-01-20
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

}
