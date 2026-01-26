<template>
  <a-card title="騎手訂單">
    <p>訂單編號：{{ order.orderId }}</p>
    <p>金額：{{ order.totalAmount }} 元</p>
    <p>
      狀態：
      <a-tag :color="statusColor(order.status)">
        {{ statusText(order.status) }}
      </a-tag>
    </p>
    <p>顧客ID：{{ order.userId }}</p>
    <p>接單時間：{{ elapsedTime }} 秒</p>
  </a-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";

const order = ref({
  orderId: "ORD003",
  userId: 5,
  totalAmount: 50.0,
  status: 4,
});

const elapsedTime = ref(0);

onMounted(() => {
  setInterval(() => {
    elapsedTime.value++;
  }, 1000);
});

function statusText(status: number) {
  switch (status) {
    case 4: return "派送中";
    case 5: return "已完成";
    default: return "未知";
  }
}

function statusColor(status: number) {
  return status === 4 ? "orange" : "green";
}
</script>
