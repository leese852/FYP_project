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

    <!-- 有菜品数据时 -->
    <div v-else>
      <!-- 分类筛选标签 -->
      <div class="category-filter" v-if="!loading && dishes.length > 0">
        <div class="category-title">分类筛选：</div>
        <div class="category-tags">
          <a-tag
              v-for="category in categoryOptions"
              :key="category.value"
              :color="selectedCategory === category.value ? 'blue' : 'default'"
              @click="toggleCategory(category.value)"
              class="category-tag"
              :class="{ 'active': selectedCategory === category.value }"
          >
            {{ category.label }}
          </a-tag>

          <a-tag
              v-if="selectedCategory"
              color="red"
              @click="clearCategory"
              class="clear-tag"
          >
            清除筛选
          </a-tag>
        </div>
      </div>

      <!-- 筛选后无结果 -->
      <div v-if="filteredDishes.length === 0 && selectedCategory" class="no-filter-result">
        <div class="empty-icon">🔍</div>
        <h3>该分类下暂无菜品</h3>
        <a-button type="link" @click="clearCategory">查看所有菜品</a-button>
      </div>

      <!-- 菜品列表 -->
      <div v-else class="dishes-container">
        <div
            v-for="dish in filteredDishes"
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
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import { dishItem } from "@/types/dish";
import { getDishList } from "@/api/dish";
import router from "@/router";

const dishes = ref<dishItem[]>([]);
const loading = ref(false);
const selectedCategory = ref<number | undefined>(undefined);

// 硬编码分类
const categoryOptions = [
  { label: '主食', value: 1 },
  { label: '汤类', value: 2 },
  { label: '饮品', value: 3 },
  { label: '甜点', value: 4 },
  // { label: '套餐', value: 7 },
  // { label: '招牌菜', value: 8 },
  // { label: '特色菜', value: 9 },
  // { label: '其他', value: 10 }
]

// 计算筛选后的菜品
const filteredDishes = computed(() => {
  if (!selectedCategory.value) {
    return dishes.value;
  }

  // 按分类筛选
  return dishes.value.filter(dish => dish.categoryId === selectedCategory.value);
});

// 切换分类筛选
const toggleCategory = (categoryId: number) => {
  if (selectedCategory.value === categoryId) {
    // 如果点击已选中的分类，则取消选择
    selectedCategory.value = undefined;
  } else {
    // 否则选中新的分类
    selectedCategory.value = categoryId;
  }
};

// 清除筛选
const clearCategory = () => {
  selectedCategory.value = undefined;
};

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
  min-height: calc(100vh - 64px); /* 减去可能的头部高度 */
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  position: relative;
}

/* 分类筛选样式优化 */
.category-filter {
  margin: 0 auto 30px;
  padding: 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  border: 1px solid rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
}

.category-title {
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 16px;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.category-title::before {
  content: '📁';
  font-size: 14px;
}

.category-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
}

.category-tag {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  padding: 8px 16px;
  border-radius: 8px;
  font-weight: 500;
  border: 2px solid transparent;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.category-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.category-tag.active {
  background: linear-gradient(135deg, #1890ff, #36cfc9);
  color: white;
  border-color: #1890ff;
  box-shadow: 0 4px 15px rgba(24, 144, 255, 0.3);
  font-weight: 600;
}

.clear-tag {
  cursor: pointer;
  margin-left: auto;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
  font-weight: 500;
}

.clear-tag:hover {
  background: #fff2f0;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 77, 79, 0.2);
}

/* 筛选后无结果样式 */
.no-filter-result {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  margin: 0 auto 30px;
  max-width: 1200px;
  border: 1px solid #f0f0f0;
  text-align: center;
}

.no-filter-result .empty-icon {
  font-size: 80px;
  margin-bottom: 20px;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

.no-filter-result h3 {
  margin: 0 0 16px 0;
  color: #333;
  font-size: 20px;
  font-weight: 600;
}

.no-filter-result .ant-btn-link {
  color: #1890ff;
  font-weight: 500;
  font-size: 15px;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.no-filter-result .ant-btn-link:hover {
  background: #e6f7ff;
  transform: translateY(-2px);
}

/* 加载状态样式优化 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 60vh;
  gap: 20px;
}

.loading-spinner {
  width: 60px;
  height: 60px;
  border: 4px solid rgba(52, 152, 219, 0.1);
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
  font-size: 18px;
  font-weight: 500;
  letter-spacing: 2px;
  animation: pulse 1.5s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

/* 空状态样式优化 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 60vh;
  text-align: center;
  padding: 40px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  margin: 0 auto;
  border: 1px solid rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
}

.empty-state .empty-icon {
  font-size: 100px;
  margin-bottom: 24px;
  animation: bounce 2s ease-in-out infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
}

.empty-state h3 {
  margin: 0 0 12px 0;
  color: #1a1a1a;
  font-size: 24px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.empty-state p {
  margin: 0;
  color: #666;
  font-size: 16px;
  line-height: 1.6;
  max-width: 400px;
}

/* 菜品网格容器优化 */
.dishes-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 30px;
  padding: 20px 0;
  max-width: 1200px;
  margin: 0 auto;
}

/* 菜品卡片样式优化 */
.dish-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  border: 1px solid rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
}

.dish-card:hover {
  transform: translateY(-12px) scale(1.02);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

.dish-card::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #ff6b6b, #4ecdc4, #45b7d1);
  transform: scaleX(0);
  transition: transform 0.4s ease;
  transform-origin: left;
}

.dish-card:hover::after {
  transform: scaleX(1);
}

/* 图片容器样式优化 */
.dish-image-container {
  position: relative;
  width: 100%;
  padding-top: 75%; /* 4:3 宽高比 */
  overflow: hidden;
  border-radius: 16px 16px 0 0;
}

.dish-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.dish-img,
.default-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.dish-card:hover .dish-img,
.dish-card:hover .default-image {
  transform: scale(1.1);
}

/* 图片遮罩效果优化 */
.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to bottom, rgba(0,0,0,0) 0%, rgba(0,0,0,0.6) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  backdrop-filter: blur(2px);
}

.dish-card:hover .image-overlay {
  opacity: 1;
}

.view-details {
  color: white;
  background: rgba(255, 255, 255, 0.25);
  padding: 12px 24px;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 0.5px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transform: translateY(20px);
  opacity: 0;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.dish-card:hover .view-details {
  transform: translateY(0);
  opacity: 1;
}

/* 菜品信息样式优化 */
.dish-info {
  padding: 24px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
}

.dish-name {
  margin: 0 0 16px 0;
  font-size: 18px;
  font-weight: 700;
  color: #1a1a1a;
  line-height: 1.4;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  transition: color 0.3s ease;
}

.dish-card:hover .dish-name {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.dish-price {
  font-size: 28px;
  font-weight: 800;
  color: #ff6b6b;
  text-align: right;
  position: relative;
  display: inline-block;
  align-self: flex-end;
}

.dish-price::before {
  content: '$';
  font-size: 18px;
  font-weight: 600;
  color: #ff9999;
  margin-right: 2px;
}

.dish-price::after {
  content: '';
  position: absolute;
  bottom: -4px;
  right: 0;
  width: 40px;
  height: 3px;
  background: linear-gradient(90deg, transparent, #ff6b6b);
  border-radius: 2px;
}

/* 添加微交互效果 */
.dish-card {
  animation: fadeInUp 0.6s ease-out;
  animation-fill-mode: both;
}

.dish-card:nth-child(1) { animation-delay: 0.1s; }
.dish-card:nth-child(2) { animation-delay: 0.2s; }
.dish-card:nth-child(3) { animation-delay: 0.3s; }
.dish-card:nth-child(4) { animation-delay: 0.4s; }
.dish-card:nth-child(5) { animation-delay: 0.5s; }

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计优化 */
@media (max-width: 1200px) {
  .dishes-container {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 24px;
    max-width: 1000px;
  }

  .category-filter,
  .no-filter-result {
    max-width: 1000px;
  }
}

@media (max-width: 768px) {
  #homePage {
    padding: 16px;
  }

  .category-filter {
    padding: 16px;
    margin-bottom: 24px;
  }

  .category-tags {
    gap: 8px;
  }

  .category-tag {
    padding: 6px 12px;
    font-size: 13px;
  }

  .dishes-container {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 20px;
    padding: 16px 0;
  }

  .dish-info {
    padding: 20px;
  }

  .dish-name {
    font-size: 16px;
    margin-bottom: 12px;
  }

  .dish-price {
    font-size: 24px;
  }

  .loading-container {
    height: 50vh;
  }

  .empty-state {
    height: 50vh;
    padding: 30px 20px;
    margin: 0 16px;
  }

  .empty-state .empty-icon {
    font-size: 80px;
  }

  .empty-state h3 {
    font-size: 20px;
  }
}

@media (max-width: 480px) {
  #homePage {
    padding: 12px;
    background: #f8f9fa;
  }

  .category-filter {
    border-radius: 12px;
    padding: 12px;
  }

  .category-title {
    font-size: 14px;
    margin-bottom: 12px;
  }

  .category-tag {
    padding: 4px 10px;
    font-size: 12px;
  }

  .dishes-container {
    grid-template-columns: 1fr;
    gap: 16px;
    padding: 12px 0;
  }

  .dish-card {
    border-radius: 12px;
  }

  .dish-image-container {
    padding-top: 66.67%; /* 3:2 宽高比 */
  }

  .dish-info {
    padding: 16px;
  }

  .view-details {
    padding: 8px 16px;
    font-size: 13px;
  }

  .no-filter-result {
    padding: 40px 16px;
    margin: 0 12px 20px;
  }

  .no-filter-result .empty-icon {
    font-size: 60px;
  }

  .no-filter-result h3 {
    font-size: 18px;
  }
}
</style>