<template>
  <div style="padding: 30px; max-width: 1000px; margin: 0 auto; background: #f5f7fa; min-height: 100vh;">
    <!-- 页面标题 -->
    <div style="background: linear-gradient(135deg, #4CAF50 0%, #2E7D32 100%);
                padding: 20px 30px;
                border-radius: 12px;
                margin-bottom: 30px;
                box-shadow: 0 4px 12px rgba(76, 175, 80, 0.2);">
      <h1 style="color: white; margin: 0; font-size: 24px; display: flex; align-items: center;">
        <span style="background: white; color: #4CAF50; width: 36px; height: 36px; border-radius: 8px;
                    display: inline-flex; align-items: center; justify-content: center; margin-right: 12px;">
          📋
        </span>
        我的反馈
      </h1>
      <p style="color: rgba(255,255,255,0.85); margin: 8px 0 0 48px; font-size: 14px;">
        查看您提交的所有反馈记录
      </p>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading"
         style="background: white;
                border-radius: 12px;
                padding: 60px;
                text-align: center;
                box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
      <div style="display: inline-block; padding: 30px; background: #fafafa; border-radius: 12px;">
        <div style="width: 60px; height: 60px; border: 4px solid #f0f0f0;
                    border-top: 4px solid #4CAF50;
                    border-radius: 50%;
                    animation: spin 1s linear infinite;
                    margin: 0 auto 20px;"></div>
        <p style="color: #595959; font-size: 16px; margin: 0;">正在加载您的反馈...</p>
      </div>
    </div>

    <!-- 未登录状态 -->
    <div v-else-if="!isLoggedIn && !error"
         style="background: white;
                border-radius: 12px;
                padding: 60px;
                text-align: center;
                box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
      <div style="display: inline-block; padding: 40px; max-width: 400px;">
        <div style="font-size: 64px; margin-bottom: 20px; color: #4CAF50;">🔒</div>
        <h3 style="color: #262626; font-size: 20px; margin-bottom: 12px; font-weight: 600;">
          请先登录
        </h3>
        <p style="color: #595959; margin-bottom: 30px; line-height: 1.6;">
          登录后即可查看您的反馈记录和提交新的反馈
        </p>
        <div style="display: flex; gap: 12px; justify-content: center;">
          <router-link to="/login"
                       style="padding: 12px 24px;
                              background: #4CAF50;
                              color: white;
                              text-decoration: none;
                              border-radius: 8px;
                              font-weight: 500;
                              transition: all 0.3s;
                              display: inline-flex;
                              align-items: center;
                              gap: 6px;">
            <span>→</span> 前往登录
          </router-link>
          <router-link to="/"
                       style="padding: 12px 24px;
                              background: #f0f0f0;
                              color: #595959;
                              text-decoration: none;
                              border-radius: 8px;
                              font-weight: 500;
                              transition: all 0.3s;">
            返回首页
          </router-link>
        </div>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error"
         style="background: white;
                border-radius: 12px;
                padding: 60px;
                text-align: center;
                box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
      <div style="display: inline-block; padding: 40px; max-width: 500px;">
        <div style="font-size: 64px; margin-bottom: 20px; color: #ff4d4f;">⚠️</div>
        <h3 style="color: #262626; font-size: 20px; margin-bottom: 12px; font-weight: 600;">
          加载失败
        </h3>
        <p style="color: #ff4d4f; margin-bottom: 20px; background: #fff2f0; padding: 12px; border-radius: 6px;">
          {{ error }}
        </p>
        <div style="display: flex; gap: 12px; justify-content: center;">
          <button @click="loadUserFeedbacks"
                  style="padding: 12px 24px;
                        background: #ff4d4f;
                        color: white;
                        border: none;
                        border-radius: 8px;
                        cursor: pointer;
                        font-weight: 500;
                        transition: all 0.3s;">
            重新加载
          </button>
          <router-link to="/"
                       style="padding: 12px 24px;
                              background: #f0f0f0;
                              color: #595959;
                              text-decoration: none;
                              border-radius: 8px;
                              font-weight: 500;
                              transition: all 0.3s;">
            返回首页
          </router-link>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else-if="feedbacks.length === 0"
         style="background: white;
                border-radius: 12px;
                padding: 80px 40px;
                text-align: center;
                box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
      <div style="display: inline-block; max-width: 400px;">
        <div style="font-size: 80px; margin-bottom: 20px; color: #bfbfbf;">📭</div>
        <h3 style="color: #262626; font-size: 20px; margin-bottom: 12px; font-weight: 600;">
          暂无反馈记录
        </h3>
        <p style="color: #595959; margin-bottom: 30px; line-height: 1.6;">
          您还没有提交过任何反馈<br>
          分享您的建议和意见，帮助我们做得更好
        </p>
        <div style="display: flex; gap: 12px; justify-content: center;">
          <router-link to="/user/feedback"
                       style="padding: 12px 24px;
                              background: #4CAF50;
                              color: white;
                              text-decoration: none;
                              border-radius: 8px;
                              font-weight: 500;
                              transition: all 0.3s;
                              display: inline-flex;
                              align-items: center;
                              gap: 6px;">
            <span>+</span> 提交反馈
          </router-link>
          <router-link to="/"
                       style="padding: 12px 24px;
                              background: #f0f0f0;
                              color: #595959;
                              text-decoration: none;
                              border-radius: 8px;
                              font-weight: 500;
                              transition: all 0.3s;">
            返回首页
          </router-link>
        </div>
      </div>
    </div>

    <!-- 有数据 -->
    <div v-else>
      <!-- 用户信息卡片 -->
      <div style="background: white;
                  padding: 24px;
                  border-radius: 12px;
                  margin-bottom: 24px;
                  box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <div style="display: flex; align-items: center; gap: 16px;">
            <div style="width: 50px; height: 50px; background: #e8f5e8; border-radius: 50%;
                       display: flex; align-items: center; justify-content: center; color: #4CAF50; font-weight: 600; font-size: 18px;">
              {{ currentUsername?.charAt(0) || 'U' }}
            </div>
            <div>
              <div style="font-size: 16px; font-weight: 600; color: #262626;">{{ currentUsername || '用户' }}</div>
              <div style="display: flex; gap: 20px; margin-top: 6px;">
                <span style="font-size: 13px; color: #8c8c8c;">
                  <span style="font-weight: 500; color: #4CAF50;">{{ feedbacks.length }}</span> 条反馈
                </span>
                <span style="font-size: 13px; color: #8c8c8c;">
                  <span style="font-weight: 500; color: #fa8c16;">{{ pendingCount }}</span> 条待处理
                </span>
                <span style="font-size: 13px; color: #8c8c8c;">
                  <span style="font-weight: 500; color: #52c41a;">{{ processedCount }}</span> 条已处理
                </span>
              </div>
            </div>
          </div>
          <div style="display: flex; gap: 12px;">
            <router-link to="/user/feedback"
                         style="padding: 10px 20px;
                                background: #4CAF50;
                                color: white;
                                text-decoration: none;
                                border-radius: 8px;
                                font-size: 14px;
                                font-weight: 500;
                                transition: all 0.3s;
                                display: inline-flex;
                                align-items: center;
                                gap: 6px;">
              <span>+</span> 提交反馈
            </router-link>
            <button @click="loadUserFeedbacks"
                    style="padding: 10px 20px;
                           background: #1890ff;
                           color: white;
                           border: none;
                           border-radius: 8px;
                           cursor: pointer;
                           font-size: 14px;
                           font-weight: 500;
                           transition: all 0.3s;
                           display: inline-flex;
                           align-items: center;
                           gap: 6px;">
              <span>🔄</span> 刷新
            </button>
          </div>
        </div>
      </div>

      <!-- 反馈列表 -->
      <div style="display: grid; gap: 16px;">
        <div v-for="feedback in feedbacks" :key="feedback.id"
             style="background: white;
                    border-radius: 12px;
                    padding: 24px;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.06);
                    transition: all 0.3s;
                    border-left: 4px solid;
                    border-left-color: feedback.type === 'COMPLAINT' ? '#ff4d4f' :
                                     feedback.type === 'PRAISE' ? '#52c41a' : '#1890ff';"
             @mouseenter="$event.currentTarget.style.transform = 'translateY(-2px)'; $event.currentTarget.style.boxShadow = '0 4px 16px rgba(0,0,0,0.1)'"
             @mouseleave="$event.currentTarget.style.transform = 'none'; $event.currentTarget.style.boxShadow = '0 2px 8px rgba(0,0,0,0.06)'">

          <!-- 反馈头部 -->
          <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 16px;">
            <div style="display: flex; align-items: center; gap: 12px;">
              <!-- 类型标签 -->
              <span :style="{
                padding: '6px 12px',
                borderRadius: '6px',
                fontSize: '13px',
                fontWeight: '600',
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

              <!-- 状态标签 -->
              <span :style="{
                padding: '6px 12px',
                borderRadius: '12px',
                fontSize: '12px',
                fontWeight: '600',
                backgroundColor: feedback.status === 'PENDING' ? '#fff7e6' : '#f6ffed',
                color: feedback.status === 'PENDING' ? '#fa8c16' : '#52c41a',
                border: '1px solid',
                borderColor: feedback.status === 'PENDING' ? '#ffd591' : '#b7eb8f',
                display: 'inline-flex',
                alignItems: 'center',
                gap: '4px'
              }">
                <span v-if="feedback.status === 'PENDING'">⏳</span>
                <span v-else>✅</span>
                {{ feedback.status === 'PENDING' ? '待处理' : '已处理' }}
              </span>
            </div>

            <span style="font-size: 12px; color: #8c8c8c; font-weight: 500;">
              #{{ feedback.id }}
            </span>
          </div>

          <!-- 反馈内容 -->
          <div style="margin-bottom: 16px;">
            <div style="color: #434343;
                        font-size: 14px;
                        line-height: 1.6;
                        background: #fafafa;
                        padding: 16px;
                        border-radius: 8px;
                        max-height: 120px;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        display: -webkit-box;
                        -webkit-line-clamp: 3;
                        -webkit-box-orient: vertical;">
              {{ feedback.content }}
            </div>
          </div>

          <!-- 底部信息 -->
          <div style="display: flex; justify-content: space-between; align-items: center;
                     padding-top: 16px; border-top: 1px solid #f0f0f0; font-size: 12px;">
            <div style="color: #8c8c8c;">
              <div>提交时间: {{ formatTime(feedback.createdAt) }}</div>
              <div v-if="feedback.updatedAt !== feedback.createdAt" style="margin-top: 4px;">
                更新时间: {{ formatTime(feedback.updatedAt) }}
              </div>
            </div>
            <div>
              <button @click="viewFeedbackDetail(feedback.id)"
                      style="padding: 6px 12px;
                            background: #f0f0f0;
                            color: #595959;
                            border: none;
                            border-radius: 6px;
                            cursor: pointer;
                            font-size: 12px;
                            font-weight: 500;
                            transition: all 0.3s;
                            display: inline-flex;
                            align-items: center;
                            gap: 4px;">
                <span>👁️</span> 查看详情
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { feedbackAPI, userUtils } from '@/api/feedback'

export default {
  name: 'MyFeedbackPage',
  data() {
    return {
      feedbacks: [],
      loading: true,
      error: '',
      currentUserId: null,
      currentUsername: ''
    }
  },
  computed: {
    isLoggedIn() {
      return userUtils.isLoggedIn()
    },
    pendingCount() {
      return this.feedbacks.filter(f => f.status === 'PENDING').length
    },
    processedCount() {
      return this.feedbacks.filter(f => f.status === 'PROCESSED').length
    }
  },
  async mounted() {
    console.log('MyFeedbackPage 组件已挂载')
    await this.initializePage()
  },
  methods: {
    async initializePage() {
      this.loading = true
      this.error = ''

      try {
        // 1. 检查登录状态
        const user = userUtils.getCurrentUser()
        if (!user) {
          this.error = '请先登录'
          this.loading = false
          return
        }

        // 2. 获取用户信息
        this.currentUserId = user?.id || null
        this.currentUsername = user?.username || user?.userAccount || '用户'

        // 3. 加载反馈
        await this.loadUserFeedbacks()

      } catch (error) {
        console.error('页面初始化失败:', error)
        this.error = `初始化失败: ${error.message}`
      } finally {
        this.loading = false
      }
    },

    async loadUserFeedbacks() {
      try {
        console.log('开始加载用户反馈...')

        // 使用 /feedback/my 接口
        const response = await feedbackAPI.getMyFeedbacks()
        console.log('获取我的反馈响应:', response)

        if (Array.isArray(response)) {
          this.feedbacks = response
        } else {
          console.warn('响应不是数组:', response)
          this.feedbacks = []
          this.error = '数据格式错误'
        }

      } catch (error) {
        console.error('加载用户反馈失败:', error)
        this.error = error.message || '加载失败'

        // 如果是未登录错误，清除本地存储
        if (error.message.includes('登录') || error.message.includes('未授权') || error.response?.status === 401) {
          userUtils.clearUser()
          this.error = '登录已过期，请重新登录'
        }
      }
    },

    viewFeedbackDetail(id) {
      console.log('查看反馈详情:', id)
      // 跳转到详情页
      this.$router.push(`/user/feedback/${id}`)
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
      if (!time) return '未知时间'
      try {
        const date = new Date(time)
        if (isNaN(date.getTime())) {
          return '无效时间'
        }
        return date.toLocaleDateString('zh-CN') + ' ' +
            date.toLocaleTimeString('zh-CN', {
              hour: '2-digit',
              minute: '2-digit',
              hour12: false
            })
      } catch (e) {
        console.error('格式化时间失败:', e, time)
        return '时间格式错误'
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
a:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15) !important;
}

button[style*="background: #4CAF50"]:hover {
  background: #6fbf73 !important;
}

button[style*="background: #1890ff"]:hover {
  background: #40a9ff !important;
}

button[style*="background: #f0f0f0"]:hover {
  background: #e0e0e0 !important;
}
</style>