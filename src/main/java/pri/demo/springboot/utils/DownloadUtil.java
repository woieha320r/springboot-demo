package pri.demo.springboot.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 下载器
 *
 * @author woieha320r
 */
public class DownloadUtil {

    public static ResponseEntity<byte[]> download(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return ResponseEntity.ok()
                    .headers(headers -> {
                        //响应数据类型
                        headers.setContentType(MediaType.parseMediaType(MediaType.MULTIPART_FORM_DATA_VALUE));
                        //文件名
                        headers.setContentDisposition(ContentDisposition.attachment()
                                .filename(file.getName(), StandardCharsets.UTF_8)
                                .build()
                        );
                        //跨域
                        headers.setAccessControlAllowOrigin("*");
                    }).body(IOUtils.toByteArray(inputStream));
        }
    }

}
