package pri.demo.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pri.demo.springboot.entity.SysUserEntity;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author woieha320r
 *
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {
}
