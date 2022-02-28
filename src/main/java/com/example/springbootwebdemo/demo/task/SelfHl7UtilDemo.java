package com.example.springbootwebdemo.demo.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.example.springbootwebdemo.core.utils.SelfHl7Util;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * hl7工具使用样例
 * 定义视图
 * 根据视图结果集生成hl7字符串
 *
  * @date 2021-09-17
 */
@Slf4j
public class SelfHl7UtilDemo {

    /**
     * 创建视图样例，字段名需为hl7字符串中的位置
     * CREATE VIEW `hl7工具样例` AS
     * SELECT '引用字段' AS `PID_3_1`,
     *        ''        AS `PID_3_2`,
     *        'PI~'     AS PID_3_4, -- hl7字符串要求的固定值
     *        'CZ'      AS PID_3_7, -- hl7字符串要求的固定值
     *        '引用字段' AS PID_5
     * FROM `引用表`
     *          LEFT JOIN `连接表` ON (`主表字段` = `连接表字段`)
     * WHERE `条件字段` = '条件'
     */

    private final String viewName = "视图名称";
    private SelfHl7Util.Info info;

    private boolean initHl7Arr() {
        List<Map<String, Object>> viewData = new ArrayList<>();
        //查询视图1条数据
        //viewData = iCommonService.execSql(StrUtil.format("SELECT TOP 1 * FROM {}", viewName));
        if (viewData.size() > 0) {
            info = SelfHl7Util.initValueArr(
                    new String[]{"PID", "PV1", "ORC", "OBR", "OBX", "NTE", "ROL", "RXO"},
                    viewData.get(0).keySet()
            );
            return true;
        } else {
            info = new SelfHl7Util.Info();
            return false;
        }
    }

    public void doIt() {
        log.debug("开始执行");

        //检测数组是否空，防止接口启动时视图无数据导致数组空
        if (!SelfHl7Util.valueArrIsEmpty(info) || initHl7Arr()) {
            try {
                List<Map<String, Object>> viewData = new ArrayList<>();
                //查询视图所有数据
                //viewData = iCommonService.execSql(StrUtil.format("SELECT * FROM {}", viewName));
                log.debug("视图结果集 >>> " + JSONUtil.toJsonStr(viewData));

                viewData.forEach(viewDataMap -> {
                    String hl7Str = getMsh() + SelfHl7Util.getStrByViewData(viewDataMap, info);
                    System.out.println("HL7字符串：" + hl7Str);
                });
            } catch (Exception e) {
                log.error("异常 >>> {}", ExceptionUtil.stacktraceToString(e));
            }
        }
        log.debug("结束执行");
    }

    /**
     * 构造MSH消息头
     */
    private static String getMsh() {
        return "MSH|^~\\&|S007|" + "***" + "^" + "***" + "|S001|010101^01|"
                + DateUtil.date().toString("yyyyMMddhhmmss.SSS") + "||RAS^O17^RAS_O17|" + UUID.randomUUID()
                + "|P|2.6" + "|||||CHN|UNICODE UTF-8|||BS037^01\n";
    }

}
