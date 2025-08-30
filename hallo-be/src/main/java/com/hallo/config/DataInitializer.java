package com.hallo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    public DataInitializer() {
    }

    @Override
    public void run(String... args) {
        // 初始化联系人数据
        // initializeContacts();

        // 初始化消息数据
        // initializeMessages();
    }

}