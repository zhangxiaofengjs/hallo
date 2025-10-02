<template>
  <v-container fluid class="h-100 pa-0 d-flex flex-column">
    <!-- 聊天头部 -->
    <v-card flat class="pa-2 d-flex align-center flex-grow-0 flex-shrink-0">
      <v-avatar size="48" class="mr-3" color="grey-lighten-1">
        <v-img :src="user?.avatar" :alt="user?.nickname" />
      </v-avatar>
      <div class="flex-grow-1">
        <div class="text-h6 text-left text-truncate">{{ user?.nickname }}</div>
        <div v-if="user?.status !== undefined" class="text-caption text-medium-emphasis text-left">
          [{{ i8nService.text(user?.status) }}]
        </div>
      </div>
      <v-btn icon="mdi-web" variant="text" size="small" />
    </v-card>
    <v-divider></v-divider>
    <v-card class="d-flex flex-column" height="100%" elevation="0">
      <!-- 消息列表 -->
      <v-card-text
        ref="messageList"
        @scroll="saveScrollPosition"
        class="flex-grow-1 pa-4 overflow-y-auto"
      >
        <v-list v-for="message in messages" :key="message.id" class="message-item mb-3">
          <v-list-item class="d-flex align-start">
            <v-avatar size="32" :class="'ml-2'">
              <v-img :src="message.from.avatar" :alt="message.from.nickname" />
            </v-avatar>

            <v-card :color="'primary'" elevation="1" rounded="lg" class="message-bubble">
              <v-card-text class="pa-3">
                <div class="text-body-2">{{ message.content }}</div>
                <div class="text-caption mt-1" :class="'text-grey'" style="text-align: right">
                  {{ message.timestamp }}
                </div>
              </v-card-text>
            </v-card>
          </v-list-item>
        </v-list>
      </v-card-text>

      <!-- 消息输入区 -->
      <v-card-actions class="message-input pa-4" style="border-top: 1px solid #e0e0e0">
        <div class="d-flex align-center w-100">
          <div class="d-flex mr-3">
            <v-btn icon="mdi-file-document-outline" variant="text" size="small" class="mr-1" />
            <v-btn icon="mdi-gift-outline" variant="text" size="small" class="mr-1" />
            <v-btn icon="mdi-message-outline" variant="text" size="small" />
          </div>

          <v-text-field
            v-model="currentMessageInput"
            placeholder="输入消息..."
            variant="outlined"
            density="compact"
            hide-details
            class="flex-grow-1"
            @keyup.enter="sendMessage"
            rounded
          />

          <v-btn
            icon="mdi-send"
            color="primary"
            class="ml-3"
            @click="sendMessage"
            :disabled="!currentMessageInput.trim()"
          />
        </div>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script lang="ts" setup>
  import { ref, watch, onMounted, computed, nextTick } from 'vue'
  import type { User } from '@/types/user'
  import i8nService from '@/utils/i8nService'
  import userService from '@/services/userService'
  import errorService from '@/utils/errorService'
  import messageService from '@/services/messageService'
  import type { Message } from '@/types/message'

  const props = defineProps<{
    uid: string
  }>()

  // 当前交谈对象
  const user = ref<User>()

  // 当前的消息列表
  const messages = ref<Message[]>([])

  // 为每个联系人维护独立的输入框状态
  const messageInputs = ref<Record<string, string>>({})

  // 存储每个联系人的滚动位置
  const contactScrollPositions = ref<Record<string, number>>({})

  const emit = defineEmits<{
    (e: 'send-message', content: string): void
  }>()

  const messageList = ref<HTMLDivElement | null>(null)

  // 获取当前联系人的输入框内容
  const currentMessageInput = computed({
    get: () => {
      return user.value?.uid ? messageInputs.value[user.value.uid] : ''
    },
    set: (value: string) => {
      if (user.value?.uid) {
        messageInputs.value[user.value.uid] = value
      }
    },
  })

  watch(
    () => [props.uid],
    () => {
      // 取得用户信息
      userService
        .getUser(props.uid)
        .then((res) => {
          user.value = res
        })
        .catch((err) => {
          errorService.error(err)
        })

      // 取得消息
      messageService
        .getLoginUserMessages(props.uid)
        .then((res) => {
          messages.value = res
        })
        .catch((err) => {
          errorService.error(err)
        })
    }
  )
  // 监听消息变化，自动滚动到底部
  // watch(
  //   () => props.messages.length,
  //   () => {
  //     // 使用nextTick确保DOM更新后再滚动到底部
  //     nextTick(() => {
  //       scrollToBottom()
  //     })
  //   }
  // )

  // 组件挂载后恢复滚动位置或滚动到底部
  onMounted(() => {
    restoreScrollPosition()
  })

  // 保存滚动位置
  const saveScrollPosition = () => {
    if (messageList.value) {
      // contactScrollPositions.value[props.uid] = messageList.value.scrollTop
    }
  }

  // 恢复滚动位置
  const restoreScrollPosition = () => {
    if (messageList.value) {
      // const savedPosition = contactScrollPositions.value[props.uid]
      // if (savedPosition !== undefined) {
      //   messageList.value.scrollTop = savedPosition
      // } else {
      //   // 使用nextTick确保DOM更新后再滚动到底部
      //   nextTick(() => {
      //     scrollToBottom()
      //   })
      // }
    }
  }

  // 滚动到底部
  const scrollToBottom = () => {
    if (messageList.value) {
      // 考虑padding的影响，确保完全滚动到底部
      messageList.value.scrollTop = messageList.value.scrollHeight - messageList.value.clientHeight
    }
  }

  // 监听联系人变化，保存当前滚动位置并恢复新联系人的滚动位置
  // watch(
  //   () => props.contact.uid,
  //   (_, oldId) => {
  //     if (messageList.value) {
  //       // 保存旧联系人的滚动位置
  //       if (oldId !== undefined) {
  //         contactScrollPositions.value[oldId] = messageList.value.scrollTop
  //       }
  //       // 恢复新联系人的滚动位置
  //       restoreScrollPosition()
  //     }
  //   }
  // )

  // 发送消息
  const sendMessage = () => {
    if (currentMessageInput.value.trim()) {
      emit('send-message', currentMessageInput.value.trim())
      currentMessageInput.value = ''
      // 使用nextTick确保DOM更新后再滚动到底部
      nextTick(() => {
        scrollToBottom()
      })
    }
  }
</script>

<style scoped>
  .message-item {
    display: flex;
    width: 100%;
  }

  .message-item.justify-end {
    justify-content: flex-end;
  }

  .message-bubble {
    max-width: 100%;
    word-break: break-word;
  }

  .message-list {
    height: calc(100vh - 200px);
  }

  /* 消息动画效果 */
  .message-item {
    animation: fadeIn 0.3s ease;
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(10px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
</style>
