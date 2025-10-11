import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Message } from '@/types/message'
import type { User } from '@/types/user'
import websocketService from '@/utils/websocketService'
import { ConnectionStatus } from '@/types/websocket'
import logService from '@/utils/logService'

// 消息订阅回调函数类型
type MessageSubscriber = (message: Message) => void

export const useWebSocketStore = defineStore('websocket', () => {
  // 状态
  const connectionStatus = ref<ConnectionStatus>(ConnectionStatus.DISCONNECTED)
  // 当前连接用户
  const user = ref<User | null>(null)
  // 连接错误信息
  const connectionError = ref<string | null>(null)

  // 消息订阅者列表
  const messageSubscribers = ref<Set<MessageSubscriber>>(new Set())

  // 消息分发函数
  const distributeMessage = (message: Message) => {
    logService.log('收到 WebSocket 消息:', message)

    // 通知所有订阅者
    messageSubscribers.value.forEach((subscriber) => {
      try {
        subscriber(message)
      } catch (error: any) {
        logService.error('消息订阅者处理失败:', error)
      }
    })
  }

  // 订阅消息
  const subscribeMessage = (callback: MessageSubscriber) => {
    messageSubscribers.value.add(callback)
    logService.log('添加消息订阅者，当前订阅者数量:', messageSubscribers.value.size)

    // 返回取消订阅函数
    return () => {
      messageSubscribers.value.delete(callback)
      logService.log('移除消息订阅者，当前订阅者数量:', messageSubscribers.value.size)
    }
  }

  // 初始化 WebSocket 连接
  const initConnection = async (u: User) => {
    if (connectionStatus.value === ConnectionStatus.CONNECTED && user.value?.uid === u.uid) {
      logService.log('WebSocket 已连接，跳过重复初始化')
      return
    }

    try {
      connectionStatus.value = ConnectionStatus.CONNECTING
      connectionError.value = null
      user.value = u

      logService.log('初始化 WebSocket 连接:' + u.uid)

      await websocketService.init(u.uid, distributeMessage)

      connectionStatus.value = ConnectionStatus.CONNECTED
      logService.log('WebSocket 连接成功')
    } catch (error: any) {
      logService.error('WebSocket 连接失败:', error)
      connectionError.value = error instanceof Error ? error.message : '连接失败'
      connectionStatus.value = ConnectionStatus.DISCONNECTED
    } finally {
      logService.log('WebSocket 连接状态:', connectionStatus.value)
    }
  }

  // 断开连接
  const disconnect = () => {
    try {
      logService.log('断开 WebSocket 连接')
      websocketService.disconnect()
      connectionStatus.value = ConnectionStatus.DISCONNECTED
      user.value = null
      connectionError.value = null
      // 清空所有订阅者
      messageSubscribers.value.clear()
    } catch (error: any) {
      logService.error('断开 WebSocket 连接失败:', error)
    }
  }

  // 发送消息
  const sendMessage = (message: Message) => {
    if (connectionStatus.value !== ConnectionStatus.CONNECTED) {
      throw new Error('WebSocket 未连接')
    }
    return websocketService.sendMessage(message)
  }

  // 重连
  const reconnect = () => {
    logService.log('1s后 重连 WebSocket 连接')
    if (user.value) {
      const currentUser = user.value
      disconnect()

      setTimeout(() => {
        initConnection(currentUser)
      }, 1000)
    }
  }

  // 清理连接状态
  const cleanup = () => {
    logService.log('清理 WebSocket 连接状态')

    disconnect()
    connectionStatus.value = ConnectionStatus.DISCONNECTED
    connectionError.value = null
  }

  return {
    // 状态
    user,
    connectionError,
    connectionStatus,

    // 方法
    initConnection,
    disconnect,
    sendMessage,
    reconnect,
    cleanup,
    subscribeMessage,
  }
})
