<template>
  <div class="user-profile-container">
    <a-card title="个人信息管理" :bordered="false" class="profile-card">
      <a-alert
          message="提示"
          description="在这里可以查看和修改您的个人信息"
          type="info"
          show-icon
          style="margin-bottom: 20px;"
      />

      <!-- 加载状态 -->
      <a-skeleton active v-if="loading" />

      <!-- 用户信息展示/编辑 -->
      <div v-else class="profile-content">
        <!-- 查看模式 -->
        <div v-if="!editing" class="view-mode">
          <a-row :gutter="[24, 24]">
            <!-- 左侧：头像区域 -->
            <a-col :span="8">
              <div class="avatar-section">
                <div class="avatar-display">
                  <a-avatar :size="120" :src="userInfo.avatar" class="main-avatar">
                    {{ userInfo.username?.charAt(0) || 'U' }}
                  </a-avatar>
                </div>
                <div class="user-name">
                  <h2>{{ userInfo.username || '未设置' }}</h2>
                </div>
              </div>
            </a-col>

            <!-- 右侧：信息详情 -->
            <a-col :span="16">
              <a-descriptions bordered :column="1">
                <a-descriptions-item label="用户名">
                  <span class="info-value">{{ userInfo.username || '未设置' }}</span>
                </a-descriptions-item>

                <a-descriptions-item label="性别">
                  <a-tag :color="userInfo.gender === 1 ? 'blue' : 'pink'">
                    {{ formatGender(userInfo.gender) }}
                  </a-tag>
                </a-descriptions-item>

                <a-descriptions-item label="邮箱">
                  <span class="info-value">{{ userInfo.email || '未设置' }}</span>
                </a-descriptions-item>

                <a-descriptions-item label="电话">
                  <span class="info-value">{{ userInfo.tel || '未设置' }}</span>
                </a-descriptions-item>

              </a-descriptions>
            </a-col>
          </a-row>

          <div class="action-buttons" style="margin-top: 32px; text-align: center;">
            <a-button type="primary" size="large" @click="startEditing">
              <template #icon><EditOutlined /></template>
              编辑信息
            </a-button>
          </div>
        </div>

        <!-- 编辑模式 -->
        <div v-else class="edit-mode">
          <a-row :gutter="[40, 0]">
            <!-- 左侧：头像编辑 -->
            <a-col :span="8">
              <div class="avatar-edit-section">
                <!-- 头像预览 -->
                <div class="avatar-preview">
                  <a-avatar :size="140" :src="avatarPreview" class="edit-avatar">
                    {{ editForm.username?.charAt(0) || 'U' }}
                  </a-avatar>
                </div>

                <!-- 图片上传组件 -->
                <div class="upload-section">
                  <a-upload
                      name="file"
                      :show-upload-list="false"
                      :before-upload="beforeUpload"
                      :custom-request="handleUpload"
                      accept=".jpg,.jpeg,.png,.gif,.webp"
                  >
                    <a-button type="primary" size="large" :loading="uploading">
                      <template #icon><UploadOutlined /></template>
                      {{ avatarFile ? '更换图片' : '选择图片' }}
                    </a-button>
                  </a-upload>

                  <div class="upload-tips">
                    <p>支持格式：JPG、PNG、GIF、WebP</p>
                    <p>建议尺寸：200×200像素</p>
                    <p>大小限制：2MB以内</p>
                    <p v-if="avatarFile" class="file-name">
                      已选择：{{ avatarFile.name }}
                      <a-button type="link" size="small" @click="clearFile">
                        清除
                      </a-button>
                    </p>
                  </div>
                </div>
              </div>
            </a-col>

            <!-- 右侧：表单编辑 -->
            <a-col :span="16">
              <a-form
                  ref="formRef"
                  :model="editForm"
                  :rules="formRules"
                  layout="vertical"
              >
                <a-form-item label="用户名" name="username">
                  <a-input
                      v-model:value="editForm.username"
                      placeholder="请输入用户名"
                      size="large"
                  >
                    <template #prefix>
                      <UserOutlined />
                    </template>
                  </a-input>
                </a-form-item>

                <a-form-item label="性别" name="gender">
                  <a-radio-group v-model:value="editForm.gender">
                    <a-radio :value="0">
                      <WomanOutlined /> 女
                    </a-radio>
                    <a-radio :value="1">
                      <ManOutlined /> 男
                    </a-radio>
                  </a-radio-group>
                </a-form-item>

                <a-form-item label="邮箱" name="email">
                  <a-input
                      v-model:value="editForm.email"
                      placeholder="请输入邮箱地址"
                      size="large"
                  >
                    <template #prefix>
                      <MailOutlined />
                    </template>
                  </a-input>
                </a-form-item>

                <a-form-item label="电话" name="tel">
                  <a-input
                      v-model:value="editForm.tel"
                      placeholder="请输入电话号码"
                      size="large"
                  >
                    <template #prefix>
                      <PhoneOutlined />
                    </template>
                  </a-input>
                </a-form-item>

                <div class="form-actions">
                  <a-space size="large">
                    <a-button @click="cancelEditing" size="large">取消</a-button>
                    <a-button
                        type="primary"
                        @click="handleSave"
                        :loading="saving"
                        size="large"
                    >
                      保存修改
                    </a-button>
                  </a-space>
                </div>
              </a-form>
            </a-col>
          </a-row>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import {
  EditOutlined,
  UserOutlined,
  MailOutlined,
  PhoneOutlined,
  LinkOutlined,
  ManOutlined,
  WomanOutlined
} from '@ant-design/icons-vue'
import { getCurrentUser, userUpdate } from '@/api/user'

// 响应式数据
const loading = ref(true)
const saving = ref(false)
const editing = ref(false)
const userInfo = ref({})
const formRef = ref()

// 编辑表单数据 - 注意字段名要和后端 DTO 完全一致！
const editForm = reactive({
  username: '',
  avatar: '',
  gender: 0,
  email: '',
  tel: ''
})

// 表单验证规则
const formRules = {
  username: [
    { required: true, message: '请输入用户名' },
    { min: 2, max: 16, message: '用户名长度为2-16个字符' },
    { pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/, message: '用户名只能包含中文、英文、数字和下划线' }
  ],
  avatar: [
    {
      validator: (_, value) => {
        if (!value) return Promise.resolve()
        const urlPattern = /^(https?:\/\/.*\.(?:png|jpg|jpeg|gif|webp))$/i
        if (urlPattern.test(value)) {
          return Promise.resolve()
        }
        return Promise.reject('请输入有效的图片URL地址（支持png/jpg/jpeg/gif/webp）')
      }
    }
  ],
  gender: [
    { required: true, message: '请选择性别' }
  ],
  email: [
    { required: false, message: '请输入邮箱地址' },
    { pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '请输入有效的邮箱地址' }
  ],
  tel: [
    { required: false, message: '请输入电话号码' },
    { pattern: /^\d{8}$/, message: '请输入有效的手机号码' }
  ]
}

// 页面加载时获取用户信息
onMounted(() => {
  fetchUserInfo()
})

// 获取用户信息
async function fetchUserInfo() {
  try {
    loading.value = true
    const res = await getCurrentUser()
    console.log('用户信息响应:', res)

    if (res?.data?.code === 0) {
      userInfo.value = res.data.data || {}
      console.log('用户信息数据:', userInfo.value)
    } else {
      message.error('获取用户信息失败: ' + (res?.data?.message || '未知错误'))
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    message.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 格式化性别显示
function formatGender(gender) {
  const map = { 0: '女', 1: '男' }
  return map[gender] || '未知'
}

// 开始编辑
function startEditing() {
  editing.value = true

  // 填充表单数据 - 注意字段名映射
  editForm.username = userInfo.value.username || ''
  editForm.avatar = userInfo.value.avatar || ''
  editForm.gender = userInfo.value.gender || 0
  editForm.email = userInfo.value.email || ''
  editForm.tel = userInfo.value.tel || ''
}
// 取消编辑
function cancelEditing() {
  editing.value = false
  formRef.value?.clearValidate()
}

// 验证头像URL
function validateAvatarUrl() {
  if (editForm.avatar && !editForm.avatar.startsWith('http')) {
    editForm.avatar = 'https://' + editForm.avatar
  }
}

// 保存用户信息
async function handleSave() {
  try {
    // 表单验证
    await formRef.value.validate()

    saving.value = true

    // 准备请求参数 - 注意字段名要和后端DTO一致
    const params = {
      username: editForm.username,
      avatar: editForm.avatar,
      gender: editForm.gender,
      email: editForm.email,
      tel: editForm.tel
    }

    console.log('更新参数:', params)

    // 调用更新接口
    const res = await userUpdate(params)
    console.log('更新响应:', res)

    if (res?.data?.code === 0) {
      message.success('个人信息更新成功')

      // 重新获取最新用户信息
      await fetchUserInfo()

      // 退出编辑模式
      editing.value = false
    } else {
      message.error('更新失败: ' + (res?.data?.message || '未知错误'))
    }

  } catch (error) {
    console.error('保存失败:', error)

    if (error.errorFields) {
      // 表单验证失败
      message.warning('请检查表单填写是否正确')
      return
    }

    // API错误
    if (error.response) {
      const errorMsg = error.response.data?.message || '更新失败，请稍后重试'
      message.error(errorMsg)
    } else {
      message.error('网络错误，请检查网络连接')
    }

  } finally {
    saving.value = false
  }
}

// 格式化时间
function formatTime(timestamp) {
  if (!timestamp) return '-'

  try {
    const date = new Date(timestamp)
    if (isNaN(date.getTime())) return '-'

    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch (e) {
    return '-'
  }
}
</script>

<style scoped>
.user-profile-container {
  padding: 24px;
  min-height: calc(100vh - 64px);
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.profile-card {
  max-width: 1000px;
  margin: 0 auto;
  border-radius: 12px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.profile-content {
  padding: 8px 0;
}

.avatar-section {
  text-align: center;
  padding: 30px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
}

.avatar-display {
  margin-bottom: 20px;
}

.main-avatar {
  border: 4px solid rgba(255, 255, 255, 0.3);
  background: rgba(255, 255, 255, 0.1);
  font-size: 36px;
  font-weight: bold;
}

.user-name h2 {
  margin: 0;
  color: white;
  font-size: 24px;
  font-weight: 600;
}

.info-value {
  font-weight: 500;
  color: #333;
}

.avatar-edit-section {
  padding: 30px;
  background: #f8f9fa;
  border-radius: 12px;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-preview {
  margin-bottom: 30px;
}

.edit-avatar {
  border: 5px solid #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  font-size: 40px;
  font-weight: bold;
}

.avatar-tip {
  margin-top: 8px;
  color: #666;
  font-size: 12px;
}

.form-actions {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
  text-align: center;
}

.action-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .user-profile-container {
    padding: 16px;
  }

  .avatar-section {
    padding: 20px 10px;
  }

  .avatar-edit-section {
    padding: 20px 10px;
  }

  .main-avatar {
    width: 80px;
    height: 80px;
    font-size: 24px;
  }

  .edit-avatar {
    width: 100px;
    height: 100px;
    font-size: 30px;
  }
}

/* 动画效果 */
.view-mode, .edit-mode {
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

:deep(.ant-descriptions-item-label) {
  width: 100px;
  font-weight: 500;
  color: #555;
}

:deep(.ant-descriptions-item-content) {
  font-size: 15px;
}
</style>