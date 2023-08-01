package xsj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xsj.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
