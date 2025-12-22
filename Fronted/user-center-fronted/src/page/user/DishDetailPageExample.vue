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
          <div class="meta-item">
            <span class="meta-label">菜品编号</span>
            <span class="meta-value">{{ dish.id }}</span>
          </div>
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

        <!-- 口味选项 -->
        <div class="flavors-section" v-if="dish.flavors && dish.flavors.length > 0">
          <h3 class="section-title">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M12 2a8 8 0 0 0-8 8c0 1.892.402 3.13 1.5 4.5L12 22l6.5-7.5c1.098-1.37 1.5-2.608 1.5-4.5a8 8 0 0 0-8-8z"/>
              <path d="M9 9h.01M15 9h.01"/>
            </svg>
            口味选择
          </h3>
          <div class="flavors-list">
            <div v-for="(flavor, index) in dish.flavors" :key="index" class="flavor-item">
              <div class="flavor-tag">{{ flavor.tag }}</div>
              <div class="flavor-options">
                <span v-for="(option, i) in flavor.list.split(',')" :key="i" class="flavor-option">
                  {{ option }}
                </span>
              </div>
            </div>
          </div>
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
          <button class="add-to-cart-btn" @click="addToCart">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="9" cy="21" r="1"/>
              <circle cx="20" cy="21" r="1"/>
              <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
            </svg>
            加入购物车
          </button>
          <button class="order-now-btn" @click="orderNow">
            立即下单
          </button>
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

    <!-- 图片放大模态框 -->
    <div v-if="showZoomModal" class="zoom-modal" @click="closeZoomModal">
      <div class="zoom-content">
        <img
            v-if="dish?.imgUrl"
            :src="`data:image/jpeg;base64,${dish.imgUrl}`"
            :alt="dish.dishName"
            class="zoomed-image"
        />
      </div>
      <button class="close-zoom-btn" @click="closeZoomModal">
        ✕
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { dishItem } from "@/types/dish";
import { useRoute, useRouter } from 'vue-router'
import { getDishById } from "@/api/dish";

const route = useRoute()
const router = useRouter()

const dish = ref<dishItem | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)
const showZoomModal = ref(false)
const recommendations = ref<any[]>([
  { id: 2, dishName: "招牌红烧肉", price: 68, imgUrl: null },
  { id: 3, dishName: "清蒸鲈鱼", price: 88, imgUrl: null },
  { id: 4, dishName: "麻婆豆腐", price: 32, imgUrl: null }
])

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

const closeZoomModal = () => {
  showZoomModal.value = false
}

const shareDish = () => {
  if (navigator.share && dish.value) {
    navigator.share({
      title: dish.value.dishName,
      text: `看看这道美味的${dish.value.dishName}，快来尝尝吧！`,
      url: window.location.href,
    })
  } else {
    alert('分享功能需要现代浏览器支持')
  }
}

const addToCart = () => {
  alert('已加入购物车')
}

const orderNow = () => {
  alert('开始下单')
}

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
/* 全局容器 */
.dish-detail-container {
  width: 100%;
  min-height: 100vh;
  background-color: #f8f9fa;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 顶部导航 */
.top-nav {
  position: sticky;
  top: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background-color: white;
  border-bottom: 1px solid #e9ecef;
  backdrop-filter: blur(10px);
}

.nav-left {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.nav-left:hover {
  background-color: #f1f3f5;
}

.nav-title {
  font-size: 18px;
  font-weight: 600;
  color: #212529;
}

.nav-right {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.share-btn {
  width: 36px;
  height: 36px;
  border: none;
  background: none;
  cursor: pointer;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.2s;
}

.share-btn:hover {
  background-color: #f1f3f5;
}

/* 加载状态 */
.loading-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  padding: 20px;
}

.loading-content {
  text-align: center;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  color: #6c757d;
  font-size: 16px;
}

/* 错误状态 */
.error-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  padding: 20px;
}

.error-content {
  text-align: center;
  max-width: 300px;
}

.error-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.error-title {
  font-size: 20px;
  font-weight: 600;
  color: #212529;
  margin-bottom: 8px;
}

.error-message {
  color: #6c757d;
  margin-bottom: 20px;
  line-height: 1.5;
}

.retry-btn {
  padding: 12px 24px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  width: 100%;
}

.retry-btn:hover {
  background-color: #2980b9;
  transform: translateY(-2px);
}

/* 主要内容区域 */
.content-scroll {
  max-width: 100%;
  overflow-x: hidden;
}

/* 图片区域 */
.image-section {
  width: 100%;
  background-color: white;
  padding: 16px;
}

.image-wrapper {
  position: relative;
  width: 100%;
  border-radius: 16px;
  overflow: hidden;
  background-color: #f8f9fa;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.main-image {
  width: 100%;
  height: auto;
  aspect-ratio: 1/1;
  object-fit: cover;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.main-image:hover {
  transform: scale(1.02);
}

.image-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  aspect-ratio: 1/1;
  color: #adb5bd;
  background-color: #f1f3f5;
}

.image-placeholder svg {
  margin-bottom: 12px;
  opacity: 0.5;
}

.image-zoom-hint {
  position: absolute;
  bottom: 12px;
  right: 12px;
  background-color: rgba(0,0,0,0.7);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  backdrop-filter: blur(10px);
}

/* 信息卡片 */
.info-card {
  background-color: white;
  border-radius: 20px 20px 0 0;
  margin-top: -20px;
  padding: 24px 20px;
  box-shadow: 0 -4px 20px rgba(0,0,0,0.05);
}

.dish-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f1f3f5;
}

.dish-name {
  font-size: 24px;
  font-weight: 700;
  color: #212529;
  margin: 0;
  line-height: 1.3;
  flex: 1;
  padding-right: 16px;
}

.price-container {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.price-label {
  font-size: 12px;
  color: #6c757d;
  margin-bottom: 4px;
}

.price-value {
  font-size: 28px;
  font-weight: 700;
  color: #e74c3c;
  white-space: nowrap;
}

/* 菜品元信息 */
.dish-meta {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.meta-item {
  display: flex;
  flex-direction: column;
}

.meta-label {
  font-size: 12px;
  color: #6c757d;
  margin-bottom: 4px;
}

.meta-value {
  font-size: 16px;
  font-weight: 500;
  color: #212529;
}

.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.active {
  background-color: #d4edda;
  color: #155724;
}

.status-badge.inactive {
  background-color: #f8d7da;
  color: #721c24;
}

/* 通用部分样式 */
.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #212529;
  margin: 24px 0 16px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid #f1f3f5;
}

.section-title svg {
  color: #3498db;
}

/* 描述区域 */
.description-section {
  margin-bottom: 24px;
}

.description-content {
  color: #495057;
  line-height: 1.6;
  font-size: 16px;
  margin: 0;
}

/* 口味区域 */
.flavors-section {
  margin-bottom: 24px;
}

.flavors-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.flavor-item {
  background-color: #f8f9fa;
  border-radius: 12px;
  padding: 16px;
  border: 1px solid #e9ecef;
}

.flavor-tag {
  font-size: 16px;
  font-weight: 600;
  color: #212529;
  margin-bottom: 12px;
}

.flavor-options {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.flavor-option {
  padding: 6px 12px;
  background-color: white;
  border: 1px solid #dee2e6;
  border-radius: 20px;
  font-size: 14px;
  color: #495057;
  transition: all 0.2s;
  cursor: pointer;
}

.flavor-option:hover {
  border-color: #3498db;
  color: #3498db;
}

/* 餐厅卡片 */
.restaurant-card {
  background-color: #f8f9fa;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 24px;
  border: 1px solid #e9ecef;
}

.restaurant-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: 600;
  color: #212529;
  margin: 0 0 20px 0;
}

.restaurant-title svg {
  color: #e74c3c;
}

.restaurant-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 24px;
}

.restaurant-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.item-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: white;
  border-radius: 10px;
  flex-shrink: 0;
}

.item-icon svg {
  color: #3498db;
}

.item-content h4 {
  font-size: 16px;
  font-weight: 600;
  color: #212529;
  margin: 0 0 4px 0;
}

.item-content p {
  font-size: 14px;
  color: #6c757d;
  margin: 0;
  line-height: 1.5;
}

/* 餐厅评分 */
.restaurant-rating {
  padding: 16px;
  background-color: white;
  border-radius: 12px;
  border: 1px solid #e9ecef;
}

.rating-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.rating-header h4 {
  font-size: 16px;
  font-weight: 600;
  color: #212529;
  margin: 0;
}

.rating-value {
  font-size: 24px;
  font-weight: 700;
  color: #e74c3c;
}

.rating-stars {
  display: flex;
  align-items: center;
  gap: 4px;
}

.star {
  font-size: 20px;
  color: #ffd700;
}

.star.half {
  position: relative;
  overflow: hidden;
  color: #e9ecef;
}

.star.half::before {
  content: '★';
  position: absolute;
  left: 0;
  width: 50%;
  overflow: hidden;
  color: #ffd700;
}

.rating-count {
  margin-left: 8px;
  font-size: 14px;
  color: #6c757d;
}

/* 推荐菜品 */
.recommendations-section {
  margin-bottom: 24px;
}

.recommendations-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 16px;
}

.recommendation-item {
  background-color: white;
  border-radius: 12px;
  padding: 12px;
  border: 1px solid #e9ecef;
  transition: all 0.2s;
  cursor: pointer;
}

.recommendation-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.1);
}

.rec-image {
  width: 100%;
  height: 100px;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f8f9fa;
  margin-bottom: 12px;
}

.rec-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.rec-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #adb5bd;
}

.rec-info h4 {
  font-size: 14px;
  font-weight: 600;
  color: #212529;
  margin: 0 0 8px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.rec-price {
  font-size: 16px;
  font-weight: 700;
  color: #e74c3c;
  margin: 0;
}

/* 操作按钮区域 */
.action-section {
  display: flex;
  gap: 12px;
  margin: 24px 0 32px 0;
  padding-top: 20px;
  border-top: 1px solid #f1f3f5;
}

.add-to-cart-btn, .order-now-btn {
  flex: 1;
  padding: 16px 24px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border: none;
}

.add-to-cart-btn {
  background-color: white;
  color: #3498db;
  border: 2px solid #3498db;
}

.add-to-cart-btn:hover {
  background-color: #f8f9fa;
  transform: translateY(-2px);
}

.order-now-btn {
  background-color: #e74c3c;
  color: white;
}

.order-now-btn:hover {
  background-color: #c0392b;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(231, 76, 60, 0.3);
}

/* 底部安全区域 */
.bottom-safe-area {
  height: 20px;
  background-color: #f8f9fa;
}

/* 无数据状态 */
.no-data-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  padding: 20px;
}

.no-data-content {
  text-align: center;
  max-width: 300px;
}

.no-data-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.no-data-title {
  font-size: 20px;
  font-weight: 600;
  color: #212529;
  margin-bottom: 8px;
}

.no-data-message {
  color: #6c757d;
  margin-bottom: 20px;
  line-height: 1.5;
}

.back-home-btn {
  padding: 12px 24px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  width: 100%;
}

.back-home-btn:hover {
  background-color: #2980b9;
  transform: translateY(-2px);
}

/* 图片放大模态框 */
.zoom-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.9);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.zoom-content {
  max-width: 100%;
  max-height: 100%;
}

.zoomed-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.close-zoom-btn {
  position: fixed;
  top: 20px;
  right: 20px;
  width: 40px;
  height: 40px;
  background-color: rgba(255,255,255,0.1);
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
  transition: background-color 0.2s;
}

.close-zoom-btn:hover {
  background-color: rgba(255,255,255,0.2);
}

/* 响应式设计 */
@media (min-width: 768px) {
  .dish-detail-container {
    max-width: 768px;
    margin: 0 auto;
    background-color: white;
  }

  .top-nav {
    max-width: 768px;
    margin: 0 auto;
    border-radius: 0 0 16px 16px;
  }

  .content-scroll {
    padding: 0 20px;
  }

  .image-section {
    padding: 24px;
  }

  .info-card {
    padding: 32px;
    border-radius: 20px;
    margin-top: 0;
    box-shadow: 0 4px 20px rgba(0,0,0,0.05);
  }

  .dish-meta {
    grid-template-columns: repeat(3, 1fr);
  }

  .flavor-options {
    flex-wrap: nowrap;
  }

  .action-section {
    max-width: 400px;
    margin-left: auto;
    margin-right: auto;
  }
}

@media (min-width: 1024px) {
  .dish-detail-container {
    max-width: 768px;
  }
}

/* 移动端特定优化 */
@media (max-width: 767px) {
  .dish-name {
    font-size: 22px;
  }

  .price-value {
    font-size: 24px;
  }

  .action-section {
    position: sticky;
    bottom: 0;
    background-color: white;
    padding: 16px 20px;
    margin: 0;
    border-top: 1px solid #e9ecef;
    z-index: 50;
  }

  .info-card {
    padding-bottom: 100px;
  }
}
</style>