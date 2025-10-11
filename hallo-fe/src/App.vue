<template>
  <RouterView />

  <!-- 全局错误对话框 -->
  <!-- <ErrorDialogComponent
    v-model:visible="errorDialogState.visible"
    :message="errorDialogState.message"
    :details="errorDialogState.details"
    :auto-close="errorDialogState.autoClose"
    :auto-close-delay="errorDialogState.autoCloseDelay"
  /> -->
</template>

<script setup lang="ts">
  import { RouterView, useRouter } from 'vue-router'
  import { onMounted, onUnmounted, watch } from 'vue'
  import { useLoginUserStore } from '@/stores/loginUserStore'
  import { useWebSocketStore } from '@/stores/websocketStore'
  import loginService from './services/loginService'
  import logService from './utils/logService'

  const loginUserStore = useLoginUserStore()
  const websocketStore = useWebSocketStore()
  const router = useRouter()

  // 获取全局错误对话框状态

  // 监听用户登录状态变化
  watch(
    () => loginUserStore.user,
    (newUser, oldUser) => {
      if (newUser && newUser !== oldUser) {
        // 用户登录，初始化 WebSocket 连接
        logService.log('用户登录，初始化 WebSocket 连接')
        websocketStore.initConnection(newUser)
      } else if (!newUser && oldUser) {
        // 用户登出，断开 WebSocket 连接
        logService.log('用户登出，断开 WebSocket 连接')
        websocketStore.disconnect()
        // 跳转到登录页面
        logService.log('跳转到登录页面')
        router.push('/login')
      } else if (!newUser) {
        // 没有用户登录，跳转到登录页面
        logService.log('没有用户登录，跳转到登录页面')
        router.push('/login')
      }
    },
    { immediate: true }
  )

  // 应用启动时检查是否有已登录用户
  onMounted(async () => {
    logService.log('应用启动，检查登录状态')

    let userToken = loginUserStore.loginToken
    if (!userToken) {
      // 没有用户token，跳转到登录页面
      logService.log('应用启动，没有用户token，跳转到登录页面')
      router.push('/login')
      return
    }

    try {
      const response = await loginService.loginWithToken(userToken)

      loginUserStore.setLoginToken(response.token)
      loginUserStore.setUser(response.user)

      logService.log('应用启动，用户登录成功')
    } catch (error: any) {
      logService.error(error)
      // 获取用户信息失败，跳转到登录页面
      logService.log('应用启动，用户登录失败，跳转到登录页面')
      router.push('/login')
    }
  })

  // 应用卸载时清理连接
  onUnmounted(() => {
    logService.log('应用卸载，清理 WebSocket 连接')
    websocketStore.cleanup()
  })
</script>

<style scoped lang="less"></style>
