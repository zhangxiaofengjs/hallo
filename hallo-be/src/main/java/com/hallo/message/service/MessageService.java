package com.hallo.message.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hallo.message.bean.Message;

@Service
public class MessageService {
    public List<Message> getMessages(String contactId) {
        Message mm = new Message();
        mm.setId("aaa");
        mm.setFrom("1");
        mm.setTo("2");
        mm.setContent("hello");
        mm.setTimestamp(LocalDateTime.now());

        Message mm1 = new Message();
        mm1.setId("aaa");
        mm1.setFrom("2");
        mm1.setTo("1");
        mm1.setContent("hello222");

        Message mm11 = new Message();
        mm1.setId("aaa");
        mm11.setFrom("2");
        mm11.setTo("1");
        mm11.setContent("hello11111");

        if (contactId.equals("1"))
            return List.of(mm, mm11);
        else
            return List.of(mm, mm1);
    }
}