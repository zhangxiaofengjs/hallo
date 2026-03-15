package com.hallo.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.hallo.api.response.Message;
import com.hallo.api.service.MessageService;
import com.hallo.dto.ChatMessage;

/**
 * websocket 聊天控制器
 *
 * @author zhangxiaofeng
 * @create 2025年10月3日 14:45:14
 */
@Controller
public class ChatController {

  private final SimpMessagingTemplate messagingTemplate;

  @Autowired
  private MessageService messageService;

  public ChatController(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  // 处理发送消息
  @MessageMapping("/send-message")
  public void sendMessage(@Payload Message message) {
    // 保存消息到数据库
    messageService.save(message);

    // 发送消息给接收者
    // messagingTemplate.convertAndSendTo1User(
    // chatMessage.getReceiverId().toString(),
    // "/user/",
    // chatMessage);

    // 发送给自己（更新消息ID，更新状态)
    // 发送给对方
    messagingTemplate.convertAndSend(
        "/user/" + message.getFrom().getUid() + "/message",
        message);
    messagingTemplate.convertAndSend(
        "/user/" + message.getTo().getUid() + "/message",
        message);
    // messagingTemplate.convertAndSend(
    // "/topic",
    // chatMessage);
  }

  /**
   * 上线
   * 
   * @param chatMessage
   */
  @MessageMapping("/status")
  public void online(@Payload ChatMessage chatMessage) {

  }

  // 处理用户正在输入的通知
  @MessageMapping("/chat.typing")
  public void typing(@Payload ChatMessage chatMessage) {
    chatMessage.setType(ChatMessage.MessageType.TYPING);

    // 发送通知给接收者
    messagingTemplate.convertAndSendToUser(
        chatMessage.getFromId().toString(),
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
          chatMessage.getToId().toString(),
          "/queue/read",
          chatMessage);
    }
  }

  // 处理用户加入通知
  @MessageMapping("/chat.join")
  public void join(@Payload ChatMessage chatMessage) {
    chatMessage.setType(ChatMessage.MessageType.JOIN);
    chatMessage.setContent(chatMessage.getFromId() + " 已上线");

    // 广播用户加入通知
    messagingTemplate.convertAndSend("/topic/public", chatMessage);
  }

  // 处理用户离开通知
  @MessageMapping("/chat.leave")
  public void leave(@Payload ChatMessage chatMessage) {
    chatMessage.setType(ChatMessage.MessageType.LEAVE);
    chatMessage.setContent(chatMessage.getFromId() + " 已离线");

    // 广播用户离开通知
    messagingTemplate.convertAndSend("/topic/public", chatMessage);
  }
}