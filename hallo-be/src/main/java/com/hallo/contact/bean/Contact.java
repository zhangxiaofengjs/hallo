package com.hallo.contact.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Contact {
    private Integer id;
    private String account;
    private String nickname;
    private String mail;
    private String avatar;
    private ContactGroupType type;
}