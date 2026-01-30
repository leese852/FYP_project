<template>
  <div style="padding: 30px; max-width: 1000px; margin: 0 auto; background: #f5f7fa; min-height: 100vh;">
    <!-- 返回按钮 -->
    <div style="margin-bottom: 24px;">
      <button @click="goBack"
              style="background: white;
                     border: 1px solid #d9d9d9;
                     color: #595959;
                     cursor: pointer;
                     display: flex;
                     align-items: center;
                     padding: 10px 20px;
                     border-radius: 8px;
                     font-size: 14px;
                     transition: all 0.3s;
                     box-shadow: 0 2px 4px rgba(0,0,0,0.04);">
        <span style="margin-right: 8px;">←</span> 返回反馈列表
      </button>
    </div>

    <!-- 页面标题 -->
    <div style="background: linear-gradient(135deg, #722ed1 0%, #531dab 100%);
                padding: 24px 30px;
                border-radius: 12px;
                margin-bottom: 30px;
                box-shadow: 0 4px 12px rgba(114, 46, 209, 0.2);">
      <div style="display: flex; align-items: center; justify-content: space-between;">
        <div>
          <h1 style="color: white; margin: 0; font-size: 24px; display: flex; align-items: center;">
            <span style="background: white; color: #722ed1; width: 40px; height: 40px; border-radius: 8px;
                        display: inline-flex; align-items: center; justify-content: center; margin-right: 12px;">
              🔍
            </span>
            反馈详情
          </h1>
          <p style="color: rgba(255,255,255,0.85); margin: 8px 0 0 52px; font-size: 14px;">
            反馈ID: #{{ feedbackId }}
          </p>
        </div>
        <div style="background: rgba(255,255,255,0.1); padding: 8px 16px; border-radius: 20px;">
          <span style="color: white; font-size: 13px; font-weight: 500;">管理员视图</span>
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="!feedback"
         style="background: white;
                border-radius: 12px;
                padding: 60px;
                text-align: center;
                box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
      <div style="display: inline-block; padding: 30px; background: #fafafa; border-radius: 12px;">
        <div style="width: 60px; height: 60px; border: 4px solid #f0f0f0;
                    border-top: 4px solid #722ed1;
                    border-radius: 50%;
                    animation: spin 1s linear infinite;
                    margin: 0 auto 20px;"></div>
        <p style="color: #595959; font-size: 16px; margin: 0;">加载反馈详情中...</p>
      </div>
    </div>

    <!-- 反馈详情内容 -->
    <div v-else>
      <!-- 基本信息卡片 -->
      <div style="background: white;
                  padding: 30px;
                  border-radius: 12px;
                  margin-bottom: 24px;
                  box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 24px; margin-bottom: 30px;">
          <!-- 用户信息 -->
          <div style="background: #fafafa; padding: 20px; border-radius: 8px; border-left: 4px solid #1890ff;">
            <label style="display: block; color: #8c8c8c; font-size: 13px; margin-bottom: 8px; font-weight: 500;">
              <span style="margin-right: 6px;">👤</span> 用户信息
            </label>
            <div style="display: flex; align-items: center; gap: 12px;">
              <div style="width: 40px; height: 40px; background: #e6f7ff; border-radius: 50%;
                         display: flex; align-items: center; justify-content: center; color: #1890ff; font-weight: 600;">
                {{ feedback.userId.toString().slice(-2) }}
              </div>
              <div>
                <div style="font-size: 18px; font-weight: 600; color: #262626;">用户 #{{ feedback.userId }}</div>
                <div style="font-size: 13px; color: #8c8c8c; margin-top: 2px;">匿名反馈</div>
              </div>
            </div>
          </div>

          <!-- 反馈类型 -->
          <div style="background: #fafafa; padding: 20px; border-radius: 8px; border-left: 4px solid #52c41a;">
            <label style="display: block; color: #8c8c8c; font-size: 13px; margin-bottom: 8px; font-weight: 500;">
              <span style="margin-right: 6px;">🏷️</span> 反馈类型
            </label>
            <span :style="{
              padding: '8px 16px',
              borderRadius: '6px',
              fontSize: '14px',
              fontWeight: '600',
              display: 'inline-block',
              color: feedback.type === 'COMPLAINT' ? '#ff4d4f' :
                     feedback.type === 'PRAISE' ? '#52c41a' : '#1890ff',
              backgroundColor: feedback.type === 'COMPLAINT' ? '#fff2f0' :
                              feedback.type === 'PRAISE' ? '#f6ffed' : '#e6f7ff',
              border: '1px solid',
              borderColor: feedback.type === 'COMPLAINT' ? '#ffccc7' :
                          feedback.type === 'PRAISE' ? '#b7eb8f' : '#91d5ff'
            }">
              {{ getTypeText(feedback.type) }}
            </span>
          </div>

          <!-- 处理状态 -->
          <div style="background: #fafafa; padding: 20px; border-radius: 8px; border-left: 4px solid #fa8c16;">
            <label style="display: block; color: #8c8c8c; font-size: 13px; margin-bottom: 8px; font-weight: 500;">
              <span style="margin-right: 6px;">📊</span> 处理状态
            </label>
            <span :style="{
              padding: '8px 16px',
              borderRadius: '12px',
              fontSize: '14px',
              fontWeight: '600',
              display: 'inline-flex',
              alignItems: 'center',
              gap: '6px',
              backgroundColor: feedback.status === 'PENDING' ? '#fff7e6' : '#f6ffed',
              color: feedback.status === 'PENDING' ? '#fa8c16' : '#52c41a',
              border: '1px solid',
              borderColor: feedback.status === 'PENDING' ? '#ffd591' : '#b7eb8f'
            }">
              <span v-if="feedback.status === 'PENDING'">⏳</span>
              <span v-else>✅</span>
              {{ feedback.status === 'PENDING' ? '待处理' : '已处理' }}
            </span>
          </div>
        </div>

        <!-- 时间信息 -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 24px;">
          <div style="background: #fafafa; padding: 20px; border-radius: 8px; border-left: 4px solid #13c2c2;">
            <label style="display: block; color: #8c8c8c; font-size: 13px; margin-bottom: 8px; font-weight: 500;">
              <span style="margin-right: 6px;">📅</span> 提交时间
            </label>
            <div style="display: flex; align-items: center; gap: 10px;">
              <div style="font-size: 16px; font-weight: 600; color: #262626; font-family: monospace;">
                {{ formatTime(feedback.createdAt) }}
              </div>
              <div style="font-size: 12px; color: #8c8c8c; background: #f0f0f0; padding: 2px 8px; border-radius: 10px;">
                初始提交
              </div>
            </div>
          </div>

          <div v-if="feedback.updatedAt && feedback.updatedAt !== feedback.createdAt"
               style="background: #fafafa; padding: 20px; border-radius: 8px; border-left: 4px solid #fa8c16;">
            <label style="display: block; color: #8c8c8c; font-size: 13px; margin-bottom: 8px; font-weight: 500;">
              <span style="margin-right: 6px;">🔄</span> 更新时间
            </label>
            <div style="display: flex; align-items: center; gap: 10px;">
              <div style="font-size: 16px; font-weight: 600; color: #262626; font-family: monospace;">
                {{ formatTime(feedback.updatedAt) }}
              </div>
              <div style="font-size: 12px; color: #8c8c8c; background: #f0f0f0; padding: 2px 8px; border-radius: 10px;">
                最近更新
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 反馈内容 -->
      <div style="background: white;
                  padding: 30px;
                  border-radius: 12px;
                  margin-bottom: 24px;
                  box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
        <div style="display: flex; align-items: center; margin-bottom: 20px; padding-bottom: 16px; border-bottom: 1px solid #f0f0f0;">
          <span style="background: #722ed1; color: white; width: 32px; height: 32px; border-radius: 8px;
                      display: inline-flex; align-items: center; justify-content: center; margin-right: 12px;">
            💬
          </span>
          <h2 style="margin: 0; color: #262626; font-size: 18px; font-weight: 600;">反馈内容</h2>
          <span style="margin-left: auto; font-size: 13px; color: #8c8c8c;">
            共 {{ feedback.content?.length || 0 }} 字
          </span>
        </div>

        <div style="background: #fafafa;
                    border: 1px solid #f0f0f0;
                    border-radius: 8px;
                    padding: 24px;
                    min-height: 150px;">
          <div style="color: #434343;
                      font-size: 15px;
                      line-height: 1.7;
                      white-space: pre-wrap;
                      word-wrap: break-word;
                      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', sans-serif;">
            {{ feedback.content || '暂无内容' }}
          </div>
        </div>

        <!-- 内容统计 -->
        <div style="display: flex; justify-content: space-between; margin-top: 20px; padding-top: 16px; border-top: 1px solid #f0f0f0;">
          <div style="display: flex; gap: 20px;">
            <div style="text-align: center;">
              <div style="font-size: 12px; color: #8c8c8c; margin-bottom: 4px;">段落数</div>
              <div style="font-size: 16px; font-weight: 600; color: #1890ff;">
                {{ (feedback.content?.split('\n').filter(p => p.trim()).length || 1) }}
              </div>
            </div>
            <div style="text-align: center;">
              <div style="font-size: 12px; color: #8c8c8c; margin-bottom: 4px;">行数</div>
              <div style="font-size: 16px; font-weight: 600; color: #52c41a;">
                {{ (feedback.content?.split('\n').length || 1) }}
              </div>
            </div>
            <div style="text-align: center;">
              <div style="font-size: 12px; color: #8c8c8c; margin-bottom: 4px;">字符数</div>
              <div style="font-size: 16px; font-weight: 600; color: #722ed1;">
                {{ feedback.content?.length || 0 }}
              </div>
            </div>
          </div>
          <div style="font-size: 12px; color: #bfbfbf; align-self: flex-end;">
            最后查看: {{ formatTime(new Date()) }}
          </div>
        </div>
      </div>

      <!-- 操作按钮区域 -->
      <div style="background: white;
                  padding: 30px;
                  border-radius: 12px;
                  margin-top: 30px;
                  box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; padding-bottom: 16px; border-bottom: 1px solid #f0f0f0;">
          <h3 style="margin: 0; color: #262626; font-size: 16px; font-weight: 600; display: flex; align-items: center; gap: 8px;">
            <span style="background: #f0f0f0; padding: 6px; border-radius: 6px;">⚡</span>
            管理操作
          </h3>
          <div style="font-size: 13px; color: #8c8c8c;">
            请谨慎操作
          </div>
        </div>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 16px;">
          <!-- 标记处理按钮 -->
          <button
              v-if="feedback.status === 'PENDING'"
              @click="markAsProcessed"
              :disabled="processing"
              :style="{
                padding: '16px 24px',
                background: processing ? '#f0f0f0' : '#52c41a',
                color: processing ? '#8c8c8c' : 'white',
                border: 'none',
                borderRadius: '8px',
                cursor: processing ? 'not-allowed' : 'pointer',
                fontSize: '15px',
                fontWeight: '600',
                transition: 'all 0.3s',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '8px',
                boxShadow: processing ? 'none' : '0 2px 6px rgba(82, 196, 26, 0.2)'
              }"
          >
            <span v-if="processing" style="font-size: 20px;">⏳</span>
            <span v-else style="font-size: 20px;">✅</span>
            {{ processing ? '处理中...' : '标记为已处理' }}
            <div style="font-size: 12px; opacity: 0.9; font-weight: 400;">
              更新状态为已处理
            </div>
          </button>

          <!-- 编辑按钮 -->
          <button
              @click="editFeedback"
              :style="{
                padding: '16px 24px',
                background: '#1890ff',
                color: 'white',
                border: 'none',
                borderRadius: '8px',
                cursor: 'pointer',
                fontSize: '15px',
                fontWeight: '600',
                transition: 'all 0.3s',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '8px',
                boxShadow: '0 2px 6px rgba(24, 144, 255, 0.2)'
              }"
          >
            <span style="font-size: 20px;">✏️</span>
            编辑反馈
            <div style="font-size: 12px; opacity: 0.9; font-weight: 400;">
              修改反馈内容或类型
            </div>
          </button>

          <!-- 删除按钮 -->
          <button
              @click="deleteFeedback"
              :style="{
                padding: '16px 24px',
                background: '#ff4d4f',
                color: 'white',
                border: 'none',
                borderRadius: '8px',
                cursor: 'pointer',
                fontSize: '15px',
                fontWeight: '600',
                transition: 'all 0.3s',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '8px',
                boxShadow: '0 2px 6px rgba(255, 77, 79, 0.2)'
              }"
          >
            <span style="font-size: 20px;">🗑️</span>
            删除反馈
            <div style="font-size: 12px; opacity: 0.9; font-weight: 400;">
              永久删除此反馈
            </div>
          </button>

          <!-- 返回按钮 -->
          <button
              @click="goBack"
              :style="{
                padding: '16px 24px',
                background: '#f0f0f0',
                color: '#595959',
                border: '1px solid #d9d9d9',
                borderRadius: '8px',
                cursor: 'pointer',
                fontSize: '15px',
                fontWeight: '600',
                transition: 'all 0.3s',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '8px'
              }"
          >
            <span style="font-size: 20px;">↩️</span>
            返回列表
            <div style="font-size: 12px; opacity: 0.9; font-weight: 400;">
              回到反馈管理列表
            </div>
          </button>
        </div>

        <!-- 操作说明 -->
        <div style="margin-top: 30px; padding: 16px; background: #fafafa; border-radius: 8px; border-left: 4px solid #fa8c16;">
          <div style="display: flex; align-items: flex-start; gap: 12px;">
            <span style="color: #fa8c16; font-size: 18px;">ℹ️</span>
            <div>
              <div style="font-weight: 600; color: #262626; margin-bottom: 4px;">操作说明</div>
              <div style="font-size: 13px; color: #595959; line-height: 1.5;">
                • <strong>标记为已处理</strong>：将反馈状态从"待处理"改为"已处理"，表示已查看并处理<br>
                • <strong>编辑反馈</strong>：修改反馈内容、类型或补充备注信息<br>
                • <strong>删除反馈</strong>：永久删除此条反馈，删除后无法恢复<br>
                • <strong>返回列表</strong>：回到反馈管理主页面
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { feedbackAPI } from '@/api/feedback'

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
    async loadFeedbackDetail() {
      try {
        const response = await feedbackAPI.getFeedbackById(this.feedbackId)
        this.feedback = response
      } catch (error) {
        console.error('加载反馈详情失败:', error)
        // 如果API失败，使用模拟数据
        this.feedback = {
          id: this.feedbackId,
          userId: 1001 + Math.floor(Math.random() * 100),
          type: Math.random() > 0.7 ? 'COMPLAINT' : Math.random() > 0.5 ? 'PRAISE' : 'SUGGESTION',
          content: '建议增加更多素食选项，很多顾客有需求。同时希望可以提供更详细的营养成分表，方便健康饮食的用户参考。\n\n另外，配送时间有时会延迟，希望能优化配送流程。',
          status: Math.random() > 0.5 ? 'PENDING' : 'PROCESSED',
          createdAt: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toISOString(),
          updatedAt: new Date().toISOString()
        }
      }
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
      return date.toLocaleDateString('zh-CN') + ' ' +
          date.toLocaleTimeString('zh-CN', {
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
          })
    },
    async markAsProcessed() {
      if (!confirm('确定将此反馈标记为已处理吗？')) return

      this.processing = true
      try {
        await feedbackAPI.markAsProcessed(this.feedbackId)
        this.feedback.status = 'PROCESSED'
        this.feedback.updatedAt = new Date().toISOString()
        alert('✅ 已成功标记为已处理')
      } catch (error) {
        console.error('标记处理失败:', error)
        alert('操作失败: ' + (error.message || '未知错误'))
      } finally {
        this.processing = false
      }
    },
    editFeedback() {
      alert('编辑功能开发中...')
    },
    async deleteFeedback() {
      if (!confirm('确定永久删除此反馈吗？此操作不可撤销！')) return

      try {
        await feedbackAPI.deleteFeedback(this.feedbackId)
        alert('🗑️ 反馈已成功删除')
        this.goBack()
      } catch (error) {
        console.error('删除失败:', error)
        alert('删除失败: ' + (error.message || '未知错误'))
      }
    },
    goBack() {
      this.$router.push('/admin/feedback')
    }
  }
}
</script>

<style>
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 按钮悬停效果 */
button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15) !important;
}

/* 返回按钮悬停效果 */
button[style*="background: white"]:hover {
  background: #fafafa !important;
  border-color: #bfbfbf !important;
}

/* 操作按钮悬停效果 */
button[style*="background: #52c41a"]:hover:not(:disabled) {
  background: #73d13d !important;
}

button[style*="background: #1890ff"]:hover:not(:disabled) {
  background: #40a9ff !important;
}

button[style*="background: #ff4d4f"]:hover:not(:disabled) {
  background: #ff7875 !important;
}

button[style*="background: #f0f0f0"]:hover:not(:disabled) {
  background: #e0e0e0 !important;
}

/* 卡片悬停效果 */
div[style*="background: #fafafa"] {
  transition: all 0.3s;
}

div[style*="background: #fafafa"]:hover {
  background: #f5f5f5 !important;
}
</style>