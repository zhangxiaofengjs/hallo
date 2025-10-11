package com.hallo.api.mapper.model;

import com.hallo.api.response.UserStatus;

import lombok.Data;

/**
 * 
 * @author zhangxiaofeng
 * @date 2025-08-30
 */
@Data
public class UserModel {
  private Integer id;
  private String uid;
  private String account;
  private String password;
  private String nickname;
  private String avatar;
  private String mail;
  private UserStatus status;
}
