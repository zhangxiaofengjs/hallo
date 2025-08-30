<template>
  <div class="chat-panel">
    <!-- 聊天头部 -->
    <div class="chat-header">
      <div class="contact-info">
        <img :src="contact.avatar" alt="{{ contact.name }}" class="avatar" />
        <div class="contact-details">
          <h3>{{ contact.nickname }}</h3>
          <p class="status">在线</p>
        </div>
      </div>
      <div class="actions">
        <button class="action-btn">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
          </svg>
        </button>
        <button class="action-btn">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <polygon
              points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"
            ></polygon>
          </svg>
        </button>
        <button class="action-btn">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <circle cx="12" cy="12" r="10"></circle>
            <line x1="2" y1="12" x2="22" y2="12"></line>
            <path
              d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"
            ></path>
          </svg>
        </button>
      </div>
    </div>

    <!-- 消息列表 -->
    <div class="message-list" ref="messageList" @scroll="saveScrollPosition">
      <div
        v-for="message in messages"
        :key="message.id"
        :class="['message-item', message.fromId === '' ? 'sent' : 'received']"
      >
        <div class="avatar-container">
          <img :src="contact.avatar" alt="{{ contact.name }}" class="avatar-small" />
        </div>
        <div class="message-content">
          <p>{{ message.content }}</p>
          <span class="time">{{ message.time }}</span>
        </div>
      </div>
    </div>

    <!-- 消息输入区 -->
    <div class="message-input">
      <div class="input-actions">
        <button class="action-btn">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"></path>
            <polyline points="14 2 14 8 20 8"></polyline>
            <line x1="16" y1="13" x2="8" y2="13"></line>
            <line x1="16" y1="17" x2="8" y2="17"></line>
            <polyline points="10 9 9 9 8 9"></polyline>
          </svg>
        </button>
        <button class="action-btn">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M18 8h1a4 4 0 0 1 0 8h-1"></path>
            <path d="M2 8h16v9a4 4 0 0 1-4 4H6a4 4 0 0 1-4-4V8z"></path>
            <line x1="6" y1="1" x2="6" y2="4"></line>
            <line x1="10" y1="1" x2="10" y2="4"></line>
            <line x1="14" y1="1" x2="14" y2="4"></line>
          </svg>
        </button>
        <button class="action-btn">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
          </svg>
        </button>
      </div>
      <input
        type="text"
        v-model="currentMessageInput"
        placeholder="输入消息..."
        class="input-field"
        @keyup.enter="sendMessage"
      />
      <button class="send-btn" @click="sendMessage">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="20"
          height="20"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <line x1="22" y1="2" x2="11" y2="13"></line>
          <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
        </svg>
      </button>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { ref, watch, onMounted, computed, nextTick } from 'vue'
  import type { Contact, Message } from '@/types'

  const props = defineProps<{
    contact: Contact
    messages: Message[]
    currentUser: {
      id: string
      name: string
      avatar: string
      status: string
    }
  }>()

  // 为每个联系人维护独立的输入框状态
  const messageInputs = ref<Record<string, string>>({})

  // 存储每个联系人的滚动位置
  const contactScrollPositions = ref<Record<string, number>>({})

  const emit = defineEmits<{
    (e: 'send-message', content: string): void
  }>()

  const messageList = ref<HTMLDivElement | null>(null)

  // 获取当前联系人的输入框内容
  const currentMessageInput = computed({
    get: () => messageInputs.value[props.contact.id] || '',
    set: (value) => {
      messageInputs.value[props.contact.id] = value
    },
  })

  // 监听消息变化，自动滚动到底部
  watch(
    () => props.messages.length,
    () => {
      // 使用nextTick确保DOM更新后再滚动到底部
      nextTick(() => {
        scrollToBottom()
      })
    }
  )

  // 组件挂载后恢复滚动位置或滚动到底部
  onMounted(() => {
    restoreScrollPosition()
  })

  // 保存滚动位置
  const saveScrollPosition = () => {
    if (messageList.value) {
      contactScrollPositions.value[props.contact.id] = messageList.value.scrollTop
    }
  }

  // 恢复滚动位置
  const restoreScrollPosition = () => {
    if (messageList.value) {
      const savedPosition = contactScrollPositions.value[props.contact.id]
      if (savedPosition !== undefined) {
        messageList.value.scrollTop = savedPosition
      } else {
        // 使用nextTick确保DOM更新后再滚动到底部
        nextTick(() => {
          scrollToBottom()
        })
      }
    }
  }

  // 滚动到底部
  const scrollToBottom = () => {
    if (messageList.value) {
      // 考虑padding的影响，确保完全滚动到底部
      messageList.value.scrollTop = messageList.value.scrollHeight - messageList.value.clientHeight
    }
  }

  // 监听联系人变化，保存当前滚动位置并恢复新联系人的滚动位置
  watch(
    () => props.contact.id,
    (_, oldId) => {
      if (messageList.value) {
        // 保存旧联系人的滚动位置
        if (oldId !== undefined) {
          contactScrollPositions.value[oldId] = messageList.value.scrollTop
        }
        // 恢复新联系人的滚动位置
        restoreScrollPosition()
      }
    }
  )

  // 发送消息
  const sendMessage = () => {
    if (currentMessageInput.value.trim()) {
      emit('send-message', currentMessageInput.value.trim())
      currentMessageInput.value = ''
      // 使用nextTick确保DOM更新后再滚动到底部
      nextTick(() => {
        scrollToBottom()
      })
    }
  }
</script>

<style lang="less" scoped>
  @import '@styles/variables.less';

  .chat-panel {
    display: flex;
    flex-direction: column;
    height: 100%;
    width: 100%;

    .chat-header {
      display: flex;
      align-items: center;
      padding: 12px 16px;
      border-bottom: 1px solid @border-color;
      background-color: #fff;
      justify-content: space-between;

      .contact-info {
        display: flex;
        align-items: center;

        .avatar {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          object-fit: cover;
        }

        .contact-details {
          margin-left: 12px;

          h3 {
            margin: 0;
            font-size: 16px;
            font-weight: 600;
            color: @text-color;
          }

          .status {
            margin: 0;
            font-size: 12px;
            color: @secondary-text-color;
          }
        }
      }

      .actions {
        display: flex;

        .action-btn {
          background: none;
          border: none;
          cursor: pointer;
          color: @text-color;
          padding: 8px;
          margin-left: 5px;
          border-radius: 50%;

          &:hover {
            background-color: rgba(0, 0, 0, 0.05);
          }
        }
      }
    }

    .message-list {
      flex: 1;
      overflow-y: auto;
      padding: 16px;
      background-color: #f5f5f5;

      .message-item {
        display: flex;
        margin-bottom: 12px;
        max-width: 70%;
        animation: fadeIn 0.3s ease;

        .avatar-container {
          margin-right: 8px;
        }

        .avatar-small {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          object-fit: cover;
        }

        .message-content {
          position: relative;
          padding: 10px 12px;
          border-radius: 18px;
          font-size: 14px;
          word-break: break-word;
          background-color: @primary-color;
          color: @text-color;
          border-radius: 4px;

          p {
            margin: 0;
          }

          .time {
            display: block;
            font-size: 10px;
            color: @secondary-text-color;
            text-align: right;
            margin-top: 3px;
          }
        }
      }
    }

    .message-input {
      display: flex;
      align-items: center;
      padding: 10px 16px;
      border-top: 1px solid @border-color;
      background-color: #fff;

      .input-actions {
        display: flex;
        margin-right: 10px;

        .action-btn {
          background: none;
          border: none;
          cursor: pointer;
          color: @text-color;
          padding: 8px;
          border-radius: 50%;

          &:hover {
            background-color: rgba(0, 0, 0, 0.05);
          }
        }
      }

      .input-field {
        flex: 1;
        padding: 10px 16px;
        border: 1px solid @border-color;
        border-radius: 20px;
        font-size: 14px;
        outline: none;

        &:focus {
          border-color: @primary-color;
        }
      }

      .send-btn {
        margin-left: 10px;
        background-color: @primary-color;
        color: white;
        border: none;
        border-radius: 50%;
        width: 40px;
        height: 40px;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;

        &:hover {
          background-color: @primary-dark-color;
        }
      }
    }
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(10px);
    }

    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
</style>
