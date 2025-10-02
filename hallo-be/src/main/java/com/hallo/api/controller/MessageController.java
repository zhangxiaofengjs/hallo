package com.hallo.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hallo.api.request.UserRequest;
import com.hallo.api.response.HttpResponse;
import com.hallo.api.response.Message;
import com.hallo.api.service.MessageService;
import com.hallo.fw.annotation.SafeResponse;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年9月28日 16:10:04
 */
@RestController
@RequestMapping("/api/message")
@SafeResponse
public class MessageController {
  @Autowired
  private MessageService messageService;

  /**
   * 获取登录用户的消息
   * 
   * @param request
   * @return
   */
  @RequestMapping("login-user-messages")
  public HttpResponse loginUserMessages(@RequestBody UserRequest request) {
    List<Message> messages = messageService.getLoginUserMessages(request);
    return HttpResponse.success(messages);
  }
}
