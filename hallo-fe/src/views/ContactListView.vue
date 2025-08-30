<template>
  <h-contact-list :contactGroups="contactGroups" :filterText="filterText" />
  <v-container v-if="contactGroups.length === 0" class="empty">Loading...</v-container>
</template>

<script lang="ts" setup>
  import { ref, onMounted } from 'vue'
  import HContactList from '@/components/ContactListComponent.vue'
  import type { ContactGroup } from '@/types/index'
  import contactService from '@/services/contactService'

  // 联系人列表
  const contactGroups = ref<ContactGroup[]>([])

  defineProps<{
    filterText?: string // 联系人搜索框的过滤文本
  }>()

  // 获取联系人列表
  const fetchContacts = async () => {
    try {
      contactGroups.value = await contactService.getContacts()
    } catch (error) {
      console.error('获取联系人列表失败:', error)
    }
  }

  // 组件挂载时获取联系人列表
  onMounted(() => {
    fetchContacts()
  })
</script>
