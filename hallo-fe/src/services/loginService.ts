import httpService from '@/utils/httpService'
import type { User, UserResponse } from '@/types/user'
import BizError from '@/types/error'
import type { LoginResponse, UserLogin } from '@/types/login'

class LoginService {
  constructor() {}

  /**
   * 以token登录
   * @param userToken
   */
  loginWithToken = async (userToken: any): Promise<UserLogin> => {
    const response = await httpService.post<LoginResponse>(`/login`, { userToken })
    if (!response.success) {
      return BizError.error(response.message)
    }

    return response.data
  }

  /**
   * 用户登录
   * @param username 账号或用户ID
   * @param password 密码
   * @returns
   */
  login = async (username: string, password: string): Promise<UserLogin> => {
    const response = await httpService.post<LoginResponse>(`/login`, {
      account: username,
      password,
    })
    if (!response.success) {
      return BizError.error(response.message)
    }
    return response.data
  }
}

export default new LoginService()
