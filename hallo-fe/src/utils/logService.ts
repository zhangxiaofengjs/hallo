import { globalErrorDialog } from '@/composables/useErrorDialog'

class LogService {
  constructor(private debug: boolean = true) {}

  /**
   * 记录错误但不抛出异常，同时显示错误对话框
   * @param err 错误对象
   * @param showDialog 是否显示对话框，默认为 true
   * @param customMessage 自定义错误消息
   */
  public error(err: any, showDialog: boolean = true, customMessage?: string): void {
    if (this.debug) {
      const msg = err instanceof Error ? err.message : String(err)
      console.error(`[错误] ${msg}`)
    }

    // 显示错误对话框
    if (showDialog) {
      globalErrorDialog.showErrorFromException(err, customMessage)
    }
  }

  /**
   * 记录错误但不抛出异常
   * @param message 错误消息
   */
  public log(message: string, ...args: any[]): void {
    if (this.debug) {
      console.info(`[信息] ${message}`, ...args)
    }
  }

  /**
   * 系统错误
   * @param error
   */
  public sysError(error: any): never {
    console.error('系统错误!' + error)
    //TODO 系统错误不抛出，使用vue错误边界处理
    throw new Error('系统错误!' + error)
  }
}

export default new LogService()
