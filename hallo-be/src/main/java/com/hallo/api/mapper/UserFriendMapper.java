package com.hallo.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hallo.api.mapper.model.UserModel;
import com.hallo.api.mapper.parameter.UserFriendParameter;

/**
 * 用户-好友关系表
 * 
 * @author zhangxiaofeng
 * @create 2025年9月28日 18:49:54
 */
@Mapper
public interface UserFriendMapper {
  List<UserModel> getFriendList(UserFriendParameter parameter);
}
