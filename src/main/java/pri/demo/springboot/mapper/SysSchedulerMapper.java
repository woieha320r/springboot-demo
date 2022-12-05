package pri.demo.springboot.mapper;

import pri.demo.springboot.entity.SysSchedulerEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 定时任务 Mapper 接口
 * </p>
 *
 * @author woieha320r
 *
 */
@Mapper
public interface SysSchedulerMapper extends BaseMapper<SysSchedulerEntity> {

}
