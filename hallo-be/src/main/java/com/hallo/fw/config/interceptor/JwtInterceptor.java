package com.hallo.fw.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hallo.api.response.User;
import com.hallo.fw.config.JwtUtil;
import com.hallo.fw.context.LoginUserContext;
import com.hallo.fw.context.SecurityContext;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年10月11日 13:55:00
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
  // 注入UserContext以使用其缓存功能
  @Autowired
  private LoginUserContext loginUserContext;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = request.getHeader("Authorization");
    try {
      final String PREFIX = "Bearer ";
      if (token == null || !token.startsWith(PREFIX)) {
        response.setStatus(401);
        response.getWriter().write("{\"code\":401,\"message\":\"无效的Token\"}");
        return false;
      }

      Claims claims = JwtUtil.parseToken(token.substring(PREFIX.length()));

      String uid = claims.getSubject();
      User user = loginUserContext.getUser(uid);

      if (user == null) {
        response.setStatus(401);
        response.getWriter().write("{\"code\":401,\"message\":\"无效的Token\"}");
        return false;
      }

      // 保存到当前用户上下文
      request.setAttribute(SecurityContext.REQUEST_KEY_LOGIN_USER, user);
      return true;
    } catch (Exception e) {
      response.setStatus(401);
      response.getWriter().write("{\"code\":401,\"message\":\"无效的Token\"}");
      return false;
    }
  }
}
