package pri.demo.springboot.mapper;

import pri.demo.springboot.entity.SysDictionaryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 字典 Mapper 接口
 * </p>
 *
 * @author woieha320r
 *
 */
@Mapper
public interface SysDictionaryMapper extends BaseMapper<SysDictionaryEntity> {

}
