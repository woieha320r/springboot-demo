package com.example.springbootwebdemo.core.cache;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Objects;

/**
 * SpringCache和Redis配置类
 * 自动配置于RedisAutoConfiguration之后（@AutoConfigureAfter）
 * 引入包含@ConfigurationProperties的配置类，以使用其配置文件属性（@EnableConfigurationProperties）
 * 当指定的class位于类路径上，才实例化（@ConditionalOnClass）
 *
  * @date 2022-02-28
 */
@Slf4j
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnClass({CacheProperties.Redis.class, RedisCacheConfiguration.class})
public class CacheConfig extends CachingConfigurerSupport {

    @Autowired
    private CacheProperties cacheProperties;

    /**
     * 自定义对象转json的配置
     * 此处配置忽略null值属性、存入redis的内容可读
     */
    @Bean
    public Jackson2JsonRedisSerializer<Object> obj2JsonSerializer() {
        ObjectMapper om = new ObjectMapper();
        //解决不能处理LocalDateTime
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).registerModule(new JavaTimeModule())
                //解决取缓存时类型为LinkedHashMap，若使用GenericJackson2JsonRedisSerializer但会报错：
                // need JSON Array to contain As.WRAPPER_ARRAY type information for class java.lang.Object
                .activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        //是否配置了忽略null值属性
        if (!cacheProperties.getRedis().isCacheNullValues()) {
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    @Bean
    public RedisCacheWriter redisCacheWriter(LettuceConnectionFactory factory) {
        //以锁写入的方式创建RedisCacheWriter对象
        return RedisCacheWriter.lockingRedisCacheWriter(factory);
    }

    /**
     * 本意是 改变 keyPrefix和cacheName和key的 组合命名方式、key和value的序列化方式
     * 但需要同时设置过期时间之类的其他属性，不然不会使用配置文件的配置
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration(Jackson2JsonRedisSerializer<Object> obj2JsonSerializer) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                //是否缓存null值属性和存入的内容是否可读，由对象转json的序列化方式决定，此处的disableCachingNullValues()控制null值无效
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(obj2JsonSerializer));
        //是否设置过期时间
        Duration timeToLiveDuration = cacheProperties.getRedis().getTimeToLive();
        long ttl = Objects.isNull(timeToLiveDuration) ? -1L : timeToLiveDuration.toMillis();
        if (ttl >= 0) {
            //这个对象的setter方法间如果断开的话，需要用引用接收，因为每次都是返回一个新的对象
            config = config.entryTtl(Duration.ofMillis(ttl));
        }
        //改变key的命名方式（默认为 全局前缀+cacheName::key） -> 全局前缀:cacheName:key
        String cacheKeyPrefix = redisProperties.getKeyPrefix();
        boolean existsPrefix = StrUtil.isNotBlank(cacheKeyPrefix);
        boolean needUsePrefix = cacheProperties.getRedis().isUseKeyPrefix();
        boolean usePrefix = existsPrefix && needUsePrefix;
        config = config.computePrefixWith(cacheName -> usePrefix ?
                cacheKeyPrefix.concat(":").concat(cacheName).concat(":")
                : cacheName.concat(":")
        );
        return config;
    }

    /**
     * SpringCache需要CacheManager
     * 这里使用RedisCacheManager实现类
     */
    @Bean
    public RedisCacheManager cacheManager(RedisCacheWriter redisCacheWriter, RedisCacheConfiguration cacheConfiguration) {
        RedisCacheManager cacheManager = RedisCacheManager.RedisCacheManagerBuilder
                .fromCacheWriter(redisCacheWriter)
                .cacheDefaults(cacheConfiguration)
                .build();
        cacheManager.setTransactionAware(true);
        return cacheManager;
    }

    /**
     * 供全局直接直接存取redis时注入使用的字符串key对象value的RedisTemplate
     * 实体类中的isSuccess需要有getter和setter，否则会直接序列化为success，而反序列化时会找不到success字段
     */
    @Bean
    public RedisTemplate<String, Object> redisStrObjTemplate(RedisConnectionFactory connectionFactory, Jackson2JsonRedisSerializer<Object> obj2JsonSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(obj2JsonSerializer);
        return redisTemplate;
    }

    /**
     * 当SpringCache无法连接redis时，不要抛异常，方法要继续执行，但要打印日志提示
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                if (RedisConnectionFailureException.class.isAssignableFrom(exception.getClass())) {
                    log.warn("无法使用缓存，请检查Redis是否开启及连接配置是否正确 >>> 异常信息：{}", exception.getMessage());
                } else {
                    throw exception;
                }
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                if (RedisConnectionFailureException.class.isAssignableFrom(exception.getClass())) {
                    log.warn("无法使用缓存，请检查Redis是否开启及连接配置是否正确 >>> 异常信息：{}", exception.getMessage());
                } else {
                    throw exception;
                }
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                if (RedisConnectionFailureException.class.isAssignableFrom(exception.getClass())) {
                    log.warn("无法使用缓存，请检查Redis是否开启及连接配置是否正确 >>> 异常信息：{}", exception.getMessage());
                } else {
                    throw exception;
                }
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                if (RedisConnectionFailureException.class.isAssignableFrom(exception.getClass())) {
                    log.warn("无法使用缓存，请检查Redis是否开启及连接配置是否正确 >>> 异常信息：{}", exception.getMessage());
                } else {
                    throw exception;
                }
            }
        };
    }
}
