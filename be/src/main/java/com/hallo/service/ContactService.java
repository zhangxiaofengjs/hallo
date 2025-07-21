package com.hallo.service;

import com.hallo.model.Contact;
import com.hallo.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    
    private final ContactRepository contactRepository;
    
    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
    
    // 获取所有联系人
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
    
    // 根据ID获取联系人
    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }
    
    // 根据组名获取联系人
    public List<Contact> getContactsByGroup(String groupName) {
        return contactRepository.findByGroupName(groupName);
    }
    
    // 根据名称搜索联系人
    public List<Contact> searchContactsByName(String name) {
        return contactRepository.findByNameContainingIgnoreCase(name);
    }
    
    // 获取在线联系人
    public List<Contact> getOnlineContacts() {
        return contactRepository.findByOnline(true);
    }
    
    // 添加新联系人
    public Contact addContact(Contact contact) {
        return contactRepository.save(contact);
    }
    
    // 更新联系人信息
    public Contact updateContact(Contact contact) {
        return contactRepository.save(contact);
    }
    
    // 删除联系人
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
    
    // 更新联系人的最后一条消息
    public Contact updateLastMessage(Long contactId, String lastMessage, String lastMessageTime) {
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            contact.setLastMessage(lastMessage);
            contact.setLastMessageTime(lastMessageTime);
            return contactRepository.save(contact);
        }
        return null;
    }
}