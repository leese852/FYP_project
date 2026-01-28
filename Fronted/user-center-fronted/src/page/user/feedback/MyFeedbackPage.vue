<!-- src/page/user/feedback/MyFeedbackPage.vue -->
<template>
  <div style="padding: 50px; max-width: 800px; margin: 0 auto;">
    <h1 style="color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px;">
      📋 我的反馈
    </h1>

    <div v-if="feedbacks.length === 0" style="text-align: center; padding: 40px;">
      <p style="color: #666;">暂无反馈记录</p>
      <router-link to="/user/feedback" style="color: #4CAF50; text-decoration: none;">
        去提交反馈 →
      </router-link>
    </div>

    <div v-else>
      <div v-for="feedback in feedbacks" :key="feedback.id"
           style="border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px; margin-bottom: 20px; background: white;">

        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
          <span style="font-weight: bold; color: #2196F3;">
            {{ getTypeText(feedback.type) }}
          </span>
          <span :style="{
            padding: '4px 12px',
            borderRadius: '12px',
            fontSize: '12px',
            backgroundColor: feedback.status === 'PENDING' ? '#FFF3CD' : '#D4EDDA',
            color: feedback.status === 'PENDING' ? '#856404' : '#155724'
          }">
            {{ feedback.status === 'PENDING' ? '待处理' : '已处理' }}
          </span>
        </div>

        <p style="color: #333; line-height: 1.6; margin: 10px 0;">
          {{ feedback.content }}
        </p>

        <div style="display: flex; justify-content: space-between; margin-top: 15px; font-size: 12px; color: #888;">
          <span>提交时间: {{ formatTime(feedback.createdAt) }}</span>
          <span v-if="feedback.updatedAt !== feedback.createdAt">
            更新时间: {{ formatTime(feedback.updatedAt) }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MyFeedbackPage',
  data() {
    return {
      feedbacks: [
        {
          id: 1,
          type: 'SUGGESTION',
          content: '菜品味道很好，建议增加辣度选项',
          status: 'PENDING',
          createdAt: '2024-01-15 14:30:00',
          updatedAt: '2024-01-15 14:30:00'
        },
        {
          id: 2,
          type: 'COMPLAINT',
          content: '配送时间比预计晚了30分钟',
          status: 'PROCESSED',
          createdAt: '2024-01-10 10:15:00',
          updatedAt: '2024-01-12 09:20:00'
        }
      ]
    }
  },
  methods: {
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
    }
  }
}
</script>