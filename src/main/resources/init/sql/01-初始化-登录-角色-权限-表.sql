CREATE TABLE IF NOT EXISTS `login_account`
(
    `id`                   BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键。自增',
    `login_name`           VARCHAR(100)     NOT NULL COMMENT '登录用户名。不可重复',
    `nick_name`            VARCHAR(100)     NOT NULL COMMENT '昵称。不可重复',
    `login_password`       VARCHAR(100)     NOT NULL COMMENT '登录密码',
    `login_password_salt`  VARCHAR(100)     NOT NULL COMMENT '登录密码盐值',
    `email_security`       VARCHAR(100)     NOT NULL DEFAULT '' COMMENT '安全邮箱',
    `email_public`         VARCHAR(100)     NOT NULL DEFAULT '' COMMENT '公开邮箱',
    `is_remind_when_login` BOOLEAN          NOT NULL DEFAULT true COMMENT '登录时安全邮箱提醒',
    `account_status`       TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '账户状态。0正常；1禁用；',
    `version`              INT UNSIGNED     NOT NULL COMMENT '乐观锁',
    `is_deleted`           BOOLEAN          NOT NULL COMMENT '逻辑删除',
    `gmt_create`           DATETIME         NOT NULL COMMENT '创建时间',
    `gmt_modified`         DATETIME         NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `login_account_UN_login_name` (`login_name`),
    UNIQUE KEY `login_account_UN_nick_name` (`nick_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='登录账户';

CREATE TABLE IF NOT EXISTS `permission`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键。自增',
    `name`         VARCHAR(100)    NOT NULL COMMENT '名称',
    `description`  VARCHAR(100)    NULL     DEFAULT '' COMMENT '描述',
    `is_enable`    BOOLEAN         NOT NULL DEFAULT true COMMENT '是否启用',
    `version`      INT UNSIGNED    NOT NULL COMMENT '乐观锁',
    `is_deleted`   BOOLEAN         NOT NULL COMMENT '逻辑删除',
    `gmt_create`   DATETIME        NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME        NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `permission_UN_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限';

CREATE TABLE IF NOT EXISTS `role`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键。自增',
    `name`         VARCHAR(100)    NOT NULL COMMENT '名称',
    `description`  VARCHAR(100)    NULL     DEFAULT '' COMMENT '描述',
    `is_enable`    BOOLEAN         NOT NULL DEFAULT true COMMENT '是否启用',
    `version`      INT UNSIGNED    NOT NULL COMMENT '乐观锁',
    `is_deleted`   BOOLEAN         NOT NULL COMMENT '逻辑删除',
    `gmt_create`   DATETIME        NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME        NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `role_UN_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色';

CREATE TABLE IF NOT EXISTS `role_permission`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键。自增',
    `role`         BIGINT UNSIGNED NOT NULL COMMENT '角色id',
    `permission`   BIGINT UNSIGNED NOT NULL COMMENT '权限id',
    `version`      INT UNSIGNED    NOT NULL COMMENT '乐观锁',
    `is_deleted`   BOOLEAN         NOT NULL COMMENT '逻辑删除',
    `gmt_create`   DATETIME        NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME        NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色拥有的权限';

CREATE TABLE IF NOT EXISTS `login_account_role`
(
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键。自增',
    `login_account` BIGINT UNSIGNED NOT NULL COMMENT '登录账户id',
    `role`          BIGINT UNSIGNED NOT NULL COMMENT '角色id',
    `version`       INT UNSIGNED    NOT NULL COMMENT '乐观锁',
    `is_deleted`    BOOLEAN         NOT NULL COMMENT '逻辑删除',
    `gmt_create`    DATETIME        NOT NULL COMMENT '创建时间',
    `gmt_modified`  DATETIME        NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='登录账户扮演的角色';

INSERT INTO permission(id, name, description, is_deleted, is_enable, `version`, gmt_create, gmt_modified)
VALUES (1, 'default_permission', '默认测试权限', false, true, 0, '2022-01-19 13:38:00', '2022-01-19 13:38:00');

INSERT INTO `role`(id, name, description, is_deleted, is_enable, `version`, gmt_create, gmt_modified)
VALUES (1, 'default_role', '默认测试角色', false, true, 0, '2022-01-19 13:38:00', '2022-01-19 13:38:00');

INSERT INTO role_permission(`role`, permission, is_deleted, `version`, gmt_create, gmt_modified)
VALUES (1, 1, false, 0, '2022-01-19 13:38:00', '2022-01-19 13:38:00');

-- 登录名：default_login_account；密码：Sbwd+pass0；
INSERT INTO login_account(id, login_name, nick_name, login_password, login_password_salt, is_remind_when_login, `version`,
                          is_deleted, account_status, gmt_create, gmt_modified)
VALUES (1, 'default_login_account', '默认登录账户', '16d207f1faf43551b682d0873de52d9f', 'default', true, 0, false, 0,
        '2022-01-19 13:38:00', '2022-01-19 13:38:00');

INSERT INTO login_account_role(login_account, `role`, is_deleted, gmt_create, `version`, gmt_modified)
VALUES (1, 1, false, '2022-01-19 13:38:00', 0, '2022-01-19 13:38:00');
