package com.hallo.api.request;

import com.hallo.api.response.UserType;

import lombok.Data;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年10月02日 21:04:16
 */
@Data
public class UserRequest {
  private String uid;
  private UserType type;
}
