import { createRouter, createWebHistory } from "vue-router";
import MainLayout from "@/components/MainLayout.vue";

const routes = [
  {
    path: "/",
    name: "Main",
    component: MainLayout,
    children: [
      {
        path: "",
        name: "ContactList",
        components: {
          contactList: () => import("@/views/ContactListView.vue"),
          chat: () => import("@/views/ChatWindowView.vue"),
        },
      },
      {
        path: "chat/:contactId",
        name: "Chat",
        components: {
          contactList: () => import("@/views/ContactListView.vue"),
          chat: () => import("@/views/ChatWindowView.vue"),
        },
        props: true,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
