package com.guole.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guole.reggie.entity.User;
import com.guole.reggie.mapper.UserMapper;
import com.guole.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author 郭乐
 * @date 2022/10/20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
