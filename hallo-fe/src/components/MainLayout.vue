<template>
  <div class="main-layout">
    <!-- 左侧联系人区域 -->
    <div class="contact-side">
      <!-- 用户信息 -->
      <UserInfo :user="currentUser" />
      <!-- 联系人列表 -->
      <router-view name="contactList" />
    </div>

    <!-- 右侧聊天区域 -->
    <div class="chat-side">
      <router-view name="chat" />
      <div class="chat-side empty" v-if="!$route.params.contactId">
        <p>请选择一个联系人开始聊天</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';
import UserInfo from './UserInfo.vue';

// 模拟当前用户数据
const currentUser = {
  id: '888',
  name: '我',
  avatar: '/icons/3.png',
  status: '在线'
};

const route = useRoute();
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