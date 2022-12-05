package pri.demo.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pri.demo.springboot.entity.SysPermissionEntity;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author woieha320r
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermissionEntity> {

    /**
     * 获取指定用户拥有的所有权限
     *
     * @param userId 用户主键
     * @return 权限列表
     */
    List<SysPermissionEntity> listByUser(@Param(value = "userId") Long userId);

    /**
     * 获取指定用户拥有的所有权限名
     *
     * @param userId 用户主键
     * @return 权限名列表
     */
    List<String> namesByUser(@Param(value = "userId") Long userId);

    /**
     * 获取系统所有权限名
     *
     * @return 权限名列表
     */
    List<String> allNames();

}
