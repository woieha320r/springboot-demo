package pri.demo.springboot.core.businessnode;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务节点声明
 *
 * @author woieha320r
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Validated
public @interface BusinessNodeAnno {

    @NotNull
    BusinessNodeEnum node();

}
