package com.hallo.api.response;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年9月28日 16:15:06
 */
public enum UserStatus {
  ONLINE(0),
  OFFLINE(1),
  AWAY(2),
  BUSY(3);

  private final Integer value;

  UserStatus(Integer value) {
    this.value = value;
  }

  @JsonValue
  public Integer getValue() {
    return this.value;
  }

  /**
   * 根据字符串值获取对应的枚举
   */
  public static UserStatus fromValue(int value) {

    for (UserStatus userStatus : UserStatus.values()) {
      if (userStatus.getValue().equals(value)) {
        return userStatus;
      }
    }

    // 如果没有匹配的枚举值，返回默认值或抛出异常
    throw new IllegalArgumentException("Unknown UserStatus: " + value);
  }
}
