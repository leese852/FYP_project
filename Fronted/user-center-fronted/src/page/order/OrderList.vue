<template>
  <a-card title="我的訂單">
    <a-spin :spinning="loading">
      <a-list bordered itemLayout="horizontal">
        <a-list-item
            v-for="item in orders"
            :key="item.id"
            @click="goToOrder(item.id)"
            style="cursor: pointer"
        >
          <!-- 顯示用還是用 orderId -->
          <a-list-item-meta
              :title="`訂單編號: ${item.orderId}`"
              :description="`狀態: ${statusText(item.status)} | 總金額: $${item.totalAmount}`"
          />
        </a-list-item>

      </a-list>

      <!-- 空列表提示 -->
      <div v-if="!loading && orders.length === 0" style="text-align:center; margin-top:20px;">
        <a-empty description="暫無訂單" />
      </div>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { getUserOrders } from "@/api/order";
import type { Order } from "@/types/order";
import { useLoginUserStore } from "@/store/useLoginUserStore";

const router = useRouter();
const orders = ref<Order[]>([]);
const loading = ref(false);

const loginUserStore = useLoginUserStore();

onMounted(async () => {
  await loginUserStore.fetchLoginUser();

  if (!loginUserStore.loginUser?.id) {
    console.warn("未登入，跳轉到登入頁");
    router.push("/user/login");
    return;
  }

  loading.value = true;
  try {
    const res = await getUserOrders();
    orders.value = res;
    console.log("✅ 訂單接口返回原始數據:", res);
  } catch (err: any) {
    console.error("❌ 載入訂單失敗:", err);
    orders.value = [];
  } finally {
    loading.value = false;
  }
});

function goToOrder(id: number) {
  router.push({ name: "orderCustomer", params: { orderId: id } });
}

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
</script>
