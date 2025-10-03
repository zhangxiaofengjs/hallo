import SockJS from 'sockjs-client'
import { Client, type StompSubscription } from '@stomp/stompjs'
import type { Message } from '@/types/message'

class WebSocketService {
  private stompClient: Client | null = null
  private messageSubscription: StompSubscription | null = null
  private topicSubscription: StompSubscription | null = null
  private userUid: string | null = null
  private handleMessage: ((message: Message) => void) | null = null

  // 初始化WebSocket连接
  public init(userUid: string, handleMessage: (message: Message) => void): Promise<void> {
    return new Promise((resolve, reject) => {
      this.userUid = userUid
      this.handleMessage = handleMessage

      const socket = new SockJS('http://localhost:8000/hallo/ws')
      this.stompClient = new Client({
        webSocketFactory: () => socket,
        debug: function (str) {
          console.log(str)
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 5000,
        heartbeatOutgoing: 5000,
      })

      this.stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame)

        // 订阅用户个人队列
        if (this.stompClient && this.userUid) {
          this.messageSubscription = this.stompClient.subscribe(
            `/user/${this.userUid}/message`,
            (messageOutput) => {
              if (this.handleMessage) {
                const message = JSON.parse(messageOutput.body) as Message
                this.handleMessage(message)
              }
            }
          )
          // 订阅广播消息
          if (this.stompClient) {
            this.topicSubscription = this.stompClient.subscribe('/topic', (messageOutput) => {
              if (this.handleMessage) {
                const message = JSON.parse(messageOutput.body) as Message //TODO 广播消息解析为独有格式
                this.handleMessage(message)
              }
            })
          }
        }

        this.online(this.userUid!)
        resolve() // 连接成功时 resolve Promise
      }

      this.stompClient.onStompError = (frame) => {
        console.log('Broker reported error: ' + frame.headers['message'])
        console.log('Additional details: ' + frame.body)
        reject(new Error(`WebSocket connection failed: ${frame.headers['message']}`))
      }

      this.stompClient.activate()
    })
  }

  // 发送消息
  public sendMessage(message: Message) {
    if (this.stompClient && this.stompClient.connected) {
      const body = JSON.stringify(message)
      this.stompClient.publish({
        destination: '/server/send-message',
        body: body,
      })
    } else {
      console.error('STOMP client is not connected')
    }
  }

  public online(userUid: string) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: '/server/online',
        body: JSON.stringify({ fromId: userUid }),
      })
    } else {
      console.error('STOMP client is not connected')
    }
  }

  // 断开WebSocket连接
  public disconnect() {
    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe()
    }
    if (this.topicSubscription) {
      this.topicSubscription.unsubscribe()
    }
    if (this.stompClient) {
      this.stompClient.deactivate()
      this.stompClient = null
    }
    console.log('Disconnected')
  }
}

const webSocketService = new WebSocketService()
export default webSocketService
