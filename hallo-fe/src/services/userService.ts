import httpService from '@/utils/httpService'
import type { User, UserGroup, UserGroupResponse, UserResponse, UserType } from '@/types/user'
import errorService from '@/utils/logService'

class UserService {
  /**
   * 获取当前用户信息
   */
  getLoginUser = async (): Promise<User> => {
    const response = await httpService.get<UserResponse>(`/user/login-user`)
    if (!response.success) {
      return errorService.throw('获取用户信息失败', response.message)
    }

    return response.data
  }

  /**
   * 获取当前用户联系人列表
   */
  getLoginUserGroups = async (): Promise<UserGroup[]> => {
    const response = await httpService.get<UserGroupResponse>(`/user/login-user-groups`)
    if (!response.success) {
      return errorService.throw('获取用户信息失败', response.message)
    }

    return response.data
  }

  /**
   * 获取用户信息
   * @param uid
   * @returns
   */
  getUser = async (uid: string, type: string): Promise<User> => {
    const response = await httpService.post<UserResponse>(`/user/user`, {
      uid,
      type,
    })
    if (!response.success) {
      return errorService.throw('获取用户信息失败', response.message)
    }
    return response.data
  }
}

export default new UserService()
