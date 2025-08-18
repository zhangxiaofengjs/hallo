<template>
  <ChatWindow v-if="selectedContact" :contact="selectedContact" :messages="contactMessages[selectedContact.id] || []"
    :current-user="currentUser" @send-message="handleSendMessage" />
</template>

<script lang="ts" setup>
import { ref, watch, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import ChatWindow from '@/components/ChatWindow.vue';
import type { Contact, Message } from '@/types/index';
import contactService from '@/services/contactService';
import messageService from '@/services/messageService';

const route = useRoute();

// 模拟当前用户数据
const currentUser = {
  id: '888',
  name: '我',
  avatar: '/icons/3.png',
  status: '在线'
};

const selectedContact = ref<Contact | null>(null);

// 存储每个联系人的消息
const contactMessages = ref<Record<string, Message[]>>({});

// 根据路由参数选择联系人
const selectContactByRoute = async () => {
  const contactId = route.params.contactId;
  if (contactId) {
    // 获取联系人信息
    const contacts = await contactService.getContacts();
    const contact = contacts.find(c => c.id === contactId);
    if (contact) {
      selectedContact.value = contact;
      // 重置未读消息数
      contact.unreadCount = 0;

      // 初始化每个联系人的消息
      const messages: Message[] = await messageService.getMessagesByContactId(contact.id);
      contactMessages.value[contact.id] = [...messages];
    }
  }
};

// 监听路由变化
watch(() => route.params.contactId, () => {
  selectContactByRoute();
}, { immediate: true });

// 处理发送消息
const handleSendMessage = (content: string) => {
  if (selectedContact.value) {
    const newMessage: Message = {
      id: Date.now().toString(),
      content,
      senderId: '',
      time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
    };
    // 将消息添加到对应联系人的消息列表中
    if (!contactMessages.value[selectedContact.value.id]) {
      contactMessages.value[selectedContact.value.id] = [];
    }
    contactMessages.value[selectedContact.value.id].push(newMessage);
    selectedContact.value.lastMessage = content;
    selectedContact.value.lastMessageTime = newMessage.time;
  }
};
</script>