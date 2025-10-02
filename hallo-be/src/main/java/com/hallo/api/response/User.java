package com.hallo.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年9月28日 16:13:49
 */
@Data
@Accessors(chain = true)
public class User {
    private String uid;
    private String name;
    private String avatar;
    private String mail;
    private UserStatus status;
}
