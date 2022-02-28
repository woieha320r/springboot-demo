package com.example.springbootwebdemo.demo.enums;

/**
 * 登录账户状态枚举类
 *
  * @date 2022-01-20
 */
public enum AccountStatusEnum {

    /**
     * 可使用
     */
    CAN_USE(0),
    /**
     * 不可使用
     */
    CAN_NOT_USE(1);

    private final int code;

    AccountStatusEnum(int code) {
        this.code = code;
    }

    /**
     * 获取编码
     */
    public int getCode() {
        return code;
    }

}
