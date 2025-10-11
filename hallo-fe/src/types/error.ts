class BizError extends Error {
  constructor(
    public code: string,
    public message: string,
    public details?: Record<string, unknown>
  ) {
    super(message)
    this.name = 'BizError'
  }

  /**
   * 显示业务错误
   * @param message
   * @returns
   */
  static error(message: string) {
    return Promise.reject(new BizError('', message))
  }
}
export default BizError
