// 用户类型
export interface User {
  id: string
  name: string
  avatar?: string
  status: string
}

// 消息类型
export interface Message {
  id: string
  fromId: string // 0 表示当前用户，其他表示联系人ID
  toId: string // 接收者ID，用于WebSocket消息
  content: string
  time: string
}

// 联系人列表组
export interface ContactGroup {
  type: ContactGroupType
  contacts: Contact[]
}

// 联系人类型
export interface Contact {
  id: string
  account: string
  nickname: string
  mail: string
  avatar?: string
  type: ContactGroupType
  unread: boolean
}

export enum ContactGroupType {
  FRIEND = 'friend',
  GROUP = 'group',
}
