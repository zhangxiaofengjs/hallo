<template>
  <h-chat-panel :user="user" @send-message="handleSendMessage" />
</template>

<script lang="ts" setup>
  import { ref, watch } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import HChatPanel from '@/components/ChatPanelComponent.vue'
  import { useWebSocketStore } from '@/stores/websocketStore'
  import type { Message } from '@/types/message'
  import type { User } from '@/types/user'
  import userService from '@/services/userService'
  import errorService from '@/utils/logService'

  const route = useRoute()
  const router = useRouter()
  const websocketStore = useWebSocketStore()

  const user = ref<User>()

  /**
   * 当路由参数变化时，重新选择联系人
   */
  watch(
    () => [route.params.uid, route.params.type],
    async () => {
      try {
        // 检查路由参数是否完整
        const routeParams = route.params as Record<string, string>
        if (!routeParams.uid || !routeParams.type) {
          // 没有参数就跳转到主页
          router.push('/')
        }

        const res = await userService.getUser(routeParams.uid, routeParams.type)
        user.value = res
      } catch (err) {
        errorService.error(err, true, '获取用户信息失败')
      }
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
