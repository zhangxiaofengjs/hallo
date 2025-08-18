<template>
  <div class="main-layout">
    <!-- 左侧联系人区域 -->
    <div class="contact-side">
      <!-- 用户信息 -->
      <UserInfo :user="currentUser" />
      <!-- 联系人列表 -->
      <ContactList :contacts="contacts" />
    </div>

    <!-- 右侧聊天区域 -->
    <div class="chat-side" v-if="selectedContact">
      <ChatWindow :contact="selectedContact" :messages="contactMessages[selectedContact.id] || []"
        :current-user="currentUser" @send-message="handleSendMessage" />
    </div>
    <div class="chat-side empty" v-else>
      <p>请选择一个联系人开始聊天</p>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import UserInfo from './UserInfo.vue';
import ContactList from './ContactList.vue';
import ChatWindow from './ChatWindow.vue';
import type { Contact, Message } from '@/types/index';
import contactService from '@/services/contactService';
import messageService from '@/services/messageService';

const route = useRoute();
const router = useRouter();

// 模拟当前用户数据
const currentUser = {
  id: '888',
  name: '我',
  avatar: '/icons/3.png',
  status: '在线'
};

// 联系人列表
const contacts = ref<Contact[]>([]);

// 获取联系人列表
const fetchContacts = async () => {
  try {
    contacts.value = await contactService.getContacts();

    // 如果有路由参数，选择对应联系人
    selectContactByRoute();
  } catch (error) {
    console.error('获取联系人列表失败:', error);
    // 可以在这里设置错误状态或显示错误消息
  }
};

// 组件挂载时获取联系人列表
onMounted(() => {
  fetchContacts();
});

const selectedContact = ref<Contact | null>(null);

// 存储每个联系人的消息
const contactMessages = ref<Record<string, Message[]>>({});


// 处理联系人选择
const handleSelectContact = (contact: Contact) => {
  selectedContact.value = contact;
  // 重置未读消息数
  contact.unreadCount = 0;
  // 更新路由
  router.push(`/chat/${contact.id}`);
};

// 根据路由参数选择联系人
const selectContactByRoute = async () => {
  const contactId = route.params.contactId;
  if (contactId) {
    const contact = contacts.value.find(c => c.id === contactId);
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

<style lang="less" scoped>
@import '@styles/variables.less';

.main-layout {
  display: flex;
  height: 100vh;
  width: 100%;
  background-color: @bg-color;

  .contact-side {
    width: 320px;
    height: 100%;
    border-right: 1px solid @border-color;
    display: flex;
    flex-direction: column;
    background-color: #fff;
  }

  .chat-side {
    flex: 1;
    display: flex;
    flex-direction: column;
    background-color: #f5f5f5;

    &.empty {
      display: flex;
      align-items: center;
      justify-content: center;
      color: #999;
    }
  }
}
</style>