package pri.demo.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pri.demo.springboot.entity.SysLoginAccountEntity;
import pri.demo.springboot.enums.LoginType;
import pri.demo.springboot.query.OthersLoginQuery;
import pri.demo.springboot.query.SelfLoginQuery;
import pri.demo.springboot.vo.TokenVo;

/**
 * <p>
 * 登录账户 服务类
 * </p>
 *
 * @author woieha320r
 */
public interface SysLoginAccountService extends IService<SysLoginAccountEntity> {

    /**
     * 用户名方式登录
     *
     * @param query 登录参数
     * @return token
     */
    TokenVo usernameLogin(SelfLoginQuery query);

    /**
     * 邮箱方式登录
     *
     * @param query 登录参数
     * @return token
     */
    TokenVo emailLogin(SelfLoginQuery query);

    /**
     * 获取用户的登录邮箱
     *
     * @param loginType  登录方式
     * @param identifier 登录方式下的唯一标识
     * @return 登录邮箱
     */
    String loginEmail(LoginType loginType, String identifier);

    /**
     * GitHub登录并向jms发布登录通知
     *
     * @param query 登录参数
     * @return token
     */
    TokenVo githubLoginAndJms(OthersLoginQuery query);

    /**
     * 获取用户的昵称
     *
     * @param loginType  登录方式
     * @param identifier 登录方式下的唯一标识
     * @return 昵称
     */
    String nickname(LoginType loginType, String identifier);
}
