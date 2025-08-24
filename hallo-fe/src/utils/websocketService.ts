import SockJS from "sockjs-client";
import { Client, type StompSubscription } from "@stomp/stompjs";
import type { Message } from "../types";

class WebSocketService {
  private stompClient: Client | null = null;
  private subscription: StompSubscription | null = null;
  private topicSubscription: StompSubscription | null = null;
  private userId: string | null = null;
  private onNewMessageCallback: ((message: Message) => void) | null = null;

  // 初始化WebSocket连接
  public init(
    userId: string,
    onNewMessageCallback: (message: Message) => void
  ) {
    this.userId = userId;
    this.onNewMessageCallback = onNewMessageCallback;

    const socket = new SockJS("http://localhost:8000/hallo/ws");
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      debug: function (str) {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    this.stompClient.onConnect = (frame) => {
      console.log("Connected: " + frame);
      // 订阅用户个人队列
      if (this.stompClient && this.userId) {
        this.subscription = this.stompClient.subscribe(
          `/user/${this.userId}/message`,
          (messageOutput) => {
            if (this.onNewMessageCallback) {
              const message = JSON.parse(messageOutput.body) as Message;
              this.onNewMessageCallback(message);
            }
          }
        );
        // 订阅广播消息
        if (this.stompClient) {
          this.topicSubscription = this.stompClient.subscribe(
            "/topic",
            (messageOutput) => {
              if (this.onNewMessageCallback) {
                const message = JSON.parse(messageOutput.body) as Message;
                this.onNewMessageCallback(message);
              }
            }
          );
        }
      }

      this.online(this.userId);
    };

    this.stompClient.onStompError = (frame) => {
      console.log("Broker reported error: " + frame.headers["message"]);
      console.log("Additional details: " + frame.body);
    };

    this.stompClient.activate();
  }

  // 发送消息
  public sendMessage(message: Message) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: "/server/send-message",
        body: JSON.stringify(message),
      });
    } else {
      console.error("STOMP client is not connected");
    }
  }

  public online(userId: string) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: "/server/online",
        body: JSON.stringify({ fromId: userId }),
      });
    } else {
      console.error("STOMP client is not connected");
    }
  }

  // 断开WebSocket连接
  public disconnect() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    if (this.topicSubscription) {
      this.topicSubscription.unsubscribe();
    }
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.stompClient = null;
    }
    console.log("Disconnected");
  }
}

const webSocketService = new WebSocketService();
export default webSocketService;
