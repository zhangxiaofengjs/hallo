package com.hallo.controller;

import com.hallo.model.Message;
import com.hallo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // 获取所有消息
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // 根据ID获取消息
    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Optional<Message> message = messageService.getMessageById(id);
        return message.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 获取两个联系人之间的所有消息
    @GetMapping("/between")
    public ResponseEntity<List<Message>> getMessagesBetweenContacts(@RequestParam Long senderId,
            @RequestParam Long receiverId) {
        List<Message> messages = messageService.getMessagesBetweenContacts(senderId, receiverId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // 发送消息
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestParam Long senderId, @RequestParam Long receiverId,
            @RequestParam String content) {
        Message message = messageService.sendMessage(senderId, receiverId, content);
        if (message != null) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 标记消息为已读
    @PutMapping("/{id}/read")
    public ResponseEntity<Message> markMessageAsRead(@PathVariable Long id) {
        Message message = messageService.markMessageAsRead(id);
        if (message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 获取未读消息
    @GetMapping("/unread/{contactId}")
    public ResponseEntity<List<Message>> getUnreadMessages(@PathVariable Long contactId) {
        List<Message> messages = messageService.getUnreadMessages(contactId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // 删除消息
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        Optional<Message> existingMessage = messageService.getMessageById(id);
        if (existingMessage.isPresent()) {
            messageService.deleteMessage(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}