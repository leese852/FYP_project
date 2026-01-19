<template>
  <div class="address-container">
    <a-button type="primary" @click="showAddModal">
      <PlusOutlined /> 添加新地址
    </a-button>

    <a-table
        :dataSource="addressList"
        :columns="columns"
        :loading="loading"
        rowKey="id"
        :pagination="false"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'isDefault'">
          <a-tag v-if="record.isDefault === 1" color="green">默认地址</a-tag>
          <span v-else>-</span>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" size="small" @click="showEditModal(record)">编辑</a-button>
            <a-button type="link" size="small" danger @click="confirmDelete(record.id)">删除</a-button>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 地址表单弹窗 -->
    <a-modal
        v-model:visible="modalVisible"
        :title="isEditing ? '编辑地址' : '添加地址'"
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
        <a-form-item label="收货人" name="contactName">
          <a-input v-model:value="formState.contactName" placeholder="请输入收货人姓名" />
        </a-form-item>

        <a-form-item label="联系电话" name="contactPhone">
          <a-input v-model:value="formState.contactPhone" placeholder="请输入8位电话号码" />
        </a-form-item>

        <a-form-item label="详细地址" name="address">
          <a-textarea v-model:value="formState.address" placeholder="请输入详细地址" :rows="3" />
        </a-form-item>

        <a-form-item name="isDefault">
          <a-checkbox v-model:checked="formState.isDefault">设为默认地址</a-checkbox>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { message, Modal } from 'ant-design-vue'
import { getAddressList, addAddress, updateAddress, deleteAddress } from '@/api/address'

// 响应式数据
const addressList = ref([])
const loading = ref(false)
const modalVisible = ref(false)
const submitting = ref(false)
const isEditing = ref(false)
const formRef = ref()
const currentId = ref(null)

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
    { required: true, message: '请输入收货人姓名' },
    { max: 20, message: '姓名最多20个字符' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系电话' },
    { pattern: /^\d{8}$/, message: '请输入8位数字电话号码' }
  ],
  address: [
    { required: true, message: '请输入详细地址' },
    { min: 5, message: '地址至少5个字符' }
  ]
}

// 表格列定义
const columns = [
  { title: '收货人', dataIndex: 'contactName', key: 'contactName', width: 120 },
  { title: '联系电话', dataIndex: 'contactPhone', key: 'contactPhone', width: 130 },
  { title: '详细地址', dataIndex: 'address', key: 'address', ellipsis: true },
  { title: '状态', key: 'isDefault', width: 100 },
  { title: '操作', key: 'action', width: 200 }
]

// 页面加载时获取地址列表
onMounted(fetchAddressList)

// 获取地址列表
async function fetchAddressList() {
  try {
    loading.value = true
    const res = await getAddressList()
    addressList.value = res?.data?.data || []
  } catch (error) {
    console.error('获取地址列表失败:', error)
    addressList.value = []
  } finally {
    loading.value = false
  }
}

// 显示添加地址弹窗
function showAddModal() {
  isEditing.value = false
  currentId.value = null
  resetForm()
  modalVisible.value = true
}

// 显示编辑地址弹窗
function showEditModal(record) {
  isEditing.value = true
  currentId.value = record.id

  formState.contactName = record.contactName
  formState.contactPhone = record.contactPhone
  formState.address = record.address
  formState.isDefault = record.isDefault === 1

  modalVisible.value = true
}

// 重置表单
function resetForm() {
  formState.contactName = ''
  formState.contactPhone = ''
  formState.address = ''
  formState.isDefault = false
  formRef.value?.clearValidate()
}

// 提交表单
async function handleSubmit() {
  try {
    await formRef.value.validate()
    submitting.value = true

    const params = {
      contactName: formState.contactName,
      contactPhone: formState.contactPhone,
      address: formState.address,
      isDefault: formState.isDefault ? 1 : 0
    }

    if (isEditing.value) {
      params.id = currentId.value
      await updateAddress(params)
      message.success('地址更新成功')
    } else {
      await addAddress(params)
      message.success('地址添加成功')
    }

    modalVisible.value = false
    fetchAddressList()
  } catch (error) {
    if (error.errorFields) return // 表单验证错误
    console.error('操作失败:', error)
    message.error('操作失败，请重试')
  } finally {
    submitting.value = false
  }
}

// 取消表单
function handleCancel() {
  modalVisible.value = false
  resetForm()
}

// 删除地址
function confirmDelete(addressId) {
  Modal.confirm({
    title: '确认删除',
    content: '确定要删除这个地址吗？',
    okType: 'danger',
    async onOk() {
      try {
        console.log('发送删除请求，地址ID:', addressId)
        const response = await deleteAddress(addressId)
        console.log('删除响应:', response)
        message.success('地址删除成功')
        fetchAddressList()
      } catch (error) {
        console.error('删除失败:', error)
      }
    }
  })
}

// // 设为默认地址
// async function setDefault(addressId) {
//   try {
//     // 直接调用更新接口，不需要先获取详情
//     await updateAddress({ id: addressId, isDefault: 1 })
//     message.success('已设为默认地址')
//     fetchAddressList()
//   } catch (error) {
//     console.error('设置失败:', error)
//   }
// }
</script>

<style scoped>
.address-container {
  padding: 20px;
}
</style>