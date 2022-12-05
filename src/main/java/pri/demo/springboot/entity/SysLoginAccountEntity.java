package pri.demo.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 登录账户
 * </p>
 *
 * @author woieha320r
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_login_account")
public class SysLoginAccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户信息主键
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 登录方式（字典code）
     */
    @TableField("login_type")
    private Short loginType;

    /**
     * 唯一标识（登录名、微信id...）
     */
    @TableField("identifier")
    private String identifier;

    /**
     * 访问凭证（登录密码、微信token...）
     */
    @TableField("credential")
    private String credential;

    /**
     * 逻辑删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间。充当乐观锁。逻辑删除判重条件之一
     */
    @TableField("update_time")
    @Version
    private LocalDateTime updateTime;
}
