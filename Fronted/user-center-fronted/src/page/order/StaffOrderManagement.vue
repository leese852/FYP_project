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
            <div class="status-cell">
              <a-tag :color="getStatusColor(record.status)">
                {{ getStatusText(record.status) }}
              </a-tag>
              <!-- 🔥 待退款状态显示处理退款按钮 -->
              <a-button
                  v-if="record.status === 8"
                  type="link"
                  size="small"
                  danger
                  @click.stop="handleRefundOrder(record)"
              >
                處理退款
              </a-button>
              <!-- 其他状态显示下拉选择 -->
              <a-select
                  v-else
                  :value="record.status"
                  :disabled="!canChangeStatus(record.status)"
                  style="width: 120px"
                  @change="(value: number) => handleStatusChange(record, value)"
              >
                <a-select-option
                    v-for="option in getAvailableStatusOptions(record.status)"
                    :key="option.value"
                    :value="option.value"
                >
                  {{ option.label }}
                </a-select-option>
              </a-select>
            </div>
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
import { message, Modal } from 'ant-design-vue'
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
  { title: '狀態', key: 'status', width: '200px' },
  { title: '總金額', key: 'totalAmount' },
  { title: '支付方式', dataIndex: 'payMethod', key: 'payMethod' },
  { title: '下單時間', key: 'createTime' },
  { title: '操作', key: 'actions' }
]

// 获取状态文本
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

// 获取状态颜色
const getStatusColor = (status: number): string => {
  const colorMap: Record<number, string> = {
    1: 'purple',
    2: 'orange',
    3: 'blue',
    4: 'cyan',
    5: 'green',
    6: 'lime',
    7: 'red',
    8: 'gold'
  }
  return colorMap[status] || 'default'
}

// 🔥 计算属性：过滤后的订单
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

// 获取当前状态参数
const currentState = computed(() => {
  const state = route.query.state
  const states = route.query.states

  // 处理单个状态参数
  if (state !== undefined && state !== null) {
    if (Array.isArray(state)) {
      return state.filter(s => s !== null).map(s => parseInt(s as string))
    } else if (typeof state === 'string') {
      return [parseInt(state)]
    }
  }

  // 处理多个状态参数（用逗号分隔）
  if (states !== undefined && states !== null && typeof states === 'string') {
    return states.split(',').map(s => parseInt(s))
  }

  // 默认显示所有员工可见的状态
  return [2, 3, 4, 5, 6, 7, 8]
})

// 当前状态标签
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

  if (JSON.stringify(currentState.value.sort()) === JSON.stringify([4, 5, 6])) {
    return '其他 (制作中、派送中、已完成)'
  }

  return `多個狀態: ${currentState.value.map(s => stateMap[s] || s).join(', ')}`
})

// 检查是否可以更改状态
const canChangeStatus = (currentStatus: number): boolean => {
  return [2, 3, 4, 7].includes(currentStatus)
}

// 获取可用的状态选项
const getAvailableStatusOptions = (currentStatus: number) => {
  const options: { value: number; label: string }[] = []

  const statusLabels: Record<number, string> = {
    2: '待接單',
    3: '已接單',
    4: '制作中',
    5: '派送中',
    6: '已完成',
    7: '已取消',
    8: '待退款'
  }

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
  }

  return options
}

// 🔥 处理退款订单（将状态8改为状态7）
const handleRefundOrder = (order: Order) => {
  Modal.confirm({
    title: '確認退款',
    content: `確定要將訂單 ${order.orderId} 從「待退款」改為「已取消」並退款給顧客嗎？`,
    okText: '確認退款',
    cancelText: '取消',
    okType: 'danger',
    async onOk() {
      try {
        loading.value = true
        const success = await updateOrderStatus(order.id.toString(), 7)

        if (success) {
          message.success('退款處理成功，訂單已取消')
          await loadOrders()
        } else {
          message.error('操作失敗')
        }
      } catch (error) {
        console.error('退款處理失敗:', error)
        message.error('退款處理失敗')
      } finally {
        loading.value = false
      }
    }
  })
}

// 处理状态变更
const handleStatusChange = async (order: Order, newStatus: number) => {
  if (order.status === newStatus) return

  const originalStatus = order.status

  try {
    loading.value = true

    if (!validateStatusTransition(originalStatus, newStatus)) {
      message.error('狀態轉換不符合規則')
      return
    }

    const success = await updateOrderStatus(order.id.toString(), newStatus)

    if (success) {
      message.success('訂單狀態更新成功')
      order.status = newStatus
      setTimeout(() => {
        loadOrders()
      }, 500)
    } else {
      message.error('更新狀態失敗')
      order.status = originalStatus
    }
  } catch (error: any) {
    console.error('发生异常:', error)
    message.error('更新狀態失敗: ' + (error.message || '未知錯誤'))
    order.status = originalStatus
  } finally {
    loading.value = false
  }
}

// 验证状态转换规则
const validateStatusTransition = (from: number, to: number): boolean => {
  if (![2, 3, 4, 5, 7].includes(from)) {
    return false
  }

  const validTransitions: Record<number, number[]> = {
    2: [3, 7],
    3: [4, 7],
    4: [5, 7],
    5: [6, 7],
    7: []
  }

  return validTransitions[from]?.includes(to) || false
}

// 加载订单
const loadOrders = async () => {
  try {
    loading.value = true
    const orders = await getAllOrders()
    allOrders.value = orders
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

.status-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

:deep(.ant-table-row) {
  cursor: pointer;
}

:deep(.ant-table-row:hover) {
  background-color: #fafafa;
}
</style>