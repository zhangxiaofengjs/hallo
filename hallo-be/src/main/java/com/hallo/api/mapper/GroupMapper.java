package com.hallo.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hallo.api.mapper.model.GroupModel;
import com.hallo.api.mapper.parameter.GroupParameter;

/**
 * 组表
 * 
 * @author zhangxiaofeng
 * @date 2025年10月02日 18:53:32
 */
@Mapper
public interface GroupMapper {
  List<GroupModel> getGroupList(GroupParameter parameter);
}
