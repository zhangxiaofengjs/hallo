<template>
  <Teleport to="body">
    <v-dialog
      v-model="visible"
      persistent
      max-width="500"
      :close-delay="closeDelay"
      :close-on-content-click="false"
      @click:outside="handleOverlayClick"
    >
      <v-card>
        <v-card-title class="d-flex align-center bg-error">
          <v-icon icon="mdi-alert-circle" color="white" class="mr-2" />
          <span class="text-white">错误提示</span>
          <v-spacer />
          <v-btn icon variant="text" color="white" @click="close">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text class="pt-4">
          <p class="text-body-1 mb-4">{{ message }}</p>
        </v-card-text>

        <v-card-actions>
          <v-spacer />
          <v-btn color="error" variant="flat" @click="close">
            关闭{{ autoClose ? '(' + closeCountDown + ')' : '' }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </Teleport>
</template>

<script lang="ts" setup>
  import { ref, watch } from 'vue'

  interface Props {
    autoClose?: boolean
    closeDelay?: number
  }

  const props = withDefaults(defineProps<Props>(), {
    autoClose: false,
    closeDelay: 5000,
  })

  const visible = ref<boolean>(false)
  const message = ref<string>('')
  const closeCountDown = ref<number>(0)

  let autoCloseTimer: number | null = null
  let countDownTimer: number | null = null

  const showError = (msg: string) => {
    message.value = msg
    visible.value = true
  }

  defineExpose({
    showError,
  })

  const close = () => {
    if (autoCloseTimer) {
      clearTimeout(autoCloseTimer)
      autoCloseTimer = null
    }
    if (countDownTimer) {
      clearInterval(countDownTimer)
      countDownTimer = null
    }
    visible.value = false
    closeCountDown.value = 0
  }

  const handleOverlayClick = () => {
    //close()
  }

  // 监听 visible 变化，处理自动关闭
  watch(
    () => visible.value,
    (newVisible) => {
      if (newVisible && props.autoClose) {
        closeCountDown.value = Math.ceil(props.closeDelay / 1000)

        // 设置倒计时更新定时器，每隔1秒更新一次
        countDownTimer = setInterval(() => {
          closeCountDown.value--
          if (closeCountDown.value <= 0) {
            if (countDownTimer) {
              clearInterval(countDownTimer)
              countDownTimer = null
            }
          }
        }, 1000)

        // 设置自动关闭定时器
        autoCloseTimer = setTimeout(() => {
          close()
        }, props.closeDelay)
      } else if (!newVisible) {
        if (autoCloseTimer) {
          clearTimeout(autoCloseTimer)
          autoCloseTimer = null
        }
        if (countDownTimer) {
          clearInterval(countDownTimer)
          countDownTimer = null
        }
      }
    }
  )
</script>

<style scoped></style>
