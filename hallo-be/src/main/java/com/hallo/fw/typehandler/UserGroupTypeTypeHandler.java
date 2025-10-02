package com.hallo.fw.typehandler;

import com.hallo.api.response.UserGroupType;

/**
 * UserGroupType 枚举类型处理器
 * 
 * @author zhangxiaofeng
 * @create 2025年10月02日
 */
public class UserGroupTypeTypeHandler extends AbstractStringEnumTypeHandler<UserGroupType> {

  @Override
  protected String getValue(UserGroupType enumValue) {
    return enumValue.getValue();
  }

  @Override
  protected UserGroupType fromValue(String stringValue) {
    return UserGroupType.fromValue(stringValue);
  }

}