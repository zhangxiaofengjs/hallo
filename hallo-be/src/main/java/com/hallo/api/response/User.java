package com.hallo.api.response;

import com.hallo.api.mapper.model.GroupModel;
import com.hallo.api.mapper.model.UserModel;

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
  private Integer id;
  private String uid;
  private String account;
  private String nickname;
  private String mail;
  private String avatar;
  private UserStatus status;
  private UserType type;

  public static User from(UserModel user) {
    return new User()
        .setId(user.getId())
        .setUid(user.getUid())
        .setAccount(user.getAccount())
        .setNickname(user.getNickname())
        .setAvatar(user.getAvatar())
        .setType(UserType.USER)
        .setMail(user.getMail())
        .setStatus(user.getStatus());
  }

  public static User from(GroupModel g) {
    return new User()
        .setId(g.getId())
        .setUid(g.getUid())
        .setAccount(g.getUid())
        .setNickname(g.getName())
        .setAvatar(g.getAvatar())
        .setType(UserType.GROUP)
        .setMail("")
        .setStatus(UserStatus.ONLINE);
  }
}