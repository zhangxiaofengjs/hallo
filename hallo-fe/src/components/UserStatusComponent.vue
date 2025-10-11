<template>
  <div v-if="!isConnected" class="websocket-status" :class="statusClass">
    <v-icon :icon="statusIcon" color="primary" size="small" />
    <span class="status-text">{{ statusText }}</span>
    <v-btn
      v-if="!isConnected && websocketStore.user"
      @click="websocketStore.reconnect"
      size="x-small"
      variant="text"
      color="primary"
    >
      重连
    </v-btn>
  </div>
</template>

<script setup lang="ts">
  import { computed } from 'vue'
  import { useWebSocketStore } from '@/stores/websocketStore'
  import { ConnectionStatus } from '@/types/websocket'

  const websocketStore = useWebSocketStore()

  const isConnected = computed(() => {
    return websocketStore.connectionStatus === ConnectionStatus.CONNECTED
  })

  const statusClass = computed(() => {
    switch (websocketStore.connectionStatus) {
      case ConnectionStatus.CONNECTED:
        return 'status-connected'
      case ConnectionStatus.CONNECTING:
        return 'status-connecting'
      case ConnectionStatus.DISCONNECTED:
      default:
        return 'error'
    }
  })

  const statusIcon = computed(() => {
    switch (websocketStore.connectionStatus) {
      case ConnectionStatus.CONNECTED:
        return 'mdi-wifi'
      case ConnectionStatus.CONNECTING:
        return 'mdi-wifi-sync'
      case ConnectionStatus.DISCONNECTED:
        return 'mdi-wifi-off'
      default:
        return 'mdi-wifi-off'
    }
  })

  const statusText = computed(() => {
    switch (websocketStore.connectionStatus) {
      case ConnectionStatus.CONNECTED:
        return '已连接'
      case ConnectionStatus.CONNECTING:
        return '连接中...'
      case ConnectionStatus.DISCONNECTED:
        return '未连接'
      default:
        return '未知状态'
    }
  })
</script>

<style scoped lang="less">
  .websocket-status {
    display: flex;
    align-items: center;
    gap: 4px;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 12px;

    &.status-connected {
      background-color: #e8f5e8;
      color: #2e7d32;
    }

    &.status-connecting {
      background-color: #fff3e0;
      color: #f57c00;
    }

    &.status-disconnected {
      background-color: #ffebee;
      color: #d32f2f;
    }

    .status-text {
      font-weight: 500;
    }
  }
</style>
