<template>
  <v-container fluid class="h-100 pa-0 d-flex flex-column">
    <!-- 聊天头部 -->
    <v-card flat :loading="isLoadingMessage" class="d-flex flex-column h-100">
      <v-card-title class="pa-2 d-flex align-center flex-grow-0 flex-shrink-0">
        <v-avatar size="48" class="mr-3" color="grey-lighten-1">
          <v-img :src="props.user?.avatar" :alt="props.user?.nickname" />
        </v-avatar>
        <div class="flex-grow-1">
          <div class="text-h6 text-left text-truncate">{{ props.user?.nickname }}</div>
          <div
            v-if="props.user?.status !== undefined"
            class="text-caption text-medium-emphasis text-left"
          >
            [{{ i8nService.text(props.user?.status) }}]
          </div>
        </div>
        <v-btn icon="mdi-web" variant="text" size="small" />
      </v-card-title>
      <v-divider></v-divider>
      <!-- 消息列表 -->
      <v-card-text
        ref="messageListContainerRef"
        class="pa-0 overflow-y-auto flex-1-1-0 h-scrollbar"
      >
        <v-list class="pa-0">
          <v-list-item class="pl-2 pr-2 pt-1 pb-1" v-for="message in messages" :key="message.id">
            <div class="d-flex flex-row">
              <div class="pr-2">
                <v-avatar
                  :color="toolService.getAvatarColor(message.from.uid)"
                  :text="message.from.nickname?.substring(0, 1) || ''"
                  :image="message.from.avatar"
                  class="text-h6"
                ></v-avatar>
              </div>
              <v-card flat class="flex-grow-1">
                <v-card-subtitle class="pl-2 pr-2">
                  <span class="text-body-2 font-weight-bold">
                    {{ message.from.nickname }}
                  </span>
                  <span class="text-caption ml-1 text-grey">{{ message.timestamp }}</span>
                  <v-icon
                    v-if="message.sendStatus !== undefined"
                    :icon="
                      message.sendStatus === SendStatus.SENDING
                        ? 'mdi-sync'
                        : message.sendStatus === SendStatus.SEND_SUCCESS
                          ? 'mdi-check-circle'
                          : 'mdi-close-circle'
                    "
                    :class="{ rotating: message.sendStatus === SendStatus.SENDING }"
                    color="grey-lighten-1"
                    size="small"
                  />
                </v-card-subtitle>
                <v-card-text class="pt-1 pl-2 pr-2 pb-1">
                  <div class="text-body-2">{{ message.content }}</div>
                </v-card-text>
                <v-card-actions class="message-action">
                  <v-btn
                    icon="mdi-reply"
                    density="compact"
                    size="small"
                    variant="plain"
                    color="grey-lighten-1"
                  />
                  <v-btn
                    icon="mdi-emoticon-plus"
                    density="compact"
                    size="small"
                    variant="plain"
                    color="grey-lighten-1"
                  />
                </v-card-actions>
              </v-card>
            </div>
          </v-list-item>
        </v-list>
      </v-card-text>
      <v-divider></v-divider>
      <!-- 消息输入区 -->
      <v-card-actions class="flex-grow-0 flex-shrink-0">
        <div class="d-flex align-center w-100">
          <v-text-field
            v-model="messageInput"
            placeholder="输入消息..."
            variant="outlined"
            density="compact"
            hide-details
            class="flex-grow-1"
            @keyup.enter="sendMessage"
          />
          <v-btn :icon="'mdi-send'" color="primary" class="ml-0" @click="sendMessage" />
        </div>
      </v-card-actions>
    </v-card>
  </v-container>
  <h-msg-dialog ref="msgDlgRef"></h-msg-dialog>
</template>

<script lang="ts" setup>
  import { ref, watch, onMounted, onUnmounted, computed, nextTick } from 'vue'
  import { type User } from '@/types/user'
  import i8nService from '@/utils/i8nService'
  import errorService from '@/utils/logService'
  import messageService from '@/services/messageService'
  import { SendStatus, type Message } from '@/types/message'
  import toolService from '@/utils/toolService'
  import { useLoginUserStore } from '@/stores/loginUserStore'
  import { useWebSocketStore } from '@/stores/websocketStore'
  import type { VCardText } from 'vuetify/components'
  import HMsgDialog from '@/components/MsgDialogComponent.vue'

  const props = defineProps<{
    user: User | undefined
  }>()

  const { user: loginUser } = useLoginUserStore()
  const websocketStore = useWebSocketStore()
  const msgDlgRef = ref<InstanceType<typeof HMsgDialog>>()

  // 当前的消息列表
  const messages = ref<Message[]>([])

  // 取消订阅函数
  let unsubscribeMessage: (() => void) | null = null

  // 是否正在加载消息
  const isLoadingMessage = ref<boolean>(false)

  // 滚动区域
  const messageListContainerRef = ref<VCardText>()

  // 获取当前联系人的输入框内容
  const messageInput = computed({
    get: () => {
      return props.user?.uid ? messageInputs.value[props.user?.uid] : ''
    },
    set: (value: string) => {
      if (props.user?.uid) {
        messageInputs.value[props.user?.uid] = value
      }
    },
  })

  // 为每个联系人维护独立的输入框状态
  const messageInputs = ref<Record<string, string>>({})

  // 发送消息
  const sendMessage = async () => {
    const content = messageInput.value.trim()
    if (!content) {
      return
    }

    const message: Message = {
      id: 0,
      uid: toolService.getUuid(),
      from: loginUser!,
      to: props.user!,
      content: content,
      timestamp: new Date().toISOString().slice(0, 19).replace('T', ' '),
      sendStatus: SendStatus.SENDING,
    }

    try {
      messages.value.push(message)

      await websocketStore.sendMessage(message)

      messageInput.value = ''
      scrollToBottom()
    } catch (error) {
      // 发送失败，更新状态并显示错误
      message.sendStatus = SendStatus.SEND_FAIL
      errorService.error(error)
    }
  }

  //监听当前交谈对象是否变化，重新初始化页面
  watch(
    () => [props.user],
    async () => {
      if (props.user?.uid) {
        isLoadingMessage.value = true

        // 取得消息列表
        try {
          messages.value = await messageService.getLoginUserMessages(
            props.user?.uid,
            props.user?.type
          )

          isLoadingMessage.value = false

          scrollToBottom(false)
        } catch (error: any) {
          msgDlgRef.value?.showError(error)
          isLoadingMessage.value = false
        }
      }
    },
    {
      immediate: true,
    }
  )
  // 处理收到的 WebSocket 消息
  const handleReceivedMessage = (message: Message) => {
    // 只处理与当前聊天对象相关的消息
    if (
      (props.user && message.from.uid === props.user.uid && message.to.uid === loginUser?.uid) ||
      (message.from.uid === loginUser?.uid && message.to.uid === props.user?.uid)
    ) {
      // 检查消息是否已存在（避免重复添加）
      const existingMessage = messages.value.find((m) => m.uid === message.uid)
      if (existingMessage) {
        // 更新消息ID和时间戳
        existingMessage.id = message.id
        existingMessage.timestamp = message.timestamp
        existingMessage.sendStatus = SendStatus.SEND_SUCCESS
      } else {
        messages.value.push(message)

        // 自动滚动到底部
        scrollToBottom()
      }
    }
  }

  // 组件挂载后恢复滚动位置或滚动到底部，并订阅消息
  onMounted(() => {
    // 订阅 WebSocket 消息
    unsubscribeMessage = websocketStore.subscribeMessage(handleReceivedMessage)
  })

  onUnmounted(() => {
    // 组件卸载时取消订阅
    if (unsubscribeMessage) {
      unsubscribeMessage()
      unsubscribeMessage = null
    }
  })

  // 滚动到底部
  const scrollToBottom = (animate: boolean = true) => {
    nextTick(() => {
      if (messageListContainerRef.value) {
        // 直接操作v-list元素，它的父容器v-card-text才是真正的滚动容器
        const scrollContainer: HTMLDivElement = messageListContainerRef.value.$el
        if (scrollContainer) {
          scrollContainer.scrollTo({
            top: scrollContainer.scrollHeight,
            behavior: animate ? 'smooth' : 'auto',
          })
        }
      }
    })
  }
</script>

<style scoped>
  .message-action {
    min-height: 12px;
    max-height: 12px;
  }

  .rotating {
    animation: rotate 1.5s linear infinite;
  }

  @keyframes rotate {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }
</style>
