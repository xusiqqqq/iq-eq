package xsj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xsj.entity.User;
import xsj.mapper.UserMapper;
import xsj.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
