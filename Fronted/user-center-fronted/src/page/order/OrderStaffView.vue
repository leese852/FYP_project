<template>
  <a-table :dataSource="orders" :columns="columns" rowKey="orderId" bordered>
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'status'">
        <a-select v-model:value="record.status" style="width: 120px">
          <a-select-option value="2">待接單</a-select-option>
          <a-select-option value="3">已接單</a-select-option>
          <a-select-option value="4">派送中</a-select-option>
          <a-select-option value="5">已完成</a-select-option>
          <a-select-option value="6">已取消</a-select-option>
        </a-select>
      </template>
    </template>
  </a-table>
</template>

<script setup lang="ts">
import { ref } from "vue";

const orders = ref([
  { orderId: "ORD001", userId: 1, status: 2, totalAmount: 120.5 },
  { orderId: "ORD002", userId: 2, status: 4, totalAmount: 80.0 },
]);

const columns = [
  { title: "訂單編號", dataIndex: "orderId", key: "orderId" },
  { title: "用户ID", dataIndex: "userId", key: "userId" },
  { title: "狀態", key: "status" },
  { title: "總金額", dataIndex: "totalAmount", key: "totalAmount" },
];
</script>
