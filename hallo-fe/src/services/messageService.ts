import type { Message } from "@/types";
import httpService from "@/utils/httpService";

class MessageService {
  /**
   *
   * @param contactId 获取联系人的消息
   * @returns
   */
  getMessagesByContactId = async (contactId: string): Promise<Message[]> => {
    try {
      const messages = await httpService.get<Message[]>(
        `/messages/${contactId}`
      );
      return messages;
    } catch (error) {
      console.error("Failed to fetch messages:", error);
      throw error;
    }
  };
}

export default new MessageService();
