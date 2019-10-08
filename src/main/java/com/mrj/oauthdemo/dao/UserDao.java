package com.mrj.oauthdemo.dao;

import com.mrj.oauthdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author MRJ
 * @date 2019/10/8 9:51
 * @Description: 用户dao层
 */
public interface UserDao extends JpaRepository<User,Long> {
    User findUserByUsername(String username);
}
