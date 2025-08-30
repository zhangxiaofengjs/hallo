import httpService from "@/utils/httpService";
import type { ContactGroup } from "@/types";

class ContactService {
  /**
   *  获取联系人列表
   */
  getContacts = async (): Promise<ContactGroup[]> => {
    try {
      const contacts = await httpService.get<ContactGroup[]>(`/contacts`);
      return contacts;
    } catch (error) {
      console.error("获取联系人列表失败:", error);
      throw error;
    }
  };
}

export default new ContactService();
