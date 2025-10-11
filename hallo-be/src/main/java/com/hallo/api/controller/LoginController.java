package com.hallo.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hallo.api.request.UserLoginRequest;
import com.hallo.api.response.HttpResponse;
import com.hallo.api.response.UserLogin;
import com.hallo.api.service.LoginService;
import com.hallo.fw.annotation.SafeResponse;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年10月11日 14:05:21
 */
@RestController
@RequestMapping("/api")
@SafeResponse
public class LoginController {
  @Autowired
  private LoginService loginService;

  /**
   * 登录
   * 
   * @param request
   * @return
   */
  @PostMapping("login")
  public HttpResponse login(@RequestBody UserLoginRequest request) {
    UserLogin res = loginService.login(request);
    return HttpResponse.success(res);
  }
}
