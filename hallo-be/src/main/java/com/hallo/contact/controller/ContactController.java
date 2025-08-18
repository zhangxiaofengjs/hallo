package com.hallo.contact.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hallo.contact.bean.Contact;
import com.hallo.contact.service.ContactService;

@RestController
@RequestMapping("/api/contacts")
// @CrossOrigin(origins = "*")
public class ContactController {
    @Autowired
    private ContactService contactService;

    // 获取所有联系人
    @GetMapping
    public ResponseEntity<List<Contact>> getContacts(@RequestParam(required = false) String userId) {
        List<Contact> contacts = contactService.getContacts(userId);
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }
}