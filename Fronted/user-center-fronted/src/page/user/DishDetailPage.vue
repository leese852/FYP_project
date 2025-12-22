<template>
  <div class="dish-detail">
    <div class="btn-back" @click="goBack">
      返回首页
    </div>

    <div v-if="dish" class="dish-content">
      <div class="dish-image">
        <img v-if="dish?.imgUrl"
             :src="`data:image/jpeg;base64,${dish.imgUrl}`"
             :alt="dish.dishName"
             class="dish-img"
        />
        <div v-else class="empty-image">
          <img src="@/assets/logo.png" alt="默认图片" class="default-image" />
        </div>
      </div>
      <div class="dish-info">
        <div class="dish-name">{{dish.dishName}}</div>
        <div class="dish-price">${{dish.price}}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

import {ref,onMounted} from "vue";
import {dishItem} from "@/types/dish";
import { useRoute, useRouter } from 'vue-router'
import {getDishById} from "@/api/dish";

const route = useRoute()
const router = useRouter()

const dish = ref<dishItem>()
const loadDishDetail = async (id: number) =>{
  console.log('🚀 开始加载菜品详情，ID:', id);
  try{
    const result = await getDishById(id)
    dish.value = result.data;
    console.log('获取菜品详情成功:', result);
  }catch(error:any){
    console.log('获取菜品详情失败:', error)
  }
}

const goBack = () => {
  window.history.back()
}
onMounted(() => {
  console.log('当前路由参数',route.params)
  console.log('🆔 获取到的菜品ID:', route.params.id);
  const dishId = route.params.id;
  if (dishId) {
    const id = Number(dishId)
    loadDishDetail(id);
  }

})
</script>

<style scoped>

</style>