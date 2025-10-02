package com.hallo.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hallo.api.response.HttpResponse;
import com.hallo.api.response.User;
import com.hallo.api.response.UserGroup;
import com.hallo.api.service.UserService;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年9月28日 16:10:04
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取登录 用户的信息
     * 
     * @return
     */
    @GetMapping("login-user")
    public HttpResponse loginUser() {
        User user = userService.getLoginUser();
        return HttpResponse.success(user);
    }

    // 获取所有联系人
    @GetMapping("user-groups")
    public HttpResponse userGroups() {
        List<UserGroup> userGroups = userService.getLoginUserGroups();
        return HttpResponse.success(userGroups);
    }
}
