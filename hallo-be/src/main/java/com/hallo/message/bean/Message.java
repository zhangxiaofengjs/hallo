package com.hallo.message.bean;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Message {
    private String id;
    private String from;
    private String to;
    private String content;
    private LocalDateTime timestamp;
}