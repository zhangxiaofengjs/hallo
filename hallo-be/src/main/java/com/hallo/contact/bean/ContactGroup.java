package com.hallo.contact.bean;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author
 * @date
 */
@Data
public class ContactGroup {
    private ContactGroupType type;
    private List<Contact> contacts;
}
