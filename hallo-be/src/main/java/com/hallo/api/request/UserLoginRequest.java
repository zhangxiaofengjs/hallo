package com.hallo.api.request;

import lombok.Data;

/**
 * 用户登录请求
 *
 * @author zhangxiaofeng
 * @create 2025年10月11日 09:03:02
 */
@Data
public class UserLoginRequest {
  private String userToken;
  private String account;
  private String password;
}