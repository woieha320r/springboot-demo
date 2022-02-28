package com.example.springbootwebdemo.property;

import com.example.springbootwebdemo.core.property.GlobalPropertySourceInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 最先执行
 * 跳转：代码 <-> 配置文件；
 * 不配置PropertySource能否读取其他激活的配置文件：否；
 * 影响配置文件显示：否；
 * 不存在时异常：是；
 *
  * @date 2022-01-20
 */
@Deprecated
@Data
@Component
@EqualsAndHashCode(callSuper = false)
public class ByValue extends GlobalPropertySourceInclude {

    /**
     * 配置文件中不存在时，报错；
     * 不用的时候改成一定有的；
     */
    @Value(value = "${app.test.other.read.value}")
    private String testValue;

}