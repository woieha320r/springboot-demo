package pri.demo.springboot.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 前端路由
 *
 * @author woieha320r
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class RouterVo {

    private String name;

    private Boolean hidden;

    private String path;

    private List<RouterVo> children;

}
