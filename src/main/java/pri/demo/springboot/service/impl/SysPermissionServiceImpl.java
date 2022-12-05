package pri.demo.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import pri.demo.springboot.entity.SysPermissionEntity;
import pri.demo.springboot.core.config.Constants;
import pri.demo.springboot.mapper.SysPermissionMapper;
import pri.demo.springboot.mapper.SysRoleMapper;
import pri.demo.springboot.service.SysPermissionService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author woieha320r
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermissionEntity> implements SysPermissionService {

    private final SysRoleMapper roleMapper;
    private final SysPermissionMapper permissionMapper;

    @Autowired
    public SysPermissionServiceImpl(SysRoleMapper roleMapper, SysPermissionMapper permissionMapper) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    /**
     * 在修改用户-角色，角色-权限关系的方法上，要删除所有${cache.redis.key-prefix}:此处cacheNames:影响到的用户id
     * TODO:能不能统一一下，不然key的生成需要手动编码，cacheNames后边统一不了，但是否有前缀是动态的，而且组合方式是配置类里定义的
     */
    @Override
    @Cacheable(cacheNames = "token-auths", key = "#userId")
    public List<SimpleGrantedAuthority> grantedAuthoritiesByUser(Long userId) {
        boolean isRoot = Objects.equals(userId, Constants.ROOT_ID);
        List<String> rolePermissionNames = isRoot ? roleMapper.allNames() : roleMapper.namesByUser(userId);
        List<SimpleGrantedAuthority> returnVal = rolePermissionNames.stream()
                .map(name -> new SimpleGrantedAuthority("ROLE_" + name))
                .collect(Collectors.toList());
        rolePermissionNames = isRoot ? permissionMapper.allNames() : permissionMapper.namesByUser(userId);
        returnVal.addAll(rolePermissionNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        return returnVal;
    }

    public void changeRole() {
        //TODO:删除缓存，见pri.demo.springboot.service.impl.SysPermissionServiceImpl.grantedAuthoritiesByUser
    }

    public void changePermission() {
        //TODO:删除缓存，见pri.demo.springboot.service.impl.SysPermissionServiceImpl.grantedAuthoritiesByUser
    }

}
