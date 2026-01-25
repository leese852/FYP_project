<template>
  <div id="homePage">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>加載中...</p>
    </div>

    <!-- 空状态 -->
    <div v-else-if="dishes.length === 0" class="empty-state">
      <div class="empty-icon">🍽️</div>
      <h3>暫無菜品數據</h3>
      <p>目前沒有可用的菜品，請稍後再試</p>
    </div>

    <!-- 菜品列表 -->
    <div v-else class="dishes-container">
      <div
          v-for="dish in dishes"
          :key="dish.id"
          class="dish-card"
          @click="goToDetail(dish.id)"
      >
        <div class="dish-image-container">
          <div class="dish-image">
            <img
                v-if="dish.imgUrl"
                :src="`data:image/jpeg;base64,${dish.imgUrl}`"
                :alt="dish.dishName"
                class="dish-img"
            />
            <div v-else class="empty-image">
              <img
                  src="../../../assets/logo.jpg"
                  alt="默认图片"
                  class="default-image"
              />
            </div>
            <!-- 悬停遮罩效果 -->
            <div class="image-overlay">
              <span class="view-details">查看详情</span>
            </div>
          </div>
        </div>

        <div class="dish-info">
          <h3 class="dish-name">{{ dish.dishName }}</h3>
          <div class="dish-price">${{ dish.price.toFixed(2) }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { dishItem } from "@/types/dish";
import { getDishList } from "@/api/dish";
import router from "@/router";

const dishes = ref<dishItem[]>([]);
const loading = ref(false);

const loadAllDishes = async () => {
  loading.value = true;
  try {
    const result = await getDishList();
    dishes.value = result.data.data;
    console.log('获取菜品成功:', result);
  } catch (error: any) {
    console.error('获取菜品失败:', error);
  } finally {
    loading.value = false;
  }
};

const goToDetail = (dishId: number) => {
  console.log("跳转的菜品id", dishId);
  router.push({
    path: `/user/dish/${dishId}`
  });
}

onMounted(() => {
  loadAllDishes();
});
</script>

<style scoped>
#homePage {
  padding: 20px;
  min-height: 100vh;
  background-color: #f8f9fa;
}

/* 加载状态样式 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  gap: 15px;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-container p {
  color: #666;
  font-size: 16px;
  font-weight: 500;
}

/* 空状态样式 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  text-align: center;
  padding: 40px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.empty-state h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 20px;
  font-weight: 600;
}

.empty-state p {
  margin: 0;
  color: #888;
  font-size: 14px;
}

/* 菜品网格容器 */
.dishes-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  padding: 10px 0;
}

/* 菜品卡片样式 */
.dish-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
  flex-direction: column;
}

.dish-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

/* 图片容器样式 */
.dish-image-container {
  position: relative;
  width: 100%;
  padding-top: 75%; /* 4:3 宽高比 */
  overflow: hidden;
}

.dish-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #f5f5f5;
}

.dish-img,
.default-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.dish-card:hover .dish-img,
.dish-card:hover .default-image {
  transform: scale(1.05);
}

/* 图片遮罩效果 */
.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.dish-card:hover .image-overlay {
  opacity: 1;
}

.view-details {
  color: white;
  background: rgba(255, 255, 255, 0.2);
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

/* 菜品信息样式 */
.dish-info {
  padding: 20px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.dish-name {
  margin: 0 0 12px 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
  line-height: 1.4;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.dish-price {
  font-size: 22px;
  font-weight: 700;
  color: #ff6b6b;
  margin-top: auto;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .dishes-container {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
}

@media (max-width: 768px) {
  .dishes-container {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 16px;
  }

  .dish-info {
    padding: 16px;
  }

  .dish-name {
    font-size: 16px;
  }

  .dish-price {
    font-size: 20px;
  }
}

@media (max-width: 480px) {
  #homePage {
    padding: 12px;
  }

  .dishes-container {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .dish-image-container {
    padding-top: 66.67%; /* 3:2 宽高比 */
  }
}
</style>