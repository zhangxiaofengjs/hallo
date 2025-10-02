package com.hallo.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hallo.api.mapper.model.UserModel;
import com.hallo.api.mapper.parameter.UserParameter;

/**
 * 用户表
 * 
 * @author zhangxiaofeng
 * @date
 */
@Mapper
public interface UserMapper {
    List<UserModel> getUserList(UserParameter parameter);
}
