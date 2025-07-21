package com.hallo.service;

import com.hallo.model.Contact;
import com.hallo.model.Message;
import com.hallo.repository.ContactRepository;
import com.hallo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final ContactRepository contactRepository;
    private final ContactService contactService;
    
    @Autowired
    public MessageService(MessageRepository messageRepository, ContactRepository contactRepository, ContactService contactService) {
        this.messageRepository = messageRepository;
        this.contactRepository = contactRepository;
        this.contactService = contactService;
    }
    
    // 获取所有消息
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    
    // 根据ID获取消息
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }
    
    // 获取两个联系人之间的所有消息
    public List<Message> getMessagesBetweenContacts(Long senderId, Long receiverId) {
        Optional<Contact> optionalSender = contactRepository.findById(senderId);
        Optional<Contact> optionalReceiver = contactRepository.findById(receiverId);
        
        if (optionalSender.isPresent() && optionalReceiver.isPresent()) {
            Contact sender = optionalSender.get();
            Contact receiver = optionalReceiver.get();
            
            return messageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
                    sender, receiver, receiver, sender);
        }
        
        return List.of();
    }
    
    // 发送消息
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        Optional<Contact> optionalSender = contactRepository.findById(senderId);
        Optional<Contact> optionalReceiver = contactRepository.findById(receiverId);
        
        if (optionalSender.isPresent() && optionalReceiver.isPresent()) {
            Contact sender = optionalSender.get();
            Contact receiver = optionalReceiver.get();
            
            LocalDateTime now = LocalDateTime.now();
            Message message = new Message(sender, receiver, content, now, false);
            Message savedMessage = messageRepository.save(message);
            
            // 更新联系人的最后一条消息
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = now.format(formatter);
            contactService.updateLastMessage(receiverId, content, formattedTime);
            
            return savedMessage;
        }
        
        return null;
    }
    
    // 标记消息为已读
    public Message markMessageAsRead(Long messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setRead(true);
            return messageRepository.save(message);
        }
        return null;
    }
    
    // 获取未读消息
    public List<Message> getUnreadMessages(Long contactId) {
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            return messageRepository.findByReceiverAndIsReadFalseOrderByTimestampAsc(contact);
        }
        return List.of();
    }
    
    // 删除消息
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}