package com.hallo.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hallo.api.mapper.GroupMapper;
import com.hallo.api.mapper.GroupMessageMapper;
import com.hallo.api.mapper.UserMapper;
import com.hallo.api.mapper.UserMessageMapper;
import com.hallo.api.mapper.model.GroupModel;
import com.hallo.api.mapper.model.MessageModel;
import com.hallo.api.mapper.model.UserModel;
import com.hallo.api.mapper.parameter.GroupParameter;
import com.hallo.api.mapper.parameter.MessageParameter;
import com.hallo.api.mapper.parameter.UserParameter;
import com.hallo.api.request.UserRequest;
import com.hallo.api.response.Message;
import com.hallo.api.response.User;
import com.hallo.api.response.UserType;
import com.hallo.fw.context.SecurityContext;
import com.hallo.fw.exception.BizException;

@Service
public class MessageService {
  @Autowired
  private UserMessageMapper userMessageMapper;

  @Autowired
  private GroupMessageMapper groupMessageMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private GroupMapper groupMapper;

  /**
   * 取得当前用户与指定用户的消息记录
   * 
   * @param request
   * @return
   */
  public List<Message> getLoginUserMessages(UserRequest request) {
    if (request.getType() == UserType.USER) {
      UserParameter userParameter = new UserParameter();
      userParameter.setUid(request.getUid());
      List<UserModel> userList = userMapper.getUserList(userParameter);

      if (userList.isEmpty()) {
        return BizException.throwException("指定用户不存在:{}", request.getUid());
      }

      UserModel toUser = userList.get(0);
      MessageParameter parameter = new MessageParameter();
      parameter.setFrom(SecurityContext.getLoginUserId());
      parameter.setTo(toUser.getId());
      List<MessageModel> messageList = userMessageMapper.getMessageList(parameter);

      return messageList.stream().map(m -> {
        Message message = new Message();
        message.setId(m.getId());
        if (m.getFromId().equals(toUser.getId())) {
          message.setFrom(User.from(toUser));
          message.setTo(SecurityContext.getLoginUser());
        } else {
          message.setTo(User.from(toUser));
          message.setFrom(SecurityContext.getLoginUser());
        }
        message.setContent(m.getContent());
        message.setTimestamp(m.getTimestamp());
        return message;
      }).collect(Collectors.toList());
    } else if (request.getType() == UserType.GROUP) {
      GroupParameter groupParameter = new GroupParameter();
      groupParameter.setUid(request.getUid());
      List<GroupModel> groupList = groupMapper.getGroupList(groupParameter);
      if (groupList.size() == 0) {
        return BizException.throwException("指定用户不存在:{}", request.getUid());
      }

      GroupModel group = groupList.get(0);

      MessageParameter parameter = new MessageParameter();
      parameter.setTo(group.getId());
      List<MessageModel> messageList = groupMessageMapper.getMessageList(parameter);

      return messageList.stream().map(m -> {
        Message message = new Message();
        message.setId(m.getId());
        message.setFrom(SecurityContext.getLoginUser());
        message.setTo(User.from(group));
        message.setContent(m.getContent());
        message.setTimestamp(m.getTimestamp());
        return message;
      }).collect(Collectors.toList());
    } else {
      return BizException.throwException("指定类型不存在:{}", request.getType());
    }
  }

  /**
   * 保存消息
   * 
   * @param message
   */
  public void save(Message message) {
    MessageModel m = new MessageModel()
        .setFromId(message.getFrom().getId())
        .setToId(message.getTo().getId())
        .setContent(message.getContent())
        .setTimestamp(LocalDateTime.now())
        .setCreatedUserId(message.getFrom().getId())
        .setUpdatedUserId(message.getFrom().getId());
    if (message.getTo().getType() == UserType.USER) {
      userMessageMapper.add(m);
    } else if (message.getTo().getType() == UserType.GROUP) {
      groupMessageMapper.add(m);
    } else {
      BizException.throwException("未知的用户类型:{}", message.getTo().getType());
    }
    // 更新消息ID
    message.setId(m.getId());
  }
}