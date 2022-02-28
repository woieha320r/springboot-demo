package com.example.springbootwebdemo.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 实体类工具
 *
  * @date 2022-02-23
 */
@Slf4j
public class MyBeanUtil {

    /**
     * 将null字段设为其所属类型的默认值
     */
    public static <T> void nullFieldToDefault(List<T> list) {
        Class<T> cls = (Class<T>) list.get(0).getClass();
        Arrays.stream(cls.getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            String fieldName = field.getName();
            if ("id".equals(fieldName)) {
                return;
            }
            for (Object obj : list) {
                try {
                    if (Objects.isNull(field.get(obj))) {
                        Class fieldType = field.getType();
                        if (Boolean.class.isAssignableFrom(fieldType)) {
                            field.set(obj, fieldName.contains("uccess"));
                        } else if (Number.class.isAssignableFrom(fieldType)) {
                            //TODO:为啥这么写不行：field.set(obj, Long.class.isAssignableFrom(fieldType) ? 0L : 0);
                            // 报Can not set java.lang.Integer field version to java.lang.Long
                            if (Long.class.isAssignableFrom(fieldType)) {
                                field.set(obj, 0L);
                            } else {
                                field.set(obj, 0);
                            }
                        } else if (String.class.isAssignableFrom(fieldType)) {
                            field.set(obj, "");
                        } else if (LocalDateTime.class.isAssignableFrom(fieldType)) {
                            field.set(obj, LocalDateTime.now());
                        } else if (Date.class.isAssignableFrom(fieldType)) {
                            field.set(obj, new Date());
                        }
                    }
                } catch (IllegalAccessException e) {
                    log.error("{}类消除null值失败，字段：{}，类型：{}", cls.getSimpleName(), fieldName, field.getType());
                }
            }
        });
    }

}
