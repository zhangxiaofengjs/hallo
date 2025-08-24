<template>
  <v-list
    :lines="false"
    density="comfortable"
    class="text-left"
    style="max-width: 280px"
  >
    <v-list-subheader>联系人</v-list-subheader>
    <v-list-item
      v-for="item in filteredContacts"
      :key="item.id"
      @click="selectContact(item)"
      :value="item"
      color="primary"
      :prepend-avatar="item.avatar"
    >
      <template v-slot:prepend>
        <v-badge bordered location="top right" color="error" content="999">
          <v-avatar color="grey-lighten-1" :image="item.avatar"> </v-avatar
        ></v-badge>
      </template>
      <v-list-item-title v-text="item.name"></v-list-item-title>
      <v-list-item-subtitle
        v-text="item.lastMessageTime + ' ' + item.lastMessage"
        class="text-truncate d-block"
      ></v-list-item-subtitle>
      <template v-slot:append>
        <v-btn
          color="grey-lighten-1"
          icon="mdi-information"
          variant="text"
        ></v-btn>
      </template>
    </v-list-item>
  </v-list>
</template>

<script lang="ts" setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import type { Contact } from "@/types";

const router = useRouter();

const props = defineProps<{
  contacts: Contact[];
}>();

const searchQuery = ref("");
const selectedContactId = ref<string | null>(null);

// 过滤联系人
const filteredContacts = computed(() => {
  if (!searchQuery.value) {
    return props.contacts;
  }
  return props.contacts.filter((contact) =>
    contact.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 选择联系人
const selectContact = (contact: Contact) => {
  selectedContactId.value = contact.id;
  router.push(`/chat/${contact.id}`);
};
</script>

<style lang="less" scoped>
@import "@styles/variables.less";
</style>
