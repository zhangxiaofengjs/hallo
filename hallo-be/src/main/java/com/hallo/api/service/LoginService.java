package com.hallo.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hallo.api.mapper.UserMapper;
import com.hallo.api.mapper.model.UserModel;
import com.hallo.api.mapper.parameter.UserParameter;
import com.hallo.api.request.UserLoginRequest;
import com.hallo.api.response.User;
import com.hallo.api.response.UserLogin;
import com.hallo.fw.config.JwtUtil;
import com.hallo.fw.config.PasswordEncoder;
import com.hallo.fw.exception.BizException;

import io.jsonwebtoken.Claims;

@Service
public class LoginService {
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * 用户登录
   * 
   * @param request
   * @return
   */
  public UserLogin login(UserLoginRequest request) {
    if (request.getUserToken() != null) {
      try {
        Claims claims = JwtUtil.parseToken(request.getUserToken());

        String uid = claims.getSubject();
        if (uid == null) {
          return BizException.throwException("登录信息已过期，请重新登录");
        }

        UserParameter parameter = new UserParameter();
        parameter.setUid(uid);
        List<UserModel> userList = userMapper.getUserList(parameter);
        if (userList.size() == 0) {
          return BizException.throwException("登录信息已过期，请重新登录");
        }

        // 发行新的token
        String token = JwtUtil.createToken(uid);

        UserModel user = userList.get(0);

        return new UserLogin().setToken(token).setUser(User.from(user));
      } catch (Exception e) {
        return BizException.throwException("登录信息已过期，请重新登录");
      }
    } else if (request.getAccount() != null && request.getPassword() != null) {
      UserParameter parameter = new UserParameter();
      parameter.setAccount(request.getAccount());
      List<UserModel> userList = userMapper.getUserList(parameter);
      if (userList.size() == 0) {
        return BizException.throwException("不存在的用户，请联系管理员");
      }

      UserModel user = userList.get(0);

      if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        return BizException.throwException("密码不正确");
      }

      // 发行新的token
      String token = JwtUtil.createToken(user.getUid());

      return new UserLogin().setToken(token).setUser(User.from(user));
    } else {
      return BizException.throwException("登录信息已过期，请重新登录");
    }
  }
}