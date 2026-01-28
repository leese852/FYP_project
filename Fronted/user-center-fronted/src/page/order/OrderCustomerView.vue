<template>
  <a-card title="訂單詳情" class="order-card">
    <a-spin :spinning="loading">
      <template v-if="order">
        <!-- 訂單基本資訊 -->
        <p>訂單編號：{{ order.orderId }}</p>
        <p>狀態：
          <a-tag :color="statusColor(order.status)">
            {{ statusText(order.status) }}
          </a-tag>
        </p>
        <p>總金額：${{ order.totalAmount }}</p>
        <p>打包費：${{ order.packAmount }}</p>
        <p>支付方式：{{ order.payMethod }}</p>
        <p>下單時間：{{ order.createTime }}</p>

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

        <!-- 💰 價格區塊 -->
        <div class="price-summary">
          <div v-for="item in order.items" :key="item.id">
            - {{ item.dishName }} x{{ item.quantity }} ${{ (item.quantity || 0) * (item.price || 0) }}
          </div>
          <div>打包費: ${{ order.packAmount }}</div>
          <div>支付方式: {{ order.payMethod }}</div>
          <div class="total">Total amount: ${{ order.totalAmount }}</div>
        </div>

        <!-- ❌ 取消訂單按鈕 -->
        <div class="cancel-btn">
          <a-button type="primary" danger @click="confirmCancel">取消訂單</a-button>
        </div>
      </template>

      <template v-else>
        <a-empty description="未找到該訂單" />
      </template>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { Modal, message } from "ant-design-vue";
import { useRoute, useRouter } from "vue-router";
import { getOrderDetails } from "@/api/order";

const route = useRoute();
const router = useRouter();

interface OrderItem {
  id: number;
  dishName: string;
  dishFlavor?: string;
  quantity: number;
  price: number;
}

interface OrderDetail {
  orderId: string;
  totalAmount: number;
  status: number;
  packAmount: number;
  payMethod: string;
  createTime?: string;
  items: OrderItem[];
}

const order = ref<OrderDetail | null>(null);
const loading = ref(false);

const itemColumns = [
  { title: "菜品名稱", dataIndex: "dishName", key: "dishName" },
  { title: "口味", dataIndex: "dishFlavor", key: "dishFlavor" },
  { title: "數量", dataIndex: "quantity", key: "quantity" },
  { title: "單價", dataIndex: "price", key: "price" },
];

onMounted(async () => {
  const idParam = route.query.id as string | undefined;
  const id = idParam ? Number(idParam) : NaN;
  if (!id || Number.isNaN(id)) {
    message.error("缺少訂單 ID");
    router.push("/order/customeorderlist");
    return;
  }

  loading.value = true;
  try {
    const data = await getOrderDetails(id);
    if (!data) {
      message.error("未找到訂單信息");
      order.value = null;
    } else {
      // 後端返回的是 OrderVO，字段名與這裡定義基本一致
      order.value = {
        orderId: data.orderId,
        totalAmount: data.totalAmount,
        status: data.status,
        packAmount: data.packAmount,
        payMethod: data.payMethod,
        createTime: data.createTime,
        // @ts-ignore
        items: data.items || [],
      };
    }
  } catch (e: any) {
    console.error("載入訂單詳情失敗:", e);
    message.error(e?.message || "載入訂單詳情失敗");
    order.value = null;
  } finally {
    loading.value = false;
  }
});

function statusText(status: number) {
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

function statusColor(status: number) {
  switch (status) {
    case 1: return "gold";
    case 2: return "blue";
    case 3: return "green";
    case 4: return "orange";
    case 5: return "cyan";
    case 6: return "red";
    case 7: return "purple";
    default: return "default";
  }
}

// 🚨 取消訂單確認（目前只跳轉到取消頁面，後端狀態更新另行實現）
function confirmCancel() {
  if (!order.value) {
    return;
  }
  Modal.confirm({
    title: "確認取消訂單",
    content: "您確定要取消這個訂單嗎？",
    okText: "是",
    cancelText: "否",
    okType: "danger",
    async onOk() {
      router.push({ path: "/order/cancel", query: { orderId: order.value?.orderId } });
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
