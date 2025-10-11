<template>
  <v-text-field
    v-model="searchKeyword"
    class="pt-0 pl-2 pb-2 flex-grow-0 flex-shrink-0"
    label="查找联系人"
    placeholder="输入姓名、昵称或邮件"
    prepend-inner-icon="mdi-magnify"
    color="primary"
    variant="plain"
    hide-details
    clearable
    density="compact"
    single-line
  ></v-text-field>
  <v-divider></v-divider>
  <v-list
    :lines="false"
    density="comfortable"
    class="text-left flex-grow-1 overflow-auto h-scrollbar"
  >
    <template v-for="userGroup in filteredUserGroups" :key="userGroup.type">
      <v-list-subheader>
        {{ i8nService.text(userGroup.type) }}
      </v-list-subheader>
      <v-list-item
        v-for="user in userGroup.users"
        :key="user.uid"
        @click="handleContactClick(user)"
        :value="user"
        color="primary"
      >
        <template v-slot:prepend>
          <div class="d-flex align-center">
            <!-- 状态指示器 -->
            <v-badge
              dot
              :color="getStatusColor(user)"
              location="bottom right"
              offset-x="6"
              offset-y="0"
            >
              <v-avatar
                :color="colorService.getAvatarColor(user.uid)"
                :text="user.nickname?.substring(0, 1)"
                :image="user.avatar"
                class="mr-2 text-h6"
              ></v-avatar>
            </v-badge>
          </div>
        </template>
        <v-list-item-title v-text="user.nickname" class="text-truncate"></v-list-item-title>
        <v-list-item-subtitle
          v-text="user.nickname + ' ' + user.nickname"
          class="text-truncate d-block"
        ></v-list-item-subtitle>
        <template v-slot:append>
          <v-badge
            v-if="user.unread"
            bordered
            location="top right"
            color="error"
            class="mr-2"
            :content="user.unread"
          ></v-badge>
        </template>
      </v-list-item>
    </template>
  </v-list>
  <h-msg-dialog ref="msgDlgRef"></h-msg-dialog>
</template>

<script lang="ts" setup>
  import userService from '@/services/userService'
  import type { User, UserGroup } from '@/types/user'
  import { UserStatus, UserType } from '@/types/user'
  import colorService from '@/utils/toolService'
  import i8nService from '@/utils/i8nService'
  import { computed, onMounted, ref } from 'vue'
  import { useRouter } from 'vue-router'
  import HMsgDialog from '@/components/MsgDialogComponent.vue'

  const router = useRouter()
  const searchKeyword = ref('')

  const userGroups = ref<UserGroup[]>([])
  const msgDlgRef = ref<InstanceType<typeof HMsgDialog>>()

  onMounted(async () => {
    try {
      // 查询当前用户信息
      userGroups.value = await userService.getLoginUserGroups()
    } catch (error: any) {
      msgDlgRef.value?.showError(error)
    }
  })

  /**
   *  过滤联系人和群组
   * @returns
   */
  const filteredUserGroups = computed(() => {
    if (!searchKeyword.value) {
      return userGroups.value
    }

    //返回过滤好的联系人和群组
    const filter = searchKeyword.value.toLowerCase()
    return userGroups.value.map((group) => ({
      ...group,
      users: group.users.filter(
        (u) =>
          u.nickname?.toLowerCase().includes(filter) ||
          u.account?.toLowerCase().includes(filter) ||
          u.mail?.toLowerCase().includes(filter)
      ),
    }))
  })

  /**
   * 取得状态指示的小圆点颜色
   * @param user
   */
  const getStatusColor = (user: User) => {
    if (user.type === UserType.GROUP) {
      return '#00000000'
    }

    switch (user.status) {
      case UserStatus.ONLINE:
        return 'success'
      case UserStatus.AWAY:
        return 'warning'
      case UserStatus.BUSY:
        return 'error'
      case UserStatus.OFFLINE:
      default:
        return '#a1a1a1'
    }
  }

  /**
   * 选择联系人时的动作
   * @param user
   */
  const handleContactClick = (user: User) => {
    //跳转到聊天页面，通过state传递type参数
    router.push({
      path: `/chat/${user.type}/${user.uid}`,
    })
  }
</script>

<style lang="less" scoped>
  @import '@styles/variables.less';
</style>
