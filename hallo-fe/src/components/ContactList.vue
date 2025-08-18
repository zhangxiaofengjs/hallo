<template>
  <div class="contact-list">
    <div class="search-box">
      <input type="text" placeholder="搜索联系人..." v-model="searchQuery" class="search-input" />
    </div>
    <ul class="contacts">
      <li v-for="contact in filteredContacts" :key="contact.id" :class="{ 'active': selectedContactId === contact.id }"
        @click="selectContact(contact)" class="contact-item">
        <div class="contact-avatar">
          <img :src="contact.avatar" alt="{{ contact.name }}" class="avatar">
          <span v-if="contact.unreadCount > 0" class="unread-badge">{{ contact.unreadCount }}</span>
        </div>
        <div class="contact-info">
          <h3 class="contact-name">{{ contact.name }}</h3>
          <p class="last-message">{{ contact.lastMessage }}</p>
        </div>
        <div class="contact-time">
          <span>{{ contact.lastMessageTime }}</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import type { Contact } from '@/types';

const router = useRouter();

const props = defineProps<{
  contacts: Contact[];
}>();



const searchQuery = ref('');
const selectedContactId = ref<string | null>(null);

// 过滤联系人
const filteredContacts = computed(() => {
  if (!searchQuery.value) {
    return props.contacts;
  }
  return props.contacts.filter(contact =>
    contact.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 选择联系人
const selectContact = (contact: Contact) => {
  selectedContactId.value = contact.id;
  router.push(`/chat/${contact.id}`);
};
</script>

<style lang="less" scoped>
@import '@styles/variables.less';

.contact-list {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .search-box {
    padding: 10px;
    border-bottom: 1px solid @border-color;

    .search-input {
      width: 100%;
      padding: 8px 12px;
      border: 1px solid @border-color;
      border-radius: 20px;
      background-color: #f5f5f5;
      font-size: 14px;

      &:focus {
        outline: none;
        border-color: @primary-color;
        background-color: #fff;
      }
    }
  }

  .contacts {
    flex: 1;
    overflow-y: auto;
    padding: 0;
    margin: 0;
    list-style: none;

    .contact-item {
      display: flex;
      align-items: center;
      padding: 12px 16px;
      border-bottom: 1px solid @border-color;
      cursor: pointer;
      transition: background-color 0.2s;

      &:hover {
        background-color: #f5f5f5;
      }

      &.active {
        background-color: #e8f0fe;
      }

      .contact-avatar {
        position: relative;

        .avatar {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          object-fit: cover;
        }

        .unread-badge {
          position: absolute;
          top: -2px;
          right: -2px;
          width: 18px;
          height: 18px;
          background-color: @primary-color;
          color: white;
          border-radius: 50%;
          font-size: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }

      .contact-info {
        margin-left: 12px;
        flex: 1;
        overflow: hidden;

        .contact-name {
          margin: 0;
          font-size: 15px;
          font-weight: 600;
          color: @text-color;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .last-message {
          margin: 0;
          font-size: 13px;
          color: @secondary-text-color;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }

      .contact-time {
        font-size: 12px;
        color: @secondary-text-color;
        white-space: nowrap;
      }
    }
  }
}
</style>