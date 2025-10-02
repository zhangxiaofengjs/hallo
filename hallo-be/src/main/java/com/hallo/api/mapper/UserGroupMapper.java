package com.hallo.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hallo.api.mapper.model.GroupModel;
import com.hallo.api.mapper.parameter.UserGroupParameter;

/**
 * 用户-群组关系表
 * 
 * @author zhangxiaofeng
 * @create 2025年9月28日 18:44:02
 */
@Mapper
public interface UserGroupMapper {
    List<GroupModel> getGroupList(UserGroupParameter gp);
}
