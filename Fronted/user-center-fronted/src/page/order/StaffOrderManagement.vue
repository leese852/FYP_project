<!-- src/views/admin/StaffOrderManagement.vue -->
<template>
  <div class="staff-order-management">
    <a-page-header
        title="訂單管理"
        :sub-title="`當前狀態: ${currentStateLabel}`"
        @back="goBack"
    >
      <template #extra>
        <a-input-search
            v-model:value="searchText"
            placeholder="搜尋訂單編號"
            style="width: 300px"
            @search="handleSearch"
        />
      </template>
    </a-page-header>

    <div class="order-table">
      <a-table
          :dataSource="filteredOrders"
          :columns="columns"
          :loading="loading"
          :pagination="pagination"
          rowKey="id"
          @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'orderId'">
            <a @click="viewOrderDetail(record)">{{ record.orderId }}</a>
          </template>

          <template v-if="column.key === 'status'">
            <a-select
                :value="record.status"
            :disabled="!canChangeStatus(record.status)"
            style="width: 120px"
            @change="(value) => handleStatusChange(record, value)"
            >
            <a-select-option
                v-for="option in getAvailableStatusOptions(record.status)"
                :key="option.value"
                :value="option.value"
            >
              {{ option.label }}
            </a-select-option>
            </a-select>
          </template>

          <template v-if="column.key === 'totalAmount'">
            ${{ record.totalAmount?.toFixed(2) }}
          </template>

          <template v-if="column.key === 'createTime'">
            {{ formatDateTime(record.createTime) }}
          </template>

          <template v-if="column.key === 'actions'">
            <a-space>
              <a-button type="link" size="small" @click="viewOrderDetail(record)">
                詳情
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getAllOrders, updateOrderStatus } from '@/api/order'
import type { Order } from '@/types/order'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const allOrders = ref<Order[]>([])
const searchText = ref('')
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true
})

const columns = [
  { title: '訂單編號', dataIndex: 'orderId', key: 'orderId' },
  { title: '用戶ID', dataIndex: 'userId', key: 'userId' },
  { title: '狀態', key: 'status' },
  { title: '總金額', key: 'totalAmount' },
  { title: '支付方式', dataIndex: 'payMethod', key: 'payMethod' },
  { title: '下單時間', key: 'createTime' },
  { title: '操作', key: 'actions' }
]

// Get current state from query params
const currentState = computed(() => {
  const state = route.query.state
  if (state) {
    return Array.isArray(state) ? state.map(s => parseInt(s)) : [parseInt(state as string)]
  }

  const states = route.query.states
  if (states) {
    return (states as string).split(',').map(s => parseInt(s))
  }

  return [2, 3, 4, 5, 6, 7, 8] // Default: show all staff-visible states
})

const currentStateLabel = computed(() => {
  const stateMap: Record<number, string> = {
    2: '待接單',
    3: '已接單',
    4: '制作中',
    5: '派送中',
    6: '已完成',
    7: '已取消',
    8: '待退款'
  }

  if (currentState.value.length === 1) {
    return stateMap[currentState.value[0]] || '未知狀態'
  }

  if (JSON.stringify(currentState.value.sort()) === JSON.stringify([4,5,6])) {
    return '其他 (制作中、派送中、已完成)'
  }

  return `多個狀態: ${currentState.value.map(s => stateMap[s] || s).join(', ')}`
})

const filteredOrders = computed(() => {
  let orders = allOrders.value.filter(order =>
      currentState.value.includes(order.status)
  )

  if (searchText.value) {
    orders = orders.filter(order =>
        order.orderId.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }

  pagination.value.total = orders.length
  const start = (pagination.value.current - 1) * pagination.value.pageSize
  const end = start + pagination.value.pageSize
  return orders.slice(start, end)
})

// Check if staff can change this status
const canChangeStatus = (currentStatus: number) => {
  return [2, 3, 4, 7, 8].includes(currentStatus)
}

// Get available status options based on current status
// Get available status options based on current status
const getAvailableStatusOptions = (currentStatus: number) => {
  console.log('📋 获取状态选项 - 数据库当前状态:', currentStatus, getStatusText(currentStatus))

  const options = []

  // 状态映射表
  const statusLabels: Record<number, string> = {
    1: '待付款',
    2: '待接單',
    3: '已接單',
    4: '制作中',
    5: '派送中',
    6: '已完成',
    7: '已取消',
    8: '待退款'
  }

  // 根据当前状态添加可转换的目标状态
  if (currentStatus === 2) {
    options.push({ value: 3, label: statusLabels[3] })
    options.push({ value: 7, label: statusLabels[7] })
  } else if (currentStatus === 3) {
    options.push({ value: 4, label: statusLabels[4] })
    options.push({ value: 7, label: statusLabels[7] })
  } else if (currentStatus === 4) {
    options.push({ value: 5, label: statusLabels[5] })
    options.push({ value: 7, label: statusLabels[7] })
  } else if (currentStatus === 5) {
    options.push({ value: 6, label: statusLabels[6] })
    options.push({ value: 7, label: statusLabels[7] })
  } else if (currentStatus === 7 || currentStatus === 8) {
    // 已取消和待退款不能更改
  }

  console.log('📋 可用选项:', options)

  // 重要：不要添加当前状态！
  return options
}

// Handle status change
// 在 handleStatusChange 函数中修改
// Handle status change
const handleStatusChange = async (order: Order, newStatus: number) => {
  console.log('🎯 handleStatusChange 被调用', {
    订单对象: order,
    原状态: order.status,
    新状态: newStatus,
    订单ID类型: typeof order.id,
    订单编号类型: typeof order.orderId
  })

  if (order.status === newStatus) return

  const originalStatus = order.status

  try {
    loading.value = true

    console.log('🔍 验证状态转换:', {
      从: originalStatus,
      到: newStatus,
      有效: validateStatusTransition(originalStatus, newStatus)
    })

    if (!validateStatusTransition(originalStatus, newStatus)) {
      message.error('狀態轉換不符合規則')
      return
    }

    // 调试：打印要调用的URL
    console.log('📤 调用更新API:', {
      方法: 'PUT',
      URL: `/api/orders/${order.id}/status?status=${newStatus}`,
      订单ID: order.id,
      状态: newStatus
    })

    const success = await updateOrderStatus(order.id, newStatus)

    console.log('📥 API响应:', {
      成功: success,
      原始状态: originalStatus,
      新状态: newStatus
    })

    if (success) {
      message.success('訂單狀態更新成功')

      // 更新本地状态
      order.status = newStatus
      console.log('✅ 本地状态已更新')

      // 重新加载数据
      setTimeout(() => {
        loadOrders()
        console.log('🔄 重新加载订单列表')
      }, 500)
    } else {
      message.error('更新狀態失敗')
      order.status = originalStatus
      console.log('❌ API调用失败，状态已恢复')
    }

  } catch (error: any) {
    console.error('💥 发生异常:', error)
    message.error('更新狀態失敗: ' + (error.message || '未知錯誤'))
    order.status = originalStatus
  } finally {
    loading.value = false
  }
}
// Validate status transition rules
const validateStatusTransition = (from: number, to: number): boolean => {
  // State 1 is not visible to staff
  // Staff can only change states 2, 3, 4, 7, 8

  if (![2, 3, 4, 7, 8].includes(from)) {
    return false
  }

  // Rules:
  // 2 -> 3 (待接單 -> 已接單)
  // 2 -> 7 (待接單 -> 已取消)
  // 3 -> 4 (已接單 -> 制作中)
  // 3 -> 7 (已接單 -> 已取消)
  // 4 -> 5 (制作中 -> 派送中) - Note: but staff can't change to 5
  // 4 -> 7 (制作中 -> 已取消)

  const validTransitions = {
    2: [3, 7],     // 待接單 can go to 已接單 or 已取消
    3: [4, 7],     // 已接單 can go to 制作中 or 已取消
    4: [5, 7],     // 制作中 can go to 派送中 or 已取消
    7: [],         // 已取消 cannot change
    8: []          // 待退款 cannot change (needs special handling)
  }

  return validTransitions[from]?.includes(to) || false
}
const getStatusText = (status: number): string => {
  const statusMap: Record<number, string> = {
    1: '待付款',
    2: '待接單',
    3: '已接單',
    4: '制作中',
    5: '派送中',
    6: '已完成',
    7: '已取消',
    8: '待退款'
  }
  return statusMap[status] || `未知(${status})`
}
const loadOrders = async () => {
  try {
    loading.value = true
    const orders = await getAllOrders()

    // 检查所有订单状态
    console.log('📊 所有订单状态:', orders.map(o => ({
      id: o.id,
      orderId: o.orderId,
      status: o.status,
      statusText: getStatusText(o.status)
    })))

    // 特别检查这个订单
    const targetOrder = orders.find(o => o.orderId === 'ORD20260127001')
    if (targetOrder) {
      console.log('🎯 API返回的订单数据:', JSON.stringify(targetOrder, null, 2))
    }

    allOrders.value = orders
    pagination.value.total = orders.filter(order =>
        currentState.value.includes(order.status)
    ).length
  } catch (error) {
    message.error('載入訂單失敗')
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.value.current = 1
}

const handleTableChange = (paginationConfig: any) => {
  pagination.value = { ...pagination.value, ...paginationConfig }
}

const formatDateTime = (dateTime: string) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-TW')
}

const viewOrderDetail = (order: Order) => {
  router.push(`/admin/orders/${order.id}`)
}

const goBack = () => {
  router.push('/admin/orders')
}

// Watch for route changes to reload data
watch(() => route.query, () => {
  loadOrders()
  pagination.value.current = 1
})

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.staff-order-management {
  background: white;
  padding: 20px;
  border-radius: 8px;
}

.order-table {
  margin-top: 20px;
}

:deep(.ant-table-row) {
  cursor: pointer;
}

:deep(.ant-table-row:hover) {
  background-color: #fafafa;
}
</style>