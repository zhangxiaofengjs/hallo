<template>
  <h-chat-panel :user="user" @send-message="handleSendMessage" />
</template>

<script lang="ts" setup>
  import { ref, watch } from 'vue'
  import { useRoute } from 'vue-router'
  import HChatPanel from '@/components/ChatPanelComponent.vue'
  import { useWebSocketStore } from '@/stores/websocket'
  import type { Message } from '@/types/message'
  import type { User } from '@/types/user'
  import userService from '@/services/userService'
  import errorService from '@/utils/logService'

  const route = useRoute()
  const websocketStore = useWebSocketStore()

  const user = ref<User>()

  const selectedContact = ref<User | null>(null)

  // 存储每个联系人的消息
  const contactMessages = ref<Record<string, Message[]>>({})

  /**
   * 当路由参数变化时，重新选择联系人
   */
  watch(
    () => [route.params.uid, route.params.type],
    () => {
      // 取得用户信息
      userService
        .getUser(route.params.uid as string, route.params.type as string)
        .then((res) => {
          user.value = res
        })
        .catch((err) => {
          errorService.error(err)
        })
    },
    { immediate: true }
  )

  /**
   * 处理发送消息
   */
  const handleSendMessage = (message: Message): void => {
    try {
      // 通过 WebSocket Store 发送消息
      websocketStore.sendMessage(message)
    } catch (error) {
      console.error('发送消息失败:', error)
      errorService.error('发送消息失败，请检查网络连接')
    }
  }
</script>
