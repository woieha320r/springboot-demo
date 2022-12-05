package pri.demo.springboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author woieha320r
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_permission")
public class SysPermissionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 级别（字典code）
     */
    @TableField("`level`")
    private Short level;

    /**
     * 父级权限主键
     */
    @TableField("parent_name")
    private String parentName;

    /**
     * 前端路由路径
     */
    @TableField("front_path")
    private String frontPath;

    /**
     * 描述
     */
    @TableField("`description`")
    private String description;

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
