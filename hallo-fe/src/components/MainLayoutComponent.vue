<template>
  <v-app>
    <v-navigation-drawer permanent width="280">
      <v-container fluid class="h-100 pa-0 d-flex flex-column">
        <v-row no-gutters>
          <v-col cols="12">
            <h-user-info :user="currentUser" />
            <v-divider></v-divider>
            <v-text-field
              v-model="filterText"
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
        <v-row no-gutters class="overflow-y-auto">
          <v-col cols="12">
            <router-view name="contactList" v-slot="{ Component }">
        <component :is="Component" :filterText="filterText" />
      </router-view>
          </v-col>
        </v-row>
      </v-container>
    </v-navigation-drawer>

    <v-main class="chat-side">
      <router-view name="chat" />
      <v-container v-if="!$route.params.contactId" class="empty">
        <h-home-page />
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts" setup>
  import { ref } from 'vue'
  import HUserInfo from './UserInfoComponent.vue'
  import HHomePage from './HomePageComponent.vue'

  const filterText = ref('')

  // 模拟当前用户数据
  const currentUser = {
    id: '888',
    name: '我',
    avatar: '/icons/3.png',
    status: '在线',
  }
</script>

<style lang="less" scoped>
  @import '@styles/variables.less';
</style>
