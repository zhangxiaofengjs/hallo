import { createContext, useContext, useEffect, useState } from "react";
import type { ReactNode } from "react";
import webSocketService from "../services/websocket";

interface Message {
  id: number;
  text: string;
  isSent: boolean;
  time: string;
}

interface WebSocketContextType {
  messages: Message[];
  sendMessage: (chatId: string, text: string) => void;
  connectToChat: (chatId: string) => void;
  disconnectFromChat: (chatId: string) => void;
  isConnected: boolean;
  connectionError: string | null;
}

const WebSocketContext = createContext<WebSocketContextType | undefined>(
  undefined
);

export const useWebSocket = () => {
  const context = useContext(WebSocketContext);
  if (!context) {
    throw new Error("useWebSocket must be used within a WebSocketProvider");
  }
  return context;
};

interface WebSocketProviderProps {
  children: ReactNode;
}

export const WebSocketProvider: React.FC<WebSocketProviderProps> = ({
  children,
}) => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [currentChatId, setCurrentChatId] = useState<string | null>(null);
  const [isConnected, setIsConnected] = useState<boolean>(false);
  const [connectionError, setConnectionError] = useState<string | null>(null);

  // 检查连接状态的函数
  const checkConnection = () => {
    const connected = webSocketService.isConnected();
    setIsConnected(connected);
    return connected;
  };

  useEffect(() => {
    // 连接WebSocket
    try {
      webSocketService.connect();
      setConnectionError(null);
    } catch (error) {
      console.error("Failed to connect to WebSocket:", error);
      setConnectionError("无法连接到聊天服务器，请稍后再试");
    }

    // 添加消息处理器
    const messageHandler = (message: Message) => {
      setMessages((prevMessages) => [...prevMessages, message]);
    };

    webSocketService.addMessageHandler(messageHandler);

    // 定期检查连接状态
    const intervalId = setInterval(() => {
      checkConnection();
    }, 5000);

    // 清理函数
    return () => {
      clearInterval(intervalId);
      webSocketService.removeMessageHandler(messageHandler);
      webSocketService.disconnect();
    };
  }, []);

  const sendMessage = (chatId: string, text: string) => {
    const newMessage: Message = {
      id: Date.now(),
      text,
      isSent: true,
      time: new Date().toLocaleTimeString(),
    };

    // 更新本地消息列表
    setMessages((prevMessages) => [...prevMessages, newMessage]);

    // 检查连接状态，如果未连接则尝试重新连接
    if (!checkConnection()) {
      try {
        console.log("WebSocket not connected. Attempting to reconnect...");
        webSocketService.connect();

        // 如果当前有聊天ID，重新订阅
        if (currentChatId) {
          webSocketService.subscribe(`/topic`);
          //   webSocketService.subscribe(`/topic/${currentChatId}`);
        }

        // 短暂延迟后再次尝试发送消息
        setTimeout(() => {
          if (webSocketService.isConnected()) {
            webSocketService.sendMessage(`/chat`, {
              text,
              senderId: currentChatId, // 这里应该使用实际的用户ID
              receiverId: chatId,
              timestamp: new Date().toISOString(),
            });
            setConnectionError(null);
          } else {
            setConnectionError("无法连接到聊天服务器，消息可能未发送");
          }
        }, 1000);
      } catch (error) {
        console.error("Failed to reconnect:", error);
        setConnectionError("无法连接到聊天服务器，消息可能未发送");
      }
    } else {
      // 已连接，直接发送消息
      webSocketService.sendMessage(`/chat/mmm`, {
        content: text,
        senderId: chatId, // 这里应该使用实际的用户ID
        receiverId: chatId,
        timestamp: new Date().toISOString(),
      });
    }
  };

  const connectToChat = (chatId: string) => {
    // 如果已经连接到其他聊天，先断开
    if (currentChatId && currentChatId !== chatId) {
      disconnectFromChat(currentChatId);
    }

    // 确保WebSocket已连接
    if (!checkConnection()) {
      try {
        console.log("WebSocket not connected. Attempting to connect...");
        webSocketService.connect();

        // 短暂延迟后再订阅，确保连接已建立
        setTimeout(() => {
          if (webSocketService.isConnected()) {
            webSocketService.subscribe(`/topic`); //订阅广播
            // webSocketService.subscribe(`/user/${chatId}`);
            setConnectionError(null);
          } else {
            setConnectionError("无法连接到聊天服务器，请稍后再试");
          }
        }, 1000);
      } catch (error) {
        console.error("Failed to connect:", error);
        setConnectionError("无法连接到聊天服务器，请稍后再试");
      }
    } else {
      // 已连接，直接订阅
      webSocketService.subscribe(`/topic`);
    }

    setCurrentChatId(chatId);

    // 清空消息列表，准备接收新聊天的消息
    setMessages([]);
  };

  const disconnectFromChat = (chatId: string) => {
    webSocketService.unsubscribe(`/topic`);
    if (currentChatId === chatId) {
      setCurrentChatId(null);
    }
  };

  return (
    <WebSocketContext.Provider
      value={{
        messages,
        sendMessage,
        connectToChat,
        disconnectFromChat,
        isConnected,
        connectionError,
      }}
    >
      {children}
    </WebSocketContext.Provider>
  );
};
