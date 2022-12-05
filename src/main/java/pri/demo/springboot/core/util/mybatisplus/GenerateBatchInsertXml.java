package pri.demo.springboot.core.util.mybatisplus;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 根据MybatisPlus实体类生成mybatis批量新增的xml映射
 * mybatisplus的batchsave不是一个insert多个值，貌似是多个insert语句批量提交，具体多少个没发现规律
 *
 * @author woieha320r6
 */
public class GenerateBatchInsertXml {

    private static final String ITER_NAME = "item";

    /**
     * 运行后将输出的映射语句复制到xml文件
     */
    public static void main(String[] args) {
        System.out.println(GenerateBatchInsertXml.generateSave(null));
    }

    public static <T> String generateSave(Class<T> cls) {
        StringBuilder fieldsStr = new StringBuilder("INSERT INTO ").append(
                cls.isAnnotationPresent(TableName.class) ? cls.getAnnotation(TableName.class).value() : StrUtil.toUnderlineCase(cls.getSimpleName())
        ).append(" (");
        StringBuilder valuesStr = new StringBuilder("(");
        for (Field field : cls.getDeclaredFields()) {
            if (isTableField(field)) {
                field.setAccessible(true);
                String fieldName = field.getName();
                String dbFieldName = field.isAnnotationPresent(TableField.class) ? field.getAnnotation(TableField.class).value() : StrUtil.toUnderlineCase(fieldName);
                fieldsStr.append("`").append(dbFieldName).append("`, ");
                valuesStr.append("#{").append(ITER_NAME).append(".").append(fieldName).append(", jdbcType=").append(getJdbcType(field)).append("},\n");
            }
        }
        return "<insert id=\"方法名\" parameterType=\"java.util.List\">\n" +
                fieldsStr.substring(0, fieldsStr.length() - 2) + ") VALUES \n" +
                "<foreach collection=\"参数对象名\" item=\"" + ITER_NAME + "\" index=\"index\" separator=\",\">\n" +
                valuesStr.substring(0, valuesStr.length() - 2) + ")\n" +
                "</foreach>\n" +
                "</insert>";
    }

    private static boolean isTableField(Field field) {
        return !field.getType().isPrimitive()
                && !(field.isAnnotationPresent(TableId.class) && Objects.equals(field.getAnnotation(TableId.class).type(), IdType.AUTO))
                && (!field.isAnnotationPresent(TableField.class) || field.getAnnotation(TableField.class).exist());
    }

    private static String getJdbcType(Field field) {
        switch (field.getType().getSimpleName()) {
            case "String":
                return "VARCHAR";
            case "Date":
            case "LocalDateTime":
                return "TIMESTAMP";
            case "BigDecimal":
                return "DECIMAL";
            case "Integer":
                return "INTEGER";
            case "Long":
                return "BIGINT";
            case "Boolean":
                return "BOOLEAN";
            case "Float":
                return "REAL";
            case "Double":
                return "DOUBLE";
            case "Short":
                return "SMALLINT";
            case "Byte":
                return "TINYINT";
            default:
                return "";
        }
    }

}
