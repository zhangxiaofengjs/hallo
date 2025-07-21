package com.hallo.config;

import com.hallo.model.Contact;
import com.hallo.model.Message;
import com.hallo.repository.ContactRepository;
import com.hallo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final ContactRepository contactRepository;
    private final MessageRepository messageRepository;
    
    @Autowired
    public DataInitializer(ContactRepository contactRepository, MessageRepository messageRepository) {
        this.contactRepository = contactRepository;
        this.messageRepository = messageRepository;
    }
    
    @Override
    public void run(String... args) {
        // 初始化联系人数据
        initializeContacts();
        
        // 初始化消息数据
        initializeMessages();
    }
    
    private void initializeContacts() {
        // 创建一些示例联系人
        List<Contact> contacts = Arrays.asList(
            new Contact("张三", "https://randomuser.me/api/portraits/men/1.jpg", "朋友", true, "你好，最近怎么样？", "09:30"),
            new Contact("李四", "https://randomuser.me/api/portraits/women/2.jpg", "朋友", false, "周末有空吗？", "昨天"),
            new Contact("王五", "https://randomuser.me/api/portraits/men/3.jpg", "同事", true, "会议推迟到下午3点", "10:15"),
            new Contact("赵六", "https://randomuser.me/api/portraits/women/4.jpg", "同事", true, "项目报告已经发给你了", "08:45"),
            new Contact("钱七", "https://randomuser.me/api/portraits/men/5.jpg", "家人", false, "晚上回来吃饭吗？", "昨天"),
            new Contact("孙八", "https://randomuser.me/api/portraits/women/6.jpg", "朋友", true, "生日快乐！", "07:30"),
            new Contact("周九", "https://randomuser.me/api/portraits/men/7.jpg", "同事", false, "明天记得带文件", "前天"),
            new Contact("吴十", "https://randomuser.me/api/portraits/women/8.jpg", "家人", true, "已经到家了", "11:20")
        );
        
        // 保存联系人到数据库
        contactRepository.saveAll(contacts);
    }
    
    private void initializeMessages() {
        // 获取已保存的联系人
        List<Contact> contacts = contactRepository.findAll();
        if (contacts.size() < 2) {
            return;
        }
        
        // 创建一些示例消息
        Contact contact1 = contacts.get(0); // 张三
        Contact contact2 = contacts.get(1); // 李四
        
        List<Message> messages = Arrays.asList(
            new Message(contact1, contact2, "你好，李四！", LocalDateTime.now().minusHours(2), true),
            new Message(contact2, contact1, "你好，张三！最近怎么样？", LocalDateTime.now().minusHours(1).minusMinutes(45), true),
            new Message(contact1, contact2, "我很好，谢谢。你呢？", LocalDateTime.now().minusHours(1).minusMinutes(30), true),
            new Message(contact2, contact1, "我也不错。周末有空吗？", LocalDateTime.now().minusHours(1), true),
            new Message(contact1, contact2, "有空，有什么计划吗？", LocalDateTime.now().minusMinutes(30), false)
        );
        
        // 保存消息到数据库
        messageRepository.saveAll(messages);
        
        // 创建更多示例消息（与其他联系人）
        if (contacts.size() >= 3) {
            Contact contact3 = contacts.get(2); // 王五
            
            List<Message> moreMessages = Arrays.asList(
                new Message(contact1, contact3, "王五，明天的会议几点开始？", LocalDateTime.now().minusDays(1), true),
                new Message(contact3, contact1, "上午10点，会议室A", LocalDateTime.now().minusDays(1).plusHours(1), true),
                new Message(contact1, contact3, "好的，谢谢提醒", LocalDateTime.now().minusDays(1).plusHours(2), true),
                new Message(contact3, contact1, "会议推迟到下午3点", LocalDateTime.now().minusHours(5), false)
            );
            
            messageRepository.saveAll(moreMessages);
        }
    }
}