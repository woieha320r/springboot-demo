package pri.demo.springboot.service;

import pri.demo.springboot.vo.TestUserVo;

/**
 * 测试
 *
 * @author woieha320r
 */
public interface TestService {

    /**
     * 创建随机用户Vo并缓存
     *
     * @param nickname 昵称
     * @return 随机用户Vo
     */
    TestUserVo cacheUserInfo(String nickname);

    /**
     * 删除缓存中的用户Vo
     *
     * @param nickname 昵称
     */
    void delCacheUserInfo(String nickname);
}
