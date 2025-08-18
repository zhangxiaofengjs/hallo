package com.hallo.message.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hallo.message.bean.Message;
import com.hallo.message.service.MessageService;

@RestController
@RequestMapping("/api/messages")
// @CrossOrigin(origins = "*")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 获取最近的消息
     * 
     * @param userId
     * @return
     */
    @GetMapping("/{contactId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable("contactId") String contactId) {
        List<Message> messages = messageService.getMessages(contactId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}