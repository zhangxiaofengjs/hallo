package com.hallo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private MessageType type;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String timestamp;

    // 消息类型枚举
    public enum MessageType {
        CHAT, // 普通聊天消息
        JOIN, // 用户加入通知
        LEAVE, // 用户离开通知
        TYPING, // 用户正在输入
        READ // 消息已读通知
    }
}