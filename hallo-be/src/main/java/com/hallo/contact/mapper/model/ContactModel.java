package com.hallo.contact.mapper.model;

import lombok.Data;

/**
 * 
 * @author zhangxiaofeng
 * @date 2025-08-30
 */
@Data
public class ContactModel {
    private Integer id;
    private String account;
    private String nickname;
    private String avatar;
    private String mail;
}
