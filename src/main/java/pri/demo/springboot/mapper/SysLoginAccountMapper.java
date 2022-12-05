package pri.demo.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pri.demo.springboot.entity.SysLoginAccountEntity;
import pri.demo.springboot.enums.LoginType;

/**
 * <p>
 * 登录账户 Mapper 接口
 * </p>
 *
 * @author woieha320r
 */
@Mapper
public interface SysLoginAccountMapper extends BaseMapper<SysLoginAccountEntity> {

    /**
     * 获取用户的登录邮箱
     *
     * @param loginType  登录方式
     * @param identifier 登录方式下的唯一标识
     * @param emailType  邮箱登录字典值
     * @return 登录邮箱
     */
    String loginEmail(
            @Param(value = "loginType") LoginType loginType
            , @Param(value = "identifier") String identifier
            , @Param(value = "emailType") LoginType emailType
    );

    /**
     * 获取用户的昵称
     *
     * @param loginType  登录方式
     * @param identifier 登录方式下的唯一标识
     * @return 昵称
     */
    String nickname(LoginType loginType, String identifier);
}
