<!-- src/page/admin/dish/DishAdd.vue -->
<template>
  <div class="dish-add-page">
    <div class="page-header">
      <h1>新增菜品</h1>
      <a-button type="link" @click="goBack">
        <template #icon><ArrowLeftOutlined /></template>
        返回
      </a-button>
    </div>

    <div class="dish-form">
      <a-form
          ref="formRef"
          :model="formState"
          :label-col="{ span: 4 }"
          :wrapper-col="{ span: 18 }"
          @finish="handleSubmit"
      >
        <!-- 基本信息 -->
        <a-card title="基本信息" style="margin-bottom: 20px;">
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="菜品名称" name="dishName">
                <a-input v-model:value="formState.dishName" placeholder="请输入菜品名称" />
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item label="菜品分类" name="categoryId">
                <a-select
                    v-model:value="formState.categoryId"
                    placeholder="请选择菜品分类"
                    :options="categoryOptions"
                />
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item label="菜品价格" name="price">
                <a-input-number
                    v-model:value="formState.price"
                    placeholder="请输入价格"
                    :min="0"
                    :step="0.01"
                    :precision="2"
                    style="width: 100%"
                    addon-before="¥"
                />
              </a-form-item>
            </a-col>

            <a-col :span="24">
              <a-form-item label="菜品图片">
                <div class="image-upload">
                  <div v-if="imagePreview" class="image-preview">
                    <img :src="imagePreview" alt="菜品图片预览" />
                    <a-button type="link" @click="removeImage">删除</a-button>
                  </div>
                  <div v-else class="upload-area" @click="triggerUpload">
                    <PictureOutlined style="font-size: 32px; color: #999;" />
                    <div>点击上传菜品图片</div>
                    <input
                        ref="fileInputRef"
                        type="file"
                        accept="image/*"
                        style="display: none"
                        @change="handleImageChange"
                    />
                  </div>
                </div>
              </a-form-item>
            </a-col>

            <a-col :span="24">
              <a-form-item label="菜品描述" name="description">
                <a-textarea v-model:value="formState.description" placeholder="请输入菜品描述" :rows="3" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>

        <!-- 口味规格 -->
        <a-card title="口味规格" style="margin-bottom: 20px;">
          <div v-for="(flavor, index) in formState.flavors" :key="index" class="flavor-item">
            <a-row :gutter="16" align="middle">
              <a-col :span="8">
                <a-input v-model:value="flavor.tag" placeholder="口味标签，如：辣度" />
              </a-col>
              <a-col :span="12">
                <a-input v-model:value="flavor.list" placeholder="可选值，逗号分隔，如：微辣,中辣,特辣" />
              </a-col>
              <a-col :span="4">
                <a-button
                    type="link"
                    danger
                    @click="removeFlavor(index)"
                    v-if="formState.flavors.length > 1"
                >
                  删除
                </a-button>
              </a-col>
            </a-row>
          </div>
          <a-button type="dashed" @click="addFlavor" style="margin-top: 10px; width: 100%">
            <PlusOutlined /> 添加口味
          </a-button>
          <div style="margin-top: 10px; color: #999; font-size: 12px;">
            示例：标签填"温度"，可选值填"热,温,冰"
          </div>
        </a-card>

        <!-- 操作按钮 -->
        <div class="form-actions">
          <a-space>
            <a-button type="primary" html-type="submit" :loading="submitting">保存</a-button>
            <a-button @click="resetForm">重置</a-button>
            <a-button @click="goBack">取消</a-button>
          </a-space>
        </div>
      </a-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined, PlusOutlined, PictureOutlined } from '@ant-design/icons-vue'
import { addDish } from '@/api/dish'

const router = useRouter()
const formRef = ref()
const fileInputRef = ref()
const submitting = ref(false)
const imagePreview = ref('')
const imageFile = ref(null)

// 硬编码分类
const categoryOptions = [
  { label: '主食', value: 1 },
  { label: '汤类', value: 2 },
  { label: '饮品', value: 3 },
  { label: '甜点', value: 4 },
]

// 表单数据 - 根据后端实体类调整
const formState = reactive({
  dishName: '',
  price: undefined,
  categoryId: undefined,
  description: '',
  flavors: [{
    tag: '',      // 对应 DishFlavor 的 tag 字段
    list: ''      // 对应 DishFlavor 的 list 字段
  }]
})

// 图片上传
const triggerUpload = () => fileInputRef.value?.click()

const handleImageChange = (event) => {
  const file = event.target.files[0]
  if (!file) return

  const reader = new FileReader()
  reader.onload = (e) => imagePreview.value = e.target.result
  reader.readAsDataURL(file)
  imageFile.value = file
  event.target.value = ''
}

const removeImage = () => {
  imagePreview.value = ''
  imageFile.value = null
}

// 口味管理
const addFlavor = () => formState.flavors.push({
  tag: '',
  list: ''
})

const removeFlavor = (index) => formState.flavors.splice(index, 1)

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
  removeImage()
  formState.flavors = [{
    tag: '',
    list: ''
  }]
}

// 提交
const handleSubmit = async () => {
  try {
    // 基本验证
    if (!formState.dishName) {
      message.error('请输入菜品名称')
      return
    }
    if (!formState.price || formState.price <= 0) {
      message.error('请输入正确的价格')
      return
    }
    if (!formState.categoryId) {
      message.error('请选择菜品分类')
      return
    }

    submitting.value = true

    // 转换图片为base64
    let imgUrlBase64 = null
    if (imageFile.value) {
      const reader = new FileReader()
      imgUrlBase64 = await new Promise((resolve) => {
        reader.onload = (e) => resolve(e.target.result)
        reader.readAsDataURL(imageFile.value)
      })
    }

    // 准备数据 - 根据 DishFlavor 实体类调整
    const submitData = {
      dishName: formState.dishName,
      price: formState.price,
      categoryId: formState.categoryId,
      description: formState.description,
      imgUrl: imgUrlBase64 ? imgUrlBase64.split(',')[1] : null,
      flavors: formState.flavors
          .filter(flavor => flavor.tag || flavor.list) // 过滤完全为空的口味
          .map(flavor => ({
            tag: flavor.tag || '默认',    // 确保 tag 不为空
            list: flavor.list || ''       // list 可以为空
          }))
    }

    console.log('提交的数据:', JSON.stringify(submitData, null, 2)) // 调试用

    const response = await addDish(submitData)

    if (response.data.code === 0) {
      message.success('添加成功')
      router.push({ path: "/admin/dish/list" })
      // 重置表单
      resetForm()
    } else {
      message.error(response.data.message || '添加失败')
    }
  } catch (error) {
    console.error('添加失败:', error)
    message.error('添加失败：' + (error.message || '请检查网络连接'))
  } finally {
    submitting.value = false
  }
}

const goBack = () => router.push({ name: 'dishList' })
</script>

<style scoped>
.dish-add-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.dish-form {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
}

.image-upload {
  width: 100%;
}

.upload-area {
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  padding: 40px;
  text-align: center;
  cursor: pointer;
  background: #fafafa;
}

.image-preview {
  position: relative;
  width: 150px;
}

.image-preview img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 8px;
}

.flavor-item {
  margin-bottom: 10px;
  padding: 10px;
  background: #fafafa;
  border-radius: 6px;
}

.form-actions {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}
</style>