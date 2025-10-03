package com.hallo.fw.context;

import com.hallo.api.response.User;
import com.hallo.api.response.UserStatus;
import com.hallo.api.response.UserType;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年9月28日 18:57:59
 */
public class SecurityContext {

  public static Integer getLoginUserId() {
    return 1;// TODO
  }

  public static String getLoginUserUid() {
    return "11111111";// TODO
  }

  public static User getLoginUser() {
    return new User()
        .setUid("11111111")
        .setAccount("11111111")
        .setNickname("zhangxiaofeng")
        .setAvatar("/icons/1.png")
        .setType(UserType.USER)
        .setMail("11111111")
        .setStatus(UserStatus.ONLINE);
    // TODO Auto-generated method stub
  }
}
