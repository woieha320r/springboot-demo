package pri.demo.springboot.core.businessnode;

import java.util.Arrays;
import java.util.Objects;

/**
 * 业务节点，与数据库对应
 *
 * @author woieha320r
 */
public enum BusinessNodeEnum {

    /**
     * 客户申请
     */
    APPLY(1),

    /**
     * 开始处理
     */
    START(2),

    /**
     * 结束处理
     */
    END(3);

    private final short node;

    BusinessNodeEnum(int node) {
        this.node = (short) node;
    }

    public short getNode() {
        return node;
    }

    public static BusinessNodeEnum getByNode(int node) {
        return Arrays.stream(values()).filter(obj -> Objects.equals(obj.node, node)).findAny().orElse(null);
    }

}
