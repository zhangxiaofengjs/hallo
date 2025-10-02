package com.hallo.api.response;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 消息模型
 * 
 * @author zhangxiaofeng
 * @date 2025年10月02日 18:53:32
 */
@Data
public class Message {
  private Integer id;
  private String from;
  private String to;
  private String content;
  private LocalDateTime timestamp;
}