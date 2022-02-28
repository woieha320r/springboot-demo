package com.example.springbootwebdemo.demo.property;

import com.example.springbootwebdemo.core.property.GlobalPropertySourceInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取配置文件中有关缓存中转批量存库的缓存key
 *
  * @version 1.0
 * @date 2022/1/25 14:12
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "cache.batch.db.log")
public class BatchSaveCacheLogProperty extends GlobalPropertySourceInclude {

    /**
     * 缓存日志的key
     */
    private String key;

    /**
     * 缓存日志是否开启
     */
    private Boolean open;

}
