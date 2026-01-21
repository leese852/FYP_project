<template>

  <div class="app">
    <!--  wdnmd，嵌套渲染 真史山代码-->
    <!--    <BasicLayout />-->

    <!-- 根据路由决定是否显示布局 -->
<!--    <router-view v-slot="{ Component }">-->
<!--      <component :is="Component" />-->
<!--    </router-view>-->
    <router-view>

    </router-view>
  </div>

</template>

<style></style>
<script setup lang="ts">
import BasicLayout from "@/layout/BasicLayout.vue";
import { useLoginUserStore } from "@/store/useLoginUserStore";
import {onMounted} from "vue";

// // 核心就三件事：
// // 1. 有个地方记住用户（Pinia Store）
// const currentUser = ref(null);
//
// // 2. 有方法能查询用户（调用API）
// async function checkLogin() {
//   const res = await getCurrentUser();
//   currentUser.value = res.data.data; // 有就是已登录，null就是未登录
// }
//
// // 3. 应用启动时查询一次（App.vue）
// onMounted(() => checkLogin());
// //编写脚本默认获取用户信息

// 应用启动时查询一次 避免刷新調用戶信息：》 很絕望
const loginUserStore = useLoginUserStore();
// loginUserStore.fetchLoginUser();
onMounted(() => loginUserStore.fetchLoginUser());
</script>
