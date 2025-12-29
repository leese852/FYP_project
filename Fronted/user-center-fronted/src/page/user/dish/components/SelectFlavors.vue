<script setup lang="ts">
  import {dishItem,flavorItem} from "@/types/dish";
  import {computed, ref,reactive,watch} from "vue";
  import {PlusOutlined, MinusOutlined,CloseOutlined} from "@ant-design/icons-vue"
import { any } from "vue-types";

  const formRef= ref()
  const visible = defineModel<boolean>('visible', { default: false })
  const props = defineProps({
    // visible: Boolean, // 是否显示弹窗
    dish: Object,      // 菜品信息
    title: {
      type: String,
      default: ''
    },
    confirmText: {
      type: String,
      default: '确认下单'
    }
  })

  const decreaseQuantity = (e: Event) => {
    e.stopPropagation() // 阻止事件冒泡
    e.preventDefault()  // 阻止默认行为
    if(formState.quantity > 1){
      formState.quantity--
    }
  }

  const increaseQuantity = (e: Event) => {
    e.stopPropagation() // 阻止事件冒泡
    e.preventDefault()  // 阻止默认行为
    if(formState.quantity < 99){
      formState.quantity++
    }
  }
  // 处理模态框状态变化
  const handleModalOpenChange = (newValue: boolean) => {
    visible.value = newValue
  }
  //数据处理 字符串转数组
  const flavors = computed(() => {
    if (!props.dish || !props.dish.flavors) return []
    return props.dish.flavors.map((item:flavorItem) => {
      const options = item.list.split(",").map((opt:string) => opt.trim())
      return {
        tag: item.tag,
        options: options
      }
    })
  })

  //reactive将一个对象转换为响应式对象 类json
  const formState = reactive({
    quantity:1,
  })
  const handleClose = () => {
    visible.value = false
  }

  const initFormState  = () => {
    formState.quantity = 1

    flavors.value.forEach((flavor:any) => {
      if(flavor.options && flavor.options.length > 0){
        (formState as any)[flavor.tag] = flavor.options[0]
      }else{
        (formState as any)[flavor.tag] = ''
      }
    })
  }

  const resetForm = initFormState
  const handleConfirm = () =>{
    const selectedFlavors: string[] = []

    flavors.value.forEach((flavor:any) => {
      const value = (formState as any)[flavor.tag]
      if (value) {
        selectedFlavors.push(`${flavor.tag}: ${value}`)
      }
    })
    alert(`
    菜品：${props.dish?.dishName}
    数量：${formState.quantity}
    口味：${selectedFlavors.join(', ')}
    总价：$${(props.dish?.price || 0) * formState.quantity}
  `)
  }

  watch(() => visible.value,(open)=>{
    if(open){
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
          <div class="dish-price">${{dish.price}}</div>
        </div>
        <a-button
          type="primary"
          @click="handleConfirm"
          class="confirm-btn"
        >
          {{confirmText}}
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

.confirm-btn {
  height: 48px;
  padding: 0 40px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 12px;
  background: linear-gradient(135deg, #1890ff, #36cfc9);
  border: none;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.2);
  transition: all 0.3s;
}

.confirm-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(24, 144, 255, 0.3);
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