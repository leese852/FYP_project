
<template>

  <div id="homePage">
    <!-- 加载状态 -->
    <div v-if="loading">加載中...</div>
    <!-- 空状态 -->
    <div v-else-if="dishes.length === 0" class="empty">暫無菜品數據</div>
    <!--    菜品列表-->
    <div v-else class="dishes-container">
      <!-- 菜品网格 -->

      <div v-for="(dish,index) in dishes"
           :key="dish.id"
           class="dish-item"
           @click="goToDetail(dish.id)"
      >

        <div class="dish-image">
          <img v-if="dish.imgUrl" :src="`data:image/jpeg;base64,${dish.imgUrl}`" :alt="dish.dishName" class="dish-img"/>
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

  </div>
</template>

<script setup lang="ts">
//ref 让基本类型变成响应式的
import {onMounted, ref} from 'vue';
import {dishItem} from "@/types/dish";
import {getDishList} from "@/api/dish";
import router from "@/router";

//定義dishes為dishItem列表類型
const dishes = ref<dishItem[]>([]);
const loading = ref(false);

const loadAllDishes = async () => {
  loading.value = true;
  try{
    const result = await getDishList();
    dishes.value = result.data;
    console.log('获取菜品成功:', result);

  }catch (error:any){
    console.error('获取菜品失败:', error);
  }finally {
    loading.value = false;
  }
};

const goToDetail = (dishId: number) => {
  console.log("跳转的菜品id", dishId)
  router.push({
    path: `user/dish/${dishId}`
  })
}

onMounted(() => {
  loadAllDishes();
})
</script>

<style scoped>
#homePage{
  padding: 16px;
}
.dishes-container{
  display: grid;
  griD-template-columns: repeat(4, 1fr);
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
}
</style>
