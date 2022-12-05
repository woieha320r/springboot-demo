package pri.demo.springboot.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录方式，与数据库一致
 *
 * @author woieha320r
 */
@Getter
@AllArgsConstructor
public enum LoginType implements BaseEnum {

    /**
     * 通过用户名登录
     */
    USERNAME(1),
    /**
     * 通过邮箱登录
     */
    EMAIL(2),
    /**
     * 通过Github登录
     */
    GITHUB(3);

    @EnumValue
    private final Integer code;
}
