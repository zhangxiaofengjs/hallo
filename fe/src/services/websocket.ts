import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

interface Message {
  id: number;
  text: string;
  isSent: boolean;
  time: string;
}

class WebSocketService {
  private client: Client;
  private subscriptions: { [key: string]: any } = {};
  private messageHandlers: ((message: Message) => void)[] = [];
  private isInitialized: boolean = false;

  constructor() {
    this.client = new Client({
      // 使用webSocketFactory而不是brokerURL
      webSocketFactory: () => new SockJS("http://localhost:8080/hallo/ws"),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: function (str) {
        console.log("STOMP: " + str);
      },
    });

    this.client.onConnect = () => {
      console.log("WebSocket connected");
      this.isInitialized = true;
      // 重新订阅所有订阅
      Object.keys(this.subscriptions).forEach((destination) => {
        this.unsubscribe(destination);
        this.subscribe(destination);
      });
    };

    this.client.onStompError = (frame) => {
      console.error("Broker reported error: " + frame.headers["message"]);
      console.error("Additional details: " + frame.body);
    };
  }

  connect() {
    console.log("Activating STOMP client...");
    if (!this.client.active) {
      try {
        this.client.activate();
      } catch (error) {
        console.error("Error activating STOMP client:", error);
      }
    } else {
      console.log("STOMP client already active");
    }
  }

  disconnect() {
    console.log("Deactivating STOMP client...");
    if (this.client.active) {
      this.client.deactivate();
      this.isInitialized = false;
    }
  }

  // 检查连接状态的辅助方法
  isConnected(): boolean {
    return this.client.connected;
  }

  subscribe(destination: string) {
    // 如果客户端已连接，立即订阅
    if (this.client.connected && !this.subscriptions[destination]) {
      console.log(`Subscribing to ${destination}`);
      this.subscriptions[destination] = this.client.subscribe(
        destination,
        this.handleMessage.bind(this)
      );
    }
    // 如果客户端未连接，记录订阅请求，等待连接后自动订阅
    else if (!this.client.connected) {
      console.log(`Queuing subscription to ${destination} until connected`);
      // 只记录目标，实际订阅将在连接时进行
      this.subscriptions[destination] = null;
    }
  }

  unsubscribe(destination: string) {
    if (this.subscriptions[destination]) {
      if (this.client.connected && this.subscriptions[destination] !== null) {
        console.log(`Unsubscribing from ${destination}`);
        this.subscriptions[destination].unsubscribe();
      }
      delete this.subscriptions[destination];
    }
  }

  sendMessage(destination: string, message: any) {
    if (this.client.connected) {
      console.log(`Sending message to ${destination}`, message);
      this.client.publish({
        destination,
        body: JSON.stringify(message),
      });
    } else {
      console.warn(`Cannot send message: not connected to WebSocket server`);
      // 可以在这里添加重试逻辑或将消息加入队列
    }
  }

  addMessageHandler(handler: (message: Message) => void) {
    this.messageHandlers.push(handler);
  }

  removeMessageHandler(handler: (message: Message) => void) {
    this.messageHandlers = this.messageHandlers.filter((h) => h !== handler);
  }

  private handleMessage(message: any) {
    try {
      console.log("Received message:", message);
      const parsedMessage = JSON.parse(message.body);

      // 转换为应用中使用的消息格式
      const appMessage: Message = {
        id: parsedMessage.id || Date.now(),
        text: parsedMessage.text || parsedMessage.content || "",
        isSent: parsedMessage.senderId === "currentUser", // 根据发送者ID判断是否是自己发送的
        time: new Date(
          parsedMessage.timestamp || Date.now()
        ).toLocaleTimeString(),
      };

      this.messageHandlers.forEach((handler) => handler(appMessage));
    } catch (error) {
      console.error("Error handling message:", error);
      console.error("Message body:", message.body);
    }
  }
}

export default new WebSocketService();
