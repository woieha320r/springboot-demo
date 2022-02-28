package com.example.springbootwebdemo.core.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.IntStream;

/**
 * 根据Map<HL7字段名, 值>拼接HL7
 * 视图字段名称必须为 行头_数字[_数字] 格式
 *
  * @since 2021-08-24
 */
@Slf4j
public class SelfHl7Util {

    /**
     * 由调用工具者持有
     */
    public static class Info {
        //有序
        ArrayList<String> headerStrArrList;
        String[][][] valueArr;
        //此下无序
        Map<String, Integer> headerWithMaxOneLevel;
        Set<String> headerStrSet;

        //公用
        String header;
        int oneLevel;
        int twoLevel;
    }

    /**
     * valueArr是否是空的
     */
    public static boolean valueArrIsEmpty(Info info) {
        boolean result = true;
        if (Objects.nonNull(info) && Objects.nonNull(info.valueArr)) {
            for (String[][] strings : info.valueArr) {
                result = result && Objects.isNull(strings);
            }
        }
        return result;
    }

    /**
     * 初始化三维数组模型
     */
    public static Info initValueArr(String[] headerStrArrOut, Set<String> keys) {
        Info info = new Info();
        //固定行头字符串，需要其顺序
        info.headerStrArrList = new ArrayList<>(Arrays.asList(headerStrArrOut));
        info.headerWithMaxOneLevel = new HashMap<>();
        info.headerStrArrList.forEach(headerStr -> info.headerWithMaxOneLevel.put(headerStr.toUpperCase(), 1));
        info.headerStrSet = info.headerWithMaxOneLevel.keySet();
        //将三维数组第一维度的个数固定为行头的数量
        info.valueArr = new String[info.headerWithMaxOneLevel.size()][][];
        //用以承载每个视图字段信息（行头，一级元素数量，二级元素数量）的容器：Map<行头, Map<一级元素下标, 一级元素包含的二级元素最大下标>>
        Map<String, Map<Integer, Integer>> oneLine = new HashMap<>();
        //遍历视图取出的字段名，其命名必须为：行头_数字[_数字]
        keys.forEach(key -> {
            //从每个视图字段中分离出其包含的信息（行头，一级元素数量，二级元素数量）
            if (!splitInfoFromKey(key, info)) {
                return;
            }
            //将字段的信息置于容器，待后遍历分析
            if (oneLine.containsKey(info.header)) {
                Map<Integer, Integer> levelMap = oneLine.get(info.header);
                if (!levelMap.containsKey(info.oneLevel) || (levelMap.get(info.oneLevel) < info.twoLevel)) {
                    levelMap.put(info.oneLevel, info.twoLevel);
                }
            } else {
                oneLine.put(info.header, MapUtil.builder(new HashMap<Integer, Integer>()).put(info.oneLevel, info.twoLevel).build());
            }
            if (info.oneLevel > info.headerWithMaxOneLevel.get(info.header)) {
                info.headerWithMaxOneLevel.put(info.header, info.oneLevel);
            }
        });
        //根据容器承载的数组索引信息开辟三维数组空间并填充行头字符串
        IntStream.range(0, headerStrArrOut.length).forEachOrdered(index -> {
            String headerStr = headerStrArrOut[index];
            String[][] oneLevelArr = new String[info.headerWithMaxOneLevel.get(headerStr.toUpperCase()) + 1][];
            oneLevelArr[0] = new String[]{headerStr};
            Optional.ofNullable(oneLine.get(headerStr)).orElse(new HashMap<>()).forEach((oneLevelIndex, oneLevelIndexSize) -> {
                oneLevelArr[oneLevelIndex] = new String[oneLevelIndexSize];
                info.valueArr[index] = oneLevelArr;
            });
        });
        log.debug("HL7工具 >>> 初始化数组模型 >>> " + Arrays.deepToString(info.valueArr));
        return info;
    }

    /**
     * 将值填充进三维数组并遍历收敛为HL7字符串
     * 不能拼接重复行头，只能处理|和^
     */
    public static String getStrByViewData(Map<String, Object> viewDataMap, Info info) {
        viewDataMap.forEach((key, value) -> {
            if (splitInfoFromKey(key, info)) {
                info.valueArr[info.headerStrArrList.indexOf(info.header)][info.oneLevel][info.twoLevel - 1] = Objects.isNull(value) ? StrUtil.EMPTY : value.toString();
            }
        });
        log.debug("HL7工具 >>> 生成hl7前的三维数组 >>> " + Arrays.deepToString(info.valueArr));
        StringBuilder strBuilder = new StringBuilder();
        Arrays.stream(info.valueArr).forEachOrdered(strStrArr -> {
            if (!Objects.isNull(strStrArr)) {
                Arrays.stream(strStrArr).forEachOrdered(strArr -> {
                    if (Objects.isNull(strArr)) {
                        strBuilder.append("|");
                    } else {
                        IntStream.range(0, strArr.length).forEachOrdered(index -> {
                            strBuilder.append(Objects.isNull(strArr[index]) ? StrUtil.EMPTY : strArr[index]).append("^");
                            if (!info.headerStrSet.contains(strArr[index])) {
                                strArr[index] = null;
                            }
                        });
                        strBuilder.deleteCharAt(strBuilder.length() - 1).append("|");
                    }
                });
            }
            strBuilder.deleteCharAt(strBuilder.length() - 1).append("\n");
        });
        return strBuilder.deleteCharAt(strBuilder.length() - 1).toString();
    }

    /**
     * 从视图字段名称中分离出 行头、一级数组元素索引、二级数组元素索引
     */
    private static boolean splitInfoFromKey(String key, Info info) {
        key = key.toUpperCase();
        if (key.length() <= 3 || !info.headerStrSet.contains(key.substring(0, 3))) {
            return false;
        }
        String[] oneLineEleArr = key.split("_");
        info.header = oneLineEleArr[0];
        info.oneLevel = Integer.parseInt(oneLineEleArr[1]);
        info.twoLevel = 1;
        if (oneLineEleArr.length > 2 && !"0".equals(oneLineEleArr[2])) {
            info.twoLevel = Integer.parseInt(oneLineEleArr[2]);
        }
        return true;
    }

}
