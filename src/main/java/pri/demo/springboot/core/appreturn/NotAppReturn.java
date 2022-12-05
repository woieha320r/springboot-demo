package pri.demo.springboot.core.appreturn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声称不要自动包装统一返回值
 * <p>
 * 例如返回文件流的controller方法
 *
 * @author woieha320r
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotAppReturn {
}
