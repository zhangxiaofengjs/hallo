<template>
  <v-card class="login-card">
    <v-card-title class="text-center">登录</v-card-title>
    <v-card-text>
      <v-form @submit.prevent="handleLogin">
        <v-text-field v-model="username" label="用户名" required outlined></v-text-field>
        <v-text-field
          v-model="password"
          label="密码"
          type="password"
          required
          outlined
        ></v-text-field>
        <v-btn type="submit" color="primary" block>登录</v-btn>
      </v-form>
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup name="LoginComponent">
  import loginService from '@/services/loginService'
  import { useLoginUserStore } from '@/stores/loginUserStore'
  import logService from '@/utils/logService'
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'

  const loginUserStore = useLoginUserStore()
  const router = useRouter()
  const username = ref<string>('')
  const password = ref<string>('')

  // 登录按钮点击事件处理函数
  const handleLogin = async () => {
    try {
      logService.log('登录信息:', { username: username.value, password: password.value })

      const response = await loginService.login(username.value, password.value)

      logService.log('登录成功:', response)

      // 处理登录成功后的逻辑
      loginUserStore.setLoginToken(response.token)
      loginUserStore.setUser(response.user)

      // 跳转到主页
      router.push('/')
    } catch (error: any) {
      logService.error('登录失败:', error)

      // 处理登录失败后的逻辑
      alert('登录失败: ' + error.message)
    }
  }
</script>
<style lang="less" scoped>
  @import '@styles/variables.less';

  .login-card {
    max-width: 400px;
    margin: 2rem auto;
    padding: 1rem;
  }
</style>
