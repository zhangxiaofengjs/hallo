package com.hallo.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.hallo.dto.ChatMessage;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 处理发送消息
    @MessageMapping("/mmm")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        // 保存消息到数据库
        // Message savedMessage = messageService.sendMessage(
        // chatMessage.getSenderId(),
        // chatMessage.getReceiverId(),
        // chatMessage.getContent());

        // if (savedMessage != null) {
        // 设置时间戳
        // LocalDateTime timestamp = savedMessage.getTimestamp();
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        // chatMessage.setTimestamp(timestamp.format(formatter));

        // 发送消息给接收者
        // messagingTemplate.convertAndSendToUser(
        // chatMessage.getReceiverId().toString(),
        // "/user/",
        // chatMessage);

        // simpMessageSendingOperations.convertAndSendToUser("1", "/message",
        // "测试convertAndSendToUser");
        // stomp.subscribe('/users/1/message', function(message){

        // });
        // 发送广播消息
        // }
        messagingTemplate.convertAndSend(
                "/topic",
                chatMessage);
    }

    // 处理用户正在输入的通知
    @MessageMapping("/chat.typing")
    public void typing(@Payload ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.TYPING);

        // 发送通知给接收者
        messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiverId().toString(),
                "/queue/typing",
                chatMessage);
    }

    // 处理消息已读通知
    @MessageMapping("/chat.read")
    public void markAsRead(@Payload ChatMessage chatMessage) {
        // 标记消息为已读
        if (chatMessage.getType() == ChatMessage.MessageType.READ) {
            // 这里可以添加标记消息为已读的逻辑

            // 发送已读通知给发送者
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getSenderId().toString(),
                    "/queue/read",
                    chatMessage);
        }
    }

    // 处理用户加入通知
    @MessageMapping("/chat.join")
    public void join(@Payload ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setContent(chatMessage.getSenderId() + " 已上线");

        // 广播用户加入通知
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }

    // 处理用户离开通知
    @MessageMapping("/chat.leave")
    public void leave(@Payload ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.LEAVE);
        chatMessage.setContent(chatMessage.getSenderId() + " 已离线");

        // 广播用户离开通知
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }
}