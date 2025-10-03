class LogService {
  constructor(private debug: boolean = true) {}

  /**
   * 抛出格式化的错误信息
   * @param message 错误消息
   * @param error 错误对象
   */
  public throw(message: string, error: any): never {
    const errorMessage = error instanceof Error ? error.message : String(error)
    const msg = `[错误] ${message} ${errorMessage}`
    console.error(msg)
    throw new Error(msg)
  }

  /**
   * 记录错误但不抛出异常
   * @param message 错误消息
   */
  public error(err: any, ...args: any[]): void {
    if (this.debug) {
      const msg = err instanceof Error ? err.message : String(err)
      console.error(`[错误] ${msg}`, ...args)
    }
  }

  /**
   * 记录错误但不抛出异常
   * @param message 错误消息
   */
  public info(message: string, ...args: any[]): void {
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
