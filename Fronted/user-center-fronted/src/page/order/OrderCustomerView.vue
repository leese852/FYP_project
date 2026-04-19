<template>
  <a-card title="訂單詳情" class="order-card">
    <!-- 訂單基本資訊 -->
    <p class="order-number">訂單編號：{{ order?.orderId }}</p>
    <p class="order-status">
      狀態：
      <a-tag :color="statusColor(order?.status)" class="status-tag-custom">
        {{ statusText(order?.status) }}
      </a-tag>
    </p>

    <!-- 🥘 顯示訂單菜品列表 -->
    <a-table
        v-if="order?.items"
        :dataSource="order.items"
        :columns="itemColumns"
        rowKey="id"
        size="middle"
        bordered
        style="margin-top: 20px"
        :pagination="false"
        class="order-items-table"
    />

    <!-- 🚚 骑手实时位置地图 (仅派送中状态显示) -->
    <div v-if="Number(order?.status) === 5" class="rider-map-section">
      <h3 class="section-title">
        <EnvironmentOutlined /> 骑手实时位置
        <a-spin v-if="riderLocationLoading" size="small" />
      </h3>
      <div id="riderMap" class="rider-map"></div>
      <div class="location-info">
        <a-tag :color="riderLocation ? 'green' : 'orange'" class="location-tag">
          {{ riderLocation ? '骑手正在配送中' : '等待骑手位置更新...' }}
        </a-tag>
        <span v-if="riderLocation" class="update-time">
          最后更新: {{ formatTime(riderLocation.updateTime) }}
        </span>
      </div>
    </div>

    <!-- 🚚 派送中顯示騎手資訊 -->
    <div v-if="Number(order?.status) === 5 && order?.rider" style="margin-top: 20px" class="rider-info">
      <h3 class="section-title">騎手資訊</h3>
      <p class="rider-name">騎手姓名：{{ order.rider.name }}</p>
      <p class="rider-phone">騎手電話：{{ order.rider.phone }}</p>
      <p class="rider-location">派送位置：{{ order.rider.location }}</p>
    </div>

    <!-- 💰 價格區塊 -->
    <div class="price-summary" v-if="order">
      <div class="pack-amount">打包費: ${{ order.packAmount }}</div>
      <div class="pay-method">支付方式: {{ order.payMethod }}</div>
      <div class="total-amount">總金額: ${{ order.totalAmount }}</div>
    </div>

    <!-- ❌ 取消訂單按鈕 (根據狀態顯示) -->
    <div class="cancel-btn" v-if="canCancelOrder(order?.status)">
      <a-button type="primary" danger size="large" @click="confirmCancel">取消訂單</a-button>
    </div>

    <!-- 🚚 確認送達按鈕 (僅派送中狀態) -->
    <div class="confirm-btn" v-if="Number(order?.status) === 5">
      <a-button type="primary" size="large" @click="confirmDelivered">確認送達</a-button>
    </div>
  </a-card>
</template>

<script setup lang="ts">
// 🔥 Google Maps 类型声明
declare const google: any;
interface Window {
  google: any;
}

import { ref, onMounted, onUnmounted, watch,nextTick  } from "vue";
import { Modal, message } from "ant-design-vue";
import { EnvironmentOutlined } from "@ant-design/icons-vue";
import { useRouter, useRoute } from "vue-router";
import { getOrderDetails, updateOrderStatus } from "@/api/order";
import { getRiderLocation } from "@/api/rider";
import type { Order } from "@/types/order";

const router = useRouter();
const route = useRoute();

const order = ref<Order | null>(null);
const loading = ref(false);

// 骑手位置相关
const riderLocation = ref<any>(null);
const riderLocationLoading = ref(false);
let map: any = null;
let riderMarker: any = null;
let intervalId: any = null;

const itemColumns = [
  { title: "菜品名稱", dataIndex: "dishName", key: "dishName", width: "40%" },
  { title: "口味", dataIndex: "dishFlavor", key: "dishFlavor", width: "20%" },
  { title: "數量", dataIndex: "quantity", key: "quantity", width: "20%" },
  { title: "單價", dataIndex: "price", key: "price", width: "20%" },
];

// 狀態轉換規則
// 1(待付款) → 2(待接單) → 3(已接單) → 4(制作中) → 5(派送中) → 6(已完成)
// 顾客取消 → 8(待退款)
// 管理员取消 → 7(已取消)

// 🔥 檢查是否可以取消訂單（顾客端）
const canCancelOrder = (status?: number): boolean => {
  if (!status) return false;
  const numStatus = Number(status);
  // 普通用户只能取消状态 1-4，会转为待退款(8)
  // 派送中(5)需要联系客服
  return [1, 2, 3, 4].includes(numStatus);
}

// 檢查是否可以確認送達
const canConfirmDelivered = (status?: number): boolean => {
  if (!status) return false;
  const numStatus = Number(status);
  // 只有派送中可以確認送達
  return numStatus === 5;
}

// 狀態文本映射
function statusText(status?: number) {
  const numStatus = Number(status);
  switch (numStatus) {
    case 1: return "待付款";
    case 2: return "待接單";
    case 3: return "已接單";
    case 4: return "製作中";
    case 5: return "派送中";
    case 6: return "已完成";
    case 7: return "已取消";
    case 8: return "待退款";
    default: return "未知";
  }
}

// 狀態顏色映射 - 橙色主题
function statusColor(status?: number) {
  const numStatus = Number(status);
  switch (numStatus) {
    case 1: return "orange";
    case 2: return "orange";
    case 3: return "blue";
    case 4: return "cyan";
    case 5: return "green";
    case 6: return "lime";
    case 7: return "red";
    case 8: return "gold";
    default: return "default";
  }
}

// 🚀 當頁面載入時，根據路由參數取訂單詳情
onMounted(async () => {
  const id = Number(route.params.orderId);
  if (!id) {
    console.error("❌ 沒有訂單 id");
    message.error("訂單ID不存在");
    return;
  }
  try {
    loading.value = true;
    const res = await getOrderDetails(id);
    order.value = res;
    console.log("✅ 訂單詳情:", res);
    console.log("订单状态值:", res?.status, "类型:", typeof res?.status);

    // 检查是否派送中，初始化地图
    if (Number(res?.status) === 5) {
      console.log("订单状态为派送中，准备初始化地图");
      setTimeout(() => {
        initMap();
        startPollingLocation();
      }, 1000);
    }
  } catch (err) {
    console.error("❌ 載入訂單失敗:", err);
    message.error("載入訂單失敗");
  } finally {
    loading.value = false;
  }
});

// 监听订单状态变化
watch(() => order.value?.status, async (newStatus, oldStatus) => {
  console.log("🔍 状态变化:", oldStatus, "->", newStatus);
  if (Number(newStatus) === 5) {
    console.log("🔍 状态变为派送中，初始化地图");
    await new Promise(resolve => setTimeout(resolve, 500));
    initMap();
    startPollingLocation();
  } else if (Number(newStatus) === 6 || Number(newStatus) === 7 || Number(newStatus) === 8) {
    stopPollingLocation();
  }
}, { immediate: true });

// ==================== 地图相关函数 ====================

// 初始化地图
const initMap = () => {
  console.log("🔍 initMap 开始执行");

  const mapContainer = document.getElementById("riderMap");
  if (!mapContainer) {
    console.warn("❌ 地图容器不存在");
    return;
  }

  const googleObj = (window as any).google;
  if (!googleObj || !googleObj.maps) {
    console.warn("❌ Google Maps API 未加载，等待中...");
    setTimeout(initMap, 500);
    return;
  }

  console.log("✅ Google Maps API 已加载");

  const defaultCenter = {
    lat: order.value?.restaurantLat || 22.3476,
    lng: order.value?.restaurantLng || 114.1065
  };

  try {
    map = new googleObj.maps.Map(mapContainer, {
      zoom: 14,
      center: defaultCenter,
      mapTypeId: googleObj.maps.MapTypeId.ROADMAP,
    });
    console.log("✅ 地图创建成功");

    // 添加骑手标记
    riderMarker = new googleObj.maps.Marker({
      position: defaultCenter,
      map: map,
      icon: {
        url: "https://maps.google.com/mapfiles/ms/icons/red-dot.png",
        scaledSize: new googleObj.maps.Size(40, 40)
      },
      title: "骑手位置",
      animation: googleObj.maps.Animation.BOUNCE
    });

    // 添加餐厅标记
    if (order.value?.restaurantLat && order.value?.restaurantLng) {
      new googleObj.maps.Marker({
        position: { lat: order.value.restaurantLat, lng: order.value.restaurantLng },
        map: map,
        icon: {
          url: "https://maps.google.com/mapfiles/ms/icons/blue-dot.png",
          scaledSize: new googleObj.maps.Size(40, 40)
        },
        title: "餐厅"
      });
    }

    // 添加顾客标记
    if (order.value?.customerLat && order.value?.customerLng) {
      new googleObj.maps.Marker({
        position: { lat: order.value.customerLat, lng: order.value.customerLng },
        map: map,
        icon: {
          url: "https://maps.google.com/mapfiles/ms/icons/green-dot.png",
          scaledSize: new googleObj.maps.Size(40, 40)
        },
        title: "我的地址"
      });
    }

    console.log("✅ 地图初始化完成");
  } catch (error) {
    console.error("❌ 地图创建失败:", error);
  }
};

// 更新骑手位置
const updateMapLocation = (lat: number, lng: number) => {
  if (!map || !riderMarker) return;
  const googleObj = (window as any).google;
  if (!googleObj) return;

  const position = new googleObj.maps.LatLng(lat, lng);
  riderMarker.setPosition(position);
  map.setCenter(position);

  riderMarker.setAnimation(googleObj.maps.Animation.BOUNCE);
  setTimeout(() => {
    if (riderMarker) riderMarker.setAnimation(null);
  }, 2000);
};

// 加载骑手位置
const loadRiderLocation = async () => {
  if (!order.value?.id) return;

  riderLocationLoading.value = true;
  try {
    const res = await getRiderLocation(order.value.id);
    if (res?.data?.data && res.data.data.lat && res.data.data.lng) {
      const location = res.data.data;
      riderLocation.value = location;
      updateMapLocation(location.lat, location.lng);
      console.log("✅ 骑手位置更新:", location.lat, location.lng);
    }
  } catch (error) {
    console.error("❌ 获取骑手位置失败:", error);
  } finally {
    riderLocationLoading.value = false;
  }
};

// 开始轮询
const startPollingLocation = () => {
  if (intervalId) clearInterval(intervalId);
  loadRiderLocation();
  intervalId = setInterval(loadRiderLocation, 5000);
  console.log("✅ 开始轮询骑手位置");
};

// 停止轮询
const stopPollingLocation = () => {
  if (intervalId) {
    clearInterval(intervalId);
    intervalId = null;
    console.log("✅ 停止轮询骑手位置");
  }
};

// 格式化时间
const formatTime = (timeStr: string) => {
  if (!timeStr) return "";
  const date = new Date(timeStr);
  return `${date.getMonth() + 1}/${date.getDate()} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
};

// ==================== 订单操作函数 ====================

// ❌ 取消訂單 - 跳转到取消原因页面
function confirmCancel() {
  if (!order.value) return;

  const status = Number(order.value.status);

  // 派送中的订单特殊处理
  if (status === 5) {
    Modal.confirm({
      title: "確認取消訂單",
      content: "訂單已在派送中，取消訂單可能需要聯繫客服，確定要取消嗎？",
      okText: "確認取消",
      cancelText: "返回",
      okType: "danger",
      async onOk() {
        try {
          const success = await updateOrderStatus(order.value!.id.toString(), 7);
          if (success) {
            message.success('取消申請已提交');
            const res = await getOrderDetails(order.value!.id);
            order.value = res;
          } else {
            message.error('取消訂單失敗');
          }
        } catch (error) {
          console.error("取消訂單失敗:", error);
          message.error('取消訂單失敗');
        }
      },
    });
    return;
  }

  // 🔥 状态 1-4：跳转到取消原因页面
  router.push({
    path: '/order/cancel',
    query: {
      orderId: order.value.orderId,
      orderDatabaseId: order.value.id.toString()
    }
  });
}

// 🚚 確認送達
async function confirmDelivered() {
  if (!order.value) return;

  Modal.confirm({
    title: "確認送達",
    content: "請確認顧客已收到餐點，確定要標記為已完成嗎？",
    okText: "確認送達",
    cancelText: "返回",
    async onOk() {
      try {
        const success = await updateOrderStatus(order.value!.id.toString(), 6);
        if (success) {
          order.value!.status = 6;
          stopPollingLocation();
          Modal.success({
            title: "確認成功",
            content: "訂單已確認送達！",
            okText: "前往評價",
            cancelText: "稍後評價",
            onOk() {
              // 🔥 使用 nextTick 确保 Vue 完成更新后再跳转
              nextTick(() => {
                router.push({
                  path: '/user/feedback',
                  query: {
                    orderId: order.value?.id,
                    type: 'delivery',
                    redirectFrom: 'order-completion'
                  }
                });
              });
            },
          });
        } else {
          message.error('操作失敗');
        }
      } catch (err) {
        console.error("❌ 確認送達失敗:", err);
        message.error('系統錯誤');
      }
    },
  });
}

// 组件卸载时清理
onUnmounted(() => {
  stopPollingLocation();
  if (map) {
    map = null;
  }
});
</script>

<style scoped>
/* 响应式字体 - 使用 rem 和 clamp 实现比例缩放 */
.order-card {
  position: relative;
  padding-bottom: 120px;
  min-height: 500px;
  font-size: clamp(14px, 1.8vw, 18px);
}

/* 订单编号 */
.order-number {
  font-size: clamp(16px, 2vw, 22px);
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

/* 订单状态 */
.order-status {
  font-size: clamp(14px, 1.6vw, 18px);
  margin-bottom: 16px;
}

/* 状态标签 */
.status-tag-custom {
  font-size: clamp(12px, 1.4vw, 16px);
  padding: 4px 12px;
}

/* 表格样式 */
.order-items-table :deep(.ant-table) {
  font-size: clamp(12px, 1.4vw, 16px);
}

.order-items-table :deep(.ant-table-thead > tr > th) {
  font-size: clamp(13px, 1.5vw, 17px);
  font-weight: 600;
}

.order-items-table :deep(.ant-table-tbody > tr > td) {
  font-size: clamp(12px, 1.4vw, 16px);
  padding: 12px 8px;
}

/* 区块标题 */
.section-title {
  font-size: clamp(16px, 1.8vw, 20px);
  font-weight: 600;
  margin-bottom: 16px;
  color: #ff7a1a;
}

/* 骑手信息 */
.rider-info p {
  font-size: clamp(13px, 1.5vw, 17px);
  margin-bottom: 8px;
}

.rider-name, .rider-phone, .rider-location {
  font-size: clamp(13px, 1.5vw, 17px);
}

/* 价格区块 */
.price-summary {
  position: absolute;
  bottom: 20px;
  right: 20px;
  text-align: right;
}

.pack-amount, .pay-method {
  font-size: clamp(12px, 1.4vw, 16px);
  color: #666;
  margin-bottom: 4px;
}

.total-amount {
  font-weight: bold;
  margin-top: 8px;
  font-size: clamp(18px, 2.2vw, 24px);
  color: #fa541c;
}

/* 按钮样式 */
.cancel-btn .ant-btn, .confirm-btn .ant-btn {
  font-size: clamp(14px, 1.6vw, 18px);
  height: auto;
  padding: 8px 24px;
  border-radius: 24px;
}

.cancel-btn {
  position: absolute;
  bottom: 20px;
  left: 20px;
}

.confirm-btn {
  position: absolute;
  bottom: 20px;
  left: 140px;
}

/* 骑手地图样式 */
.rider-map-section {
  margin-top: 20px;
  padding: 16px;
  background: #f5f5f5;
  border-radius: 12px;
}

.rider-map-section h3 {
  font-size: clamp(15px, 1.7vw, 19px);
  margin-bottom: 12px;
  color: #ff7a1a;
}

.rider-map-section h3 .anticon {
  margin-right: 8px;
  font-size: clamp(14px, 1.6vw, 18px);
}

.rider-map {
  height: 350px;
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e8e8e8;
}

.location-info {
  margin-top: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.location-tag {
  font-size: clamp(12px, 1.4vw, 15px);
  padding: 4px 12px;
}

.update-time {
  font-size: clamp(11px, 1.2vw, 14px);
  color: #999;
}

/* 响应式 - 小屏幕 */
@media (max-width: 768px) {
  .rider-map {
    height: 250px;
  }

  .cancel-btn, .confirm-btn {
    position: static;
    margin-top: 10px;
    display: inline-block;
  }

  .cancel-btn {
    left: auto;
    margin-right: 10px;
  }

  .confirm-btn {
    left: auto;
  }

  .price-summary {
    position: static;
    margin-top: 20px;
    text-align: right;
  }
}

/* 大屏幕优化 */
@media (min-width: 1200px) {
  .order-card {
    max-width: 1200px;
    margin: 0 auto;
  }

  .order-number {
    font-size: 24px;
  }

  .total-amount {
    font-size: 28px;
  }
}
</style>