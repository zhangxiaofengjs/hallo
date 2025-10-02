/**
 * 放置共通的处理类
 */

import type { UserGroupType, UserStatus } from './user'

// http 响应数据
export interface HttpResponse<T> {
  success: boolean
  message: string
  data: T
}

// 消息类型
export interface Message {
  id: string
  fromId: string // 0 表示当前用户，其他表示联系人ID
  toId: string // 接收者ID，用于WebSocket消息
  content: string
  time: string
}
