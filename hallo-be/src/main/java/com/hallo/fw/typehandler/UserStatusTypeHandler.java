package com.hallo.fw.typehandler;

import com.hallo.api.response.UserStatus;

/**
 * UserStatus 枚举类型处理器
 * 
 * @author zhangxiaofeng
 * @create 2025年10月02日
 */
public class UserStatusTypeHandler extends AbstractIntegerEnumTypeHandler<UserStatus> {

  @Override
  protected Integer getValue(UserStatus enumValue) {
    return enumValue.getValue();
  }

  @Override
  protected UserStatus fromValue(Integer stringValue) {
    return UserStatus.fromValue(stringValue);
  }

}