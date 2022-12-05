package pri.demo.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pri.demo.springboot.entity.SysRoleEntity;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author woieha320r
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {

    /**
     * 获取指定用户扮演的所有角色名称
     *
     * @param userId 用户主键
     * @return 角色名称列表
     */
    List<String> namesByUser(@Param(value = "userId") Long userId);

    /**
     * 获取系统所有角色名称
     *
     * @return 角色名称列表
     */
    List<String> allNames();

}
