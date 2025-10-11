import type { HttpResponse } from '.'

// 后端交互返回值定义
export interface UserResponse extends HttpResponse<User> {}
export interface UserGroupResponse extends HttpResponse<UserGroup[]> {}

// 用户状态
export enum UserStatus {
  ONLINE = 0,
  OFFLINE = 1,
  AWAY = 2,
  BUSY = 3,
}

// 用户类型
export interface User {
  uid: string
  token: string
  account?: string
  nickname?: string
  mail?: string
  avatar?: string
  type: UserType
  status?: UserStatus
  unread?: number
}

// 联系人列表组类型
export enum UserGroupType {
  FAVORITE = 'favorite', // 收藏
  GROUP = 'group', // 分组
  FRIEND = 'friend', // 好友
}

export enum UserType {
  GROUP = 'group', // 分组
  USER = 'user', // 好友
}

// 联系人列表组
export interface UserGroup {
  type: UserGroupType
  users: User[]
}
