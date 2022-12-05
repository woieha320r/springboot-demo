package pri.demo.springboot.core.config;

import java.net.URISyntaxException;

/**
 * 全局常量
 *
 * @author woieha320r
 */
public interface Constants {

    Long ROOT_ID = 0L;

    String ROOT_NAME = "root";

    String REDIS_BUSINESS_NODES = "businessNodes";

    String REDIS_LOGS = "logs";

    String REDIS_ERROR_PREFIX = "error:";

    String JMS_BUSINESS_NODE = "business-node";

    String JMS_LOGIN = "login";

    /**
     * 获取classpath
     */
    static String classpath() throws URISyntaxException {
        return Constants.class.getResource("/").toURI().getPath();
    }

}
