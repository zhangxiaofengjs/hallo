<template>
  <v-app>
    <v-navigation-drawer permanent width="280">
      <v-container fluid class="h-100 pa-0 d-flex flex-column">
        <v-row no-gutters>
          <v-col cols="12">
            <UserInfo :user="currentUser" />
            <v-divider></v-divider>
            <v-text-field
              v-model="searchQuery"
              class="pt-0 pl-2 pb-2"
              label="搜索联系人"
              placeholder="输入姓名或昵称或者email"
              prepend-inner-icon="mdi-magnify"
              color="primary"
              variant="plain"
              hide-details
              clearable
              density="compact"
              single-line
            ></v-text-field>
            <v-divider></v-divider>
          </v-col>
        </v-row>
        <v-row no-gutters class="flex-grow-1 overflow-y-auto">
          <v-col cols="12">
            <router-view name="contactList" />
          </v-col>
        </v-row>
      </v-container>
    </v-navigation-drawer>

    <v-main class="chat-side">
      <router-view name="chat" />
      <v-container v-if="!$route.params.contactId" class="empty">
        <p>请选择一个联系人开始聊天</p>
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import UserInfo from "./UserInfo.vue";

const searchQuery = ref("");

// 模拟当前用户数据
const currentUser = {
  id: "888",
  name: "我",
  avatar: "/icons/3.png",
  status: "在线",
};
</script>

<style lang="less" scoped>
@import "@styles/variables.less";
</style>
