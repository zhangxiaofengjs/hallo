package com.hallo.fw.context;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.hallo.api.mapper.UserMapper;
import com.hallo.api.mapper.model.UserModel;
import com.hallo.api.mapper.parameter.UserParameter;
import com.hallo.api.response.User;
import com.hallo.fw.util._List;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年10月11日 14:24:08
 */
@Component
public class LoginUserContext {
  private static final String CACHE_KEY = "login-user-cache";

  @Autowired
  private UserMapper userMapper;

  /**
   * 从缓存中获取用户信息
   * 缓存名称为"userCache"，key为userId
   * 第一次调用时会执行方法体，之后会从缓存中获取
   */
  @Cacheable(value = CACHE_KEY, key = "#uid")
  public User getUser(String uid) {
    // 当缓存中没有时，会执行这个方法从数据库查询
    System.out.println("从数据库查询用户信息: " + uid);
    UserParameter parameter = new UserParameter();
    parameter.setUid(uid);
    List<UserModel> users = userMapper.getUserList(parameter);

    if (_List.isEmpty(users)) {
      return null;
    }
    return User.from(users.get(0));
  }

  /**
   * 清除指定用户的缓存
   */
  @CacheEvict(value = CACHE_KEY, key = "#uid")
  public void clearUserCache(String uid) {
    // 当用户信息更新时调用此方法清除缓存
    System.out.println("清除用户缓存: " + uid);
  }

  /**
   * 清除所有用户缓存
   */
  @CacheEvict(value = CACHE_KEY, allEntries = true)
  public void clearAllUserCache() {
    System.out.println("清除所有用户缓存");
  }
}
