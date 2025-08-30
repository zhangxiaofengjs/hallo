import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/components/MainLayoutComponent.vue'

const routes = [
  {
    // 登录路由
    path: '/login',
    name: 'login',
    component: () => import('@/views/LoginView.vue'),
  },
  {
    path: '/',
    name: 'main',
    component: MainLayout,
    children: [
      {
        // 默认路由：显示联系人列表和空聊天区域
        path: '',
        name: 'hp',
        components: {
          contactListView: () => import('@/views/ContactListView.vue'),
          chatView: () => import('@/views/ChatView.vue'),
        },
      },

      {
        // 聊天路由：根据联系人 ID 显示聊天内容
        path: 'chat/:contactId',
        name: 'chat',
        components: {
          contactListView: () => import('@/views/ContactListView.vue'),
          chatView: () => import('@/views/ChatView.vue'),
        },
        props: {
          chat: true, // 将路由参数传递给 ChatView.vue
        },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
