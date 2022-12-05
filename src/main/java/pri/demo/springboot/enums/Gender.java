package pri.demo.springboot.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别，与数据库同
 *
 * @author woieha320r
 */
@Getter
@AllArgsConstructor
public enum Gender implements BaseEnum {

    /**
     * 男
     */
    MAN(1),
    /**
     * 女
     */
    WOMEN(2),
    /**
     * 隐藏
     */
    HIDDEN(3);

    @EnumValue
    private final Integer code;

}
