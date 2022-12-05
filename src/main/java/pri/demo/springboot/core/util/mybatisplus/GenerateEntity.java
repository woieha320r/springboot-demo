package pri.demo.springboot.core.util.mybatisplus;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collections;

/**
 * 使用MybatisPlus查库生成实体、mapper、服务类
 *
 * @author woieha320r6
 */
public class GenerateEntity {

    private static final String DB_URL = "jdbc:mysql://localhost:3310/springboot-demo?characterEncoding=UTF-8&useUnicode=true&useSSL=true";
    private static final String DB_USER_NAME = "root";
    private static final String DB_PASSWORD = "123456";
    private static final String OUTPUT_PATH = "/Volumes/CaseSensitive/springboot-demo/src/main/java";
    private static final String OUTPUT_MAPPER_XML_PATH = "/Volumes/CaseSensitive/springboot-demo/src/main/resources/mapper";
    private static final String PACKAGE_PATH = "pri.demo.springboot";
    private static final String PACKAGE_PATH_ENTITY = "entity";
    private static final String PACKAGE_PATH_MAPPER = "mapper";
    private static final String PACKAGE_PATH_SERVICE = "service";
    private static final String PACKAGE_PATH_CONTROLLER = "controller";
    private static final String PACKAGE_PATH_SERVICE_IMPL = "service.impl";

    private static void generate(String... tableNames) {
        FastAutoGenerator.create(
                new DataSourceConfig.Builder(DB_URL, DB_USER_NAME, DB_PASSWORD)
                        .keyWordsHandler(new MySqlKeyWordsHandler())
        ).globalConfig(builder -> {
            // 全局配置
            builder.author("woieha320r") // 设置作者
                    .outputDir(OUTPUT_PATH) // 指定输出目录
                    .dateType(DateType.TIME_PACK) // 时间策略
            // .commentDate("yyyy/MM/dd") // 注释日期
            ;
        }).packageConfig(builder -> {
            // 设置包名
            builder.parent(PACKAGE_PATH)
                    .entity(PACKAGE_PATH_ENTITY)
                    .mapper(PACKAGE_PATH_MAPPER)
                    .service(PACKAGE_PATH_SERVICE)
                    .serviceImpl(PACKAGE_PATH_SERVICE_IMPL)
                    .controller(PACKAGE_PATH_CONTROLLER)
                    .pathInfo(Collections.singletonMap(OutputFile.xml, OUTPUT_MAPPER_XML_PATH)); // 设置mapperXml生成路径
        }).strategyConfig(builder -> {
            // 策略配置
            builder.addInclude(tableNames)
                    .entityBuilder() // 实体策略
                    .enableFileOverride() // 覆盖已有文件
                    // .idType(IdType.AUTO)
                    .naming(NamingStrategy.underline_to_camel)
                    .columnNaming(NamingStrategy.underline_to_camel)
                    .enableRemoveIsPrefix() // 移除is前缀
                    .versionColumnName("update_time") // 乐观锁，配置类开启乐观锁插件
                    // .logicDeleteColumnName("is_deleted") // 逻辑删除，也可在配置文件配置全局的
                    // 为数据库字段生成自动填充值注解，需加实现MetaObjectHandler接口的处理器（本例MybatisPlusMetaObjectHandler）
                    /*
                    .addTableFills(
                            new Column("is_deleted", FieldFill.INSERT),
                            new Column("create_time", FieldFill.INSERT),
                            new Column("update_time", FieldFill.INSERT_UPDATE))
                     */
                    .enableLombok()
                    .enableChainModel()
                    .enableTableFieldAnnotation()
                    .formatFileName("%sEntity")
                    .mapperBuilder() // mapper策略
                    .enableFileOverride() // 覆盖已有文件
                    .mapperAnnotation(Mapper.class)
                    .enableBaseResultMap()
                    .enableBaseColumnList()
                    .formatMapperFileName("%sMapper")
                    .formatXmlFileName("%sMapper")
                    .serviceBuilder() // service策略
                    .formatServiceFileName("%sService")
                    .formatServiceImplFileName("%sServiceImpl")
            // .controllerBuilder() // controller策略
            // .enableRestStyle()
            // .enableHyphenStyle()
            // .formatFileName("%sController")
            ;
            // 使用Freemarker引擎模板，默认的是Velocity引擎模板：.templateEngine(new FreemarkerTemplateEngine()).execute()
        }).templateEngine(new FreemarkerTemplateEngine()).execute();
    }

    /**
     * ⚠️用完以后注释掉，防止点错启动，行为会覆盖原entity
     */
    public static void main(String[] args) {
        // generate("sys_dictionary_type", "sys_dictionary", "sys_user", "sys_login_account", "sys_permission"
        //         , "sys_role", "sys_map_role_permission", "sys_map_user_role", "sys_business_track", "sys_timing_task"
        //         , "sys_log");
    }
}
