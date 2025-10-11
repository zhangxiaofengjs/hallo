import type { Message, MessageResponse } from '@/types/message'
import httpService from '@/utils/httpService'
import BizError from '@/types/error'

class MessageService {
  /**
   *
   * @param contactId 获取联系人的消息
   * @returns
   */
  getLoginUserMessages = async (uid: string, type: string): Promise<Message[]> => {
    const response = await httpService.post<MessageResponse>(`/message/login-user-messages`, {
      uid,
      type,
    })
    if (!response.success) {
      return BizError.error(response.message)
    }

    return response.data
  }
}

export default new MessageService()
