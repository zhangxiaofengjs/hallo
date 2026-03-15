<template>
  <v-app>
    <v-navigation-drawer permanent width="280">
      <v-container fluid class="h-100 pa-0 d-flex flex-column">
        <h-user-info class="flex-grow-0 flex-shrink-0" />
        <h-user-status class="flex-grow-0 flex-shrink-0 ma-2" />
        <v-divider></v-divider>
        <router-view name="contactList"></router-view>
      </v-container>
    </v-navigation-drawer>

    <v-main>
      <router-view name="chat" />
    </v-main>
  </v-app>
</template>

<script lang="ts" setup>
  import HUserInfo from './UserInfoComponent.vue'
  import HUserStatus from './UserStatusComponent.vue'
</script>

<style lang="less" scoped>
  @import '@styles/variables.less';
</style>
