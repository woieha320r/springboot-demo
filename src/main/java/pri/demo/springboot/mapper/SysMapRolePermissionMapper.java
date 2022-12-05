package pri.demo.springboot.mapper;

import pri.demo.springboot.entity.SysMapRolePermissionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 关系:角色<->权限 Mapper 接口
 * </p>
 *
 * @author woieha320r
 *
 */
@Mapper
public interface SysMapRolePermissionMapper extends BaseMapper<SysMapRolePermissionEntity> {

}
