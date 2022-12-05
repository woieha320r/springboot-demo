package pri.demo.springboot.core.util.encryption;

import com.baomidou.mybatisplus.core.toolkit.AES;

/**
 * 获取随即密钥，用于通过MybatisPlus加解密配置文件
 *
 * @author woieha320r6
 */
public class RandomKey {

    public static void main(String[] args) {
        System.out.println(AES.generateRandomKey());
    }

}
