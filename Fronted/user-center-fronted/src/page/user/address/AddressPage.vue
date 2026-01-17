<template>
  <div class="address-container">
    <!-- 页面标题和添加按钮 -->
    <div class="page-header">
      <h2>地址管理</h2>
      <a-button type="primary" @click="showAddModal">
        <template #icon><PlusOutlined /></template>
        新增地址
      </a-button>
    </div>

    <!-- 地址列表表格 -->
    <a-table
        :dataSource="safeAddressList"
        :columns="columns"
        :loading="loading"
        :pagination="false"
        rowKey="id"
    >
      <template #bodyCell="{ column, record }">
        <!-- 默认地址标识 -->
        <template v-if="column.key === 'isDefault'">
          <a-tag v-if="record.isDefault === 1" color="green">默认地址</a-tag>
          <span v-else class="text-muted">-</span>
        </template>

        <!-- 操作列 -->
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" size="small" @click="showEditModal(record)">编辑</a-button>
            <a-button
                type="link"
                size="small"
                danger
                @click="confirmDelete(record.id)"
            >删除</a-button>
            <a-button
                v-if="record.isDefault !== 1"
                type="link"
                size="small"
                @click="setDefault(record.id)"
            >设为默认</a-button>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 添加/编辑地址的弹窗 -->
    <a-modal
        v-model:open="modalVisible"
        :title="isEditing ? '编辑地址' : '新增地址'"
        @ok="handleSubmit"
        @cancel="handleCancel"
        :confirmLoading="submitting"
    >
      <a-form
          ref="formRef"
          :model="formState"
          :rules="formRules"
          layout="vertical"
      >
        <a-form-item label="收货人姓名" name="contactName">
          <a-input
              v-model:value="formState.contactName"
              placeholder="请输入收货人姓名"
          />
        </a-form-item>

        <a-form-item label="联系电话" name="contactPhone">
          <a-input
              v-model:value="formState.contactPhone"
              placeholder="请输入联系电话"
          />
        </a-form-item>

        <a-form-item label="详细地址" name="address">
          <a-textarea
              v-model:value="formState.address"
              placeholder="请输入详细地址，如：XX市XX区XX街道XX号"
              :rows="3"
          />
        </a-form-item>

        <a-form-item name="isDefault">
          <a-checkbox v-model:checked="formState.isDefault">
            设为默认地址
          </a-checkbox>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message, Modal } from 'ant-design-vue'
import {
  getAddressList,
  getAddressById,
  addAddress,
  updateAddress,
  deleteAddress
} from '@/api/address'

// 响应式数据
const addressList = ref([])
const loading = ref(false)
const modalVisible = ref(false)
const submitting = ref(false)
const isEditing = ref(false)
const currentId = ref(null)
const formRef = ref()

// 表单数据
const formState = reactive({
  contactName: '',
  contactPhone: '',
  address: '',
  isDefault: false
})

// 表单验证规则
const formRules = {
  contactName: [
    { required: true, message: '请输入收货人姓名', trigger: 'blur' },
    { max: 20, message: '姓名最多20个字符', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入详细地址', trigger: 'blur' },
    { min: 5, message: '地址至少5个字符', trigger: 'blur' },
    { max: 200, message: '地址最多200个字符', trigger: 'blur' }
  ]
}

// 表格列定义
const columns = [
  {
    title: '收货人',
    dataIndex: 'contactName',
    key: 'contactName',
    width: '120px'
  },
  {
    title: '联系电话',
    dataIndex: 'contactPhone',
    key: 'contactPhone',
    width: '130px'
  },
  {
    title: '详细地址',
    dataIndex: 'address',
    key: 'address',
    ellipsis: true
  },
  {
    title: '状态',
    key: 'isDefault',
    width: '100px'
  },
  {
    title: '操作',
    key: 'action',
    width: '200px'
  }
]

// 安全的地址列表，确保总是返回数组
const safeAddressList = computed(() => {
  const list = addressList.value
  return Array.isArray(list) ? list : []
})

// 页面加载时获取地址列表
onMounted(() => {
  fetchAddressList()
})

// 获取地址列表
const fetchAddressList = async () => {
  try {
    loading.value = true
    const response = await getAddressList()

    // 注意：根据你的拦截器，response 已经是 { code, data, message, description }
    if (response.code === 0) {
      // 确保数据是数组
      const data = response.data
      if (Array.isArray(data)) {
        addressList.value = data
      } else {
        console.warn('API返回的data不是数组:', data)
        addressList.value = []
      }
    } else {
      message.error(response.message || '获取地址列表失败')
      addressList.value = []
    }
  } catch (error) {
    console.error('获取地址列表失败:', error)
    addressList.value = []
    message.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 显示添加地址弹窗
const showAddModal = () => {
  isEditing.value = false
  currentId.value = null
  resetForm()
  modalVisible.value = true
}

// 显示编辑地址弹窗
const showEditModal = (record) => {
  isEditing.value = true
  currentId.value = record.id

  // 填充表单数据
  Object.assign(formState, {
    contactName: record.contactName,
    contactPhone: record.contactPhone,
    address: record.address,
    isDefault: record.isDefault === 1
  })

  modalVisible.value = true
}

// 重置表单
const resetForm = () => {
  Object.assign(formState, {
    contactName: '',
    contactPhone: '',
    address: '',
    isDefault: false
  })

  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 提交表单（添加或更新地址）
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    const addressData = {
      contactName: formState.contactName,
      contactPhone: formState.contactPhone,
      address: formState.address,
      isDefault: formState.isDefault ? 1 : 0
    }

    if (isEditing.value) {
      addressData.id = currentId.value
      const response = await updateAddress(addressData)
      if (response.code === 0) {
        message.success('地址更新成功')
        modalVisible.value = false
        fetchAddressList()
      } else {
        message.error(response.message || '更新失败')
      }
    } else {
      const response = await addAddress(addressData)
      if (response.code === 0) {
        message.success('地址添加成功')
        modalVisible.value = false
        fetchAddressList()
      } else {
        message.error(response.message || '添加失败')
      }
    }
  } catch (error) {
    // 表单验证失败
    if (!error.response) {
      // 不是API错误，可能是验证错误
      return
    }
    console.error('表单提交失败:', error)
    message.error('操作失败，请重试')
  } finally {
    submitting.value = false
  }
}

// 取消表单
const handleCancel = () => {
  modalVisible.value = false
  resetForm()
}

// 删除地址确认
const confirmDelete = (addressId) => {
  Modal.confirm({
    title: '确认删除',
    content: '确定要删除这个地址吗？',
    okText: '确定',
    cancelText: '取消',
    okType: 'danger',
    async onOk() {
      try {
        const response = await deleteAddress(addressId)
        if (response.data.code === 0) {
          message.success('地址删除成功')
          fetchAddressList()
        } else {
          message.error(response.message || '删除失败')
        }
      } catch (error) {
        console.error('删除地址失败:', error)
        message.error('删除失败，请重试')
      }
    }
  })
}

// 设为默认地址
const setDefault = async (addressId) => {
  try {
    // 先获取地址详情
    const getResponse = await getAddressById(addressId)
    if (getResponse.data.code === 0) {
      const addressData = getResponse.data.data

      // 更新为默认地址
      const updateResponse = await updateAddress({
        ...addressData,
        isDefault: 1
      })

      if (updateResponse.data.code === 0) {
        message.success('已设为默认地址')
        fetchAddressList()
      } else {
        message.error(updateResponse.message || '设置失败')
      }
    } else {
      message.error(getResponse.data.message || '获取地址失败')
    }
  } catch (error) {
    console.error('设置默认地址失败:', error)
    message.error('设置失败，请重试')
  }
}
</script>

<style scoped>
.address-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  min-height: 500px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.page-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: rgba(0, 0, 0, 0.85);
}

.text-muted {
  color: rgba(0, 0, 0, 0.45);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .page-header h2 {
    font-size: 18px;
  }

  .address-container {
    padding: 16px;
  }
}

/* 表格样式优化 */
:deep(.ant-table) {
  background: #fff;
}

:deep(.ant-table-thead > tr > th) {
  background: #fafafa;
  font-weight: 600;
}
</style>