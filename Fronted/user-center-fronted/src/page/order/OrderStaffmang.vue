<!-- src/views/admin/OrderDashboard.vue -->
<template>
  <div class="order-dashboard">
    <h2>訂單管理</h2>
    <div class="dashboard-stats">
      <!-- Box 1: 待接單 (State 2) -->
      <div class="stat-box" @click="viewOrdersByState(2)">
        <div class="stat-header">
          <h3>待接單</h3>
          <span class="stat-count">{{ stats.pendingAcceptance }}</span>
        </div>
        <p class="stat-subtitle">{{ stats.pendingAcceptance }} 筆訂單待處理</p>
        <div class="stat-footer">
          <span class="state-label">狀態: 2</span>
          <a-button type="link" size="small">查看詳情 →</a-button>
        </div>
      </div>

      <!-- Box 2: 已接單 (State 3) -->
      <div class="stat-box" @click="viewOrdersByState(3)">
        <div class="stat-header">
          <h3>已接單</h3>
          <span class="stat-count">{{ stats.accepted }}</span>
        </div>
        <p class="stat-subtitle">{{ stats.accepted }} 筆訂單已接單</p>
        <div class="stat-footer">
          <span class="state-label">狀態: 3</span>
          <a-button type="link" size="small">查看詳情 →</a-button>
        </div>
      </div>

      <!-- Box 3: 待退款 (State 8) -->
      <div class="stat-box" @click="viewOrdersByState(8)">
        <div class="stat-header">
          <h3>待退款</h3>
          <span class="stat-count">{{ stats.pendingRefund }}</span>
        </div>
        <p class="stat-subtitle">{{ stats.pendingRefund }} 筆訂單待退款</p>
        <div class="stat-footer">
          <span class="state-label">狀態: 8</span>
          <a-button type="link" size="small">查看詳情 →</a-button>
        </div>
      </div>

      <!-- Box 4: 其他 (States 4,5,6) -->
      <div class="stat-box" @click="viewOtherOrders()">
        <div class="stat-header">
          <h3>其他</h3>
          <span class="stat-count">{{ stats.others }}</span>
        </div>
        <div class="stat-details">
          <div class="stat-item">
            <span class="stat-state">派送中 (4):</span>
            <span class="stat-number">{{ stats.delivering }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-state">已完成 (5):</span>
            <span class="stat-number">{{ stats.completed }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-state">已完成 (6):</span>
            <span class="stat-number">{{ stats.completedOld }}</span>
          </div>
        </div>
        <div class="stat-footer">
          <span class="state-label">狀態: 4,5,6</span>
          <a-button type="link" size="small">查看詳情 →</a-button>
        </div>
      </div>

      <!-- Box 5: 已取消 (State 7) -->
      <div class="stat-box" @click="viewOrdersByState(7)">
        <div class="stat-header">
          <h3>已取消</h3>
          <span class="stat-count">{{ stats.cancelled }}</span>
        </div>
        <p class="stat-subtitle">{{ stats.cancelled }} 筆訂單已取消</p>
        <div class="stat-footer">
          <span class="state-label">狀態: 7</span>
          <a-button type="link" size="small">查看詳情 →</a-button>
        </div>
      </div>
    </div>

    <!-- Recent Orders Table -->
    <div class="recent-orders">
      <div class="section-header">
        <h3>最近訂單</h3>
        <div class="header-right">
          <span class="total-count">總計: {{ allOrders.length }} 筆訂單</span>
          <a-button
              type="primary"
              size="small"
              @click="refreshOrders"
              :loading="loading"
          >
            <template #icon>
              <ReloadOutlined />
            </template>
            刷新
          </a-button>
        </div>
      </div>
      <a-table
          :dataSource="recentOrders"
          :columns="columns"
          :pagination="false"
          :loading="loading"
          rowKey="id"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'orderId'">
            <a @click="viewOrderDetail(record)">{{ record.orderId }}</a>
          </template>
          <template v-if="column.key === 'status'">
            <div class="status-cell">
              <a-tag :color="getStatusColor(record.status)" class="status-tag">
                {{ getStatusLabel(record.status) }}
              </a-tag>
              <span class="status-code">({{ record.status }})</span>
            </div>
          </template>
          <template v-if="column.key === 'totalAmount'">
            <span class="amount">${{ formatAmount(record.totalAmount) }}</span>
          </template>
          <template v-if="column.key === 'createTime'">
            <span>{{ formatDateTime(record.createTime) }}</span>
          </template>
          <template v-if="column.key === 'actions'">
            <a-space>
              <a-button type="link" size="small" @click="viewOrderDetail(record)">
                查看
              </a-button>
              <a-button
                  type="link"
                  size="small"
                  danger
                  @click="viewOrdersByState(record.status)"
              >
                同狀態訂單
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </div>

    <!-- Summary Stats -->
    <div class="summary-stats">
      <a-card title="訂單統計概覽">
        <div class="summary-grid">
          <div class="summary-item">
            <span class="summary-label">待處理訂單:</span>
            <span class="summary-value">{{ stats.pendingAcceptance + stats.accepted }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">進行中訂單:</span>
            <span class="summary-value">{{ stats.delivering }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">已完成訂單:</span>
            <span class="summary-value">{{ stats.completed + stats.completedOld }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">異常訂單:</span>
            <span class="summary-value">{{ stats.cancelled + stats.pendingRefund }}</span>
          </div>
        </div>
      </a-card>
    </div>

    <!-- Loading State -->
    <a-spin :spinning="loading" size="large" v-if="loading && allOrders.length === 0">
      <div class="loading-placeholder"></div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import { ReloadOutlined } from '@ant-design/icons-vue'
import { getAllOrders } from '@/api/order'
import type { Order } from '@/types/order'

const router = useRouter()
const loading = ref(false)

// Define OrderStats interface
interface OrderStats {
  pendingAcceptance: number  // State 2
  accepted: number          // State 3
  pendingRefund: number     // State 8
  delivering: number        // State 4
  completed: number         // State 5
  completedOld: number      // State 6
  cancelled: number         // State 7
  others: number            // Total of states 4,5,6
}

// Initialize stats with 0 values
const stats = ref<OrderStats>({
  pendingAcceptance: 0,
  accepted: 0,
  pendingRefund: 0,
  delivering: 0,
  completed: 0,
  completedOld: 0,
  cancelled: 0,
  others: 0
})

const recentOrders = ref<Order[]>([])
const allOrders = ref<Order[]>([])

const columns = [
  {
    title: '訂單編號',
    dataIndex: 'orderId',
    key: 'orderId',
    width: '20%'
  },
  {
    title: '用戶ID',
    dataIndex: 'userId',
    key: 'userId',
    width: '15%'
  },
  {
    title: '狀態',
    key: 'status',
    width: '20%'
  },
  {
    title: '總金額',
    key: 'totalAmount',
    width: '15%'
  },
  {
    title: '下單時間',
    dataIndex: 'createTime',
    key: 'createTime',
    width: '20%'
  },
  {
    title: '操作',
    key: 'actions',
    width: '10%'
  }
]

// Load all orders
const loadOrders = async () => {
  try {
    loading.value = true
    const orders = await getAllOrders()
    allOrders.value = orders

    // Show recent 5 orders (sorted by createTime descending)
    const sortedOrders = [...orders].sort((a, b) => {
      const timeA = a.createTime ? new Date(a.createTime).getTime() : 0
      const timeB = b.createTime ? new Date(b.createTime).getTime() : 0
      return timeB - timeA
    })
    recentOrders.value = sortedOrders.slice(0, 5)

    // Calculate statistics
    calculateStats(orders)

    message.success(`成功載入 ${orders.length} 筆訂單`)
  } catch (error: any) {
    console.error('載入訂單失敗:', error)

    if (error.response?.status === 404) {
      Modal.warning({
        title: 'API 端點不存在',
        content: '請確認後端 /api/orders/all 端點已實現',
        okText: '了解'
      })
    } else {
      message.error('載入訂單失敗: ' + (error.message || '未知錯誤'))
    }
  } finally {
    loading.value = false
  }
}

const calculateStats = (orders: Order[]) => {
  const newStats: OrderStats = {
    pendingAcceptance: 0,
    accepted: 0,
    pendingRefund: 0,
    delivering: 0,
    completed: 0,
    completedOld: 0,
    cancelled: 0,
    others: 0
  }

  orders.forEach(order => {
    switch (order.status) {
      case 2:
        newStats.pendingAcceptance++
        break
      case 3:
        newStats.accepted++
        break
      case 4:
        newStats.delivering++
        break
      case 5:
        newStats.completed++
        break
      case 6:
        newStats.completedOld++
        break
      case 7:
        newStats.cancelled++
        break
      case 8:
        newStats.pendingRefund++
        break
      default:
        // 處理其他狀態
        break
    }
  })

  // Calculate "others" total (states 4,5,6)
  newStats.others = newStats.delivering + newStats.completed + newStats.completedOld

  stats.value = newStats
}

const getStatusLabel = (status: number): string => {
  const statusMap: Record<number, string> = {
    1: '待付款',
    2: '待接單',
    3: '已接單',
    4: '派送中',
    5: '已完成',
    6: '已完成', // 如果狀態6也是已完成
    7: '已取消',
    8: '待退款'
  }
  return statusMap[status] || `未知狀態(${status})`
}

const getStatusColor = (status: number): string => {
  const colorMap: Record<number, string> = {
    1: 'default',
    2: 'orange',
    3: 'blue',
    4: 'purple',
    5: 'green',
    6: 'green', // 如果狀態6也是已完成
    7: 'red',
    8: 'gold'
  }
  return colorMap[status] || 'default'
}

const formatAmount = (amount: number | undefined): string => {
  if (amount === undefined || amount === null) return '0.00'
  return amount.toFixed(2)
}

const formatDateTime = (dateTime: string | undefined): string => {
  if (!dateTime) return '無日期'
  try {
    const date = new Date(dateTime)
    return date.toLocaleString('zh-TW', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch (e) {
    return dateTime
  }
}

const viewOrdersByState = (state: number | number[]) => {
  if (Array.isArray(state)) {
    router.push({
      path: '/admin/orders/staff',
      query: { states: state.join(',') }
    })
  } else {
    router.push({
      path: '/admin/orders/staff',
      query: { state: state }
    })
  }
}

const viewOtherOrders = () => {
  viewOrdersByState([4, 5, 6])
}

const viewOrderDetail = (order: Order) => {
  if (!order.id) {
    message.warning('訂單ID不存在')
    return
  }
  router.push(`/admin/orders/${order.id}`)
}

const refreshOrders = () => {
  loadOrders()
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.order-dashboard {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.order-dashboard h2 {
  color: #1890ff;
  margin-bottom: 24px;
  font-size: 24px;
  font-weight: 600;
}

.dashboard-stats {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-box {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #e8e8e8;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.stat-box:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  border-color: #1890ff;
}

.stat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.stat-header h3 {
  margin: 0;
  color: #1890ff;
  font-size: 20px;
  font-weight: 600;
}

.stat-count {
  font-size: 36px;
  font-weight: bold;
  color: #52c41a;
  line-height: 1;
  min-width: 40px;
  text-align: right;
}

.stat-subtitle {
  color: #666;
  margin-bottom: 20px;
  font-size: 14px;
  flex-grow: 1;
}

.stat-details {
  flex-grow: 1;
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  padding: 6px 0;
  border-bottom: 1px dashed #f0f0f0;
}

.stat-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.stat-state {
  color: #666;
  font-size: 13px;
}

.stat-number {
  font-weight: 600;
  color: #1890ff;
  font-size: 16px;
  min-width: 30px;
  text-align: right;
}

.stat-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 2px solid #f0f0f0;
  padding-top: 16px;
  margin-top: auto;
}

.state-label {
  color: #999;
  font-size: 12px;
  background: #f5f5f5;
  padding: 4px 8px;
  border-radius: 4px;
}

.recent-orders {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 30px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  margin: 0;
  color: #1890ff;
  font-size: 18px;
  font-weight: 600;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.total-count {
  color: #666;
  font-size: 14px;
  background: #f5f5f5;
  padding: 6px 12px;
  border-radius: 16px;
}

.status-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-tag {
  margin: 0;
  min-width: 70px;
  text-align: center;
}

.status-code {
  color: #999;
  font-size: 12px;
  font-family: monospace;
}

.amount {
  color: #fa541c;
  font-weight: 600;
  font-family: monospace;
}

.summary-stats {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  padding: 10px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
  transition: all 0.3s;
}

.summary-item:hover {
  background: #f0f9ff;
  border-color: #1890ff;
}

.summary-label {
  color: #666;
  font-size: 14px;
}

.summary-value {
  font-size: 24px;
  font-weight: 700;
  color: #1890ff;
}

.loading-placeholder {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Responsive design */
@media (max-width: 1200px) {
  .dashboard-stats {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .dashboard-stats {
    grid-template-columns: 1fr;
  }

  .summary-grid {
    grid-template-columns: 1fr;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .header-right {
    width: 100%;
    justify-content: space-between;
  }
}

/* Animation for count changes */
@keyframes count-up {
  from {
    opacity: 0.5;
    transform: scale(1.1);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.stat-count {
  animation: count-up 0.3s ease-out;
}
</style>