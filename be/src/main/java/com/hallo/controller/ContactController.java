package com.hallo.controller;

import com.hallo.model.Contact;
import com.hallo.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactController {
    
    private final ContactService contactService;
    
    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }
    
    // 获取所有联系人
    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }
    
    // 根据ID获取联系人
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Optional<Contact> contact = contactService.getContactById(id);
        return contact.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // 根据组名获取联系人
    @GetMapping("/group/{groupName}")
    public ResponseEntity<List<Contact>> getContactsByGroup(@PathVariable String groupName) {
        List<Contact> contacts = contactService.getContactsByGroup(groupName);
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }
    
    // 根据名称搜索联系人
    @GetMapping("/search")
    public ResponseEntity<List<Contact>> searchContactsByName(@RequestParam String name) {
        List<Contact> contacts = contactService.searchContactsByName(name);
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }
    
    // 获取在线联系人
    @GetMapping("/online")
    public ResponseEntity<List<Contact>> getOnlineContacts() {
        List<Contact> contacts = contactService.getOnlineContacts();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }
    
    // 添加新联系人
    @PostMapping
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact) {
        Contact newContact = contactService.addContact(contact);
        return new ResponseEntity<>(newContact, HttpStatus.CREATED);
    }
    
    // 更新联系人信息
    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contact) {
        Optional<Contact> existingContact = contactService.getContactById(id);
        if (existingContact.isPresent()) {
            contact.setId(id);
            Contact updatedContact = contactService.updateContact(contact);
            return new ResponseEntity<>(updatedContact, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 删除联系人
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        Optional<Contact> existingContact = contactService.getContactById(id);
        if (existingContact.isPresent()) {
            contactService.deleteContact(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 更新联系人的最后一条消息
    @PutMapping("/{id}/last-message")
    public ResponseEntity<Contact> updateLastMessage(
            @PathVariable Long id,
            @RequestParam String lastMessage,
            @RequestParam String lastMessageTime) {
        Contact updatedContact = contactService.updateLastMessage(id, lastMessage, lastMessageTime);
        if (updatedContact != null) {
            return new ResponseEntity<>(updatedContact, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}