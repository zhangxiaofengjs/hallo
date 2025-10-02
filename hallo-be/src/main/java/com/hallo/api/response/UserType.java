package com.hallo.api.response;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年9月28日 16:15:06
 */
public enum UserType {
  USER("user"),
  GROUP("group");

  private final String value;

  UserType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return this.value;
  }

  /**
   * 根据字符串值获取对应的枚举
   */
  public static UserType fromValue(String value) {

    for (UserType userType : UserType.values()) {
      if (userType.getValue().equals(value)) {
        return userType;
      }
    }

    // 如果没有匹配的枚举值，返回默认值或抛出异常
    throw new IllegalArgumentException("Unknown UserType: " + value);
  }
}
