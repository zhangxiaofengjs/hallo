package com.hallo.api.service;

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
    UserParameter userParameter = new UserParameter();
    userParameter.setUid(request.getUid());
    List<UserModel> userList = userMapper.getUserList(userParameter);

    if (!userList.isEmpty()) {
      UserModel toUser = userList.get(0);
      MessageParameter parameter = new MessageParameter();
      parameter.setFrom(SecurityContext.getLoginUserId());
      parameter.setTo(toUser.getId());
      List<MessageModel> messageList = userMessageMapper.getMessageList(parameter);

      return messageList.stream().map(m -> {
        Message message = new Message();
        message.setId(m.getId());
        message.setFrom(SecurityContext.getLoginUserUid());
        message.setTo(toUser.getUid());
        message.setContent(m.getContent());
        message.setTimestamp(m.getTimestamp());
        return message;
      }).collect(Collectors.toList());
    }

    GroupParameter groupParameter = new GroupParameter();
    groupParameter.setUid(request.getUid());
    List<GroupModel> groupList = groupMapper.getGroupList(groupParameter);
    if (groupList.size() != 0) {
      GroupModel group = groupList.get(0);

      MessageParameter parameter = new MessageParameter();
      parameter.setTo(group.getId());
      List<MessageModel> messageList = groupMessageMapper.getMessageList(parameter);

      return messageList.stream().map(m -> {
        Message message = new Message();
        message.setId(m.getId());
        message.setFrom(SecurityContext.getLoginUserUid());
        message.setTo(group.getUid());
        message.setContent(m.getContent());
        message.setTimestamp(m.getTimestamp());
        return message;
      }).collect(Collectors.toList());
    }

    return BizException.throwException("指定用户不存在:{}", request.getUid());
  }
}