<template>
  <h-chat-panel
    v-if="selectedContact"
    :contact="selectedContact"
    :messages="contactMessages[selectedContact.id] || []"
    :current-user="currentUser"
    @send-message="handleSendMessage"
  />
</template>

<script lang="ts" setup>
  import { ref, watch, onMounted, onUnmounted } from 'vue'
  import { useRoute } from 'vue-router'
  import HChatPanel from '@/components/ChatPanelComponent.vue'
  import type { Contact, Message } from '@/types/index'
  import contactService from '@/services/contactService'
  import messageService from '@/services/messageService'
  import websocketService from '@/utils/websocketService'

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
    websocketService.init(currentUser.id, (message: Message) => {
      // 处理接收到的新消息
      if (selectedContact.value) {
        // 将新消息添加到对应联系人的消息列表中
        if (!contactMessages.value[selectedContact.value.id]) {
          contactMessages.value[selectedContact.value.id] = []
        }
        contactMessages.value[selectedContact.value.id].push(message)

        // 更新联系人的最后消息和时间
        // selectedContact.value.lastMessage = message.content
        // selectedContact.value.lastMessageTime = message.time

        // 如果消息不是来自当前聊天的联系人，则增加未读计数
        if (message.fromId !== selectedContact.value.id) {
          // selectedContact.value.unreadCount = (selectedContact.value.unreadCount || 0) + 1
        }
      }
    })
  })

  // 组件卸载时断开WebSocket连接
  onUnmounted(() => {
    websocketService.disconnect()
  })

  // 监听路由变化
  watch(
    () => route.params.contactId,
    () => {
      handleContactSelectChanged()
    },
    { immediate: false }
  )

  const selectedContact = ref<Contact | null>(null)

  // 存储每个联系人的消息
  const contactMessages = ref<Record<string, Message[]>>({})

  // 根据路由参数选择联系人
  const handleContactSelectChanged = async () => {
    const contactId = route.params.contactId
    if (contactId) {
      // 获取联系人信息
      const contacts = await contactService.getContacts()
      // const contact = contacts.find((c) => c.id === contactId)
      // if (contact) {
      // selectedContact.value = contact
      // 重置未读消息数
      // contact.unreadCount = 0

      // 初始化每个联系人的消息
      // const messages: Message[] = await messageService.getMessagesByContactId(contact.id)
      // contactMessages.value[contact.id] = [...messages]
      // }
    }
  }

  // 处理发送消息
  const handleSendMessage = (content: string) => {
    if (selectedContact.value) {
      const newMessage: Message = {
        id: Date.now().toString(),
        fromId: currentUser.id,
        toId: selectedContact.value.id,
        content,
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      }

      // 通过WebSocket发送消息
      websocketService.sendMessage(newMessage)

      // 将消息添加到本地消息列表以实现即时显示
      if (!contactMessages.value[selectedContact.value.id]) {
        contactMessages.value[selectedContact.value.id] = []
      }
      contactMessages.value[selectedContact.value.id].push(newMessage)
      // selectedContact.value.lastMessage = content
      // selectedContact.value.lastMessageTime = newMessage.time
    }
  }
</script>
