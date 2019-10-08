package com.mrj.oauthdemo.service.impl;

import com.mrj.oauthdemo.dao.UserDao;
import com.mrj.oauthdemo.entity.User;
import com.mrj.oauthdemo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author MRJ
 * @date 2019/10/8 9:57
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }
}
