<template>
  <div style="padding: 50px; max-width: 800px; margin: 0 auto;">
    <h1 style="color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px;">
      📋 我的反馈
    </h1>

    <!-- 测试按钮 -->
    <div v-if="!loading" style="margin-bottom: 20px; padding: 15px; background: #f5f5f5; border-radius: 8px;">
      <button @click="runTests" style="padding: 8px 16px; margin-right: 10px; background: #2196F3; color: white; border: none; border-radius: 4px;">
        运行API测试
      </button>
      <button @click="refreshPage" style="padding: 8px 16px; margin-right: 10px; background: #4CAF50; color: white; border: none; border-radius: 4px;">
        刷新页面
      </button>
      <button @click="checkLoginStatus" style="padding: 8px 16px; background: #FF9800; color: white; border: none; border-radius: 4px;">
        检查登录状态
      </button>
    </div>

    <!-- 状态信息 -->
    <div v-if="statusMessage" style="margin-bottom: 20px; padding: 10px; background: #e3f2fd; border-radius: 4px;">
      {{ statusMessage }}
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" style="text-align: center; padding: 40px;">
      <div style="display: inline-block; padding: 20px; background: #f5f5f5; border-radius: 8px;">
        <p style="margin-bottom: 10px;">加载中...</p>
        <div style="width: 100%; height: 4px; background: #e0e0e0; border-radius: 2px;">
          <div style="width: 60%; height: 100%; background: #4CAF50; border-radius: 2px; animation: loading 1.5s infinite;"></div>
        </div>
      </div>
    </div>

    <!-- 未登录状态 -->
    <div v-else-if="!isLoggedIn && !error" style="text-align: center; padding: 40px;">
      <div style="display: inline-block; padding: 30px; background: #FFF3CD; border-radius: 8px; max-width: 400px;">
        <p style="color: #856404; font-size: 18px; margin-bottom: 15px;">
          🔐 请先登录
        </p>
        <p style="color: #666; margin-bottom: 20px;">
          您需要登录后才能查看反馈记录
        </p>
        <div style="display: flex; gap: 10px; justify-content: center;">
          <router-link to="/login" style="padding: 10px 20px; background: #4CAF50; color: white; text-decoration: none; border-radius: 4px;">
            去登录 →
          </router-link>
          <button @click="checkLoginStatus" style="padding: 10px 20px; background: #2196F3; color: white; border: none; border-radius: 4px;">
            重新检查
          </button>
        </div>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" style="text-align: center; padding: 40px;">
      <div style="display: inline-block; padding: 30px; background: #F8D7DA; border-radius: 8px; max-width: 500px;">
        <p style="color: #721C24; font-size: 18px; margin-bottom: 15px;">
          ⚠️ 加载失败
        </p>
        <p style="color: #721C24; margin-bottom: 20px;">{{ error }}</p>
        <div style="display: flex; gap: 10px; justify-content: center;">
          <button @click="loadFeedbacks" style="padding: 10px 20px; background: #DC3545; color: white; border: none; border-radius: 4px;">
            重试
          </button>
          <button @click="checkLoginStatus" style="padding: 10px 20px; background: #6c757d; color: white; border: none; border-radius: 4px;">
            检查登录状态
          </button>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else-if="feedbacks.length === 0" style="text-align: center; padding: 40px;">
      <div style="display: inline-block; padding: 30px; background: #f8f9fa; border-radius: 8px; max-width: 400px;">
        <p style="color: #666; font-size: 18px; margin-bottom: 15px;">
          📭 暂无反馈记录
        </p>
        <p style="color: #666; margin-bottom: 20px;">
          您还没有提交过任何反馈
        </p>
        <router-link to="/user/feedback" style="padding: 10px 20px; background: #4CAF50; color: white; text-decoration: none; border-radius: 4px;">
          去提交反馈 →
        </router-link>
      </div>
    </div>

    <!-- 有数据 -->
    <div v-else>
      <!-- 用户信息 -->
      <div style="margin-bottom: 25px; padding: 15px; background: #e8f5e8; border-radius: 8px;">
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <div>
            <p style="margin: 0 0 5px 0; color: #2E7D32;">
              <strong>👤 用户信息</strong>
            </p>
            <p style="margin: 0; color: #555;">
              用户ID: <strong>{{ currentUserId }}</strong> |
              用户名: <strong>{{ currentUsername }}</strong> |
              共 <strong>{{ feedbacks.length }}</strong> 条反馈
            </p>
          </div>
          <button @click="loadFeedbacks" style="padding: 6px 12px; background: #4CAF50; color: white; border: none; border-radius: 4px;">
            刷新列表
          </button>
        </div>
      </div>

      <!-- 反馈列表 -->
      <div v-for="feedback in feedbacks" :key="feedback.id"
           style="border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px; margin-bottom: 20px; background: white; transition: all 0.3s;">

        <!-- 反馈头部 -->
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 15px;">
          <div>
            <span style="font-weight: bold; font-size: 16px; color: #2196F3; margin-right: 10px;">
              {{ getTypeText(feedback.type) }}
            </span>
            <span :style="{
              padding: '4px 12px',
              borderRadius: '12px',
              fontSize: '12px',
              fontWeight: 'bold',
              backgroundColor: feedback.status === 'PENDING' ? '#FFF3CD' : '#D4EDDA',
              color: feedback.status === 'PENDING' ? '#856404' : '#155724'
            }">
              {{ feedback.status === 'PENDING' ? '待处理' : '已处理' }}
            </span>
          </div>
          <span style="font-size: 12px; color: #888;">
            #{{ feedback.id }}
          </span>
        </div>

        <!-- 反馈内容 -->
        <p style="color: #333; line-height: 1.6; margin: 10px 0; padding: 10px; background: #f9f9f9; border-radius: 4px;">
          {{ feedback.content }}
        </p>

        <!-- 反馈信息 -->
        <div style="font-size: 12px; color: #666; margin: 10px 0;">
          <div style="display: flex; gap: 15px; margin-bottom: 5px;">
            <span>用户ID: {{ feedback.userId }}</span>
            <span>类型: {{ feedback.type }}</span>
          </div>
        </div>

        <!-- 时间信息 -->
        <div style="display: flex; justify-content: space-between; margin-top: 15px; font-size: 12px; color: #888; border-top: 1px solid #eee; padding-top: 10px;">
          <div>
            <div>提交时间: {{ formatTime(feedback.createdAt) }}</div>
            <div v-if="feedback.updatedAt !== feedback.createdAt" style="margin-top: 5px;">
              更新时间: {{ formatTime(feedback.updatedAt) }}
            </div>
          </div>
          <div>
            <button @click="viewFeedbackDetail(feedback.id)" style="padding: 4px 8px; background: #2196F3; color: white; border: none; border-radius: 3px; font-size: 11px;">
              查看详情
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { feedbackAPI, userUtils, testAPI } from '@/api/feedback'

export default {
  name: 'MyFeedbackPage',
  data() {
    return {
      feedbacks: [],
      loading: true,
      error: '',
      statusMessage: '',
      currentUserId: null,
      currentUsername: '',
      testResults: null
    }
  },
  computed: {
    isLoggedIn() {
      return userUtils.isLoggedIn()
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
      this.statusMessage = '正在初始化...'

      try {
        // 1. 检查登录状态
        this.statusMessage = '检查登录状态...'
        const loginStatus = await userUtils.checkLoginStatus()
        console.log('登录状态检查结果:', loginStatus)

        if (!loginStatus.loggedIn) {
          this.error = `请先登录 (${loginStatus.reason})`
          this.loading = false
          this.statusMessage = ''
          return
        }

        // 2. 获取用户信息
        const user = userUtils.getCurrentUser()
        this.currentUserId = user?.id || null
        this.currentUsername = user?.username || user?.userAccount || '未知用户'
        this.statusMessage = `欢迎 ${this.currentUsername}，正在加载反馈...`

        // 3. 加载反馈
        await this.loadUserFeedbacks()

      } catch (error) {
        console.error('页面初始化失败:', error)
        this.error = `初始化失败: ${error.message}`
      } finally {
        this.loading = false
        this.statusMessage = ''
      }
    },

    async loadUserFeedbacks() {
      try {
        console.log('开始加载用户反馈...')
        this.statusMessage = '正在加载反馈数据...'

        // 使用 /feedback/my 接口
        const response = await feedbackAPI.getMyFeedbacks()
        console.log('获取我的反馈响应:', response)

        if (Array.isArray(response)) {
          this.feedbacks = response
          this.statusMessage = `成功加载 ${response.length} 条反馈`

          // 2秒后清除状态消息
          setTimeout(() => {
            this.statusMessage = ''
          }, 2000)
        } else {
          console.warn('响应不是数组:', response)
          this.feedbacks = []
          this.error = '数据格式错误'
        }

      } catch (error) {
        console.error('加载用户反馈失败:', error)
        this.error = error.message || '加载失败'

        // 如果是未登录错误，清除本地存储
        if (error.message.includes('登录') || error.message.includes('未授权')) {
          userUtils.clearUser()
          this.error = '登录已过期，请重新登录'
        }
      }
    },

    async checkLoginStatus() {
      this.statusMessage = '正在检查登录状态...'
      try {
        const status = await userUtils.checkLoginStatus()
        console.log('登录状态:', status)

        if (status.loggedIn) {
          this.statusMessage = '登录状态正常'
          // 重新加载数据
          await this.loadUserFeedbacks()
        } else {
          this.statusMessage = `未登录: ${status.reason}`
          this.error = `请先登录 (${status.reason})`
        }

        // 3秒后清除状态消息
        setTimeout(() => {
          this.statusMessage = ''
        }, 3000)

      } catch (error) {
        console.error('检查登录状态失败:', error)
        this.statusMessage = `检查失败: ${error.message}`
      }
    },

    async runTests() {
      this.statusMessage = '正在运行API测试...'
      try {
        const results = await testAPI.testAll()
        console.log('测试结果:', results)

        if (results.success) {
          this.statusMessage = 'API测试成功！'
          // 如果测试成功，重新加载数据
          if (results.currentUser) {
            userUtils.saveUser(results.currentUser)
            this.currentUserId = results.currentUser.id
            this.currentUsername = results.currentUser.username || results.currentUser.userAccount
          }
          if (results.myFeedbacks) {
            this.feedbacks = results.myFeedbacks
          }
        } else {
          this.statusMessage = `测试失败: ${results.reason || results.error}`
        }

        setTimeout(() => {
          this.statusMessage = ''
        }, 3000)

      } catch (error) {
        console.error('运行测试失败:', error)
        this.statusMessage = `测试失败: ${error.message}`
      }
    },

    viewFeedbackDetail(id) {
      console.log('查看反馈详情:', id)
      // 这里可以跳转到详情页，或者显示模态框
      alert(`查看反馈 #${id} 的详情\n开发中...`)
    },

    refreshPage() {
      console.log('刷新页面')
      this.loading = true
      this.error = ''
      this.statusMessage = '正在刷新...'

      // 重新初始化
      setTimeout(async () => {
        await this.initializePage()
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
      if (!time) return '未知时间'
      try {
        const date = new Date(time)
        if (isNaN(date.getTime())) {
          return '无效时间'
        }
        return date.toLocaleString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit'
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
@keyframes loading {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(300%); }
}
</style>