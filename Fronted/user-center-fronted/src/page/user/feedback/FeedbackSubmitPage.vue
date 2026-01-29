<template>
  <div style="padding: 50px; max-width: 600px; margin: 0 auto;">
    <h1 style="color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px;">
      📝 提交反馈
    </h1>

    <div style="background: #f9f9f9; padding: 30px; border-radius: 8px; margin-top: 20px;">

      <div style="margin-bottom: 20px;">
        <label style="display: block; margin-bottom: 8px; font-weight: 500; color: #555;">
          反馈类型
        </label>
        <div style="display: flex; gap: 15px;">
          <label style="display: flex; align-items: center; cursor: pointer;">
            <input type="radio" v-model="feedbackType" value="SUGGESTION" style="margin-right: 8px;">
            <span>建议</span>
          </label>
          <label style="display: flex; align-items: center; cursor: pointer;">
            <input type="radio" v-model="feedbackType" value="COMPLAINT" style="margin-right: 8px;">
            <span>投诉</span>
          </label>
          <label style="display: flex; align-items: center; cursor: pointer;">
            <input type="radio" v-model="feedbackType" value="PRAISE" style="margin-right: 8px;">
            <span>表扬</span>
          </label>
        </div>
      </div>

      <div style="margin-bottom: 20px;">
        <label style="display: block; margin-bottom: 8px; font-weight: 500; color: #555;">
          反馈内容
        </label>
        <textarea
            v-model="feedbackContent"
            placeholder="请输入您的反馈内容..."
            style="width: 100%; height: 150px; padding: 12px; border: 1px solid #ddd; border-radius: 4px; font-family: inherit;"
            maxlength="500"
        ></textarea>
        <div style="text-align: right; margin-top: 5px; color: #999; font-size: 12px;">
          字数: {{ feedbackContent.length }}/500
        </div>
      </div>

      <div style="display: flex; gap: 10px; margin-top: 30px;">
        <button
            @click="submitFeedback"
            :disabled="!feedbackContent.trim() || submitting"
            :style="{
            padding: '12px 30px',
            backgroundColor: (feedbackContent.trim() && !submitting) ? '#4CAF50' : '#cccccc',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: (feedbackContent.trim() && !submitting) ? 'pointer' : 'not-allowed',
            fontSize: '16px',
            flex: 1
          }"
        >
          {{ submitting ? '提交中...' : '提交反馈' }}
        </button>

        <button
            @click="$router.push('/user/feedback/my')"
            style="padding: 12px 30px; background: #6c757d; color: white; border: none; border-radius: 4px; cursor: pointer;"
        >
          查看我的反馈
        </button>
      </div>
    </div>

    <!-- 成功提示 -->
    <div v-if="showSuccess" style="margin-top: 20px; padding: 15px; background: #D4EDDA; color: #155724; border-radius: 4px;">
      反馈提交成功！感谢您的反馈。
    </div>

    <!-- 错误提示 -->
    <div v-if="errorMessage" style="margin-top: 20px; padding: 15px; background: #F8D7DA; color: #721C24; border-radius: 4px;">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import { feedbackAPI } from '@/api/feedback'

export default {
  name: 'FeedbackSubmitPage',
  data() {
    return {
      feedbackContent: '',
      feedbackType: 'SUGGESTION',
      submitting: false,
      showSuccess: false,
      errorMessage: ''
    }
  },
  methods: {
    async submitFeedback() {
      if (!this.feedbackContent.trim()) {
        this.errorMessage = '请输入反馈内容'
        return
      }

      this.submitting = true
      this.errorMessage = ''

      try {
        // 调用真实API
        const response = await feedbackAPI.submitFeedback({
          content: this.feedbackContent,
          type: this.feedbackType
        })

        console.log('提交成功:', response)

        this.showSuccess = true
        this.feedbackContent = ''

        // 3秒后隐藏成功提示
        setTimeout(() => {
          this.showSuccess = false
        }, 3000)

      } catch (error) {
        console.error('提交失败:', error)
        this.errorMessage = error.response?.data?.message || '提交失败，请稍后重试'
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>