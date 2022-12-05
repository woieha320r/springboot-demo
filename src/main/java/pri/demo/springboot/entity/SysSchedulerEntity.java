package pri.demo.springboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务
 * </p>
 *
 * @author woieha320r
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_scheduler")
public class SysSchedulerEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务唯一标识
     */
    @TableField("task_key")
    private String taskKey;

    /**
     * class全名称
     */
    @TableField("class_name")
    private String className;

    /**
     * cron表达式
     */
    @TableField("cron")
    private String cron;

    /**
     * 是否自启动
     */
    @TableField("is_init_start")
    private Boolean initStart;

    /**
     * 任务描述
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
