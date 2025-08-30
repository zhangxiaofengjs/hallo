package com.hallo.contact.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hallo.contact.bean.Contact;
import com.hallo.contact.bean.ContactGroup;
import com.hallo.contact.bean.ContactGroupType;
import com.hallo.contact.mapper.ContactMapper;
import com.hallo.contact.mapper.model.ContactModel;
import com.hallo.contact.mapper.model.GroupModel;
import com.hallo.contact.mapper.parameter.ContactParameter;
import com.hallo.contact.mapper.parameter.GroupParameter;
import com.hallo.fw.util._List;

@Service
public class ContactService {
    @Autowired
    private ContactMapper contactMapper;

    // 获取所有联系人
    public List<ContactGroup> getContacts(Integer userId) {
        List<ContactGroup> result = new ArrayList<>();

        // 取得收藏的联系人
        // TODO

        // 取得群组
        GroupParameter gp = new GroupParameter();
        gp.setUserId(userId);
        List<GroupModel> groups = contactMapper.getGroupList(gp);

        ContactGroup contacGroup = new ContactGroup();
        contacGroup.setType(ContactGroupType.GROUP);
        contacGroup.setContacts(_List.create());
        result.add(contacGroup);

        groups.stream().map(g -> {
            return new Contact()//
                    .setId(g.getId())//
                    .setAccount(g.getId().toString())//
                    .setNickname(g.getName())//
                    .setAvatar(g.getAvatar())//
                    .setType(ContactGroupType.GROUP)//
                    .setMail("");
        }).forEach(contacGroup.getContacts()::add);

        // 取得联系人
        ContactParameter parameter = new ContactParameter();
        parameter.setUserId(userId);
        List<ContactModel> contacts = contactMapper.getFriendList(parameter);

        contacGroup = new ContactGroup();
        contacGroup.setType(ContactGroupType.FRIEND);
        contacGroup.setContacts(_List.create());
        result.add(contacGroup);

        contacts.stream().map(c -> {
            return new Contact()//
                    .setId(c.getId())//
                    .setAccount(c.getAccount())//
                    .setNickname(c.getNickname())//
                    .setAvatar(c.getAvatar())//
                    .setType(ContactGroupType.FRIEND)//
                    .setMail(c.getMail());
        }).forEach(contacGroup.getContacts()::add);

        return result;
    }
}