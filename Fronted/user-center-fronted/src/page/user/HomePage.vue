
<template>
  <div id="homePage">
<!--    菜品列表-->
    <div v-if="loading">加載中...</div>
    <div v-else-if="dishes.length === 0" class="empty">暫無菜品數據</div>
    <div v-else class="dishes-container">
      <div v-for="(dish,index) in dishes" :key="dish.id" class="dish-item">

      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
//ref 让基本类型变成响应式的
import { ref } from 'vue';
import {dishItem} from "@/types/dish";

//定義dishes為dishItem列表類型
const dishes = ref<dishItem[]>([]);
const loading = ref(false);
</script>

<style scoped>

</style>
