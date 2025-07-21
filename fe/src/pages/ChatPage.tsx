import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import ChatArea from "../components/ChatArea";
import MessageInput from "../components/MessageInput";
import "../App.less";

interface Message {
  id: number;
  text: string;
  isSent: boolean;
  time: string;
}

interface ChatPageProps {
  messages: Message[];
  inputValues: { [key: number]: string };
  onInputChange: (value: string) => void;
  onSendMessage: (message: string) => void;
  mockMessagesByContact: {
    [key: number]: Message[];
  };
}

const ChatPage: React.FC<ChatPageProps> = ({
  messages,
  inputValues,
  onInputChange,
  onSendMessage,
  mockMessagesByContact,
}) => {
  const { contactId } = useParams<{ contactId: string }>();
  const numericContactId = contactId ? parseInt(contactId) : 1;
  const [currentMessages, setCurrentMessages] = useState<Message[]>([]);

  useEffect(() => {
    if (contactId && mockMessagesByContact) {
      const id = parseInt(contactId);
      // 安全访问消息数据，未定义的contactId返回空数组
      const contactMessages =
        id in mockMessagesByContact ? mockMessagesByContact[id] : [];
      setCurrentMessages(contactMessages);
    }
  }, [contactId, mockMessagesByContact]);

  // 处理发送消息
  const handleSendMessage = (message: string) => {
    if (message.trim()) {
      const newMessage = {
        id: currentMessages.length + 1,
        text: message,
        isSent: true,
        time: new Date().toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
        }),
      };

      setCurrentMessages([...currentMessages, newMessage]);

      // 调用父组件的发送消息方法
      onSendMessage(message);

      // 模拟收到回复（2秒后）
      setTimeout(() => {
        const reply = {
          id: currentMessages.length + 2,
          text: "这是一个自动回复消息，模拟对方的回复。",
          isSent: false,
          time: new Date().toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit",
          }),
        };
        setCurrentMessages((prevMessages) => [...prevMessages, reply]);
      }, 2000);
    }
  };

  return (
    <div className="right-panel">
      <ChatArea messages={currentMessages} />
      <MessageInput
        value={inputValues[numericContactId] || ""}
        onChange={onInputChange}
        onSendMessage={handleSendMessage}
      />
    </div>
  );
};

export default ChatPage;
