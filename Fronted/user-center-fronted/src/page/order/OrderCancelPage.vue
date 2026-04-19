<template>
  <a-card title="取消訂單" class="cancel-card">
    <p>訂單編號：{{ orderId }}</p>
    <p v-if="orderStatus" class="order-status">
      當前狀態：<a-tag :color="getStatusColor(orderStatus)">{{ getStatusText(orderStatus) }}</a-tag>
    </p>

    <a-form layout="vertical" @submit.prevent="submitCancel">
      <a-form-item label="取消原因" required>
        <a-textarea
            v-model:value="cancelReason"
            rows="4"
            placeholder="請輸入取消原因（例如：我不想要了、買錯了等）"
        />
      </a-form-item>

      <a-form-item>
        <a-space>
          <a-button type="primary" danger :loading="submitting" @click="submitCancel">
            提交取消申請
          </a-button>
          <a-button @click="goBack">返回</a-button>
        </a-space>
      </a-form-item>
    </a-form>

    <div class="cancel-info">
      <a-alert
          message="取消訂單說明"
          description="取消申請提交後，訂單狀態將變為「待退款」，管理員審核後會處理退款。"
          type="info"
          show-icon
      />
    </div>
  </a-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { message } from "ant-design-vue";
import { updateOrderStatus, getOrderDetails } from "@/api/order";

const route = useRoute();
const router = useRouter();

// 從 query 取得訂單編號
const orderId = route.query.orderId as string;
const orderDatabaseId = route.query.orderDatabaseId as string;
const cancelReason = ref("");
const submitting = ref(false);
const orderStatus = ref<number | null>(null);

// 获取订单状态
onMounted(async () => {
  if (orderDatabaseId) {
    try {
      const order = await getOrderDetails(parseInt(orderDatabaseId));
      if (order) {
        orderStatus.value = order.status;
      }
    } catch (error) {
      console.error("获取订单状态失败:", error);
    }
  }
});

// 提交取消原因
async function submitCancel() {
  if (!cancelReason.value.trim()) {
    message.warning("請輸入取消原因！");
    return;
  }

  if (!orderDatabaseId) {
    message.error("訂單ID不存在");
    return;
  }

  submitting.value = true;

  try {
    // 🔥 调用取消订单API，会自动转为状态8（待退款）
    const success = await updateOrderStatus(orderDatabaseId, 7);

    if (success) {
      // TODO: 保存取消原因到后端（如果有单独的API）
      console.log("取消訂單:", orderId, "原因:", cancelReason.value);
      message.success('取消申請已提交，等待管理員審核');

      // 跳轉回顧客訂單頁面
      router.push("/order/customeorderlist");
    } else {
      message.error('取消訂單失敗');
    }
  } catch (error) {
    console.error("取消訂單失敗:", error);
    message.error('取消訂單失敗');
  } finally {
    submitting.value = false;
  }
}

function goBack() {
  router.back();
}

function getStatusText(status: number): string {
  const map: Record<number, string> = {
    1: '待付款',
    2: '待接單',
    3: '已接單',
    4: '製作中',
    5: '派送中',
    6: '已完成',
    7: '已取消',
    8: '待退款'
  };
  return map[status] || '未知';
}

function getStatusColor(status: number): string {
  const map: Record<number, string> = {
    1: 'purple',
    2: 'orange',
    3: 'blue',
    4: 'cyan',
    5: 'green',
    6: 'lime',
    7: 'red',
    8: 'gold'
  };
  return map[status] || 'default';
}
</script>

<style scoped>
.cancel-card {
  max-width: 600px;
  margin: 40px auto;
}

.order-status {
  margin-bottom: 16px;
}

.cancel-info {
  margin-top: 20px;
}
</style>