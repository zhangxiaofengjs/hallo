<template>
  <h-chat-panel v-if="currentUid" :uid="currentUid" @send-message="handleSendMessage" />
</template>

<script lang="ts" setup>
  import { ref, watch, onMounted, onUnmounted } from 'vue'
  import { useRoute } from 'vue-router'
  import HChatPanel from '@/components/ChatPanelComponent.vue'
  import websocketService from '@/utils/websocketService'
  import type { Message } from '@/types/message'
  import type { User } from '@/types/user'

  const route = useRoute()

  // 模拟当前用户数据
  const currentUser = {
    id: '888',
    name: '我',
    avatar: '/icons/3.png',
    status: '在线',
  }

  // 初始化WebSocket连接
  onMounted(() => {
    console.log('初始化WebSocket连接 onMounted')
    websocketService.init(currentUser.id, (message: Message) => {
      // 处理接收到的新消息
      if (selectedContact.value) {
        // 将新消息添加到对应联系人的消息列表中
        if (!contactMessages.value[selectedContact.value.uid]) {
          contactMessages.value[selectedContact.value.uid] = []
        }
        contactMessages.value[selectedContact.value.uid].push(message)

        // 更新联系人的最后消息和时间
        // selectedContact.value.lastMessage = message.content
        // selectedContact.value.lastMessageTime = message.time

        // 如果消息不是来自当前聊天的联系人，则增加未读计数
        if (message.from !== selectedContact.value.uid) {
          // selectedContact.value.unreadCount = (selectedContact.value.unreadCount || 0) + 1
        }
      }
    })
  })

  // 组件卸载时断开WebSocket连接
  onUnmounted(() => {
    console.log('初始化WebSocket连接 onUnmounted')
    websocketService.disconnect()
  })

  const currentUid = ref<string>('')
  const selectedContact = ref<User | null>(null)

  // 存储每个联系人的消息
  const contactMessages = ref<Record<string, Message[]>>({})

  // 根据路由参数选择联系人
  const handleContactSelectChanged = async () => {
    currentUid.value = route.params.uid as string
    // if (selectedUid.value) {
    //   // 模拟获取联系人信息
    //   const mockContact: Contact = {
    //     uid: uid,
    //     account: `user${uid}`,
    //     nickname: `联系人${uid}`,
    //     mail: `user${uid}@example.com`,
    //     avatar: '/icons/1.png',
    //     type: UserType.FRIEND,
    //     status: UserStatus.ONLINE,
    //     unread: 0,
    //   }
    //   selectedContact.value = mockContact

    //   // 初始化该联系人的消息列表（如果还没有的话）
    //   if (!contactMessages.value[uid]) {
    //     contactMessages.value[uid] = []
    //   }
    // } else {
    //   selectedContact.value = null
    // }
  }

  /**
   * 当路由参数变化时，重新选择联系人
   */
  watch(
    () => route.params.uid,
    () => {
      handleContactSelectChanged()
    },
    { immediate: true }
  )

  // 处理发送消息
  const handleSendMessage = (content: string) => {
    if (selectedContact.value) {
      const newMessage: Message = {
        id: 1,
        from: currentUser.id,
        to: selectedContact.value.uid,
        content,
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      }

      // 通过WebSocket发送消息
      websocketService.sendMessage(newMessage)

      // 将消息添加到本地消息列表以实现即时显示
      if (!contactMessages.value[selectedContact.value.uid]) {
        contactMessages.value[selectedContact.value.uid] = []
      }
      contactMessages.value[selectedContact.value.uid].push(newMessage)
      // selectedContact.value.lastMessage = content
      // selectedContact.value.lastMessageTime = newMessage.time
    }
  }
</script>
