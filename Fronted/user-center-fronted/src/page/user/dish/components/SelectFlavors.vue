<script setup lang="ts">
import { dishItem, flavorItem } from "@/types/dish";
import { computed, ref, reactive, watch } from "vue";
import { PlusOutlined, MinusOutlined, CloseOutlined } from "@ant-design/icons-vue"
import { message } from 'ant-design-vue';
import { addCart } from "@/api/cart"; // 导入购物车API

const formRef = ref()
const visible = defineModel<boolean>('visible', { default: false })
const emits = defineEmits(['added-to-cart']); // 添加事件发射

const props = defineProps({
  dish: Object as () => dishItem,      // 菜品信息
  title: {
    type: String,
    default: ''
  },
  confirmText: {
    type: String,
    default: '加入购物车' // 修改默认文本
  }
})

// 处理模态框状态变化
const handleModalOpenChange = (newValue: boolean) => {
  visible.value = newValue
}

// 数据处理：字符串转数组
const flavors = computed(() => {
  if (!props.dish || !props.dish.flavors) return []
  return props.dish.flavors.map((item: flavorItem) => {
    const options = item.list.split(",").map((opt: string) => opt.trim())
    return {
      tag: item.tag,
      options: options
    }
  })
})

// 响应式表单状态
const formState = reactive({
  quantity: 1,
})

// 数量减少
const decreaseQuantity = (e: Event) => {
  e.stopPropagation()
  e.preventDefault()
  if (formState.quantity > 1) {
    formState.quantity--
  }
}

// 数量增加
const increaseQuantity = (e: Event) => {
  e.stopPropagation()
  e.preventDefault()
  if (formState.quantity < 99) {
    formState.quantity++
  }
}

// 关闭模态框
const handleClose = () => {
  visible.value = false
}

// 初始化表单状态
const initFormState = () => {
  formState.quantity = 1

  // 重置口味选择
  flavors.value.forEach((flavor: any) => {
    if (flavor.options && flavor.options.length > 0) {
      (formState as any)[flavor.tag] = flavor.options[0]
    } else {
      (formState as any)[flavor.tag] = ''
    }
  })
}

const resetForm = initFormState

// 计算总价
const totalPrice = computed(() => {
  return (props.dish?.price || 0) * formState.quantity
})

// 获取选择的口味
const getSelectedFlavors = () => {
  const selectedFlavors: string[] = []
  flavors.value.forEach((flavor: any) => {
    const value = (formState as any)[flavor.tag]
    if (value) {
      selectedFlavors.push(`${value}`) // 只添加值，不添加标签
    }
  })
  return selectedFlavors.join(', ')
}

// 处理确认按钮点击（调用add cart）
const handleConfirm = async () => {
  try {
    if (!props.dish) {
      message.error('菜品信息不存在')
      return
    }

    // 验证表单
    await formRef.value?.validate()

    const selectedFlavors = getSelectedFlavors()

    // 构建购物车数据 - 根据你的Cart实体结构
    const cartData = {
      dishId: props.dish.id,           // 对应Cart的dishId
      name: props.dish.dishName,       // 对应Cart的name
      dishFlavor: selectedFlavors,     // 对应Cart的dishFlavor
      number: formState.quantity,      // 对应Cart的number
      amount: totalPrice.value         // 对应Cart的amount
      // userId会自动从session获取，不需要前端传
      // create_time会在后端自动生成
    }

    console.log('添加到购物车的数据:', cartData)

    // 调用add cart API
    await addCart(cartData)

    // 成功提示
    message.success('已成功添加到购物车')

    // 发射事件，通知父组件
    emits('added-to-cart', {
      dish: props.dish,
      quantity: formState.quantity,
      flavors: selectedFlavors,
      total: totalPrice.value
    })

    // 关闭模态框
    handleClose()

  } catch (error: any) {
    console.error('添加到购物车失败:', error)

    if (error.errorFields) {
      // 表单验证失败
      message.error('请选择口味')
    } else if (error.response?.data?.message) {
      // API返回的错误信息
      message.error(error.response.data.message)
    } else {
      // 其他错误
      message.error('添加到购物车失败，请稍后重试')
    }
  }
}

// 监听模态框打开状态
watch(() => visible.value, (open) => {
  if (open) {
    initFormState()
  }
})
</script>

<template>
  <!-- 遮罩层 -->
  <a-modal
      v-model:open="visible"
      :title="dish.dishName"
      :footer="null"
      :closable="false"
      :mask-closable="true"
      :keyboard="true"
      class="flavor-modal"
      width="500px"
      @cancel="handleClose"
      @update:open="handleModalOpenChange"
      :after-close="resetForm"
  >
    <div class="float-close-btn" @click="handleClose">
      <CloseOutlined/>
    </div>

    <a-form
        ref="formRef"
        :model="formState"
        layout="vertical"
        class="flavor-form"
    >
      <a-form-item
          v-for="flavor in flavors"
          :key="flavor.tag"
          :label="flavor.tag"
          :name="flavor.tag"
          :rules="[{ required: true, message: `请选择${flavor.tag}` }]"
      >
        <a-radio-group
            v-model:value="formState[flavor.tag]"
            class="flavor-radio-group"
        >
          <a-radio-button
              v-for="option in flavor.options"
              :key="option"
              :value="option"
              class="flavor-radio"
          >
            {{option}}
          </a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="数量">
        <a-input-number
            v-model:value="formState.quantity"
            :min="1"
            :max="99"
            size="default"
            class="quantity-input"
        >
          <template #addonBefore>
            <MinusOutlined @click="decreaseQuantity"/>
          </template>
          <template #addonAfter>
            <PlusOutlined @click="increaseQuantity"/>
          </template>
        </a-input-number>
      </a-form-item>

      <div class="modal-footer">
        <div class="dish-info">
          <div class="dish-price">¥{{ totalPrice.toFixed(2) }}</div>
          <div v-if="formState.quantity > 1" class="unit-price">
            单价：¥{{ dish.price?.toFixed(2) || '0.00' }}
          </div>
        </div>
        <a-button
            type="primary"
            @click="handleConfirm"
            class="confirm-btn"
        >
          {{ confirmText }}
        </a-button>
      </div>
    </a-form>
  </a-modal>
</template>

<style scoped>
/* 浮窗关闭按钮样式 */
.float-close-btn {
  position: absolute;
  top: 16px;
  right: 16px;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: white;
  border: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 1000;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

.float-close-btn:hover {
  background: #f5f5f5;
  border-color: #d9d9d9;
  transform: scale(1.1);
}

.float-close-btn:active {
  transform: scale(0.95);
}

/* 确保模态框内容区域有相对定位 */
.flavor-modal :deep(.ant-modal-content) {
  position: relative;
}
.flavor-modal :deep(.ant-modal-content) {
  border-radius: 16px;
}

.flavor-modal :deep(.ant-modal-header) {
  border-bottom: none;
  padding-bottom: 8px;
}

.flavor-modal :deep(.ant-modal-title) {
  font-size: 25px;
  font-weight: 600;
  color: #1a1a1a;
}

.flavor-modal :deep(.ant-modal-body) {
  padding: 0 24px 24px;
}

/* 表单样式 */
.flavor-form :deep(.ant-form-item) {
  margin-bottom: 20px;
}

.flavor-form :deep(.ant-form-item-label) {
  padding-bottom: 6px;
  font-weight: 500;
}

.flavor-form :deep(.ant-form-item-label > label) {
  color: #333;
  font-size: 15px;
}

.flavor-form :deep(.ant-form-item-label > label::after) {
  content: "*";
  color: #ff4d4f;
  margin-left: 4px;
}

/* 口味选择 */
.flavor-radio-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 4px;
}

/* 数量选择 */
.quantity-input {
  width: 140px;
  position: relative;
  z-index: 10;
}

.quantity-input :deep(.ant-input-number-input) {
  text-align: center;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.quantity-input :deep(.ant-input-number-handler-wrap) {
  display: none;
}

.quantity-input :deep(.ant-input-number-group-addon) {
  background: transparent;
  border: 1.5px solid #e8e8e8;
  cursor: pointer;
  transition: all 0.2s;
}

.quantity-input :deep(.ant-input-number-group-addon:hover) {
  border-color: #1890ff;
  color: #1890ff;
}

/* 底部操作栏 */
.modal-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.dish-info {
  text-align: center;
}

.dish-price {
  font-size: 24px;
  font-weight: 700;
  color: #ff4d4f;
  margin-bottom: 4px;
  pointer-events: none;
}

.unit-price {
  font-size: 12px;
  color: #999;
}

.confirm-btn {
  height: 48px;
  padding: 0 40px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 12px;
  background: linear-gradient(135deg, #52c41a, #73d13d);
  border: none;
  box-shadow: 0 4px 12px rgba(82, 196, 26, 0.2);
  transition: all 0.3s;
}

.confirm-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(82, 196, 26, 0.3);
  background: linear-gradient(135deg, #73d13d, #95de64);
}

/* 响应式设计 */
@media (max-width: 576px) {
  .flavor-modal :deep(.ant-modal) {
    max-width: calc(100vw - 32px);
    margin: 16px;
  }

  .modal-footer {
    flex-direction: column;
    gap: 16px;
  }

  .confirm-btn {
    width: 100%;
  }

  .dish-info {
    width: 100%;
  }
}
</style>