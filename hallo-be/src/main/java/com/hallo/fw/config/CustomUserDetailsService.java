package com.hallo.fw.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年10月11日 13:46:04
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // 这里应该从数据库加载用户信息
    // 现在我们创建一个默认用户用于测试
    return User.builder()
        .username("user")
        .password("{noop}password") // {noop}表示不加密的密码
        .roles("USER")
        .build();
  }
}
