<!-- src/page/admin/dish/DishEdit.vue -->
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { ArrowLeftOutlined, PlusOutlined, PictureOutlined } from '@ant-design/icons-vue'
import { getDishById, updateDish } from '@/api/dish'

const route = useRoute()
const router = useRouter()

// 添加加载状态和错误状态
const loading = ref(false)
const loadError = ref(false)

// 获取菜品ID
const dishId = ref(0)

// 在onMounted中获取ID并加载数据
onMounted(() => {
  // 从路由参数获取ID
  const idFromRoute = route.params.id

  if (!idFromRoute || isNaN(parseInt(idFromRoute))) {
    message.error('菜品ID无效')
    router.push('/admin/dish/list')
    return
  }

  dishId.value = parseInt(idFromRoute)
  loadDishData()
})

// 状态选项
const statusOptions = [
  { label: '上架', value: 1 },
  { label: '下架', value: 0 }
]

// 分类选项
const categoryOptions = [
  { label: '主食', value: 1 },
  { label: '热菜', value: 2 },
  { label: '凉菜', value: 3 },
  { label: '汤类', value: 4 },
  { label: '饮品', value: 5 },
  { label: '甜点', value: 6 },
  { label: '套餐', value: 7 },
  { label: '招牌菜', value: 8 },
  { label: '特色菜', value: 9 },
  { label: '其他', value: 10 }
]

// 表单数据
const formState = reactive({
  id: 0,
  dishName: '',
  price: undefined,
  categoryId: undefined,
  description: '',
  isAvailable: 1,
  imgUrl: '',
  flavors: [{ tag: '', list: '' }]
})

// 图片处理
const fileInputRef = ref()
const imageFile = ref(null)

// 提交状态
const submitting = ref(false)

// 获取图片URL
const getImageUrl = (imgData) => {
  if (!imgData) return ''
  // 如果已经是完整的data URL，直接返回
  if (typeof imgData === 'string' && imgData.startsWith('data:')) {
    return imgData
  }
  // 如果是base64字符串，添加前缀
  return `data:image/jpeg;base64,${imgData}`
}

// 加载菜品数据
const loadDishData = async () => {
  if (!dishId.value || dishId.value <= 0) return

  loading.value = true
  loadError.value = false

  try {
    console.log('开始加载菜品数据，ID:', dishId.value)

    const response = await getDishById(dishId.value)
    console.log('API响应:', response)

    if (response.data.code === 0 && response.data.data) {
      const dishData = response.data.data
      console.log('菜品数据:', dishData)

      // 填充表单数据
      formState.id = dishId.value
      formState.dishName = dishData.dishName || ''
      formState.price = dishData.price ? parseFloat(dishData.price) : undefined
      formState.categoryId = dishData.categoryId
      formState.description = dishData.description || ''
      formState.isAvailable = dishData.isAvailable || 0

      // 处理图片
      if (dishData.imgUrl) {
        formState.imgUrl = getImageUrl(dishData.imgUrl)
      } else {
        formState.imgUrl = ''
      }

      // 处理口味数据
      if (dishData.flavors && Array.isArray(dishData.flavors) && dishData.flavors.length > 0) {
        formState.flavors = dishData.flavors.map(flavor => ({
          tag: flavor.tag || '',
          list: flavor.list || ''
        }))
      } else {
        formState.flavors = [{ tag: '', list: '' }]
      }

      console.log('表单数据已填充:', formState)
    } else {
      loadError.value = true
      message.error(response.data.message || '加载菜品信息失败')
    }
  } catch (error) {
    console.error('加载菜品数据失败:', error)
    loadError.value = true
    message.error('加载菜品信息失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 图片上传
const triggerUpload = () => {
  fileInputRef.value?.click()
}

const handleImageChange = (event) => {
  const file = event.target.files[0]
  if (!file) return

  // 验证文件类型
  const validTypes = ['image/jpeg', 'image/png', 'image/jpg', 'image/gif', 'image/webp']
  if (!validTypes.includes(file.type)) {
    message.error('只能上传 JPG、PNG、GIF、WEBP 格式的图片')
    return
  }

  // 验证文件大小（2MB）
  const maxSize = 2 * 1024 * 1024
  if (file.size > maxSize) {
    message.error('图片大小不能超过 2MB')
    return
  }

  // 读取文件
  const reader = new FileReader()
  reader.onload = (e) => {
    formState.imgUrl = e.target.result
  }
  reader.readAsDataURL(file)

  imageFile.value = file
  event.target.value = ''
}

const removeImage = () => {
  formState.imgUrl = ''
  imageFile.value = null
}

// 口味管理
const addFlavor = () => {
  formState.flavors.push({ tag: '', list: '' })
}

const removeFlavor = (index) => {
  if (formState.flavors.length > 1) {
    formState.flavors.splice(index, 1)
  }
}

// 处理表单提交
const handleSubmit = async () => {
  // 基本验证
  if (!formState.dishName || formState.dishName.trim() === '') {
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

  try {
    // 准备提交数据
    const submitData = {
      id: dishId.value,
      dishName: formState.dishName.trim(),
      price: formState.price,
      categoryId: formState.categoryId,
      description: formState.description?.trim() || '',
      isAvailable: formState.isAvailable,
      // 处理图片：如果是新上传的图片，提取base64部分
      imgUrl: formState.imgUrl?.startsWith('data:image/')
          ? formState.imgUrl.split(',')[1]
          : formState.imgUrl || null,
      // 处理口味数据
      flavors: formState.flavors
          .filter(flavor => flavor.tag?.trim() || flavor.list?.trim())
          .map(flavor => ({
            tag: flavor.tag?.trim() || '',
            list: flavor.list?.trim() || ''
          }))
    }

    console.log('提交的更新数据:', JSON.stringify(submitData, null, 2))

    // 调用更新API
    const response = await updateDish(submitData)

    if (response.data.code === 0) {
      message.success('菜品更新成功！')
      // 延迟返回，让用户看到成功提示
      setTimeout(() => {
        router.push('/admin/dish/list')
      }, 1500)
    } else {
      message.error(response.data.message || '更新失败')
    }
  } catch (error) {
    console.error('更新菜品失败:', error)
    message.error('更新失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

const goBack = () => {
  router.push('/admin/dish/list')
}
</script>

<template>
  <div class="dish-edit-page">
    <!-- 加载中状态 -->
    <div v-if="loading" class="loading-state">
      <a-spin size="large" tip="加载菜品信息中..." />
    </div>

    <!-- 加载失败状态 -->
    <div v-else-if="loadError" class="error-state">
      <a-result
          status="error"
          title="加载失败"
          sub-title="无法加载菜品信息，请检查网络连接或菜品ID是否正确"
      >
        <template #extra>
          <a-button type="primary" @click="goBack">返回列表</a-button>
          <a-button @click="loadDishData">重试</a-button>
        </template>
      </a-result>
    </div>

    <!-- 正常显示 -->
    <div v-else>
      <div class="page-header">
        <h1>编辑菜品 <span v-if="dishId">(ID: {{ dishId }})</span></h1>
        <a-button type="link" @click="goBack">
          <template #icon><ArrowLeftOutlined /></template>
          返回列表
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
                <a-form-item label="菜品名称">
                  <a-input
                      v-model:value="formState.dishName"
                      placeholder="请输入菜品名称"
                  />
                </a-form-item>
              </a-col>

              <a-col :span="12">
                <a-form-item label="菜品分类">
                  <a-select
                      v-model:value="formState.categoryId"
                      placeholder="请选择菜品分类"
                      :options="categoryOptions"
                  />
                </a-form-item>
              </a-col>

              <a-col :span="12">
                <a-form-item label="菜品价格">
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

              <a-col :span="12">
                <a-form-item label="菜品状态">
                  <a-select
                      v-model:value="formState.isAvailable"
                      placeholder="请选择状态"
                      :options="statusOptions"
                  />
                </a-form-item>
              </a-col>

              <a-col :span="24">
                <a-form-item label="菜品图片">
                  <div class="image-upload">
                    <!-- 显示现有图片 -->
                    <div v-if="formState.imgUrl" class="image-preview">
                      <img :src="formState.imgUrl" alt="菜品图片" />
                      <div class="image-actions">
                        <a-button type="link" @click="triggerUpload">更换</a-button>
                        <a-button type="link" danger @click="removeImage">删除</a-button>
                      </div>
                      <input
                          ref="fileInputRef"
                          type="file"
                          accept="image/*"
                          style="display: none"
                          @change="handleImageChange"
                      />
                    </div>

                    <!-- 上传区域 -->
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
                <a-form-item label="菜品描述">
                  <a-textarea
                      v-model:value="formState.description"
                      placeholder="请输入菜品描述"
                      :rows="3"
                  />
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
          </a-card>

          <!-- 操作按钮 -->
          <div class="form-actions">
            <a-space>
              <a-button type="primary" html-type="submit" :loading="submitting">保存修改</a-button>
              <a-button @click="goBack">取消</a-button>
            </a-space>
          </div>
        </a-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dish-edit-page {
  padding: 20px;
  min-height: calc(100vh - 64px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.page-header h1 span {
  font-size: 14px;
  color: #666;
  font-weight: normal;
  margin-left: 8px;
}

.dish-form {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
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
  transition: all 0.3s;
}

.upload-area:hover {
  border-color: #1890ff;
  background: #e6f7ff;
}

.image-preview {
  position: relative;
  width: 150px;
  height: 150px;
  margin-bottom: 10px;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.image-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-around;
  background: rgba(0, 0, 0, 0.6);
  padding: 8px;
  border-bottom-left-radius: 8px;
  border-bottom-right-radius: 8px;
}

.flavor-item {
  margin-bottom: 10px;
  padding: 10px;
  background: #fafafa;
  border-radius: 6px;
  border: 1px solid #f0f0f0;
}

.form-actions {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
  margin-top: 20px;
}

.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60vh;
}

.error-state {
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .dish-edit-page {
    padding: 16px;
  }

  .dish-form {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}
</style>