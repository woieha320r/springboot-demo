package pri.demo.springboot.core.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus插件
 *
 * @author woieha320r
 */
@Configuration
public class MyMybatisPlusInterceptor {

    /**
     * 多插件顺序建议：多租户,动态表名,分页,乐观锁,sql 性能规范,防止全表更新与删除
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        /*
         * 分页插件，多个插件使用的情况，请将分页插件放到 插件执行链 最后面
         * 自定义分页模型可继承Page或实现IPage
         * */
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        /*
         * 乐观锁插件，实体类字段上加@Version注解
         * 仅支持 updateById(id) 与 update(entity, wrapper) 方法
         * 在 update(entity, wrapper) 方法下, wrapper不能复用
         *
         * 更新的时候带着@Version的值，不一致则更新失败，要想用的话要提前从库里取@Version的值
         * */
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        //防止全表更新与删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

}
