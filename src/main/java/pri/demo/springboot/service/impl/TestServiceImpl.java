package pri.demo.springboot.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pri.demo.springboot.enums.Gender;
import pri.demo.springboot.service.TestService;
import pri.demo.springboot.vo.TestUserVo;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 测试服务，供TestController使用
 *
 * @author woieha320r
 */
@Service
public class TestServiceImpl implements TestService {

    @Cacheable(cacheNames = "test", key = "#nickname")
    @Override
    public TestUserVo cacheUserInfo(String nickname) {
        return new TestUserVo()
                .setNickname(nickname)
                .setGender(Gender.HIDDEN)
                .setLocalDateTime(LocalDateTime.now())
                .setDate(new Date());
    }

    @CacheEvict(cacheNames = "test", key = "#nickname")
    @Override
    public void delCacheUserInfo(String nickname) {
    }
}
