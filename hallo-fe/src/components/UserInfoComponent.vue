<template>
  <v-card flat class="pa-2 d-flex align-center">
    <v-avatar size="48" class="mr-3">
      <v-img :src="user?.avatar" :alt="user?.nickname" />
    </v-avatar>
    <div class="flex-grow-1">
      <div class="text-h6 text-left">{{ user?.nickname }}</div>
      <div v-if="user?.status !== undefined" class="text-caption text-medium-emphasis text-left">
        [{{ i8nService.text(user?.status) }}]
      </div>
    </div>
    <v-btn icon="mdi-menu-down" size="small" variant="text"></v-btn>
  </v-card>
</template>

<script lang="ts" name="UserInfoComponent" setup>
  import userService from '@/services/userService'
  import type { User } from '@/types/user'
  import { onMounted, ref } from 'vue'
  import errorService from '@/utils/errorService'
  import i8nService from '@/utils/i8nService'

  // 当前登录用户数据
  const user = ref<User | null>(null)

  onMounted(async () => {
    try {
      // 查询当前用户信息
      user.value = await userService.getLoginUser()
    } catch (error: any) {
      errorService.error(error)
    }
  })
</script>

<style lang="less" scoped>
  @import '@styles/variables.less';
</style>
