<template>
  <v-list>
    <v-list-item
      :prepend-avatar="user.avatar"
      :subtitle="user.status"
      :title="user.name"
      class="text-left"
    >
      <template v-slot:append>
        <v-btn icon="mdi-menu-down" size="small" variant="text"></v-btn>
      </template>
    </v-list-item>
  </v-list>
</template>

<script lang="ts" name="UserXInfo" setup>
  // 假设 User 类型定义在项目内，尝试根据常见目录结构调整路径
  import type { User } from '@/types/index'

  defineOptions({
    name: 'HUserInfo', // 标签名变为 <custom-name>
  })

  // 模拟当前登录用户数据
  const props = defineProps<{
    user: User
  }>()
</script>

<style lang="less" scoped>
  @import '@styles/variables.less';
</style>
