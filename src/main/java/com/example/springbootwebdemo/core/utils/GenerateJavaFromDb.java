package com.example.springbootwebdemo.core.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;

import java.util.Collections;

/**
 * 使用MybatisPlus查库生成实体、mapper、服务类
 *
  * @date 2022-01-19
 */
public class GenerateJavaFromDb {

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3310/spring_boot_web_demo?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false";
    private static final String DB_USER_NAME = "root";
    private static final String DB_PASSWORD = "0a!root9z+";
    private static final String AUTHOR = "xiaxx";
    private static final String OUTPUT_PATH = "D:\\Projects\\SpringBootWebDemo\\src\\main\\java";
    private static final String OUTPUT_MAPPER_XML_PATH = "D:\\Projects\\SpringBootWebDemo\\src\\main\\resources\\mapper";
    private static final String PACKAGE_PATH = "com.example.springbootwebdemo";
    private static final String PACKAGE_PATH_ENTITY = "entity";
    private static final String PACKAGE_PATH_MAPPER = "mapper";
    private static final String PACKAGE_PATH_SERVICE = "service";
    private static final String PACKAGE_PATH_CONTROLLER = "controller";
    private static final String PACKAGE_PATH_SERVICE_IMPL = "service.impl";

    public static void main(String[] args) {
        FastAutoGenerator.create(
                new DataSourceConfig.Builder(DB_URL, DB_USER_NAME, DB_PASSWORD)
                        .keyWordsHandler(new MySqlKeyWordsHandler())
        ).globalConfig(builder -> {
            // 全局配置
            builder.author(AUTHOR) // 设置作者
                    .enableSwagger() // 开启 swagger 模式
                    .outputDir(OUTPUT_PATH) // 指定输出目录
                    .dateType(DateType.TIME_PACK) // 时间策略
                    .commentDate("yyyy-MM-dd"); // 注释日期
        }).packageConfig(builder -> {
            // 设置包名
            builder.parent(PACKAGE_PATH)
                    .entity(PACKAGE_PATH_ENTITY)
                    .mapper(PACKAGE_PATH_MAPPER)
                    .service(PACKAGE_PATH_SERVICE)
                    .serviceImpl(PACKAGE_PATH_SERVICE_IMPL)
                    .controller(PACKAGE_PATH_CONTROLLER)
                    .pathInfo(Collections.singletonMap(OutputFile.mapperXml, OUTPUT_MAPPER_XML_PATH)); // 设置mapperXml生成路径
        }).strategyConfig(builder -> {
            // 策略配置
            builder.addInclude("!!!表名1", "!!!表名2")
                    .entityBuilder() // 实体策略
                    .idType(IdType.AUTO)
                    .versionColumnName("version") // 乐观锁，配置类开启乐观锁插件
                    .logicDeleteColumnName("is_deleted") // 逻辑删除，配置文件配置逻辑删除值
                    .addTableFills(
                            new Column("version", FieldFill.INSERT),
                            new Column("is_deleted", FieldFill.INSERT),
                            new Column("gmt_create", FieldFill.INSERT),
                            new Column("gmt_modified", FieldFill.INSERT_UPDATE)) // 自动填充字段值，需加MetaObjectHandler处理器
                    .enableLombok()
                    .enableChainModel()
                    .enableTableFieldAnnotation()
                    .formatFileName("%sEntity")
                    .mapperBuilder() // mapper策略
                    .enableMapperAnnotation()
                    .enableBaseResultMap()
                    .enableBaseColumnList()
                    .superClass("com.baomidou.mybatisplus.core.mapper.BaseMapper")
                    .formatMapperFileName("%sMapper")
                    .formatXmlFileName("%sMapper")
                    .serviceBuilder() // service策略
                    .superServiceClass("com.baomidou.mybatisplus.extension.service.IService")
                    .superServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl")
                    .formatServiceFileName("%sService")
                    .formatServiceImplFileName("%sServiceImpl")
                    .controllerBuilder() // controller策略
                    .enableRestStyle()
                    .formatFileName("%sController");
            // 使用Freemarker引擎模板，默认的是Velocity引擎模板：.templateEngine(new FreemarkerTemplateEngine()).execute()
        }).templateEngine(new FreemarkerTemplateEngine()).execute();
    }
}
