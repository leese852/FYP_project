<!-- src/page/employee/feedback/FeedbackDetailPage.vue -->
<template>
  <div style="padding: 30px; max-width: 800px; margin: 0 auto;">
    <!-- 返回按钮 -->
    <div style="margin-bottom: 20px;">
      <button @click="goBack" style="background: none; border: none; color: #007BFF; cursor: pointer; display: flex; align-items: center;">
        ← 返回列表
      </button>
    </div>

    <h1 style="color: #333; border-bottom: 2px solid #6F42C1; padding-bottom: 10px;">
      🔍 反馈详情 #{{ feedbackId }}
    </h1>

    <div v-if="feedback" style="margin-top: 30px;">
      <!-- 基本信息卡片 -->
      <div style="background: #f8f9fa; padding: 25px; border-radius: 8px; margin-bottom: 25px;">
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px;">
          <div>
            <label style="display: block; color: #666; font-size: 14px; margin-bottom: 5px;">用户ID</label>
            <div style="font-weight: 500; color: #333;">{{ feedback.userId }}</div>
          </div>
          <div>
            <label style="display: block; color: #666; font-size: 14px; margin-bottom: 5px;">反馈类型</label>
            <span :style="{
              padding: '6px 12px',
              borderRadius: '4px',
              fontSize: '14px',
              fontWeight: '500',
              color: feedback.type === 'COMPLAINT' ? '#DC3545' :
                     feedback.type === 'PRAISE' ? '#28A745' : '#007BFF',
              backgroundColor: feedback.type === 'COMPLAINT' ? '#F8D7DA' :
                              feedback.type === 'PRAISE' ? '#D4EDDA' : '#D1ECF1'
            }">
              {{ getTypeText(feedback.type) }}
            </span>
          </div>
          <div>
            <label style="display: block; color: #666; font-size: 14px; margin-bottom: 5px;">处理状态</label>
            <span :style="{
              padding: '6px 12px',
              borderRadius: '12px',
              fontSize: '14px',
              fontWeight: 'bold',
              backgroundColor: feedback.status === 'PENDING' ? '#FFF3CD' : '#D4EDDA',
              color: feedback.status === 'PENDING' ? '#856404' : '#155724'
            }">
              {{ feedback.status === 'PENDING' ? '待处理' : '已处理' }}
            </span>
          </div>
        </div>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-top: 20px;">
          <div>
            <label style="display: block; color: #666; font-size: 14px; margin-bottom: 5px;">提交时间</label>
            <div style="color: #333;">{{ formatTime(feedback.createdAt) }}</div>
          </div>
          <div v-if="feedback.updatedAt && feedback.updatedAt !== feedback.createdAt">
            <label style="display: block; color: #666; font-size: 14px; margin-bottom: 5px;">更新时间</label>
            <div style="color: #333;">{{ formatTime(feedback.updatedAt) }}</div>
          </div>
        </div>
      </div>

      <!-- 反馈内容 -->
      <div style="margin-bottom: 30px;">
        <h3 style="color: #333; margin-bottom: 15px; padding-left: 10px; border-left: 4px solid #6F42C1;">
          反馈内容
        </h3>
        <div style="background: white; border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px;">
          <pre style="margin: 0; white-space: pre-wrap; word-wrap: break-word; font-family: inherit; line-height: 1.6; color: #333;">
{{ feedback.content }}
          </pre>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div style="display: flex; gap: 15px; margin-top: 40px; padding-top: 20px; border-top: 1px solid #e0e0e0;">
        <button
            v-if="feedback.status === 'PENDING'"
            @click="markAsProcessed"
            :disabled="processing"
            :style="{
            padding: '12px 30px',
            backgroundColor: processing ? '#cccccc' : '#28A745',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: processing ? 'not-allowed' : 'pointer',
            fontSize: '16px',
            flex: 1
          }"
        >
          {{ processing ? '处理中...' : '标记为已处理' }}
        </button>

        <button
            @click="deleteFeedback"
            :style="{
            padding: '12px 30px',
            backgroundColor: '#DC3545',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer',
            fontSize: '16px',
            flex: 1
          }"
        >
          删除反馈
        </button>

        <button
            @click="goBack"
            :style="{
            padding: '12px 30px',
            backgroundColor: '#6c757d',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer',
            fontSize: '16px',
            flex: 1
          }"
        >
          返回列表
        </button>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-else style="text-align: center; padding: 50px; color: #666;">
      加载中...
    </div>
  </div>
</template>

<script>
export default {
  name: 'FeedbackDetailPage',
  data() {
    return {
      feedback: null,
      feedbackId: null,
      processing: false
    }
  },
  created() {
    this.feedbackId = this.$route.params.id
    this.loadFeedbackDetail()
  },
  methods: {
    loadFeedbackDetail() {
      // 模拟加载数据
      setTimeout(() => {
        this.feedback = {
          id: this.feedbackId,
          userId: 1001,
          type: 'SUGGESTION',
          content: '建议增加更多素食选项，很多顾客有需求。同时希望可以提供更详细的营养成分表，方便健康饮食的用户参考。',
          status: 'PENDING',
          createdAt: '2024-01-15 14:30:00',
          updatedAt: '2024-01-15 14:30:00'
        }
      }, 500)
    },
    getTypeText(type) {
      const map = {
        'SUGGESTION': '建议',
        'COMPLAINT': '投诉',
        'PRAISE': '表扬'
      }
      return map[type] || type
    },
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      return date.toLocaleString('zh-CN')
    },
    markAsProcessed() {
      if (confirm('确定标记为已处理吗？')) {
        this.processing = true
        setTimeout(() => {
          this.feedback.status = 'PROCESSED'
          this.feedback.updatedAt = new Date().toISOString()
          this.processing = false
          alert('已标记为已处理')
        }, 1000)
      }
    },
    deleteFeedback() {
      if (confirm('确定删除这条反馈吗？删除后无法恢复。')) {
        setTimeout(() => {
          alert('删除成功')
          this.goBack()
        }, 1000)
      }
    },
    goBack() {
      this.$router.push('/admin/feedback')
    }
  }
}
</script>