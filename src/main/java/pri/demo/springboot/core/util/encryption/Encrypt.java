package pri.demo.springboot.core.util.encryption;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.AES;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 此行为会修改原文件
 * 将指定文件中mpwmy:至行尾的内容使用key加密，mpwmy:会被替换为mpw:，使其可在运行时被MybatisPlus解密
 * MybatisPlus需要启动时以springboot方式传入参数--mpw.key指定解密key才能解密，idea可在program arguments中添加
 *
 * MybatisPlus是通过实现EnvironmentPostProcessor做的，自己做也行，百度
 *
 * @author woieha320r6
 */
public class Encrypt {

    protected String key;
    protected final List<String> confFilePaths = new ArrayList<>();

    protected void getInfo() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("输入Key:");
            key = scanner.nextLine();
            System.out.print("输入配置文件路径（空行结束）:");
            String confFilePath;
            while (true) {
                confFilePath = scanner.nextLine();
                if (Objects.isNull(confFilePath) || confFilePath.isEmpty()) {
                    break;
                }
                confFilePaths.add(confFilePath);
            }
        }
    }

    protected void encrypt() {
        for (String filePath : confFilePaths) {
            FileUtil.writeLines(
                    FileUtil.readLines(filePath, StandardCharsets.UTF_8).stream().map(str -> {
                        if (str.contains("mpwmy:")) {
                            String[] strArr = str.split("mpwmy:");
                            if (strArr.length != 2) {
                                throw new RuntimeException(filePath + "中存在不合规的mpwmy:前缀：" + str);
                            }
                            return strArr[0] + "mpw:" + AES.encrypt(strArr[1], key);
                        }
                        return str;
                    }).collect(Collectors.toList()),
                    filePath,
                    StandardCharsets.UTF_8
            );
            System.out.println("已完成：" + filePath);
        }
    }

    public static void main(String[] args) {
        Encrypt encrypt = new Encrypt();
        encrypt.getInfo();
        encrypt.encrypt();
    }

}
