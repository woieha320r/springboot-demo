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
 * 关系:角色<->权限
 * </p>
 *
 * @author woieha320r
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_map_role_permission")
public class SysMapRolePermissionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限主键
     */
    @TableField("permission_id")
    private Long permissionId;

    /**
     * 角色主键
     */
    @TableField("role_id")
    private Long roleId;

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
