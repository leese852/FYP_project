<!-- src/page/employee/feedback/FeedbackDetailPage.vue -->
<template>
  <div class="feedback-detail-page">
    <el-card class="detail-card">
      <!-- 头部标题和返回按钮 -->
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-button
                type="text"
                icon="el-icon-arrow-left"
                @click="goBack"
            >
              返回列表
            </el-button>
          </div>
          <div class="header-center">
            <h2>反馈详情</h2>
            <span class="feedback-id">ID: {{ feedback.id }}</span>
          </div>
          <div class="header-right">
            <el-button
                v-if="feedback.status === 'PENDING'"
                type="primary"
                @click="markAsProcessed"
            >
              标记为已处理
            </el-button>
            <el-button
                type="danger"
                @click="handleDelete"
            >
              删除反馈
            </el-button>
          </div>
        </div>
      </template>

      <!-- 反馈基本信息 -->
      <div class="feedback-info">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>提交用户：</label>
              <span class="value">{{ feedback.userName || '用户' + feedback.userId }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>反馈类型：</label>
              <el-tag :type="getTypeTag(feedback.type)">
                {{ getTypeText(feedback.type) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>处理状态：</label>
              <el-tag :type="feedback.status === 'PENDING' ? 'warning' : 'success'">
                {{ feedback.status === 'PENDING' ? '待处理' : '已处理' }}
              </el-tag>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="8">
            <div class="info-item">
              <label>提交时间：</label>
              <span class="value">{{ formatTime(feedback.createdAt) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>更新时间：</label>
              <span class="value">{{ formatTime(feedback.updatedAt) }}</span>
            </div>
          </el-col>
          <el-col :span="8" v-if="feedback.status === 'PROCESSED'">
            <div class="info-item">
              <label>处理时间：</label>
              <span class="value">{{ formatTime(feedback.updatedAt) }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 反馈内容 -->
      <div class="feedback-content">
        <h3>反馈内容</h3>
        <div class="content-box">
          <pre>{{ feedback.content }}</pre>
        </div>
      </div>

      <!-- 管理员回复（可选功能） -->
      <div class="admin-reply" v-if="showReplySection">
        <h3>管理员回复</h3>
        <el-form :model="replyForm" :rules="replyRules" ref="replyFormRef">
          <el-form-item prop="replyContent">
            <el-input
                v-model="replyForm.replyContent"
                type="textarea"
                :rows="4"
                placeholder="请输入回复内容..."
                maxlength="500"
                show-word-limit
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitReply" :loading="replying">
              提交回复
            </el-button>
            <el-button @click="showReplySection = false">取消</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 历史回复（如果有） -->
      <div class="reply-history" v-if="replies.length > 0">
        <h3>回复记录</h3>
        <div class="reply-list">
          <div v-for="reply in replies" :key="reply.id" class="reply-item">
            <div class="reply-header">
              <span class="reply-user">{{ reply.replyBy }}</span>
              <span class="reply-time">{{ formatTime(reply.createdAt) }}</span>
            </div>
            <div class="reply-content">{{ reply.content }}</div>
          </div>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button
            v-if="!showReplySection"
            type="primary"
            @click="showReplySection = true"
        >
          <i class="el-icon-chat-line-round"></i>
          回复用户
        </el-button>
        <el-button type="info" @click="goBack">
          返回列表
        </el-button>
      </div>
    </el-card>

    <!-- 删除确认对话框 -->
    <el-dialog
        v-model="deleteDialogVisible"
        title="确认删除"
        width="400px"
    >
      <span>确定要删除这条反馈吗？删除后无法恢复。</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="confirmDelete" :loading="deleting">
            确认删除
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getFeedbackById,
  deleteFeedback,
  updateFeedback
} from '@/api/feedback'
import type { FeedbackResponse } from '@/types/feedback'

const route = useRoute()
const router = useRouter()

// 数据
const feedback = ref<FeedbackResponse>({} as FeedbackResponse)
const replies = ref<any[]>([]) // 回复列表
const loading = ref(false)
const deleting = ref(false)
const replying = ref(false)

// UI状态
const showReplySection = ref(false)
const deleteDialogVisible = ref(false)

// 回复表单
const replyForm = ref({
  replyContent: ''
})
const replyFormRef = ref()

// 验证规则
const replyRules = {
  replyContent: [
    { required: true, message: '请输入回复内容', trigger: 'blur' },
    { min: 5, message: '回复内容至少5个字符', trigger: 'blur' }
  ]
}

// 加载反馈详情
const loadFeedbackDetail = async () => {
  loading.value = true
  try {
    const feedbackId = Number(route.params.id)
    const data = await getFeedbackById(feedbackId)
    feedback.value = data

    // 这里可以加载回复历史（如果你的后端支持回复功能）
    // replies.value = await getRepliesByFeedbackId(feedbackId)
  } catch (error) {
    ElMessage.error('加载反馈详情失败')
    router.push('/employee/feedback')
  } finally {
    loading.value = false
  }
}

// 标记为已处理
const markAsProcessed = async () => {
  try {
    await updateFeedback(feedback.value.id, {
      content: feedback.value.content,
      type: feedback.value.type,
      status: 'PROCESSED' // 更新状态
    })

    ElMessage.success('已标记为已处理')
    loadFeedbackDetail() // 重新加载
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 提交回复
const submitReply = async () => {
  try {
    await replyFormRef.value.validate()
    replying.value = true

    // 这里调用回复API（如果你的后端支持）
    // await submitFeedbackReply(feedback.value.id, replyForm.value.replyContent)

    ElMessage.success('回复提交成功')
    showReplySection.value = false
    replyForm.value.replyContent = ''

    // 重新加载回复历史
    // replies.value = await getRepliesByFeedbackId(feedback.value.id)
  } catch (error) {
    // 验证失败时不处理
  } finally {
    replying.value = false
  }
}

// 处理删除
const handleDelete = () => {
  deleteDialogVisible.value = true
}

const confirmDelete = async () => {
  deleting.value = true
  try {
    await deleteFeedback(feedback.value.id)
    ElMessage.success('删除成功')
    router.push('/employee/feedback')
  } catch (error) {
    ElMessage.error('删除失败')
  } finally {
    deleting.value = false
    deleteDialogVisible.value = false
  }
}

// 辅助函数
const getTypeText = (type: string) => {
  const typeMap: Record<string, string> = {
    'SUGGESTION': '建议',
    'COMPLAINT': '投诉',
    'PRAISE': '表扬'
  }
  return typeMap[type] || type
}

const getTypeTag = (type: string) => {
  const tagMap: Record<string, string> = {
    'SUGGESTION': 'primary',
    'COMPLAINT': 'danger',
    'PRAISE': 'success'
  }
  return tagMap[type] || 'info'
}

const formatTime = (timeStr: string) => {
  if (!timeStr) return '-'
  return new Date(timeStr).toLocaleString('zh-CN')
}

const goBack = () => {
  router.push('/employee/feedback')
}

// 生命周期
onMounted(() => {
  loadFeedbackDetail()
})
</script>

<style scoped>
.feedback-detail-page {
  max-width: 1200px;
  margin: 20px auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.header-center {
  text-align: center;
  flex: 1;
}

.header-center h2 {
  margin: 0;
  color: #333;
}

.feedback-id {
  color: #666;
  font-size: 14px;
  margin-left: 10px;
}

.feedback-info {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.info-item {
  margin-bottom: 10px;
}

.info-item label {
  color: #666;
  font-weight: 500;
  margin-right: 10px;
}

.info-item .value {
  color: #333;
  font-weight: 500;
}

.feedback-content {
  margin: 30px 0;
}

.feedback-content h3 {
  margin-bottom: 15px;
  color: #333;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}

.content-box {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.content-box pre {
  margin: 0;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: inherit;
  line-height: 1.6;
  color: #333;
}

.admin-reply,
.reply-history {
  margin: 30px 0;
}

.admin-reply h3,
.reply-history h3 {
  margin-bottom: 15px;
  color: #333;
}

.reply-list {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  overflow: hidden;
}

.reply-item {
  padding: 15px;
  border-bottom: 1px solid #e9ecef;
  background: #f8f9fa;
}

.reply-item:last-child {
  border-bottom: none;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 14px;
}

.reply-user {
  color: #409eff;
  font-weight: 500;
}

.reply-time {
  color: #666;
}

.reply-content {
  color: #333;
  line-height: 1.6;
}

.action-buttons {
  margin-top: 30px;
  text-align: center;
}

.action-buttons .el-button {
  margin: 0 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .header-center {
    order: -1;
    width: 100%;
    margin-bottom: 10px;
  }

  .header-right {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    gap: 10px;
  }

  .el-row {
    margin-top: 0 !important;
  }

  .el-col {
    margin-bottom: 15px;
  }
}
</style>