<template>
  <div class="no-contact-container">
    <img src="/logo.png" alt="Hallo Logo" class="logo" />
    <h1 class="welcome-message">欢迎使用 Hallo</h1>
    <p class="current-date">{{ formattedDate }}</p>
  </div>
</template>

<script lang="ts" setup>
  import { computed } from 'vue'

  const formattedDate = computed(() => {
    const today = new Date()
    const year = today.getFullYear()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    return `${year}年${month}月${day}日`
  })
</script>

<style scoped>
  .logo {
    width: 120px;
    height: 120px;
    margin-bottom: 24px;
    object-fit: contain;
  }
  .no-contact-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    text-align: center;
  }

  .welcome-message {
    font-size: 24px;
    margin-bottom: 16px;
    color: var(--primary-color);
  }

  .current-date {
    font-size: 16px;
    color: var(--secondary-text-color);
  }
</style>
