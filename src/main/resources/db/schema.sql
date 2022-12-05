create TABLE IF NOT EXISTS `sys_dictionary_type`
(
    `id`          BIGINT UNSIGNED auto_increment                                     NOT NULL COMMENT '主键',
    `code`        TINYINT UNSIGNED                                                   NOT NULL COMMENT '编码',
    `description` VARCHAR(20)                                                        NOT NULL COMMENT '描述',
    `is_deleted`  BOOLEAN  DEFAULT FALSE                                             NOT NULL COMMENT '逻辑删除',
    `create_time` DATETIME DEFAULT current_timestamp()                               NOT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP() NOT NULL COMMENT '修改时间。充当乐观锁。逻辑删除判重条件之一',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dictionary_type_code` (`code`, `is_deleted`, `update_time`)
) COMMENT ='字典类型';

create TABLE IF NOT EXISTS `sys_dictionary`
(
    `id`                   BIGINT UNSIGNED auto_increment                                     NOT NULL COMMENT '主键',
    `dictionary_type_code` TINYINT UNSIGNED                                                   NOT NULL COMMENT '字典类型code',
    `code`                 SMALLINT UNSIGNED                                                  NOT NULL COMMENT '字典编码',
    `description`          VARCHAR(20)                                                        NOT NULL COMMENT '描述',
    `is_deleted`           BOOLEAN  DEFAULT FALSE                                             NOT NULL COMMENT '逻辑删除',
    `create_time`          DATETIME DEFAULT current_timestamp()                               NOT NULL COMMENT '创建时间',
    `update_time`          DATETIME DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP() NOT NULL COMMENT '修改时间。充当乐观锁。逻辑删除判重条件之一',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dictionary_dictionary_type_code_code` (`dictionary_type_code`, `code`, `is_deleted`, `update_time`)
) COMMENT ='字典';

create TABLE IF NOT EXISTS `sys_user`
(
    `id`          BIGINT UNSIGNED auto_increment                                     NOT NULL COMMENT '主键',
    `nickname`    VARCHAR(20)                                                        NOT NULL COMMENT '昵称',
    `avatar`      VARCHAR(100)                                                       NOT NULL COMMENT '头像URL地址',
    `gender`      SMALLINT UNSIGNED                                                  NOT NULL COMMENT '性别（字典code）',
    `is_deleted`  BOOLEAN  DEFAULT FALSE                                             NOT NULL COMMENT '逻辑删除',
    `create_time` DATETIME DEFAULT current_timestamp()                               NOT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP() NOT NULL COMMENT '修改时间。充当乐观锁。逻辑删除判重条件之一',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_nickname` (`nickname`, `is_deleted`, `update_time`)
) COMMENT ='用户信息';

create TABLE IF NOT EXISTS `sys_login_account`
(
    `id`          BIGINT UNSIGNED auto_increment                                     NOT NULL COMMENT '主键',
    `user_id`     BIGINT UNSIGNED                                                    NOT NULL COMMENT '用户信息主键',
    `login_type`  SMALLINT UNSIGNED                                                  NOT NULL COMMENT '登录方式（字典code）',
    `identifier`  VARCHAR(100)                                                       NOT NULL COMMENT '唯一标识（登录名、微信id...）',
    `credential`  VARCHAR(100)                                                       NOT NULL COMMENT '访问凭证（登录密码、微信token...）',
    `is_deleted`  BOOLEAN  DEFAULT FALSE                                             NOT NULL COMMENT '逻辑删除',
    `create_time` DATETIME DEFAULT current_timestamp()                               NOT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP() NOT NULL COMMENT '修改时间。充当乐观锁。逻辑删除判重条件之一',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_login_account_login_type_identifier` (`login_type`, `identifier`, `is_deleted`, `update_time`)
) COMMENT ='登录账户';

create TABLE IF NOT EXISTS `sys_permission`
(
    `id`          BIGINT UNSIGNED auto_increment                                         NOT NULL COMMENT '主键',
    `name`        VARCHAR(20)                                                            NOT NULL COMMENT '名称',
    `level`       SMALLINT UNSIGNED                                                      NOT NULL COMMENT '级别（字典code）',
    `parent_name` VARCHAR(20)                                                            NOT NULL COMMENT '父级权限',
    `front_path`  VARCHAR(100)                                                           NOT NULL COMMENT '前端路由路径',
    `description` VARCHAR(100) DEFAULT ''                                                NOT NULL COMMENT '描述',
    `is_deleted`  BOOLEAN      DEFAULT FALSE                                             NOT NULL COMMENT '逻辑删除',
    `create_time` DATETIME     DEFAULT current_timestamp()                               NOT NULL COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP() NOT NULL COMMENT '修改时间。充当乐观锁。逻辑删除判重条件之一',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_name` (`name`, `is_deleted`, `update_time`)
) COMMENT ='权限';

create TABLE IF NOT EXISTS `sys_role`
(
    `id`          BIGINT UNSIGNED auto_increment                                     NOT NULL COMMENT '主键',
    `name`        VARCHAR(20)                                                        NOT NULL COMMENT '名称',
    `is_deleted`  BOOLEAN  DEFAULT FALSE                                             NOT NULL COMMENT '逻辑删除',
    `create_time` DATETIME DEFAULT current_timestamp()                               NOT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP() NOT NULL COMMENT '修改时间。充当乐观锁。逻辑删除判重条件之一',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_parent_id_hierarchy_name` (`name`, `is_deleted`, `update_time`)
) COMMENT ='角色';

create TABLE IF NOT EXISTS `sys_map_role_permission`
(
    `id`            BIGINT UNSIGNED auto_increment                                     NOT NULL COMMENT '主键',
    `permission_id` BIGINT UNSIGNED                                                    NOT NULL COMMENT '权限主键',
    `role_id`       BIGINT UNSIGNED                                                    NOT NULL COMMENT '角色主键',
    `is_deleted`    BOOLEAN  DEFAULT FALSE                                             NOT NULL COMMENT '逻辑删除',
    `create_time`   DATETIME DEFAULT current_timestamp()                               NOT NULL COMMENT '创建时间',
    `update_time`   DATETIME DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP() NOT NULL COMMENT '修改时间。充当乐观锁。逻辑删除判重条件之一',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_map_role_permission_role_id_permission_id` (`role_id`, `permission_id`, `is_deleted`, `update_time`)
) COMMENT ='关系:角色<->权限';

create TABLE IF NOT EXISTS `sys_map_user_role`
(
    `id`          BIGINT UNSIGNED auto_increment                                     NOT NULL COMMENT '主键',
    `user_id`     BIGINT UNSIGNED                                                    NOT NULL COMMENT '用户信息主键',
    `role_id`     BIGINT UNSIGNED                                                    NOT NULL COMMENT '角色主键',
    `is_deleted`  BOOLEAN  DEFAULT FALSE                                             NOT NULL COMMENT '逻辑删除',
    `create_time` DATETIME DEFAULT current_timestamp()                               NOT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP() NOT NULL COMMENT '修改时间。充当乐观锁。逻辑删除判重条件之一',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_map_user_role_user_id_role_id` (`user_id`, `role_id`, `is_deleted`, `update_time`)
) COMMENT ='关系:用户<->角色';

create TABLE IF NOT EXISTS `sys_business_track`
(
    `id`          BIGINT UNSIGNED auto_increment                                     NOT NULL COMMENT '主键',
    `user_id`     BIGINT UNSIGNED                                                    NOT NULL COMMENT '用户主键',
    `node`        SMALLINT UNSIGNED                                                  NOT NULL COMMENT '业务节点（字典code）',
    `is_success`  BOOLEAN                                                            NOT NULL COMMENT '是否成功',
    `is_deleted`  BOOLEAN  DEFAULT FALSE                                             NOT NULL COMMENT '逻辑删除',
    `create_time` DATETIME DEFAULT current_timestamp()                               NOT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP() NOT NULL COMMENT '修改时间。充当乐观锁。逻辑删除判重条件之一',
    PRIMARY KEY (`id`)
) COMMENT ='业务轨迹';

create TABLE IF NOT EXISTS `sys_log`
(
    `id`               BIGINT UNSIGNED auto_increment                                     NOT NULL COMMENT '主键',
    `method_signature` VARCHAR(100)                                                       NOT NULL COMMENT '方法签名',
    `elapsed_time`     SMALLINT UNSIGNED                                                  NOT NULL COMMENT '耗时（秒）',
    `is_success`       BOOLEAN                                                            NOT NULL COMMENT '是否成功',
    `is_deleted`       BOOLEAN  DEFAULT FALSE                                             NOT NULL COMMENT '逻辑删除',
    `create_time`      DATETIME DEFAULT current_timestamp()                               NOT NULL COMMENT '创建时间',
    `update_time`      DATETIME DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP() NOT NULL COMMENT '修改时间。充当乐观锁。逻辑删除判重条件之一',
    PRIMARY KEY (`id`)
) COMMENT ='日志';

create TABLE IF NOT EXISTS `sys_scheduler`
(
    `id`            BIGINT UNSIGNED auto_increment                                         NOT NULL COMMENT '主键',
    `task_key`      VARCHAR(50)                                                            NOT NULL COMMENT '任务唯一标识',
    `class_name`    VARCHAR(100)                                                           NOT NULL COMMENT 'class全名称',
    `cron`          VARCHAR(100)                                                           NOT NULL COMMENT 'cron表达式',
    `is_init_start` BOOLEAN                                                                NOT NULL COMMENT '是否自启动',
    `description`   VARCHAR(100) DEFAULT ''                                                NOT NULL COMMENT '任务描述',
    `is_deleted`    BOOLEAN      DEFAULT FALSE                                             NOT NULL COMMENT '逻辑删除',
    `create_time`   DATETIME     DEFAULT current_timestamp()                               NOT NULL COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT current_timestamp() ON UPDATE CURRENT_TIMESTAMP() NOT NULL COMMENT '修改时间。充当乐观锁。逻辑删除判重条件之一',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_timing_task_task_key` (`task_key`, `is_deleted`, `update_time`)
) COMMENT ='定时任务';