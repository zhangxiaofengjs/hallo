import type { HttpResponse } from '.'
import type { User } from './user'

// 后端交互返回值定义
export interface LoginResponse extends HttpResponse<UserLogin> {}

// 用户类型
export interface UserLogin {
  token: string
  user: User
}
