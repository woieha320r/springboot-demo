package pri.demo.springboot.core.util.encryption;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.AES;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 此行为会修改原文件
 * 将指定文件中mpwmy:至行尾的内容使用key解密，mpw:会被替换为mpwmy:
 *
 * @author woieha320r6
 */
public class Decrypt extends Encrypt {

    private void decrypt() {
        for (String filePath : confFilePaths) {
            FileUtil.writeLines(
                    FileUtil.readLines(filePath, StandardCharsets.UTF_8).stream().map(str -> {
                        if (str.contains("mpw:")) {
                            String[] strArr = str.split("mpw:");
                            if (strArr.length != 2) {
                                throw new RuntimeException(filePath + "中存在不合规的mympw:前缀：" + str);
                            }
                            return strArr[0] + "mpwmy:" + AES.decrypt(strArr[1], key);
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
        Decrypt decrypt = new Decrypt();
        decrypt.getInfo();
        decrypt.decrypt();
    }

}
