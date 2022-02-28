USE `spring_boot_web_demo`;

CREATE TABLE IF NOT EXISTS `global_log`
(
    `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键。自增',
    `http_method`        VARCHAR(100)    NOT NULL DEFAULT '' COMMENT 'HTTP方法',
    `login_account`      BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '登录账户',
    `method_description` VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '方法描述',
    `request_url`        VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '请求路径',
    `remote_address`     VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '客户端IP',
    `remote_host`        VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '客户端主机名',
    `remote_port`        INT UNSIGNED    NOT NULL DEFAULT 0 COMMENT '客户端端口',
    `local_address`      VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '接收请求的服务端IP',
    `local_name`         VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '接收请求的服务端主机名',
    `local_port`         INT UNSIGNED    NOT NULL DEFAULT 0 COMMENT '接收请求的服务端端口',
    `request_time`       DATETIME        NOT NULL COMMENT '请求时间',
    `is_success`         BOOLEAN         NOT NULL DEFAULT true COMMENT '请求是否成功',
    `class_path`         VARCHAR(100)    NOT NULL COMMENT '执行的方法路径',
    `request_param`      VARCHAR(2000)   NOT NULL DEFAULT '' COMMENT '请求参数',
    `response_body`      VARCHAR(4000)   NOT NULL DEFAULT '' COMMENT '响应体',
    `exception_message`  VARCHAR(3000)   NOT NULL DEFAULT '' COMMENT '异常信息',
    `version`            INT UNSIGNED    NOT NULL COMMENT '乐观锁',
    `is_deleted`         BOOLEAN         NOT NULL COMMENT '逻辑删除',
    `gmt_create`         DATETIME        NOT NULL COMMENT '创建时间',
    `gmt_modified`       DATETIME        NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='全局日志';