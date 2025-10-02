import { UserGroupType, UserStatus } from '@/types/user'

class I8nService {
  constructor() {}

  public getI8n(key: string): string {
    return key
  }

  TEXT_MAP = {
    [UserStatus.ONLINE]: '在线',
    [UserStatus.OFFLINE]: '离线',
    [UserStatus.AWAY]: '离开',
    [UserStatus.BUSY]: '忙碌',
    [UserGroupType.FAVORITE]: '收藏',
    [UserGroupType.GROUP]: '分组',
    [UserGroupType.FRIEND]: '好友',
  } as const

  /**
   * 根据状态获取中文表示文本
   * @param status 用户状态
   * @returns 对应的中文文本
   */
  text = (status: undefined | null | UserStatus | UserGroupType): string => {
    if (status === undefined || status === null) {
      return ''
    }
    return this.TEXT_MAP[status] || ''
  }
}

export default new I8nService()
