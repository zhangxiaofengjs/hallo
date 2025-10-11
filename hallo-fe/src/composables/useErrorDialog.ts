import { reactive } from 'vue'

interface ErrorDialogState {
  visible: boolean
  message: string
  details?: string
  autoClose?: boolean
  autoCloseDelay?: number
}

const errorDialogState = reactive<ErrorDialogState>({
  visible: false,
  message: '',
  details: '',
  autoClose: false,
  autoCloseDelay: 5000,
})

export function useErrorDialog() {
  /**
   * 显示错误对话框
   * @param message 错误消息
   * @param details 详细错误信息（可选）
   * @param options 配置选项
   */
  const showError = (
    message: string,
    details?: string,
    options?: {
      autoClose?: boolean
      autoCloseDelay?: number
    }
  ) => {
    errorDialogState.message = message
    errorDialogState.details = details || ''
    errorDialogState.autoClose = options?.autoClose || false
    errorDialogState.autoCloseDelay = options?.autoCloseDelay || 5000
    errorDialogState.visible = true
  }

  /**
   * 隐藏错误对话框
   */
  const hideError = () => {
    errorDialogState.visible = false
  }

  /**
   * 从错误对象中提取信息并显示
   * @param error 错误对象
   * @param customMessage 自定义消息（可选）
   */
  const showErrorFromException = (
    error: any,
    customMessage?: string,
    options?: {
      autoClose?: boolean
      autoCloseDelay?: number
    }
  ) => {
    let message = customMessage || '发生了一个错误'
    let details = ''

    if (error instanceof Error) {
      if (!customMessage) {
        message = error.message || '未知错误'
      }
      details = error.stack || error.toString()
    } else if (typeof error === 'string') {
      if (!customMessage) {
        message = error
      }
      details = error
    } else if (error && typeof error === 'object') {
      // 处理 API 错误响应
      if (error.response?.data?.message) {
        if (!customMessage) {
          message = error.response.data.message
        }
        details = JSON.stringify(error.response.data, null, 2)
      } else if (error.message) {
        if (!customMessage) {
          message = error.message
        }
        details = JSON.stringify(error, null, 2)
      } else {
        details = JSON.stringify(error, null, 2)
      }
    } else {
      details = String(error)
    }

    showError(message, details, options)
  }

  return {
    errorDialogState,
    showError,
    hideError,
    showErrorFromException,
  }
}

// 导出全局实例
export const globalErrorDialog = useErrorDialog()
