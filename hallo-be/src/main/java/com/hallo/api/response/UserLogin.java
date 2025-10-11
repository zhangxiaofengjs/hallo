package com.hallo.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年10月11日 14:54:07
 */
@Data
@Accessors(chain = true)
public class UserLogin {
  private String token;
  private User user;
}
