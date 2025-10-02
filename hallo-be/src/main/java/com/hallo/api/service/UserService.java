package com.hallo.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hallo.api.mapper.UserFriendMapper;
import com.hallo.api.mapper.UserGroupMapper;
import com.hallo.api.mapper.UserMapper;
import com.hallo.api.mapper.model.GroupModel;
import com.hallo.api.mapper.model.UserModel;
import com.hallo.api.mapper.parameter.UserFriendParameter;
import com.hallo.api.mapper.parameter.UserGroupParameter;
import com.hallo.api.mapper.parameter.UserParameter;
import com.hallo.api.response.Contact;
import com.hallo.api.response.User;
import com.hallo.api.response.UserGroup;
import com.hallo.api.response.UserGroupType;
import com.hallo.api.response.UserStatus;
import com.hallo.api.response.UserType;
import com.hallo.fw.context.SecurityContext;
import com.hallo.fw.exception.BizException;
import com.hallo.fw.util._List;

@Service
public class UserService {
  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserGroupMapper userGroupMapper;

  @Autowired
  private UserFriendMapper userFriendMapper;

  /**
   * 取得当前用户的群组和联系人
   * 
   * @param userId
   * @return
   */
  public List<UserGroup> getLoginUserGroups() {
    Integer userId = SecurityContext.getLoginUserId();

    List<UserGroup> result = new ArrayList<>();

    // 取得收藏的联系人或者群组
    UserGroup favUserGroup = new UserGroup();
    favUserGroup.setType(UserGroupType.FAVORITE);
    favUserGroup.setContacts(_List.create());
    result.add(favUserGroup);
    // TODO

    // 取得群组
    UserGroupParameter gp = new UserGroupParameter();
    gp.setUserId(userId);
    List<GroupModel> groups = userGroupMapper.getGroupList(gp);

    UserGroup userGroup = new UserGroup();
    userGroup.setType(UserGroupType.GROUP);
    userGroup.setContacts(_List.create());
    result.add(userGroup);

    groups.stream().map(g -> {
      return new Contact()//
          .setUid(g.getUid())//
          .setAccount(g.getUid())//
          .setNickname(g.getName())//
          .setAvatar(g.getAvatar())//
          .setType(UserType.GROUP)
          .setMail("")
          .setStatus(UserStatus.ONLINE);
    }).forEach(userGroup.getContacts()::add);

    // 取得联系人
    UserFriendParameter parameter = new UserFriendParameter();
    parameter.setUserId(userId);
    List<UserModel> users = userFriendMapper.getFriendList(parameter);

    UserGroup friendUserGroup = new UserGroup();
    friendUserGroup.setType(UserGroupType.FRIEND);
    friendUserGroup.setContacts(_List.create());
    result.add(friendUserGroup);

    users.stream().map(c -> {
      return new Contact()
          .setUid(c.getUid())
          .setAccount(c.getAccount())
          .setNickname(c.getNickname())
          .setAvatar(c.getAvatar())
          .setType(UserType.USER)
          .setMail(c.getMail())
          .setStatus(c.getStatus());
    }).forEach(friendUserGroup.getContacts()::add);

    return result;
  }

  /**
   * 取得当前登录用户信息
   * 
   * @param userId
   * @return
   */
  public User getLoginUser() {
    UserParameter parameter = new UserParameter();
    parameter.setId(SecurityContext.getLoginUserId());
    List<UserModel> userList = userMapper.getUserList(parameter);
    if (userList.size() == 0) {
      BizException.throwException("指定用户不存在:{}", parameter.getId());
    }

    UserModel userModel = userList.get(0);

    return new User()//
        .setUid(userModel.getUid())//
        .setName(userModel.getNickname())//
        .setAvatar(userModel.getAvatar())//
        .setMail(userModel.getMail())
        .setStatus(userModel.getStatus());
  }
}