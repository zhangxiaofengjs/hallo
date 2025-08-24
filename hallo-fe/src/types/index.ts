// 用户类型
export interface User {
  id: string;
  name: string;
  avatar: string;
  status: string;
}

// 消息类型
export interface Message {
  id: string;
  fromId: string; // 0 表示当前用户，其他表示联系人ID
  toId: string; // 接收者ID，用于WebSocket消息
  content: string;
  time: string;
}

// 联系人类型
export interface Contact {
  id: string;
  name: string;
  avatar: string;
  lastMessage: string;
  lastMessageTime: string;
  unreadCount: number;
}
