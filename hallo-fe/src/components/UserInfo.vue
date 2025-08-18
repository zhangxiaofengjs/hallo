<template>
  <div class="user-info">
    <img :src="user.avatar" alt="用户头像" class="avatar">
    <div class="user-details">
      <h3>{{ user.name }}</h3>
      <p class="status">{{ user.status }}</p>
    </div>
    <button class="settings-btn">
      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none"
        stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="12" cy="12" r="3"></circle>
        <path
          d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z">
        </path>
      </svg>
    </button>
  </div>
</template>

<script lang="ts" setup>
// 假设 User 类型定义在项目内，尝试根据常见目录结构调整路径
import type { User } from '@/types/index';

// 模拟当前登录用户数据
const props = defineProps<{
  user: User;
}>();
</script>

<style lang="less" scoped>
@import '@styles/variables.less';

.user-info {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid @border-color;
  background-color: #f5f5f5;

  .avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    object-fit: cover;
  }

  .user-details {
    margin-left: 12px;
    flex: 1;

    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: @text-color;
    }

    .status {
      margin: 0;
      font-size: 12px;
      color: @secondary-text-color;
    }
  }

  .settings-btn {
    background: none;
    border: none;
    cursor: pointer;
    color: @text-color;
    padding: 5px;
    border-radius: 50%;

    &:hover {
      background-color: rgba(0, 0, 0, 0.05);
    }
  }
}
</style>