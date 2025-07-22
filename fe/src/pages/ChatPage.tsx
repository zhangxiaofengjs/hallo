import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import ChatArea from "../components/ChatArea";
import MessageInput from "../components/MessageInput";
import { useWebSocket } from "../contexts/WebSocketContext";
import "../App.less";

interface ChatPageProps {
  inputValues: { [key: number]: string };
  onInputChange: (value: string) => void;
}

const ChatPage: React.FC<ChatPageProps> = ({ inputValues, onInputChange }) => {
  const { contactId } = useParams<{ contactId: string }>();
  const numericContactId = contactId ? parseInt(contactId) : 1;
  const {
    messages,
    sendMessage,
    connectToChat,
    disconnectFromChat,
    isConnected,
    connectionError,
  } = useWebSocket();

  // 当联系人ID变化时，连接到相应的聊天
  useEffect(() => {
    if (contactId) {
      // 连接到特定聊天
      connectToChat(`${contactId}`);

      // 组件卸载时断开连接
      return () => {
        disconnectFromChat(`${contactId}`);
      };
    }
  }, [contactId, connectToChat, disconnectFromChat]);

  // 处理发送消息
  const handleSendMessage = (message: string) => {
    if (message.trim() && contactId) {
      // 使用WebSocket发送消息
      sendMessage(`${contactId}`, message);

      // 清空输入框
      onInputChange("");
    }
  };

  // 处理重新连接
  const handleReconnect = () => {
    if (contactId) {
      connectToChat(`${contactId}`);
    }
  };

  return (
    <div className="right-panel">
      {connectionError && (
        <div className="connection-error">
          <p>{connectionError}</p>
          <button onClick={handleReconnect}>重新连接</button>
        </div>
      )}

      {!isConnected && !connectionError && (
        <div className="connection-status">
          <p>正在连接聊天服务器...</p>
        </div>
      )}

      <ChatArea messages={messages} />
      <MessageInput
        value={inputValues[numericContactId] || ""}
        onChange={onInputChange}
        onSendMessage={handleSendMessage}
        disabled={!isConnected}
      />
    </div>
  );
};

export default ChatPage;
