package com.hallo.api.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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

  // 客户端采集的临时ID
  private String uid;

  private User from;
  private User to;
  private String content;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;
}