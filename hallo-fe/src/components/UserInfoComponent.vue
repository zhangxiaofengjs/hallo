<template>
  <v-list pt8 pb0>
    <v-list-item
      :prepend-avatar="user?.avatar"
      :title="user?.name"
      :subtitle="user?.status !== undefined ? '[' + i8nService.text(user?.status) + ']' : ''"
      class="text-left"
    >
      <template v-slot:append>
        <v-btn icon="mdi-menu-down" size="small" variant="text"></v-btn>
      </template>
    </v-list-item>
  </v-list>
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
