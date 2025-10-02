import type { Message, MessageResponse } from '@/types/message'
import errorService from '@/utils/errorService'
import httpService from '@/utils/httpService'

class MessageService {
  /**
   *
   * @param contactId 获取联系人的消息
   * @returns
   */
  getLoginUserMessages = async (uid: string): Promise<Message[]> => {
    const response = await httpService.post<MessageResponse>(`/message/login-user-messages`, {
      uid,
    })
    if (!response.success) {
      return errorService.throw('获取用户消息失败', response.message)
    }

    return response.data
  }
}

export default new MessageService()
