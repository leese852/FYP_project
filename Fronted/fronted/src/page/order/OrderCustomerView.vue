<template>
  <a-card title="訂單詳情" class="order-card">
    <!-- 訂單基本資訊 -->
    <p>訂單編號：{{ order.orderId }}</p>
    <p>
      狀態：
      <a-tag :color="statusColor(order.status)">
        {{ statusText(order.status) }}
      </a-tag>
    </p>

    <!-- 🥘 顯示訂單菜品列表 -->
    <a-table
        :dataSource="order.items"
        :columns="itemColumns"
        rowKey="id"
        size="small"
        bordered
        style="margin-top: 20px"
        :pagination="false"
    />

    <!-- 🚚 派送中顯示騎手資訊 -->
    <div v-if="order.status === 4" style="margin-top: 20px">
      <p>騎手姓名：{{ order.rider?.name }}</p>
      <p>騎手電話：{{ order.rider?.phone }}</p>
      <p>派送位置：{{ order.rider?.location }}</p>
    </div>

    <!-- 💰 價格區塊 -->
    <!-- 💰 價格區塊 -->
    <div class="price-summary">
      <div v-for="item in order.items" :key="item.id">
        - {{ item.dishName }} x{{ item.quantity }} ${{ item.quantity * item.price }}
      </div>
      <div>打包費: ${{ order.packAmount }}</div>
      <div>支付方式: {{ order.payMethod }}</div>
      <div class="total">Total amount: ${{ order.totalAmount }}</div>
    </div>


    <!-- ❌ 取消訂單按鈕 -->
    <div class="cancel-btn">
      <a-button type="primary" danger @click="confirmCancel">取消訂單</a-button>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { Modal } from "ant-design-vue";
import { useRouter } from "vue-router";

const router = useRouter();

const order = ref({
  orderId: "ORD001",
  totalAmount: 120.5,
  status: 4,
  rider: { name: "王小明", phone: "98765432", location: "九龍城區" },
  packAmount: 10,
  payMethod: "信用卡",
  items: [
    { id: 1, dishName: "宮保雞丁", dishFlavor: "微辣", quantity: 2, price: 40 },
    { id: 2, dishName: "酸辣湯", dishFlavor: "正常", quantity: 1, price: 30 },
  ],
});

const itemColumns = [
  { title: "菜品名稱", dataIndex: "dishName", key: "dishName" },
  { title: "口味", dataIndex: "dishFlavor", key: "dishFlavor" },
  { title: "數量", dataIndex: "quantity", key: "quantity" },
  { title: "單價", dataIndex: "price", key: "price" },
];

function statusText(status: number) {
  switch (status) {
    case 2: return "待接單";
    case 3: return "已接單";
    case 4: return "派送中";
    case 5: return "已完成";
    case 6: return "已取消";
    default: return "未知";
  }
}

function statusColor(status: number) {
  switch (status) {
    case 2: return "blue";
    case 3: return "green";
    case 4: return "orange";
    case 5: return "cyan";
    case 6: return "red";
    default: return "default";
  }
}

// 🚨 取消訂單確認
function confirmCancel() {
  Modal.confirm({
    title: "確認取消訂單",
    content: "您確定要取消這個訂單嗎？",
    okText: "是",
    cancelText: "否",
    okType: "danger",
    async onOk() {
      // 跳轉到取消原因頁面
      router.push({ path: "/order/cancel", query: { orderId: order.value.orderId } });
    },
  });
}
</script>

<style scoped>
.order-card {
  position: relative;
  padding-bottom: 120px; /* 預留底部空間給價格區塊和按鈕 */
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
</style>
