package pri.demo.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pri.demo.springboot.entity.SysLogEntity;

import java.util.List;

/**
 * <p>
 * 日志 Mapper 接口
 * </p>
 *
 * @author woieha320r
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLogEntity> {

    /**
     * 批量存库
     *
     * @param list 日志列表
     */
    void batchSave(@Param(value = "list") List<Object> list);

}
