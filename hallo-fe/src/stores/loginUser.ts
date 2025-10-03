import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User } from '@/types/user'

export const useLoginUserStore = defineStore('login-user', () => {
  const KEY_LOGIN_USER = 'login-user'

  // 状态
  const user = ref<User>()

  // 操作方法
  const setUser = (u: User) => {
    user.value = u
    // 保存到本地存储
    localStorage.setItem(KEY_LOGIN_USER, JSON.stringify(u))
  }

  const clearUser = () => {
    if (user.value) {
      user.value = undefined
    }
    // 清除本地存储
    localStorage.removeItem(KEY_LOGIN_USER)
  }

  // 从本地存储恢复用户信息
  const restoreUser = () => {
    const savedUser = localStorage.getItem(KEY_LOGIN_USER)
    if (savedUser) {
      try {
        user.value = JSON.parse(savedUser)
      } catch (error) {
        console.error('Failed to restore user from localStorage:', error)
        clearUser()
      }
    }
  }

  // 初始化时恢复用户信息
  restoreUser()

  return {
    // 状态
    user,
    // 方法
    setUser,
    clearUser,
    restoreUser,
  }
})
