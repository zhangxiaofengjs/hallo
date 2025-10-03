import type { HttpResponse } from '.'
import type { User } from './user'

// 后端交互返回值定义
export interface MessageResponse extends HttpResponse<Message[]> {}

// 消息类型
export interface Message {
  id: number
  uid?: string
  from: User
  to: User
  content: string
  timestamp: string
  sendStatus?: SendStatus
}

export enum SendStatus {
  SENDING = 0,
  SEND_SUCCESS = 1,
  SEND_FAIL = 2,
}
