package com.example.springbootwebdemo;

import cn.hutool.core.util.StrUtil;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * 确定加密使用的密码：ENCRYPTOR_PASSWORD
 * 运行此方法得到加密的字符串后复制到配置文件（或直接使用mvn方式，没有尝试：mvn jasypt:encrypt -Djasypt.encryptor.password=密码）
 * 程序启动时：
 * 要么在配置文件直接配置密码：jasypt.encryptor.password=ENCRYPTOR_PASSWORD的值（这是可逆的，岂不是跟不加密一样）
 * 要么将ENCRYPTOR_PASSWORD的值放进环境变量SBWD_PASS，配置文件中读取环境变量的值：jasypt.encryptor.password=${SBWD_PASS:null}
 * IDEA配置用户环境变量：Run/Edit Configurations/Spring Boot/SpringBootWebDemoApplication/Environment/environment variables -> User environment variables:
 *
  * @date 2022-02-25
 */
public class EncryptSettingFile {

    private final BasicTextEncryptor encryptor = new BasicTextEncryptor();
    private static final String ENCRYPTOR_PASSWORD = "sbwd-sf/salt";

    public String myEncryptStr(String settingsStr, boolean removeSpacePrefix) {
        StringBuilder strBuilder = new StringBuilder();
        Arrays.stream(settingsStr.split("\n")).forEachOrdered(str -> {
            if (!str.startsWith("#") && str.contains("=")) {
                String[] val = str.split("=");
                if (val.length == 1) {
                    String[] tempVal = new String[2];
                    tempVal[0] = val[0];
                    tempVal[1] = "";
                    val = tempVal;
                }
                if (removeSpacePrefix) {
                    while (val[1].startsWith(" ")) {
                        val[1] = StrUtil.removePrefix(val[1], " ");
                    }
                }
                str = val[0] + "=ENC(" + encryptor.encrypt(val[1]) + ")";
            }
            strBuilder.append(str).append("\n");
        });
        return strBuilder.toString();
    }

    public String myDecryptStr(String settingsStr, boolean removeSpacePrefix) {
        StringBuilder strBuilder = new StringBuilder();
        Arrays.stream(settingsStr.split("\n")).forEachOrdered(str -> {
            if (removeSpacePrefix) {
                if (str.contains(" ENC(")) {
                    str = str.replaceAll(" ENC\\(", "ENC(");
                }
            }
            if (!str.startsWith("#") && str.contains("=ENC(")) {
                String[] val = str.split("=ENC\\(");
                if (val.length == 1) {
                    String[] tempVal = new String[2];
                    tempVal[0] = val[0];
                    tempVal[1] = ")";
                    val = tempVal;
                }
                val[1] = StrUtil.removeSuffix(val[1], ")");
                str = val[0] + "=" + encryptor.decrypt(val[1]);
            }
            strBuilder.append(str).append("\n");
        });
        return strBuilder.toString();
    }

    @Before
    public void init() {
        encryptor.setPassword(ENCRYPTOR_PASSWORD);
    }

    /**
     * 加密
     */
    @Test
    public void encrypt() {
        String waitEncryptStr = "# 无需token认证的接口\n" +
                "app.token.ignore-path=/, /**/*.html, /**/*.css, /**/*.js, /favicon.ico, /v2/api-docs, /swagger-resources, /token/**, /login/**, /websocket/**, /test/**\n" +
                "#\n" +
                "# redis\n" +
                "spring.redis.host=localhost\n" +
                "spring.redis.port=6379\n" +
                "spring.redis.username=\n" +
                "spring.redis.password=\n#\n" +
                "# 用于读取的数据库连接\n" +
                "app.datasource.read.url=localhost:3310\n" +
                "app.datasource.read.dbname=spring_boot_web_demo\n" +
                "app.datasource.read.username=root\n" +
                "app.datasource.read.password=0a!root9z+\n" +
                "#\n" +
                "# 用于写入的数据库连接\n" +
                "app.datasource.write.url=localhost:3310\n" +
                "app.datasource.write.dbname=spring_boot_web_demo\n" +
                "app.datasource.write.username=root\n" +
                "app.datasource.write.password=0a!root9z+\n" +
                "#\n" +
                "# activemq5连接\n" +
                "spring.activemq.broker-url=tcp://127.0.0.1:61616\n" +
                "spring.activemq.user=root\n" +
                "spring.activemq.password=0aroot9z\n" +
                "#\n" +
                "# 邮件相关\n" +
                "spring.mail.host=smtp.163.com\n" +
                "spring.mail.port=25\n" +
                "spring.mail.username=\n" +
                "spring.mail.password=";
        System.out.println(myEncryptStr(waitEncryptStr, true));
    }

    /**
     * 解密
     */
    @Test
    public void decryptStr() {
        String waitDecryptStr = "# 无需token认证的接口\n" +
                "app.token.ignore-path=ENC(0pqHoh2RSpcVW3gajbJ9jj4RBimW7ZY5sTmZtTabj0235siFNZcJ3D3B5SF8y7/P02pEaNLawxYtPhjYdeBxKpnLburRJ+u/JwbZiWEhLtvtuUQXzF7lNeCS36JdNzQue2TiXCxHKOlG4VxBNfB/oBTqKpTvOxZy7YEeRtEqbFvlfOm/Lxf1ku5vJ9hwsxHG)\n" +
                "#\n" +
                "# redis\n" +
                "spring.redis.host=ENC(7EAZrTq9EKcWqLV3QGlXoO+fthJdY0pW)\n" +
                "spring.redis.port=ENC(mr6x2NhUv4UQQS9fzu4Ngw==)\n" +
                "spring.redis.username=ENC(v5YYaZHbTRK93yLWNhJLpQ==)\n" +
                "spring.redis.password=ENC(nG4XlQocjTYlWk/Aap4XCA==)\n" +
                "#\n" +
                "# 用于读取的数据库连接\n" +
                "app.datasource.read.url=ENC(Qrb7Mh/WeAcZBkBNy3yctKFL1dyg6AgN)\n" +
                "app.datasource.read.dbname=ENC(DkhdT5126htU+YZhaOd6+7OaL/2l0j/wn882OVWFWmo=)\n" +
                "app.datasource.read.username=ENC(x6G6x3wZ/aZvu8uvRvQIJg==)\n" +
                "app.datasource.read.password=ENC(7mleTTM8atfvKipkBTkMoKOS6fgBDak3)\n" +
                "#\n" +
                "# 用于写入的数据库连接\n" +
                "app.datasource.write.url=ENC(Idgo6GaeKyGt6IDcvYyVw7SzzUdST9X9)\n" +
                "app.datasource.write.dbname=ENC(9f9Cz81SW/QxhhABqQ7VDtCD9swiSP+RijLVqCilNOc=)\n" +
                "app.datasource.write.username=ENC(jEfR0ZeV0r0VlmolntFBDQ==)\n" +
                "app.datasource.write.password=ENC(WdaSOY3Q4TY49oQOtoHAsEIEMgT+JOM8)\n" +
                "#\n" +
                "# activemq5连接\n" +
                "spring.activemq.broker-url=ENC(xsUlaQyMa5YhzVNc95w5yv+596iVT90ROdu2z5qRwiI=)\n" +
                "spring.activemq.user=ENC(Slc1g89U85oxhDg5nnUOkg==)\n" +
                "spring.activemq.password=ENC(hM5uy+mIDf466QmczlRCiF23wl4RLhkP)\n" +
                "#\n" +
                "# 邮件相关\n" +
                "spring.mail.host=ENC(GhzyWEmhrX0FN/2VLoBXjVq0hmB1rUVQ)\n" +
                "spring.mail.port=ENC(KtdZ3Loimr2nheC30MHFEA==)\n" +
                "spring.mail.username=ENC(v5YYaZHbTRK93yLWNhJLpQ==)\n" +
                "spring.mail.password=ENC(nG4XlQocjTYlWk/Aap4XCA==)";
        System.out.println(myDecryptStr(waitDecryptStr, true));
    }

}
