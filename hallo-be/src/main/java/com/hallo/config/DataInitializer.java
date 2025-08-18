package com.hallo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    public DataInitializer() {
    }

    @Override
    public void run(String... args) {
        // 初始化联系人数据
        // initializeContacts();

        // 初始化消息数据
        // initializeMessages();
    }

    private void initializeContacts() {
        // 创建一些示例联系人
        // List<Contact> contacts = Arrays.asList(
        // new Contact("张三", "https://randomuser.me/api/portraits/men/1.jpg", "朋友",
        // true, "你好，最近怎么样？", "09:30"),
        // new Contact("李四", "https://randomuser.me/api/portraits/women/2.jpg", "朋友",
        // false, "周末有空吗？", "昨天"),
        // new Contact("王五", "https://randomuser.me/api/portraits/men/3.jpg", "同事",
        // true, "会议推迟到下午3点", "10:15"),
        // new Contact("赵六", "https://randomuser.me/api/portraits/women/4.jpg", "同事",
        // true, "项目报告已经发给你了", "08:45"),
        // new Contact("钱七", "https://randomuser.me/api/portraits/men/5.jpg", "家人",
        // false, "晚上回来吃饭吗？", "昨天"),
        // new Contact("孙八", "https://randomuser.me/api/portraits/women/6.jpg", "朋友",
        // true, "生日快乐！", "07:30"),
        // new Contact("周九", "https://randomuser.me/api/portraits/men/7.jpg", "同事",
        // false, "明天记得带文件", "前天"),
        // new Contact("吴十", "https://randomuser.me/api/portraits/women/8.jpg", "家人",
        // true, "已经到家了", "11:20"));

        // // 保存联系人到数据库
        // contactRepository.saveAll(contacts);
    }

    private void initializeMessages() {

    }
}