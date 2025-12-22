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
        <button class="share-btn" >
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

    <!-- 错误状态 -->
    <div v-else-if="error" class="error-state">
      <div class="error-content">
        <div class="error-icon">⚠️</div>
        <h3 class="error-tilte">加载失败</h3>
        <p class="error-message">{{ error }}</p>
        <button class="retry-btn" >重新加载</button>
      </div>
    </div>

    <!-- 主要内容 - 垂直滚动布局 -->
    <div v-else-if="dish" class="content-scroll">
      <!-- 菜品图片区域 -->
      <div class="image-section">
        <div class="image-wrapper">
          <img
              v-if="dish.imgUrl"
              :src="`data:image/jpeg;base64,${dish.imgUrl}`"
              :alt="dish.dishName"
              class="main-image"

          />
        </div>
      </div>
    </div>
  </div>

</template>

<script setup lang="ts">

import { useRouter, useRoute } from "vue-router";
import { ShareAltOutlined } from "@ant-design/icons-vue";
import { dishItem } from "@/types/dish";
import {onMounted, ref} from "vue";
import {getDishById} from "@/api/dish";

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const error = ref<string | null>(null)
const dish = ref<dishItem | null>(null)

const loadDishDetail = async(id) =>{
  loading.value = true
  error.value = null

  try{
    const result = await getDishById(id)
    dish.value = result.data
    console.log("获取菜品成功",dish.value)
  }catch(error){
    console.log("菜品获取失败",error)
    error.value="加载菜品详情失败，请稍后重试"
  }finally {
    loading.value=false
  }
}
const goBack =()=>{
  router.back();
}
const zoomImage = {}
const retry = {}
const shareDish = {}
onMounted(() =>{
  const dishId = route.params.id;
  console.log('当前路由参数', route.params)
  console.log("传入的id",id)
  if(dishId){
    const id = Number(dishId)
    loadDishDetail(id)
  }
})
</script>

<style></style>