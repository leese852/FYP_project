<template>
  <div class="cart-container">
    <div class="cart-header">
      <h2>我的购物车</h2>
      <div class="cart-actions">
        <a-button
            danger
            :disabled="!cartItems.length"
            @click="handleClearCart"
        >
          <DeleteOutlined /> 清空购物车
        </a-button>
        <a-button
            type="primary"
            :disabled="!selectedItems.length"
            @click="handleCheckout"
        >
          <ShoppingCartOutlined /> 结算选中 (¥{{ selectedAmount.toFixed(2) }})
        </a-button>
      </div>
    </div>

    <div class="cart-selection-actions" v-if="cartItems.length">
      <a-checkbox
          v-model:checked="selectAll"
          @change="handleSelectAll"
          :indeterminate="indeterminate"
      >
        全选 (已选 {{ selectedItems.length }} 项)
      </a-checkbox>
      <a-button
          type="link"
          danger
          :disabled="!selectedItems.length"
          @click="handleDeleteSelected"
      >
        <DeleteOutlined /> 删除选中
      </a-button>
    </div>

    <a-empty v-if="!cartItems.length && !loading" description="购物车是空的">
      <a-button type="primary" @click="$router.push('/')">去点餐</a-button>
    </a-empty>

    <a-spin :spinning="loading">
      <div v-if="cartItems.length" class="cart-content">
        <!-- 收貨地址選擇區塊 -->
        <div class="address-section">
          <span class="label">收貨地址：</span>
          <a-select
              v-model:value="selectedAddressId"
              :loading="addressLoading"
              placeholder="請選擇收貨地址"
              style="min-width: 260px"
          >
            <a-select-option
                v-for="addr in addressList"
                :key="addr.id"
                :value="addr.id"
            >
              {{ addr.contactName }}（{{ addr.contactPhone }}）- {{ addr.address }}
              <span v-if="addr.isDefault === 1">【默認】</span>
            </a-select-option>
          </a-select>
          <a-button type="link" @click="router.push('/user/address')">
            管理地址
          </a-button>
        </div>

        <a-table
            :dataSource="cartItems"
            :columns="columns"
            :pagination="false"
            rowKey="id"
            class="cart-table"
            :rowSelection="rowSelection"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'dishInfo'">
              <div class="dish-info">
                <div class="dish-name">{{ record.name }}</div>
                <div v-if="record.dishFlavor" class="dish-flavor">
                  <a-tag color="blue">{{ record.dishFlavor }}</a-tag>
                </div>
              </div>
            </template>

            <template v-else-if="column.key === 'unitPrice'">
              ¥{{ getUnitPrice(record).toFixed(2) }}
            </template>

            <template v-else-if="column.key === 'quantity'">
              <a-input-number
                  v-model:value="record.number"
                  :min="1"
                  :max="99"
                  size="small"
                  @change="handleQuantityChange(record)"
                  style="width: 100px"
              />
            </template>

            <template v-else-if="column.key === 'subtotal'">
              <span class="amount">¥{{ (record.amount || 0).toFixed(2) }}</span>
            </template>

            <template v-else-if="column.key === 'action'">
              <a-button type="link" danger @click="handleRemoveItem(record.id)">
                <DeleteOutlined /> 删除
              </a-button>
            </template>
          </template>
        </a-table>

        <div class="cart-summary">
          <div class="summary-info">
            <div>共 {{ totalItems }} 件商品</div>
            <div>已选 {{ selectedTotalItems }} 件商品</div>
<!--            <div class="total-amount">-->
<!--              合计：<span class="amount-large">¥{{ totalAmount.toFixed(2) }}</span>-->
<!--            </div>-->
            <div class="selected-amount" v-if="selectedItems.length">
              选中商品：<span class="amount-large">¥{{ selectedAmount.toFixed(2) }}</span>
            </div>
          </div>
          <div class="checkout-actions">
            <a-button
                type="primary"
                size="large"
                @click="handleCheckout"
                :disabled="!selectedItems.length"
            >
              结算选中商品
            </a-button>
          </div>
        </div>
      </div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Table, Modal, message } from 'ant-design-vue'
import { DeleteOutlined, ShoppingCartOutlined } from '@ant-design/icons-vue'
import type { TableColumnsType } from 'ant-design-vue'
import {getAllCart, deleteCart, deleteAllCart, addCart, updateQty} from '@/api/cart'
import { getAddressList } from '@/api/address'
import { placeOrderFromCart } from '@/api/cart'
import {processPayment} from "@/api/payment";

const router = useRouter()
const cartItems = ref<any[]>([])
const selectedRowKeys = ref<number[]>([])
const loading = ref(false)
const selectAll = ref(false)
const indeterminate = ref(false)

// 地址相關
const addressList = ref<any[]>([])
const selectedAddressId = ref<number | null>(null)
const addressLoading = ref(false)

// 计算选中的商品
const selectedItems = computed(() => {
  return cartItems.value.filter(item => selectedRowKeys.value.includes(item.id))
})

// 计算单价 - amount 就是单价
const getUnitPrice = (item: any) => {
  return Number(item.amount) || 0
}

// 计算小计 - 单价 × 数量
const getSubtotal = (item: any) => {
  const unitPrice = Number(item.amount) || 0
  const quantity = Number(item.number) || 0
  return unitPrice * quantity
}

// 计算属性
const totalItems = computed(() => {
  return cartItems.value.reduce((sum, item) => {
    return sum + (Number(item.number) || 0)
  }, 0)
})

const totalAmount = computed(() => {
  return cartItems.value.reduce((sum, item) => {
    return sum + getSubtotal(item)
  }, 0)
})

const selectedTotalItems = computed(() => {
  return selectedItems.value.reduce((sum, item) => {
    return sum + (Number(item.number) || 0)
  }, 0)
})

const selectedAmount = computed(() => {
  return selectedItems.value.reduce((sum, item) => {
    return sum + getSubtotal(item)
  }, 0)
})

// 表格列定义
const columns: TableColumnsType = [
  {
    title: '商品信息',
    key: 'dishInfo',
    dataIndex: 'name',
    width: '40%'
  },
  {
    title: '单价',
    key: 'unitPrice',
    align: 'center',
    width: '15%'
  },
  {
    title: '数量',
    key: 'quantity',
    align: 'center',
    width: '15%'
  },
  {
    title: '操作',
    key: 'action',
    align: 'center',
    width: '15%'
  }
]

// 表格行选择配置
const rowSelection = computed(() => ({
  selectedRowKeys: selectedRowKeys.value,
  onChange: (selectedKeys: number[]) => {
    selectedRowKeys.value = selectedKeys
    updateSelectionState()
  },
  getCheckboxProps: (record: any) => ({
    disabled: false,
    name: record.name,
  }),
  type: 'checkbox' as const
}))

// 更新全选状态
const updateSelectionState = () => {
  const allKeys = cartItems.value.map(item => item.id)
  const selectedCount = selectedRowKeys.value.length
  selectAll.value = selectedCount === allKeys.length
  indeterminate.value = selectedCount > 0 && selectedCount < allKeys.length
}

// 全选/取消全选
const handleSelectAll = (e: any) => {
  if (e.target.checked) {
    selectedRowKeys.value = cartItems.value.map(item => item.id)
  } else {
    selectedRowKeys.value = []
  }
  indeterminate.value = false
}

// 监听数据变化，更新选择状态
watch(cartItems, () => {
  updateSelectionState()
})

// 加载购物车
const loadCart = async () => {
  try {
    loading.value = true
    const response = await getAllCart()
    console.log('完整的API响应:', response)

    // 根据控制台输出，数据结构是：response.data.data
    if (response && response.data && response.data.data) {
      const cartData = response.data.data
      if (Array.isArray(cartData)) {
        cartItems.value = cartData.map(item => ({
          ...item,
          id: Number(item.id) || 0,
          number: Number(item.number) || 1,
          amount: Number(item.amount) || 0, // 注意：这里是单价
          dishId: Number(item.dishId) || 0,
        }))
        console.log('解析后的购物车数据:', cartItems.value)

        // 调试：查看第一个商品的数据结构
        if (cartItems.value.length > 0) {
          const firstItem = cartItems.value[0]
          console.log('第一个商品详细信息:', {
            id: firstItem.id,
            name: firstItem.name,
            amount: firstItem.amount, // 应该是单价
            number: firstItem.number, // 数量
            subtotal: firstItem.amount * firstItem.number // 小计
          })
        }
      }
    } else if (response && response.data) {
      if (Array.isArray(response.data)) {
        cartItems.value = response.data
      }
    }

    // 清空选择状态
    selectedRowKeys.value = []
    selectAll.value = false
    indeterminate.value = false

  } catch (error: any) {
    console.error('加载购物车失败:', error)
    message.error(error.message || '加载购物车失败')
    cartItems.value = []
  } finally {
    loading.value = false
  }
}

// 加載用戶地址列表
const loadAddressList = async () => {
  try {
    addressLoading.value = true
    const res = await getAddressList()
    const list = res?.data?.data || []
    addressList.value = list
    // 默認選中「默認地址」
    const defaultAddr = list.find((item: any) => item.isDefault === 1)
    selectedAddressId.value = defaultAddr ? defaultAddr.id : (list[0]?.id ?? null)
  } catch (error: any) {
    console.error('加載地址列表失敗:', error)
    addressList.value = []
  } finally {
    addressLoading.value = false
  }
}

// 删除单个商品
const handleRemoveItem = async (id: number) => {
  Modal.confirm({
    title: '确认删除',
    content: '确定要删除这个商品吗？',
    onOk: async () => {
      try {
        await deleteCart(id)
        message.success('删除成功')
        // 从选中列表中移除
        selectedRowKeys.value = selectedRowKeys.value.filter(key => key !== id)
        loadCart()
      } catch (error: any) {
        message.error(error.message || '删除失败')
      }
    }
  })
}

// 删除选中的商品
const handleDeleteSelected = async () => {
  if (!selectedItems.value.length) {
    message.warning('请先选择要删除的商品')
    return
  }

  Modal.confirm({
    title: '确认删除',
    content: `确定要删除选中的 ${selectedItems.value.length} 件商品吗？`,
    onOk: async () => {
      try {
        const deletePromises = selectedItems.value.map(item => deleteCart(item.id))
        await Promise.all(deletePromises)
        message.success(`成功删除 ${selectedItems.value.length} 件商品`)
        // 清空选中状态
        selectedRowKeys.value = []
        loadCart()
      } catch (error: any) {
        message.error(error.message || '删除失败')
      }
    }
  })
}

// 清空购物车
const handleClearCart = async () => {
  Modal.confirm({
    title: '确认清空',
    content: '确定要清空购物车吗？',
    okType: 'danger',
    onOk: async () => {
      try {
        await deleteAllCart()
        message.success('购物车已清空')
        cartItems.value = []
        selectedRowKeys.value = []
        selectAll.value = false
      } catch (error: any) {
        message.error(error.message || '清空失败')
      }
    }
  })
}

// 修改数量 - 修复版本
const handleQuantityChange = async (item: any) => {
  try {
    const newNumber = Number(item.number) || 1
    if (newNumber < 1 || newNumber > 99) {
      message.warning('数量必须在1-99之间')
      loadCart() // 恢复原值
      return
    }

    await updateQty({
      id: item.id,
      number: newNumber,

    })

    // 更新本地数据
    item.number = newNumber
    // amount（单价）保持不变

  } catch (error: any) {
    console.error('更新失败:', error)
    message.error(error.message || '更新失败')
    loadCart() // 失败后重新加载
  }
}

// 结算选中的商品
// 结算选中的商品
// 结算选中的商品
const handleCheckout = async () => {
  if (!selectedRowKeys.value.length) {
    message.warning('请选择要结算的商品')
    return
  }

  if (!selectedAddressId.value) {
    message.warning('请先选择收货地址')
    return
  }

  // 🔥 获取选中的地址坐标
  const selectedAddress = addressList.value.find(addr => addr.id === selectedAddressId.value)
  if (!selectedAddress) {
    message.warning('请选择有效的收货地址')
    return
  }

  // 🔥 检查地址是否有坐标
  if (!selectedAddress.lng || !selectedAddress.lat) {
    message.warning('所选地址缺少坐标信息，请重新选择或编辑地址')
    return
  }

  try {
    const requestData = {
      addressId: selectedAddressId.value,
      cartIds: selectedRowKeys.value,
      addressLng: selectedAddress.lng,
      addressLat: selectedAddress.lat
    }

    console.log('下单请求参数:', requestData)

    const response = await placeOrderFromCart(requestData)
    console.log('完整响应:', response)

    // 🔥 修复：正确获取订单数据
    let orderData = null
    let orderId = null
    let amount = null

    // 尝试多种可能的数据结构
    if (response?.data?.data) {
      orderData = response.data.data
      console.log('从 response.data.data 获取:', orderData)
    } else if (response?.data) {
      orderData = response.data
      console.log('从 response.data 获取:', orderData)
    } else if (response) {
      orderData = response
      console.log('从 response 获取:', orderData)
    }

    if (orderData) {
      // 尝试获取 orderId
      orderId = orderData.orderId || orderData.id || orderData.order_id
      // 尝试获取 amount
      amount = orderData.totalAmount || orderData.amount || orderData.total_amount

      console.log('提取的订单信息:', { orderId, amount, orderData })
    }

    if (!orderId) {
      console.error('无法获取订单ID，响应数据:', response)
      message.error('订单创建失败：未获取到订单号')
      return
    }

    if (!amount || amount <= 0) {
      console.error('金额无效:', amount)
      message.error('订单创建失败：金额无效')
      return
    }

    message.success(`订单生成成功，前往付款...`)

    const payUrl = `http://localhost:8080/api/alipay/pay?orderId=${orderId}&amount=${amount}`
    console.log('跳转支付:', payUrl)
    window.location.href = payUrl

  } catch (error: any) {
    console.error('下单失败:', error)
    let errorMsg = '下单失败'
    if (error.response) {
      errorMsg = error.response.data?.message || error.response.data?.msg || errorMsg
    } else if (error.request) {
      errorMsg = '网络请求失败，请检查网络连接'
    } else {
      errorMsg = error.message || errorMsg
    }
    message.error(errorMsg)
  }
}

// 初始化
onMounted(() => {
  loadCart()
  loadAddressList()
})
</script>

<style scoped>
.cart-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.cart-header h2 {
  margin: 0;
  font-size: 20px;
}

.cart-actions {
  display: flex;
  gap: 10px;
}

.cart-selection-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 12px 16px;
  background: #fafafa;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
}

.cart-content {
  margin-top: 20px;
}

.address-section {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.address-section .label {
  font-weight: 500;
}

.dish-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.dish-name {
  font-weight: 500;
}

.dish-flavor {
  display: inline-block;
}

.amount {
  color: #ff4d4f;
  font-weight: 600;
}

.cart-summary {
  margin-top: 20px;
  padding: 20px;
  background: #fafafa;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
}

.summary-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 20px;
}

.total-amount, .selected-amount {
  font-size: 16px;
  font-weight: 500;
}

.selected-amount {
  color: #1890ff;
}

.amount-large {
  color: #ff4d4f;
  font-size: 24px;
  font-weight: 700;
}

.checkout-actions {
  display: flex;
  justify-content: flex-end;
}

.checkout-actions .ant-btn {
  height: 48px;
  padding: 0 32px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
  background: linear-gradient(135deg, #1890ff, #36cfc9);
  border: none;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.2);
  transition: all 0.3s ease;
}

.checkout-actions .ant-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(24, 144, 255, 0.3);
  background: linear-gradient(135deg, #36cfc9, #1890ff);
}

.checkout-actions .ant-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .cart-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .cart-actions {
    width: 100%;
    justify-content: flex-end;
  }

  .cart-selection-actions {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .summary-info {
    grid-template-columns: 1fr;
  }

  .checkout-actions .ant-btn {
    width: 100%;
  }
}

/* 选中行的样式 */
.cart-table :deep(.ant-table-tbody > tr.ant-table-row-selected > td) {
  background-color: #e6f7ff;
}

.cart-table :deep(.ant-table-tbody > tr:hover > td) {
  background-color: #fafafa;
}
</style>