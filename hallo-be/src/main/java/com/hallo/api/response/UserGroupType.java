package com.hallo.api.response;

import com.fasterxml.jackson.annotation.JsonValue;

/** */
public enum UserGroupType {
  FRIEND("friend"),
  GROUP("group"),
  FAVORITE("favorite");

  private String value;

  UserGroupType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  /**
   * 根据字符串值获取对应的枚举
   */
  public static UserGroupType fromValue(String value) {
    if (value == null) {
      return null;
    }

    for (UserGroupType userGroupType : UserGroupType.values()) {
      if (userGroupType.getValue().equals(value)) {
        return userGroupType;
      }
    }

    // 如果没有匹配的枚举值，返回默认值或抛出异常
    throw new IllegalArgumentException("Unknown UserGroupType: " + value);
  }
}
