<template>
  <div class="dish-detail-container">
    <!-- 顶部导航 -->
    <div class="top-nav">
      <div class="nav-left" @click="goBack">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M19 12H5M12 19l-7-7 7-7"/>
        </svg>
      </div>
      <div class="nav-title">菜品详情</div>
      <div class="nav-right">
        <button class="share-btn" @click="shareDish">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="18" cy="5" r="3"/>
            <circle cx="6" cy="12" r="3"/>
            <circle cx="18" cy="19" r="3"/>
            <path d="M8.59 13.51l6.83 3.98M15.41 6.51l-6.82 3.98"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-content">
        <div class="spinner"></div>
        <p class="loading-text">正在加载菜品信息...</p>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error-state">
      <div class="error-content">
        <div class="error-icon">⚠️</div>
        <h3 class="error-title">加载失败</h3>
        <p class="error-message">{{ error }}</p>
        <button class="retry-btn" @click="retry">重新加载</button>
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
              @click="zoomImage"
          />
          <div v-else class="image-placeholder">
            <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
              <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <path d="M21 15l-5-5L5 21"/>
            </svg>
            <p>暂无图片</p>
          </div>
          <div v-if="dish.imgUrl" class="image-zoom-hint">
            点击图片查看大图
          </div>
        </div>
      </div>

      <!-- 菜品信息卡片 -->
      <div class="info-card">
        <div class="dish-header">
          <h1 class="dish-name">{{ dish.dishName }}</h1>
          <div class="price-container">
            <span class="price-label">价格</span>
            <span class="price-value">¥{{ dish.price }}</span>
          </div>
        </div>

        <div class="dish-meta">
          <div class="meta-item" v-if="dish.categoryId">
            <span class="meta-label">分类</span>
            <span class="meta-value">{{ dish.categoryName || `分类 ${dish.categoryId}` }}</span>
          </div>
          <div class="meta-item" v-if="dish.status">
            <span class="meta-label">状态</span>
            <span :class="['status-badge', dish.status === 1 ? 'active' : 'inactive']">
              {{ dish.status === 1 ? '在售' : '下架' }}
            </span>
          </div>
        </div>

        <!-- 菜品描述 -->
        <div class="description-section" v-if="dish.description">
          <h3 class="section-title">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
              <path d="M14 2v6h6M16 13H8M16 17H8M10 9H8"/>
            </svg>
            菜品描述
          </h3>
          <p class="description-content">{{ dish.description }}</p>
        </div>



        <!-- 餐厅信息卡片 -->
        <div class="restaurant-card">
          <h2 class="restaurant-title">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
              <circle cx="12" cy="10" r="3"/>
            </svg>
            餐厅信息
          </h2>

          <div class="restaurant-info">
            <div class="restaurant-item">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                  <circle cx="12" cy="7" r="4"/>
                </svg>
              </div>
              <div class="item-content">
                <h4>美味餐厅</h4>
                <p>专注于传统与现代融合的美食体验</p>
              </div>
            </div>

            <div class="restaurant-item">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                  <circle cx="12" cy="10" r="3"/>
                </svg>
              </div>
              <div class="item-content">
                <h4>餐厅地址</h4>
                <p>上海市黄浦区南京东路123号美食广场3楼</p>
              </div>
            </div>

            <div class="restaurant-item">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/>
                </svg>
              </div>
              <div class="item-content">
                <h4>联系电话</h4>
                <p>021-1234-5678</p>
              </div>
            </div>

            <div class="restaurant-item">
              <div class="item-icon">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <circle cx="12" cy="12" r="10"/>
                  <path d="M12 6v6l4 2"/>
                </svg>
              </div>
              <div class="item-content">
                <h4>营业时间</h4>
                <p>周一至周日 10:00 - 22:00</p>
              </div>
            </div>
          </div>

          <!-- 餐厅评分 -->
          <div class="restaurant-rating">
            <div class="rating-header">
              <h4>餐厅评分</h4>
              <span class="rating-value">4.8</span>
            </div>
            <div class="rating-stars">
              <span class="star filled">★</span>
              <span class="star filled">★</span>
              <span class="star filled">★</span>
              <span class="star filled">★</span>
              <span class="star half">★</span>
              <span class="rating-count">(328条评价)</span>
            </div>
          </div>
        </div>

        <!-- 推荐菜品 -->
        <div class="recommendations-section" v-if="recommendations.length > 0">
          <h3 class="section-title">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
            </svg>
            推荐菜品
          </h3>
          <div class="recommendations-list">
            <div v-for="(rec, index) in recommendations" :key="index" class="recommendation-item">
              <div class="rec-image">
                <img v-if="rec.imgUrl" :src="`data:image/jpeg;base64,${rec.imgUrl}`" :alt="rec.dishName">
                <div v-else class="rec-placeholder">
                  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
                    <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                    <circle cx="8.5" cy="8.5" r="1.5"/>
                    <path d="M21 15l-5-5L5 21"/>
                  </svg>
                </div>
              </div>
              <div class="rec-info">
                <h4>{{ rec.dishName }}</h4>
                <p class="rec-price">¥{{ rec.price }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 操作按钮区域 -->
        <div class="action-section">
          <button class="add-to-cart-btn" @click="showModal=true">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="9" cy="21" r="1"/>
              <circle cx="20" cy="21" r="1"/>
              <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
            </svg>
            加入购物车
          </button>
          <SelectFlavors
              v-model:visible="showModal"
              :dish="dish"
              title="${{dish}}"
              confirm-text="确认下单"
              @confirm="handleOrderConfirm"
          />
        </div>
      </div>

      <!-- 底部安全区域 -->
      <div class="bottom-safe-area"></div>
    </div>

    <div v-else class="no-data-state">
      <div class="no-data-content">
        <div class="no-data-icon">🍽️</div>
        <h3 class="no-data-title">菜品不存在</h3>
        <p class="no-data-message">该菜品可能已被下架或不存在</p>
        <button class="back-home-btn" @click="goBack">返回首页</button>
      </div>
    </div>


</div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from "vue";
import { dishItem } from "@/types/dish";
import { useRoute, useRouter } from 'vue-router'
import { getDishById } from "@/api/dish";
import SelectFlavors from '@/page/user/dish/components/SelectFlavors.vue'

const route = useRoute()
const router = useRouter()

const dish = ref<dishItem | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)
const showZoomModal = ref(false)
const showModal =ref(false)
const quantity = ref(1)
const notes = ref('')
const recommendations = ref<any[]>([
  { id: 2, dishName: "招牌红烧肉", price: 68, imgUrl: null },
  { id: 3, dishName: "清蒸鲈鱼", price: 88, imgUrl: null },
  { id: 4, dishName: "麻婆豆腐", price: 32, imgUrl: null }
])

// 加载菜品详情
const loadDishDetail = async (id: number) => {
  console.log('开始加载菜品详情，ID:', id);
  loading.value = true
  error.value = null

  try {
    const result = await getDishById(id)
    dish.value = result.data;
    console.log('获取菜品详情成功:', result);

  } catch(error: any) {
    console.log('获取菜品详情失败:', error)
    error.value = "加载菜品详情失败，请稍后重试"
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

const retry = () => {
  const dishId = route.params.id;
  if (dishId) {
    const id = Number(dishId)
    loadDishDetail(id);
  }
}

const zoomImage = () => {
  if (dish.value?.imgUrl) {
    showZoomModal.value = true
  }
}

// 处理下单确认
const handleOrderConfirm = (orderData:any) => {
  console.log('用户下单数据:', orderData)
  alert('下单成功！')
  // 这里可以调用下单API
}

// 监听菜品变化，重置数量
watch(dish, () => {
  quantity.value = 1
  notes.value = ''
})

onMounted(() => {
  console.log('当前路由参数', route.params)
  console.log('🆔 获取到的菜品ID:', route.params.id);
  const dishId = route.params.id;
  if (dishId) {
    const id = Number(dishId)
    loadDishDetail(id);
  }
})
</script>

<style scoped>
/* 桌面端居中布局 */
@media (min-width: 768px) {
  .dish-detail-container {
    max-width: 600px;
    margin: 0 auto;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    min-height: 100vh;
  }
}

/* 简洁顶部导航 */
.top-nav {
  position: sticky;
  top: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  background: white;
  border-bottom: 1px solid #f0f0f0;
  backdrop-filter: blur(8px);
}

.nav-left {
  padding: 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.nav-left:hover {
  background-color: #f5f5f5;
}

.nav-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

/* 加载状态 */
.loading-state,
.error-state,
.no-data-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  padding: 40px 20px;
}

.loading-content,
.error-content,
.no-data-content {
  text-align: center;
  animation: fadeIn 0.3s ease;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #1890ff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 16px;
}

.loading-text {
  color: #666;
  font-size: 15px;
}

/* 错误状态 */
.error-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.8;
}

.error-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.error-message {
  color: #666;
  margin-bottom: 20px;
  line-height: 1.5;
}

.retry-btn,
.back-home-btn {
  background: #1890ff;
  color: white;
  border: none;
  padding: 10px 24px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.retry-btn:hover,
.back-home-btn:hover {
  background: #40a9ff;
}

/* 图片区域 */
.image-section {
  position: relative;
  margin-bottom: 16px;
}

.image-wrapper {
  position: relative;
  overflow: hidden;
  background: #fafafa;
  border-radius: 0 0 12px 12px;
}

.main-image {
  width: 100%;
  height: 280px;
  object-fit: cover;
  display: block;
}

.image-placeholder {
  width: 100%;
  height: 280px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f0f0f0;
  color: #999;
}

.image-placeholder svg {
  opacity: 0.4;
  margin-bottom: 8px;
}

.image-placeholder p {
  font-size: 14px;
}

/* 信息卡片 */
.info-card {
  background: white;
  border-radius: 12px 12px 0 0;
  padding: 20px;
  margin-top: -12px;
  position: relative;
}

.dish-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.dish-name {
  font-size: 22px;
  font-weight: 600;
  color: #333;
  margin: 0;
  flex: 1;
}

.price-container {
  text-align: right;
  margin-left: 16px;
}

.price-label {
  display: block;
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.price-value {
  font-size: 24px;
  font-weight: 700;
  color: #ff4d4f;
}

/* 菜品元信息 */
.dish-meta {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: #fafafa;
  border-radius: 6px;
  font-size: 13px;
}

.meta-label {
  color: #666;
}

.meta-value {
  color: #333;
  font-weight: 500;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 500;
}

.status-badge.active {
  background: #f6ffed;
  color: #52c41a;
  border: 1px solid #b7eb8f;
}

.status-badge.inactive {
  background: #fff2e8;
  color: #fa541c;
  border: 1px solid #ffbb96;
}

/* 描述部分 */
.description-section {
  margin-bottom: 20px;
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 12px 0;
}

.section-title svg {
  color: #1890ff;
}

.description-content {
  color: #666;
  line-height: 1.6;
  margin: 0;
  font-size: 14px;
}

/* 餐厅信息卡片 */
.restaurant-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  border: 1px solid #f0f0f0;
}

.restaurant-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px 0;
}

.restaurant-info {
  margin-bottom: 20px;
}

.restaurant-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 16px;
}

.item-icon {
  flex-shrink: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f0f0;
  border-radius: 6px;
}

.item-content h4 {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin: 0 0 4px 0;
}

.item-content p {
  font-size: 13px;
  color: #666;
  margin: 0;
  line-height: 1.4;
}

/* 餐厅评分 */
.restaurant-rating {
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
}

.rating-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.rating-header h4 {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin: 0;
}

.rating-value {
  font-size: 18px;
  font-weight: 700;
  color: #fa541c;
}

.rating-stars {
  display: flex;
  align-items: center;
  gap: 4px;
}

.star {
  font-size: 16px;
  color: #ddd;
}

.star.filled {
  color: #faad14;
}

.rating-count {
  font-size: 12px;
  color: #999;
  margin-left: 8px;
}

/* 推荐菜品 */
.recommendations-section {
  margin-bottom: 20px;
}

.recommendations-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 12px;
  margin-top: 12px;
}

.recommendation-item {
  background: white;
  border-radius: 8px;
  padding: 12px;
  border: 1px solid #f0f0f0;
  transition: border-color 0.2s;
  cursor: pointer;
}

.recommendation-item:hover {
  border-color: #1890ff;
}

.rec-image {
  width: 100%;
  height: 80px;
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 8px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
}

.rec-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.rec-placeholder svg {
  color: #bfbfbf;
}

.rec-info h4 {
  font-size: 13px;
  font-weight: 500;
  color: #333;
  margin: 0 0 4px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.rec-price {
  font-size: 16px;
  font-weight: 600;
  color: #ff4d4f;
  margin: 0;
}

/* 操作按钮区域 */
.action-section {
  position: sticky;
  bottom: 0;
  background: white;
  padding: 16px;
  border-top: 1px solid #f0f0f0;
  margin-top: 20px;
}

.add-to-cart-btn {
  width: 100%;
  background: #ff4d4f;
  color: white;
  border: none;
  padding: 14px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.add-to-cart-btn:hover {
  background: #ff7875;
}

.add-to-cart-btn:active {
  background: #d9363e;
}

/* 底部安全区域 */
.bottom-safe-area {
  height: env(safe-area-inset-bottom);
  background: transparent;
}

/* 无数据状态 */
.no-data-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.no-data-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.no-data-message {
  color: #666;
  margin-bottom: 20px;
  line-height: 1.5;
}

/* 基础动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* 响应式优化 */
@media (max-width: 480px) {
  .info-card {
    padding: 16px;
  }

  .dish-name {
    font-size: 20px;
  }

  .price-value {
    font-size: 22px;
  }

  .recommendations-list {
    grid-template-columns: repeat(2, 1fr);
  }

  .add-to-cart-btn {
    padding: 12px;
    font-size: 15px;
  }
}
</style>