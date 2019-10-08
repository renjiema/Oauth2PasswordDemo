package com.mrj.oauthdemo.service;

import com.mrj.oauthdemo.entity.User;

/**
 * @author MRJ
 * @date 2019/10/8 9:57
 * @Description:
 */
public interface UserService {
    User findUserByUsername(String username);
}
