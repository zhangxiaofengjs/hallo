import { createRouter, createWebHistory } from 'vue-router';
import MainLayout from '@/components/MainLayout.vue';

const routes = [
  {
    path: '/',
    name: 'Main',
    component: MainLayout,
  },
  {
    path: '/chat/:contactId',
    name: 'Chat',
    component: MainLayout,
    props: true,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;