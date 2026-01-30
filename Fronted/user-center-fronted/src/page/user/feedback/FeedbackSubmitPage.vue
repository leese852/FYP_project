<template>
  <div style="padding: 30px; max-width: 800px; margin: 0 auto; background: #f5f7fa; min-height: 100vh;">
    <!-- 页面标题 -->
    <div style="background: linear-gradient(135deg, #13c2c2 0%, #08979c 100%);
                padding: 20px 30px;
                border-radius: 12px;
                margin-bottom: 30px;
                box-shadow: 0 4px 12px rgba(19, 194, 194, 0.2);">
      <h1 style="color: white; margin: 0; font-size: 24px; display: flex; align-items: center;">
        <span style="background: white; color: #13c2c2; width: 36px; height: 36px; border-radius: 8px;
                    display: inline-flex; align-items: center; justify-content: center; margin-right: 12px;">
          📝
        </span>
        提交反馈
      </h1>
      <p style="color: rgba(255,255,255,0.85); margin: 8px 0 0 48px; font-size: 14px;">
        分享您的建议、意见或表扬，帮助我们做得更好
      </p>
    </div>

    <!-- 反馈表单 -->
    <div style="background: white;
                padding: 30px;
                border-radius: 12px;
                margin-bottom: 24px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.06);">

      <!-- 类型选择 -->
      <div style="margin-bottom: 32px;">
        <div style="display: flex; align-items: center; margin-bottom: 16px; padding-bottom: 12px; border-bottom: 1px solid #f0f0f0;">
          <span style="background: #13c2c2; color: white; width: 32px; height: 32px; border-radius: 8px;
                      display: inline-flex; align-items: center; justify-content: center; margin-right: 12px;">
            🏷️
          </span>
          <h2 style="margin: 0; color: #262626; font-size: 18px; font-weight: 600;">反馈类型</h2>
        </div>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 16px;">
          <label
              @click="feedbackType = 'SUGGESTION'"
              :style="{
              border: feedbackType === 'SUGGESTION' ? '2px solid #1890ff' : '1px solid #f0f0f0',
              borderRadius: '12px',
              padding: '20px 16px',
              cursor: 'pointer',
              textAlign: 'center',
              transition: 'all 0.3s',
              backgroundColor: feedbackType === 'SUGGESTION' ? '#e6f7ff' : 'white',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              gap: '12px'
            }">
            <span style="font-size: 32px; color: #1890ff;">💡</span>
            <div>
              <div style="font-weight: 600; color: #262626; margin-bottom: 4px;">建议</div>
              <div style="font-size: 12px; color: #8c8c8c;">提供改进建议</div>
            </div>
            <input type="radio" v-model="feedbackType" value="SUGGESTION" style="display: none;">
          </label>

          <label
              @click="feedbackType = 'COMPLAINT'"
              :style="{
              border: feedbackType === 'COMPLAINT' ? '2px solid #ff4d4f' : '1px solid #f0f0f0',
              borderRadius: '12px',
              padding: '20px 16px',
              cursor: 'pointer',
              textAlign: 'center',
              transition: 'all 0.3s',
              backgroundColor: feedbackType === 'COMPLAINT' ? '#fff2f0' : 'white',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              gap: '12px'
            }">
            <span style="font-size: 32px; color: #ff4d4f;">😠</span>
            <div>
              <div style="font-weight: 600; color: #262626; margin-bottom: 4px;">投诉</div>
              <div style="font-size: 12px; color: #8c8c8c;">问题反馈</div>
            </div>
            <input type="radio" v-model="feedbackType" value="COMPLAINT" style="display: none;">
          </label>

          <label
              @click="feedbackType = 'PRAISE'"
              :style="{
              border: feedbackType === 'PRAISE' ? '2px solid #52c41a' : '1px solid #f0f0f0',
              borderRadius: '12px',
              padding: '20px 16px',
              cursor: 'pointer',
              textAlign: 'center',
              transition: 'all 0.3s',
              backgroundColor: feedbackType === 'PRAISE' ? '#f6ffed' : 'white',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              gap: '12px'
            }">
            <span style="font-size: 32px; color: #52c41a;">😊</span>
            <div>
              <div style="font-weight: 600; color: #262626; margin-bottom: 4px;">表扬</div>
              <div style="font-size: 12px; color: #8c8c8c;">给予肯定</div>
            </div>
            <input type="radio" v-model="feedbackType" value="PRAISE" style="display: none;">
          </label>
        </div>
      </div>

      <!-- 内容输入 -->
      <div style="margin-bottom: 32px;">
        <div style="display: flex; align-items: center; margin-bottom: 16px; padding-bottom: 12px; border-bottom: 1px solid #f0f0f0;">
          <span style="background: #13c2c2; color: white; width: 32px; height: 32px; border-radius: 8px;
                      display: inline-flex; align-items: center; justify-content: center; margin-right: 12px;">
            💬
          </span>
          <div style="flex: 1;">
            <h2 style="margin: 0; color: #262626; font-size: 18px; font-weight: 600;">反馈内容</h2>
            <p style="margin: 4px 0 0 0; color: #8c8c8c; font-size: 13px;">
              请详细描述您的反馈内容，我们会认真阅读并改进
            </p>
          </div>
          <div style="font-size: 14px; color: #8c8c8c; background: #f0f0f0; padding: 4px 12px; border-radius: 12px;">
            最多500字
          </div>
        </div>

        <div style="position: relative;">
          <textarea
              v-model="feedbackContent"
              placeholder="请输入您的反馈内容...&#10;例如：菜品口味、服务质量、配送体验等"
              style="width: 100%;
                     height: 180px;
                     padding: 20px;
                     border: 1px solid #d9d9d9;
                     border-radius: 8px;
                     font-family: inherit;
                     font-size: 15px;
                     line-height: 1.6;
                     resize: vertical;
                     transition: all 0.3s;
                     color: #262626;"
              maxlength="500"
              @focus="$event.target.style.borderColor = '#13c2c2'"
              @blur="$event.target.style.borderColor = '#d9d9d9'"
          ></textarea>

          <!-- 字数统计 -->
          <div style="display: flex; justify-content: space-between; margin-top: 12px;">
            <div style="font-size: 13px; color: #8c8c8c;">
              <span v-if="feedbackContent.length < 100">💡 请详细描述</span>
              <span v-else-if="feedbackContent.length < 300">📝 内容不错，继续</span>
              <span v-else>✅ 内容丰富</span>
            </div>
            <div :style="{
              fontSize: '14px',
              fontWeight: '600',
              color: feedbackContent.length >= 500 ? '#ff4d4f' :
                     feedbackContent.length >= 400 ? '#fa8c16' : '#52c41a'
            }">
              {{ feedbackContent.length }}/500
            </div>
          </div>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 16px; margin-top: 40px;">
        <button
            @click="submitFeedback"
            :disabled="!feedbackContent.trim() || submitting"
            :style="{
              padding: '18px 24px',
              background: (!feedbackContent.trim() || submitting) ? '#f0f0f0' : '#13c2c2',
              color: (!feedbackContent.trim() || submitting) ? '#8c8c8c' : 'white',
              border: 'none',
              borderRadius: '8px',
              cursor: (!feedbackContent.trim() || submitting) ? 'not-allowed' : 'pointer',
              fontSize: '16px',
              fontWeight: '600',
              transition: 'all 0.3s',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              gap: '10px',
              boxShadow: (!feedbackContent.trim() || submitting) ? 'none' : '0 2px 6px rgba(19, 194, 194, 0.3)'
            }"
        >
          <span v-if="submitting" style="display: inline-block; width: 16px; height: 16px;
                border: 2px solid rgba(255,255,255,0.3);
                border-top: 2px solid white;
                border-radius: 50%;
                animation: spin 1s linear infinite;"></span>
          <span v-else>🚀</span>
          {{ submitting ? '提交中...' : '提交反馈' }}
        </button>

        <button
            @click="$router.push('/user/feedback/my')"
            :style="{
              padding: '18px 24px',
              background: '#f0f0f0',
              color: '#595959',
              border: '1px solid #d9d9d9',
              borderRadius: '8px',
              cursor: 'pointer',
              fontSize: '16px',
              fontWeight: '600',
              transition: 'all 0.3s',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              gap: '10px'
            }"
        >
          <span>📋</span>
          查看我的反馈
        </button>

        <button
            @click="$router.push('/')"
            :style="{
              padding: '18px 24px',
              background: 'white',
              color: '#595959',
              border: '1px solid #d9d9d9',
              borderRadius: '8px',
              cursor: 'pointer',
              fontSize: '16px',
              fontWeight: '600',
              transition: 'all 0.3s',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              gap: '10px'
            }"
        >
          <span>🏠</span>
          返回首页
        </button>
      </div>

      <!-- 提示信息 -->
      <div style="margin-top: 32px; padding: 16px; background: #fafafa; border-radius: 8px; border-left: 4px solid #fa8c16;">
        <div style="display: flex; align-items: flex-start; gap: 12px;">
          <span style="color: #fa8c16; font-size: 18px;">💡</span>
          <div>
            <div style="font-weight: 600; color: #262626; margin-bottom: 4px;">提交说明</div>
            <div style="font-size: 13px; color: #595959; line-height: 1.6;">
              • 提交后，反馈将进入待处理状态，管理员会尽快查看<br>
              • 您可以在"我的反馈"页面查看提交记录和处理状态<br>
              • 对于紧急问题，建议直接联系客服
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 成功提示 -->
    <div v-if="showSuccess"
         style="background: white;
                border: 1px solid #b7eb8f;
                border-left: 4px solid #52c41a;
                border-radius: 12px;
                padding: 24px;
                margin-top: 24px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
      <div style="display: flex; align-items: center; gap: 16px;">
        <div style="background: #f6ffed; width: 48px; height: 48px; border-radius: 50%;
                   display: flex; align-items: center; justify-content: center; color: #52c41a; font-size: 24px;">
          ✅
        </div>
        <div>
          <h3 style="margin: 0; color: #262626; font-size: 18px; font-weight: 600;">提交成功！</h3>
          <p style="margin: 6px 0 0 0; color: #595959;">
            感谢您的反馈，我们已收到并将尽快处理。您可以在"我的反馈"页面查看进度。
          </p>
        </div>
      </div>
    </div>

    <!-- 错误提示 -->
    <div v-if="errorMessage"
         style="background: white;
                border: 1px solid #ffccc7;
                border-left: 4px solid #ff4d4f;
                border-radius: 12px;
                padding: 24px;
                margin-top: 24px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
      <div style="display: flex; align-items: center; gap: 16px;">
        <div style="background: #fff2f0; width: 48px; height: 48px; border-radius: 50%;
                   display: flex; align-items: center; justify-content: center; color: #ff4d4f; font-size: 24px;">
          ⚠️
        </div>
        <div>
          <h3 style="margin: 0; color: #262626; font-size: 18px; font-weight: 600;">提交失败</h3>
          <p style="margin: 6px 0 0 0; color: #ff4d4f;">
            {{ errorMessage }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { feedbackAPI, userUtils } from '@/api/feedback'

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
  beforeMount() {
    // 检查登录状态
    const user = userUtils.getCurrentUser()
    if (!user) {
      this.$router.push('/login?redirect=/user/feedback')
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
        const response = await feedbackAPI.submitFeedback({
          content: this.feedbackContent.trim(),
          type: this.feedbackType
        })

        console.log('提交成功:', response)

        this.showSuccess = true
        this.feedbackContent = ''
        this.feedbackType = 'SUGGESTION'

        // 5秒后自动跳转到我的反馈页面
        setTimeout(() => {
          this.showSuccess = false
          this.$router.push('/user/feedback/my')
        }, 5000)

      } catch (error) {
        console.error('提交失败:', error)
        this.errorMessage = error.message || '提交失败，请稍后重试'

        // 如果是未登录错误，清除本地存储并跳转登录
        if (error.message.includes('登录') || error.message.includes('未授权') || error.response?.status === 401) {
          userUtils.clearUser()
          this.errorMessage = '登录已过期，请重新登录'
          setTimeout(() => {
            this.$router.push('/login?redirect=/user/feedback')
          }, 2000)
        }
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style>
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 悬停效果 */
button:hover:not(:disabled),
label:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15) !important;
}

/* 类型选择卡片悬停 */
label[style*="cursor: pointer"]:hover {
  border-color: #13c2c2 !important;
  background: #f0f9f9 !important;
}

/* 提交按钮悬停 */
button[style*="background: #13c2c2"]:hover:not(:disabled) {
  background: #36cfc9 !important;
}

/* 查看反馈按钮悬停 */
button[style*="background: #f0f0f0"]:hover:not(:disabled) {
  background: #e0e0e0 !important;
}

/* 返回首页按钮悬停 */
button[style*="background: white"]:hover:not(:disabled) {
  background: #fafafa !important;
}
</style>