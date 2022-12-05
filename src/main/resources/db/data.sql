/* 字典类型 */
INSERT INTO `sys_dictionary_type` (`code`, `description`)
SELECT 1, '登录方式'
WHERE NOT EXISTS(SELECT * FROM `sys_dictionary_type` WHERE `code` = 1 AND `is_deleted` IS FALSE);
INSERT INTO `sys_dictionary_type` (`code`, `description`)
SELECT 2, '权限层级'
WHERE NOT EXISTS(SELECT * FROM `sys_dictionary_type` WHERE `code` = 2 AND `is_deleted` IS FALSE);
INSERT INTO `sys_dictionary_type` (`code`, `description`)
SELECT 3, '业务节点'
WHERE NOT EXISTS(SELECT * FROM `sys_dictionary_type` WHERE `code` = 3 AND `is_deleted` IS FALSE);
INSERT INTO `sys_dictionary_type` (`code`, `description`)
SELECT 4, '性别'
WHERE NOT EXISTS(SELECT * FROM `sys_dictionary_type` WHERE `code` = 4 AND `is_deleted` IS FALSE);

/*字典*/
/*登录方式*/
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 1, 1, '通过用户名登录'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 1 AND `code` = 1 AND `is_deleted` IS FALSE);
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 1, 2, '通过邮箱登录'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 1 AND `code` = 2 AND `is_deleted` IS FALSE);
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 1, 3, '通过Github登录'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 1 AND `code` = 3 AND `is_deleted` IS FALSE);
/*权限层级*/
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 2, 1, '模块'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 2 AND `code` = 1 AND `is_deleted` IS FALSE);
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 2, 2, '页面'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 2 AND `code` = 2 AND `is_deleted` IS FALSE);
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 2, 3, '按钮'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 2 AND `code` = 3 AND `is_deleted` IS FALSE);
/*性别*/
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 4, 1, '男'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 4 AND `code` = 1 AND `is_deleted` IS FALSE);
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 4, 2, '女'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 4 AND `code` = 2 AND `is_deleted` IS FALSE);
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 4, 3, '隐藏'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 4 AND `code` = 3 AND `is_deleted` IS FALSE);
/*业务节点*/
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 3, 1, '客户申请'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 3 AND `code` = 1 AND `is_deleted` IS FALSE);
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 3, 2, '开始处理'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 3 AND `code` = 2 AND `is_deleted` IS FALSE);
INSERT INTO `sys_dictionary` (`dictionary_type_code`, `code`, `description`)
SELECT 3, 3, '结束处理'
WHERE NOT EXISTS(
        SELECT * FROM `sys_dictionary` WHERE `dictionary_type_code` = 3 AND `code` = 3 AND `is_deleted` IS FALSE);
/*定时任务*/
INSERT INTO `sys_scheduler` (task_key, class_name, cron, is_init_start, description)
SELECT 'logRecord', 'pri.demo.springboot.core.scheduler.LogRecordScheduler', '0 0/3 * * * ?', true, 'Redis日记批量存储至数据库'
WHERE NOT EXISTS(SELECT * FROM `sys_scheduler` WHERE `task_key` = 'logRecord' AND `is_deleted` IS FALSE);
INSERT INTO `sys_scheduler` (task_key, class_name, cron, is_init_start, description)
SELECT 'businessNodeRecord',
       'pri.demo.springboot.core.scheduler.BusinessNodeRecordScheduler',
       '0 0/3 * * * ?',
       true,
       'Redis业务节点批量存储至数据库'
WHERE NOT EXISTS(SELECT * FROM `sys_scheduler` WHERE `task_key` = 'businessNodeRecord' AND `is_deleted` IS FALSE);