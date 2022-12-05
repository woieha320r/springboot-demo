package pri.demo.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pri.demo.springboot.entity.SysPermissionEntity;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author woieha320r
 */
public interface SysPermissionService extends IService<SysPermissionEntity> {

    /**
     * 获取业务用户对应SpringSecurity支持的角色/权限列表
     *
     * @param userId 业务用户主键
     * @return SpringSecurity支持的角色/权限列表
     */
    List<SimpleGrantedAuthority> grantedAuthoritiesByUser(Long userId);

}
