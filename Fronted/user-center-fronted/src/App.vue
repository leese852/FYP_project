<template>
  <div class="app">
    <BasicLayout />
  </div>
</template>

<style></style>
<script setup lang="ts">
import BasicLayout from "@/layout/BasicLayout.vue";
import { useLoginUserStore } from "@/store/useLoginUserStore";

//编写脚本默认获取用户信息
// const loginUserStore = useLoginUserStore();
// loginUserStore.fetchLoginUser();
</script>
