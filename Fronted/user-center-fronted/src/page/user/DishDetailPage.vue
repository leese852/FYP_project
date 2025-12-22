<template>
  <div class="dish-detail-container">
    <!-- 顶部导航 -->
    <div class="top-nav">
    <!-- 左边内容-->
      <div class="nav-left" @click="goBack">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M19 12H5M12 19l-7-7 7-7"/>
        </svg>
      </div>

      <div class="nav-title">菜品详情</div>

      <div class="nav-right">
        <button class="share-btn" @click="shareDish">
          <ShareAltOutlined />
        </button>
      </div>
    </div>

<!--    加载状态-->
    <div v-if="loading" class="loading-state">
      <div class="loading-content">
        <div class="spinner"></div>
        <p class="loading-text">正在加载菜品信息..</p>
      </div>
    </div>
  </div>

</template>

<script setup>

import { useRouter, useRoute } from "vue-router";
import { ShareAltOutlined } from "@ant-design/icons-vue";
import {ref} from "vue";

const route = useRoute()
const router = useRouter()
const loading = ref(true)

const goBack = () => {
  router.back()
}
const shareDish = () => {
  console.log("分享菜品")
}
</script>

<style></style>