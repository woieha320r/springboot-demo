package pri.demo.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pri.demo.springboot.entity.SysBusinessTrackEntity;

import java.util.List;

/**
 * <p>
 * 业务轨迹 Mapper 接口
 * </p>
 *
 * @author woieha320r
 */
@Mapper
public interface SysBusinessTrackMapper extends BaseMapper<SysBusinessTrackEntity> {

    /**
     * 批量保存
     *
     * @param list 业务节点列表
     */
    void batchSave(@Param(value = "list") List<Object> list);
}
