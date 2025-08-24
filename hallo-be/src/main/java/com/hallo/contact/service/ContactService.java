package com.hallo.contact.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hallo.contact.bean.Contact;

@Service
public class ContactService {

    public ContactService() {
    }

    // 获取所有联系人
    public List<Contact> getContacts(String userId) {
        Contact c = new Contact();
        c.setId("1");
        c.setGroupName("friends");
        c.setName("张三");
        c.setAvatar("/icons/1.png");
        Contact c1 = new Contact();
        c1.setId("2");
        c1.setName("张三2");
        c1.setAvatar("/icons/2.png");

        List<Contact> cs = new ArrayList<>();
        cs.add(c);
        for (int i = 0; i < 10; i++) {
            cs.add(c1);
        }
        cs.add(c1);
        return cs;
    }

}