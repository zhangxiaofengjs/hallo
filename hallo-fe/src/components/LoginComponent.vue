<template>
  <v-card class="login-card">
    <v-card-title class="text-center">登录</v-card-title>
    <v-card-text>
      <v-form @submit.prevent="handleLogin">
        <v-text-field v-model="form.username" label="用户名" required outlined></v-text-field>
        <v-text-field
          v-model="form.password"
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
  import { ref } from 'vue'

  const form = ref({
    username: '',
    password: '',
  })

  const handleLogin = () => {
    console.log('登录信息:', form.value)
    // 这里可以添加实际的登录逻辑，例如调用 API
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
