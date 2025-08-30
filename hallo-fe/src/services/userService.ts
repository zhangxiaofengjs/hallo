import httpService from '@/utils/httpService'
import type { User } from '@/types'

class UserService {
  /**
   *  获取联系人列表
   */
  getUser = async (): Promise<User> => {
    try {
      const contacts = await httpService.get<User>(`/user`)
      return contacts
    } catch (error) {
      console.error('获取联系人列表失败:', error)
      throw error
    }
  }
}

export default new UserService()
