<template>
  <ContactList :contacts="contacts" />
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import ContactList from '@/components/ContactList.vue';
import type { Contact } from '@/types/index';
import contactService from '@/services/contactService';

// 联系人列表
const contacts = ref<Contact[]>([]);

// 获取联系人列表
const fetchContacts = async () => {
  try {
    contacts.value = await contactService.getContacts();
  } catch (error) {
    console.error('获取联系人列表失败:', error);
  }
};

// 组件挂载时获取联系人列表
onMounted(() => {
  fetchContacts();
});
</script>