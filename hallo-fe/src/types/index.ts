/**
 * 放置共通的处理类
 */

// http 响应数据
export interface HttpResponse<T> {
  success: boolean
  message: string
  data: T
}
