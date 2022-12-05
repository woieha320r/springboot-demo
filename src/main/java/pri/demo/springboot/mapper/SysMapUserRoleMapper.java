package pri.demo.springboot.mapper;

import pri.demo.springboot.entity.SysMapUserRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 关系:用户<->角色 Mapper 接口
 * </p>
 *
 * @author woieha320r
 *
 */
@Mapper
public interface SysMapUserRoleMapper extends BaseMapper<SysMapUserRoleEntity> {

}
