<!-- src/page/order/OrderDetail.vue -->
<template>
  <a-card title="訂單詳情" class="order-card">
    <!-- 訂單基本資訊 -->
    <p>訂單編號：{{ order?.orderId }}</p>
    <p>
      狀態：
      <a-tag :color="getStatusColor(order?.status)">
        {{ getStatusLabel(order?.status) }}
      </a-tag>
    </p>

    <!-- 🥘 顯示訂單菜品列表 -->
    <a-table
        v-if="order?.items"
        :dataSource="order.items"
        :columns="itemColumns"
        rowKey="id"
        size="small"
        bordered
        style="margin-top: 20px"
        :pagination="false"
    />

    <!-- 狀態管理 (員工可以變更的狀態) -->
    <div class="status-management" v-if="canChangeStatus(order?.status)">
      <h3>狀態管理</h3>
      <div class="status-actions">
        <a-space>
          <template v-for="option in getAvailableStatusOptions(order?.status)" :key="option.value">
            <a-button
                :type="option.value === order?.status ? 'default' : 'primary'"
                :disabled="option.value === order?.status"
                @click="handleStatusChange(option.value)"
            >
              {{ option.label }}
            </a-button>
          </template>
        </a-space>
      </div>
    </div>

    <!-- 🚚 派送中顯示騎手資訊 -->
    <div v-if="order?.status === 5 && order?.rider" style="margin-top: 20px">
      <h3>騎手資訊</h3>
      <p>騎手姓名：{{ order.rider.name }}</p>
      <p>騎手電話：{{ order.rider.phone }}</p>
      <p>派送位置：{{ order.rider.location }}</p>
    </div>

    <!-- 💰 價格區塊 -->
    <div class="price-summary" v-if="order">
      <div>打包費: ${{ order.packAmount }}</div>
      <div>支付方式: {{ order.payMethod }}</div>
      <div class="total">總金額: ${{ order.totalAmount }}</div>
    </div>

    <!-- ❌ 取消訂單按鈕 (根據狀態顯示) -->
    <div class="cancel-btn" v-if="canCancelOrder(order?.status)">
      <a-button type="primary" danger @click="confirmCancel">取消訂單</a-button>
    </div>

    <!-- 🚚 確認送達按鈕 (僅派送中狀態) -->
    <div class="confirm-btn" v-if="order?.status === 5">
      <a-button type="primary" @click="confirmDelivered">確認送達</a-button>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { Modal, message } from "ant-design-vue";
import { useRouter, useRoute } from "vue-router";
import { getOrderDetails, updateOrderStatus } from "@/api/order";
import type { Order } from "@/types/order";

const router = useRouter();
const route = useRoute();

const order = ref<Order | null>(null);
const loading = ref(false);

const itemColumns = [
  { title: "菜品名稱", dataIndex: "dishName", key: "dishName" },
  { title: "口味", dataIndex: "dishFlavor", key: "dishFlavor" },
  { title: "數量", dataIndex: "quantity", key: "quantity" },
  { title: "單價", dataIndex: "price", key: "price" },
];

// 🚀 當頁面載入時，根據路由參數取訂單詳情
onMounted(async () => {
  const id = Number(route.params.id || route.params.orderId);
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
  } catch (err) {
    console.error("❌ 載入訂單失敗:", err);
    message.error("載入訂單失敗");
  } finally {
    loading.value = false;
  }
});

// 狀態標籤和顏色
function getStatusLabel(status?: number) {
  switch (status) {
    case 1: return "待付款";
    case 2: return "待接單";
    case 3: return "已接單";
    case 4: return "制作中";
    case 5: return "派送中";
    case 6: return "已完成";
    case 7: return "已取消";
    case 8: return "退款";
    default: return "未知";
  }
}

function getStatusColor(status?: number) {
  switch (status) {
    case 1: return "purple";
    case 2: return "orange";
    case 3: return "blue";
    case 4: return "purple";  // 制作中
    case 5: return "cyan";    // 派送中
    case 6: return "green";   // 已完成
    case 7: return "red";     // 已取消
    case 8: return "gold";    // 退款
    default: return "default";
  }
}

// 檢查員工是否可以變更此狀態
const canChangeStatus = (status?: number): boolean => {
  if (!status) return false;
  // 員工只能變更狀態 2, 3, 4, 7, 8
  return [2, 3, 4, 7, 8].includes(status);
}

// 檢查是否可以取消訂單
const canCancelOrder = (status?: number): boolean => {
  if (!status) return false;
  // 狀態 2, 3, 4 可以取消
  return [2, 3, 4].includes(status);
}

// 獲取可用的狀態選項
const getAvailableStatusOptions = (currentStatus?: number) => {
  if (!currentStatus) return [];

  const options = [];

  // 根據新的狀態轉換規則：
  // 2(待接單) → 3(已接單), 7(已取消)
  // 3(已接單) → 4(制作中), 7(已取消)
  // 4(制作中) → 5(派送中), 7(已取消)
  // 5(派送中) → 6(已完成) [員工不能變更]
  // 7(已取消) → 無
  // 8(待退款) → 無

  if (currentStatus === 2) {
    options.push({ value: 3, label: '已接單' });
    options.push({ value: 7, label: '已取消' });
  } else if (currentStatus === 3) {
    options.push({ value: 4, label: '制作中' });
    options.push({ value: 7, label: '已取消' });
  } else if (currentStatus === 4) {
    options.push({ value: 5, label: '派送中' });
    options.push({ value: 7, label: '已取消' });
  }

  // 總是包含當前狀態
  options.unshift({
    value: currentStatus,
    label: `${getStatusLabel(currentStatus)} (保持不變)`
  });

  return options;
}

// 處理狀態變更
const handleStatusChange = async (newStatus: number) => {
  if (!order.value || order.value.status === newStatus) return;

  // 驗證狀態轉換規則
  const validTransitions: Record<number, number[]> = {
    2: [3, 7],  // 待接單 → 已接單, 已取消
    3: [4, 7],  // 已接單 → 制作中, 已取消
    4: [5, 7],  // 制作中 → 派送中, 已取消
    5: [6],     // 派送中 → 已完成 (但員工不能變更)
    7: [],      // 已取消 → 無
    8: []       // 待退款 → 無
  };

  const currentStatus = order.value.status;

  if (!validTransitions[currentStatus]?.includes(newStatus)) {
    message.error(`無法從 ${getStatusLabel(currentStatus)} 變更為 ${getStatusLabel(newStatus)}`);
    return;
  }

  Modal.confirm({
    title: '確認變更狀態',
    content: `確定要將訂單 ${order.value.orderId} 從 ${getStatusLabel(currentStatus)} 變更為 ${getStatusLabel(newStatus)} 嗎？`,
    okText: '確認',
    cancelText: '取消',
    async onOk() {
      try {
        loading.value = true;
        const success = await updateOrderStatus(order.value.orderId, newStatus);

        if (success) {
          message.success('狀態變更成功');
          // 重新加載訂單數據
          const res = await getOrderDetails(order.value.id);
          order.value = res;
        } else {
          message.error('狀態變更失敗');
        }
      } catch (error: any) {
        message.error('狀態變更失敗: ' + (error.message || '未知錯誤'));
      } finally {
        loading.value = false;
      }
    }
  });
}

// ❌ 取消訂單
function confirmCancel() {
  if (!order.value) return;
  Modal.confirm({
    title: "確認取消訂單",
    content: "您確定要取消這個訂單嗎？",
    okText: "是",
    cancelText: "否",
    okType: "danger",
    async onOk() {
      try {
        loading.value = true;
        const success = await updateOrderStatus(order.value.orderId, 7); // 更新為已取消

        if (success) {
          message.success('訂單已取消');
          // 重新加載訂單數據
          const res = await getOrderDetails(order.value.id);
          order.value = res;
        } else {
          message.error('取消訂單失敗');
        }
      } catch (error) {
        message.error('取消訂單失敗');
      } finally {
        loading.value = false;
      }
    },
  });
}

// 🚚 確認送達
async function confirmDelivered() {
  if (!order.value) return;
  try {
    const success = await updateOrderStatus(order.value.orderId, 6); // 更新為已完成
    if (success) {
      order.value.status = 6; // 前端同步更新狀態
      Modal.success({
        title: "確認成功",
        content: "訂單已確認送達！",
      });
    } else {
      Modal.error({
        title: "操作失敗",
        content: "無法更新訂單狀態，請稍後再試。",
      });
    }
  } catch (err) {
    console.error("❌ 確認送達失敗:", err);
    Modal.error({
      title: "操作失敗",
      content: "系統錯誤，請稍後再試。",
    });
  }
}
</script>

<style scoped>
.order-card {
  position: relative;
  padding-bottom: 120px;
  min-height: 500px;
}

.status-management {
  margin-top: 20px;
  padding: 16px;
  background: #f5f5f5;
  border-radius: 8px;
}

.status-management h3 {
  margin-bottom: 12px;
  color: #1890ff;
}

.status-actions {
  margin-top: 10px;
}

.price-summary {
  position: absolute;
  bottom: 20px;
  right: 20px;
  text-align: right;
  font-size: 14px;
  color: #333;
}
.price-summary .total {
  font-weight: bold;
  margin-top: 8px;
  font-size: 18px;
  color: #fa541c;
}

.cancel-btn {
  position: absolute;
  bottom: 20px;
  left: 20px;
}

.confirm-btn {
  position: absolute;
  bottom: 20px;
  left: 140px; /* 避免和取消按鈕重疊 */
}
</style>