package com.mrj.oauthdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MRJ
 * @date 2019/10/8 16:22
 * @Description: 测试访问受保护资源
 */
@RestController
public class UserController {
    @RequestMapping("/current")
    public String current() {
        return "admin";
    }
}
