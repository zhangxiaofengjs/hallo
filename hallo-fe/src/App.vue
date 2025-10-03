<template>
  <RouterView />
</template>

<script setup lang="ts">
  import { RouterView } from 'vue-router'
  import { onMounted, onUnmounted, watch } from 'vue'
  import { useLoginUserStore } from '@/stores/loginUser'
  import { useWebSocketStore } from '@/stores/websocket'
  import userService from './services/userService'
  import logService from './utils/logService'

  const loginUserStore = useLoginUserStore()
  const websocketStore = useWebSocketStore()

  // 监听用户登录状态变化
  watch(
    () => loginUserStore.user,
    (newUser, oldUser) => {
      if (newUser && newUser !== oldUser) {
        // 用户登录，初始化 WebSocket 连接
        console.log('用户登录，初始化 WebSocket 连接')
        websocketStore.initConnection(newUser)
      } else if (!newUser && oldUser) {
        // 用户登出，断开 WebSocket 连接
        console.log('用户登出，断开 WebSocket 连接')
        websocketStore.disconnect()
      }
    },
    { immediate: true }
  )

  // 应用启动时检查是否有已登录用户
  onMounted(() => {
    // 查询当前用户信息
    userService
      .getLoginUser()
      .then((res) => {
        // 将用户信息保存到 Pinia 存储中
        loginUserStore.setUser(res)

        logService.info('应用启动，发现已登录用户，初始化 WebSocket 连接')
        websocketStore.initConnection(res)
      })
      .catch((error) => {
        logService.error(error)
      })
  })

  // 应用卸载时清理连接
  onUnmounted(() => {
    websocketStore.cleanup()
  })
</script>

<style scoped lang="less"></style>
