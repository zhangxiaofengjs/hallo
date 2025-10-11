package com.hallo.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hallo.api.mapper.GroupMapper;
import com.hallo.api.mapper.UserFriendMapper;
import com.hallo.api.mapper.UserGroupMapper;
import com.hallo.api.mapper.UserMapper;
import com.hallo.api.mapper.model.GroupModel;
import com.hallo.api.mapper.model.UserModel;
import com.hallo.api.mapper.parameter.GroupParameter;
import com.hallo.api.mapper.parameter.UserFriendParameter;
import com.hallo.api.mapper.parameter.UserGroupParameter;
import com.hallo.api.mapper.parameter.UserParameter;
import com.hallo.api.request.UserRequest;
import com.hallo.api.response.User;
import com.hallo.api.response.UserGroup;
import com.hallo.api.response.UserGroupType;
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
  private GroupMapper groupMapper;

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
    favUserGroup.setUsers(_List.create());
    result.add(favUserGroup);
    // TODO

    // 取得群组
    UserGroupParameter gp = new UserGroupParameter();
    gp.setUserId(userId);
    List<GroupModel> groups = userGroupMapper.getGroupList(gp);

    UserGroup userGroup = new UserGroup();
    userGroup.setType(UserGroupType.GROUP);
    userGroup.setUsers(_List.create());
    result.add(userGroup);

    groups.stream().map(g -> User.from(g)).forEach(userGroup.getUsers()::add);

    // 取得联系人
    UserFriendParameter parameter = new UserFriendParameter();
    parameter.setUserId(userId);
    List<UserModel> users = userFriendMapper.getFriendList(parameter);

    UserGroup friendUserGroup = new UserGroup();
    friendUserGroup.setType(UserGroupType.FRIEND);
    friendUserGroup.setUsers(_List.create());
    result.add(friendUserGroup);

    users.stream().map(c -> User.from(c)).forEach(friendUserGroup.getUsers()::add);

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

    UserModel user = userList.get(0);

    return User.from(user);
  }

  /**
   * 取得用户或组信息
   * 
   * @param userId
   * @param request
   * @return
   */
  public User getUser(UserRequest request) {
    if (request.getType() == UserType.USER) {
      UserParameter parameter = new UserParameter();
      parameter.setUid(request.getUid());
      List<UserModel> userList = userMapper.getUserList(parameter);
      if (userList.size() == 0) {
        BizException.throwException("指定用户不存在:{}", parameter.getId());
      }
      UserModel user = userList.get(0);

      return User.from(user);
    } else if (request.getType() == UserType.GROUP) {
      GroupParameter groupParameter = new GroupParameter();
      groupParameter.setUid(request.getUid());
      List<GroupModel> groupList = groupMapper.getGroupList(groupParameter);
      if (groupList.size() == 0) {
        BizException.throwException("指定群组不存在:{}", request.getUid());
      }
      return User.from(groupList.get(0));
    } else {
      return BizException.throwException("指定类型不存在:{}", request.getType());
    }
  }
}