package com.hallo.api.response;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author
 * @date
 */
@Data
public class UserGroup {
  private UserGroupType type;
  private List<User> users;
}
