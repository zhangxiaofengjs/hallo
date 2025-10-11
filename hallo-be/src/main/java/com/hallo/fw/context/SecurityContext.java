package com.hallo.fw.context;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hallo.api.response.User;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年9月28日 18:57:59
 */
@Component
public class SecurityContext {
  public static final String REQUEST_KEY_LOGIN_USER = "__HALLO_REQUEST_KEY_LOGIN_USER__";

  public static Integer getLoginUserId() {
    return getLoginUser().getId();
  }

  public static String getLoginUserUid() {
    return getLoginUser().getUid();
  }

  public static User getLoginUser() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      return (User) request.getAttribute(SecurityContext.REQUEST_KEY_LOGIN_USER);
    }
    return null;
  }
}
