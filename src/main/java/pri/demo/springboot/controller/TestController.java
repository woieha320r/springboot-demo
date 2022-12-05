package pri.demo.springboot.controller;

import cn.hutool.core.util.RandomUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pri.demo.springboot.core.appreturn.NotAppReturn;
import pri.demo.springboot.core.businessnode.BusinessNodeAnno;
import pri.demo.springboot.core.businessnode.BusinessNodeEnum;
import pri.demo.springboot.core.config.Constants;
import pri.demo.springboot.service.TestService;
import pri.demo.springboot.utils.DownloadUtil;
import pri.demo.springboot.vo.TestUserVo;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 测试
 *
 * @author woieha320r
 */
@Api(tags = "测试")
@ApiSupport(author = "woieha320r")
@RestController
@RequestMapping(value = "/test")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @ApiOperation(value = "当前时间（Date），业务节点，异常")
    @GetMapping(value = "/d")
    @BusinessNodeAnno(node = BusinessNodeEnum.APPLY)
    public Date date() {
        int x = 1 / 0;
        return new Date();
    }

    @ApiOperation(value = "当前时间（LocalDateTime）")
    @GetMapping(value = "/ldt")
    public LocalDateTime ldt() {
        return LocalDateTime.now();
    }

    @PreAuthorize(value = "hasRole('abc')")
    @ApiOperation(value = "随机字符串，无权")
    @GetMapping(value = "/str")
    public String str() {
        return RandomUtil.randomString(8);
    }

    @ApiOperation(value = "用户信息，缓存Redis直至调用删除缓存")
    @PostMapping(value = "/ui")
    public TestUserVo userInfo(@ApiParam(value = "昵称", required = true) @NotEmpty @RequestParam String nickname) {
        return testService.cacheUserInfo(nickname);
    }

    @ApiOperation(value = "删除ui创建的缓存用户信息，再调ui是新的")
    @DeleteMapping(value = "/ui/del")
    public void delUserInfo(@ApiParam(value = "昵称", required = true) @NotEmpty @RequestParam String nickname) {
        testService.delCacheUserInfo(nickname);
    }

    @NotAppReturn
    @ApiOperation(value = "下载classpath下的文件")
    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> download(@ApiParam(value = "路径", required = true) @NotEmpty @RequestParam String path) throws URISyntaxException, IOException {
        return DownloadUtil.download(Constants.classpath() + path);
    }

}
