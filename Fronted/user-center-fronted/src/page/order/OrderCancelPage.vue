<template>
  <a-card title="取消訂單" class="cancel-card">
    <p>訂單編號：{{ orderId }}</p>

    <a-form layout="vertical" @submit.prevent="submitCancel">
      <a-form-item label="取消原因">
        <a-textarea
            v-model:value="cancelReason"
            rows="4"
            placeholder="請輸入取消原因"
        />
      </a-form-item>

      <a-form-item>
        <a-button type="primary" danger @click="submitCancel">
          提交取消
        </a-button>
      </a-form-item>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

// 從 query 取得訂單編號
const orderId = route.query.orderId as string;

const cancelReason = ref("");

// 提交取消原因
function submitCancel() {
  if (!cancelReason.value) {
    alert("請輸入取消原因！");
    return;
  }

  // 這裡之後會呼叫後端 API，例如 /api/orders/cancel
  console.log("取消訂單:", orderId, "原因:", cancelReason.value);

  // 提交後跳轉回顧客訂單頁面
  router.push("/order/customer");
}
</script>

<style scoped>
.cancel-card {
  max-width: 600px;
  margin: 40px auto;
}
</style>
