package com.example.springbootwebdemo.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootwebdemo.demo.entity.GlobalLogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 全局日志 Mapper 接口
 * </p>
 *
  * @since 2022-01-21
 */
@Mapper
public interface GlobalLogMapper extends BaseMapper<GlobalLogEntity> {

    void batchSaveLog(List<GlobalLogEntity> list);

}
