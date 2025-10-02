package com.hallo.api.mapper.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 消息模型
 * 
 * @author zhangxiaofeng
 * @date 2025年10月02日 18:53:32
 */
@Data
public class MessageModel {
  private Integer id;
  private Integer fromId;
  private Integer toId;
  private String content;
  private LocalDateTime timestamp;
}