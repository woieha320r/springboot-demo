package com.example.springbootwebdemo.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.springbootwebdemo.core.security.MyLoginAccount;
import com.example.springbootwebdemo.demo.enums.AccountStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 登录账户
 * </p>
 *
  * @since 2022-01-20
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("login_account")
@ApiModel(value = "LoginAccountEntity对象", description = "登录账户")
public class LoginAccountEntity implements Serializable, MyLoginAccount {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键。自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("登录用户名。不可重复")
    @TableField("login_name")
    private String loginName;

    @ApiModelProperty("昵称。不可重复")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty("登录密码")
    @TableField("login_password")
    private String loginPassword;

    @ApiModelProperty("登录密码盐值")
    @TableField("login_password_salt")
    private String loginPasswordSalt;

    @ApiModelProperty("安全邮箱")
    @TableField("email_security")
    private String emailSecurity;

    @ApiModelProperty("公开邮箱")
    @TableField("email_public")
    private String emailPublic;

    @ApiModelProperty("登录时安全邮箱提醒")
    @TableField("is_remind_when_login")
    private Boolean remindWhenLogin;

    @ApiModelProperty("账户状态。0正常；1禁用；")
    @TableField("account_status")
    private Integer accountStatus;

    @ApiModelProperty("乐观锁")
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    private Integer version;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("修改时间")
    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @ApiModelProperty("扮演的角色")
    @TableField(exist = false)
    private List<RoleEntity> roles;

    @ApiModelProperty("拥有的权限")
    @TableField(exist = false)
    private List<PermissionEntity> permissions;

    /**
     * SimpleGrantedAuthority没有无参构造不能反序列化，用字符串存一下，需要时现构造
     */
    @ApiModelProperty("用于生成spring security支持的权限说明")
    @TableField(exist = false)
    private List<String> authoritiesStr;

    @Override
    public Boolean getEnable() {
        return Objects.equals(accountStatus, AccountStatusEnum.CAN_USE.getCode());
    }

    @Override
    public Boolean getRemindWhenLogin() {
        return remindWhenLogin;
    }

    @Override
    public String getSecurityEmail() {
        return emailSecurity;
    }
}
