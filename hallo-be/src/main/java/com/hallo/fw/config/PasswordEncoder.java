package com.hallo.fw.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年10月11日 15:10:24
 */
@Component
public class PasswordEncoder {
  private final BCryptPasswordEncoder passwordEncoder;

  public PasswordEncoder() {
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  /**
   * 密码加密
   * 
   * @param password
   * @return
   */
  public String encode(String password) {
    return passwordEncoder.encode(password);
  }

  /**
   * 密码匹配
   * 
   * @param rawPassword
   * @param encodedPassword
   * @return
   */
  public boolean matches(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }
}
