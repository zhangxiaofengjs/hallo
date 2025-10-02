package com.hallo.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 联系人抽象
 *
 * @author zhangxiaofeng
 * @create 2025年9月28日 18:48:00
 */
@Data
@Accessors(chain = true)
public class User {
  private String uid;
  private String account;
  private String nickname;
  private String mail;
  private String avatar;
  private UserStatus status;
  private UserType type;
}