<template>
  <v-card flat class="pa-2 d-flex align-center">
    <v-avatar size="48" class="mr-3">
      <v-img :src="user?.avatar" :alt="user?.nickname" />
    </v-avatar>
    <div class="flex-grow-1">
      <div class="text-h6 text-left">{{ user?.nickname }}</div>
      <div v-if="user?.status !== undefined" class="text-caption text-medium-emphasis text-left">
        今天心情不错
      </div>
    </div>
    <v-btn icon="mdi-menu-down" size="small" variant="text"></v-btn>
  </v-card>
</template>

<script lang="ts" name="UserInfoComponent" setup>
  import { useLoginUserStore } from '@/stores/loginUser'

  // 用户
  const { user } = useLoginUserStore()
</script>

<style lang="less" scoped>
  @import '@styles/variables.less';
</style>
