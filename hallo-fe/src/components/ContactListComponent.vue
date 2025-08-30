<template>
  <v-list :lines="false" density="comfortable" class="text-left">
    <template v-for="contactGroup in filteredContactGroups" :key="contactGroup.type">
      <v-list-subheader>
        {{ contactGroup.type == ContactGroupType.FRIEND ? '联系人' : '群组' }}
      </v-list-subheader>
      <v-list-item
        v-for="contact in contactGroup.contacts"
        :key="contact.id"
        @click="handleContactClick(contact)"
        :value="contact"
        color="primary"
        :prepend-avatar="contact.avatar"
      >
        <template v-slot:prepend>
          <v-badge bordered location="top right" color="error" content="999">
            <v-avatar color="grey-lighten-1" :image="contact.avatar"></v-avatar>
          </v-badge>
        </template>
        <v-list-item-title v-text="contact.nickname"></v-list-item-title>
        <v-list-item-subtitle
          v-text="contact.nickname + ' ' + contact.nickname"
          class="text-truncate d-block"
        ></v-list-item-subtitle>
        <template v-slot:append>
          <v-btn color="grey-lighten-1" icon="mdi-information" variant="text"></v-btn>
        </template>
      </v-list-item>
    </template>
  </v-list>
</template>

<script lang="ts" setup>
  import { computed } from 'vue'
  import { useRouter } from 'vue-router'
  import type { Contact, ContactGroup } from '@/types'
  import { ContactGroupType } from '@/types'

  const router = useRouter()

  const props = defineProps<{
    contactGroups: ContactGroup[]
    filterText?: string
  }>()

  // 过滤联系人
  const filteredContactGroups = computed(() => {
    if (!props.filterText) {
      return props.contactGroups
    }

    //返回过滤好的联系人和群组
    const filter = props.filterText.toLowerCase()
    return props.contactGroups.map((group) => ({
      ...group,
      contacts: group.contacts.filter(
        (contact) =>
          contact.nickname?.toLowerCase().includes(filter) ||
          contact.account?.toLowerCase().includes(filter) ||
          contact.mail?.toLowerCase().includes(filter)
      ),
    }))
  })

  // 选择联系人
  const handleContactClick = (contact: Contact) => {
    router.push(`/chat/${contact.id}`)
  }
</script>

<style lang="less" scoped>
  @import '@styles/variables.less';
</style>
