<template>
  <v-app>
    <v-navigation-drawer permanent width="280">
      <v-container fluid class="h-100 pa-0 d-flex flex-column">
        <v-row class="no-gutters flex-grow-0">
          <v-col cols="12">
            <h-user-info />
            <v-divider></v-divider>
          </v-col>
        </v-row>
        <v-row no-gutters class="overflow-y-auto">
          <v-col cols="12">
            <router-view name="contactList"></router-view>
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
  import HUserInfo from './UserInfoComponent.vue'
  import HHomePage from './HomePageComponent.vue'
</script>

<style lang="less" scoped>
  @import '@styles/variables.less';
</style>
