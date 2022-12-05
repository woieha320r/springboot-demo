package pri.demo.springboot.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * RedisTemple配置，同时接管SpringCache的Redis配置，兼容其配置文件
 * TODO:看能不能整合时间格式化，大概是jackson的ObjectMapper那一级别，因为redis用的RedisSerializer，spring的接口是啥还没看
 *
 * @author woieha320r
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching
public class RedisCacheConfig {

    private final CacheProperties cacheProperties;

    @Value(value = "${spring.jackson.date-format}")
    private String dateFormatStr;

    @Value(value = "${spring.jackson.time-zone}")
    private String timeZone;

    @Autowired
    public RedisCacheConfig(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        //按配置文件声明的格式格式化LocalDateTime
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateFormatStr)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateFormatStr)));
        //按配置文件声明的格式格式化Date
        DateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

        ObjectMapper objectMapper = new ObjectMapper();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper
                //处理LocalDateTime
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(javaTimeModule)
                .setDateFormat(dateFormat)
                //存入的数据带有@class类型，反序列化时自动转换
                .activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL)
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        );
        return jackson2JsonRedisSerializer;
    }

    /**
     * ttl和nullValue没和SpringCache同步（RedisCacheConfiguration）
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer
            , LettuceConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
        CacheProperties.Redis redisCacheProperties = cacheProperties.getRedis();
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(redisCacheProperties.getTimeToLive())
                .computePrefixWith(cacheName ->
                        (redisCacheProperties.isUseKeyPrefix() ? redisCacheProperties.getKeyPrefix() + ":" : "")
                                + cacheName + ":"
                )
                //不使用jdk序列化，否则是byte[]，不可读
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        jackson2JsonRedisSerializer
                ));
        if (!redisCacheProperties.isCacheNullValues()) {
            redisCacheConfiguration.disableCachingNullValues();
        }
        return redisCacheConfiguration;
    }

    @Bean
    public RedisCacheManager redisCacheManager(
            LettuceConnectionFactory connectionFactory
            , RedisCacheConfiguration redisCacheConfiguration
    ) {
        return RedisCacheManager.builder()
                .cacheWriter(RedisCacheWriter.lockingRedisCacheWriter(connectionFactory))
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

}
