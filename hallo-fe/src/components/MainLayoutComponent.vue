<template>
  <v-app>
    <v-navigation-drawer permanent width="280">
      <v-container fluid class="h-100 pa-0 d-flex flex-column">
        <h-user-info class="flex-grow-0 flex-shrink-0" />
        <v-divider></v-divider>
        <router-view name="contactList"></router-view>
      </v-container>
    </v-navigation-drawer>

    <v-main>
      <router-view name="chat" />
      <v-container v-if="!$route.params.uid" class="empty">
        <h-home-page />
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts" setup>
  import HUserInfo from './UserInfoComponent.vue'
  import HHomePage from './HomePageComponent.vue'
</script>

<style lang="less" scoped>
  @import '@styles/variables.less';

  .v-app {
    width: 100vw !important;
    max-width: none !important;
  }

  .v-main {
    height: 100vh;
    display: flex;
    flex-direction: column;
  }

  .empty {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>
