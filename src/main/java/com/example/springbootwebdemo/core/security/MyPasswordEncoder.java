package com.example.springbootwebdemo.core.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

/**
 * spring security密码处理器实现类
 *
  * @date 2021-11-12
 */
@Component
public class MyPasswordEncoder implements PasswordEncoder {

    /**
     * 无需知道具体值，不入库，可以在程序启动时随机生成，但运行期间不能变。仅作为PasswordEncoder不能灵活入参盐值的解决方案
     */
    public static final String SEPARATOR = "N0t_Ch@n9e";

    /**
     * 此处入参是包含自定义分隔符的密码和盐，分离后调用encodePasswordWithSalt加密返回
     * 外部密码全部应该是：密码+MyPasswordEncoder.SEPARATOR+盐
     */
    @Override
    public String encode(CharSequence rawPassword) {
        // springsecurity框架在执行userdetailservice的loadUserByUsername之前会先为计时旁路攻击作准备（在用户未找到时仍以错误密码验证一遍密码）
        // 此时会以"USER_NOT_FOUND_PASSWORD"为密码调用此方法，所以传入的密码里不一定有自定义的分隔符，要先判断，不然会异常
        if (rawPassword.toString().contains(SEPARATOR)) {
            String[] passwordSaltArr = rawPassword.toString().split(SEPARATOR);
            return MD5.create().digestHex(passwordSaltArr[0] + passwordSaltArr[1], StandardCharsets.UTF_8).toLowerCase(Locale.ROOT);
        }
        return null;
    }

    /**
     * 此处入参是包含自定义分隔符的密码和盐，分离后调用encodePasswordWithSalt加密返回
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Objects.equals(encode(rawPassword), encodedPassword);
    }

    /**
     * 外部密码全部应该是：密码+MyPasswordEncoder.SEPARATOR+盐
     */
    public static String getPasswordOutSideUseBeforeEncode(String password, String salt) {
        return password + SEPARATOR + salt;
    }

    /**
     * 密码生成演示
     */
    public static void main(String[] args) {
        String salt = "default";
        String passwordBase = "Sbwd+pass0";
        String password = new MyPasswordEncoder().encode(getPasswordOutSideUseBeforeEncode(passwordBase, salt));
        System.out.println(StrUtil.format("明文密码：{}\n盐值：{}\n加密密码：{}", password, salt, password));
    }

}
