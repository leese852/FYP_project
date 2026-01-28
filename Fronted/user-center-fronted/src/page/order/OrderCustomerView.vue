<template>
  <a-card title="訂單詳情" class="order-card">
    <!-- 訂單基本資訊 -->
    <p>訂單編號：{{ order?.orderId }}</p>
    <p>
      狀態：
      <a-tag :color="statusColor(order?.status)">
        {{ statusText(order?.status) }}
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

    <!-- 🚚 派送中顯示騎手資訊 -->
    <div v-if="order?.status === 4 && order?.rider" style="margin-top: 20px">
      <p>騎手姓名：{{ order.rider.name }}</p>
      <p>騎手電話：{{ order.rider.phone }}</p>
      <p>派送位置：{{ order.rider.location }}</p>
    </div>

    <!-- 💰 價格區塊 -->
    <div class="price-summary" v-if="order">
      <div>打包費: ${{ order.packAmount }}</div>
      <div>支付方式: {{ order.payMethod }}</div>
      <div class="total">Total amount: ${{ order.totalAmount }}</div>
    </div>

    <!-- ❌ 取消訂單按鈕 -->
    <div class="cancel-btn" v-if="order">
      <a-button type="primary" danger @click="confirmCancel">取消訂單</a-button>
    </div>

    <!-- 🚚 確認送達按鈕 -->
    <div class="confirm-btn" v-if="order?.status === 4">
      <a-button type="primary" @click="confirmDelivered">確認送達</a-button>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { Modal } from "ant-design-vue";
import { useRouter, useRoute } from "vue-router";
import { getOrderDetails, updateOrderStatus } from "@/api/order";
import type { Order } from "@/types/order";

const router = useRouter();
const route = useRoute();

const order = ref<Order | null>(null);

const itemColumns = [
  { title: "菜品名稱", dataIndex: "dishName", key: "dishName" },
  { title: "口味", dataIndex: "dishFlavor", key: "dishFlavor" },
  { title: "數量", dataIndex: "quantity", key: "quantity" },
  { title: "單價", dataIndex: "price", key: "price" },
];

// 🚀 當頁面載入時，根據路由參數取訂單詳情
onMounted(async () => {
  const id = Number(route.params.orderId); // 數字 id
  if (!id) {
    console.error("❌ 沒有訂單 id");
    return;
  }
  try {
    const res = await getOrderDetails(id);
    order.value = res;
    console.log("✅ 訂單詳情:", res);
  } catch (err) {
    console.error("❌ 載入訂單失敗:", err);
  }
});

function statusText(status?: number) {
  switch (status) {
    case 1: return "待付款";
    case 2: return "待接單";
    case 3: return "已接單";
    case 4: return "派送中";
    case 5: return "已完成";
    case 6: return "已取消";
    case 7: return "退款";
    default: return "未知";
  }
}

function statusColor(status?: number) {
  switch (status) {
    case 1: return "purple";
    case 2: return "blue";
    case 3: return "green";
    case 4: return "orange";
    case 5: return "cyan";
    case 6: return "red";
    case 7: return "magenta";
    default: return "default";
  }
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
      router.push({ path: "/order/cancel", query: { orderId: order.value?.id } });
    },
  });
}

// 🚚 確認送達
async function confirmDelivered() {
  if (!order.value) return;
  try {
    const success = await updateOrderStatus(order.value.id, 5); // 更新為已完成
    if (success) {
      order.value.status = 5; // 前端同步更新狀態
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
