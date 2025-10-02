import type { HttpResponse } from '.'

// 后端交互返回值定义
export interface MessageResponse extends HttpResponse<Message[]> {}

// 消息类型
export interface Message {
  id: number
  from: {
    uid: string
    nickname: string
    avatar: string
  }
  to: {
    uid: string
    nickname: string
    avatar: string
  }
  content: string
  timestamp: string
}
