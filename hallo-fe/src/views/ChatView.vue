<template>
  <h-chat-panel :user="user" />
</template>

<script lang="ts" setup>
  import { ref, watch } from 'vue'
  import { useRouter } from 'vue-router'
  import HChatPanel from '@/components/ChatPanelComponent.vue'
  import type { User } from '@/types/user'
  import userService from '@/services/userService'
  import errorService from '@/utils/logService'

  const props = defineProps<{
    type: string
    uid: string
  }>()

  const router = useRouter()

  const user = ref<User>()

  /**
   * 当路由参数变化时，重新选择联系人
   */
  watch(
    () => [props.uid, props.type],
    async () => {
      try {
        // 检查路由参数是否完整
        if (!props.uid || !props.type) {
          // 没有参数就跳转到主页
          router.push('/')
        }

        const res = await userService.getUser(props.uid, props.type)
        user.value = res
      } catch (err) {
        errorService.error(err, true, '获取用户信息失败')
      }
    },
    { immediate: true }
  )
</script>
