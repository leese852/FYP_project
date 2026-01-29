<template>
  <div style="padding: 30px; max-width: 1200px; margin: 0 auto;">
    <h1 style="color: #333; border-bottom: 2px solid #2196F3; padding-bottom: 10px;">
      👨‍💼 反馈管理
    </h1>

    <!-- 加载状态 -->
    <div v-if="loading" style="text-align: center; padding: 40px;">
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" style="text-align: center; padding: 40px; color: #DC3545;">
      <p>加载失败: {{ error }}</p>
      <button @click="loadFeedbacks" style="margin-top: 10px; padding: 8px 16px;">
        重试
      </button>
    </div>

    <!-- 正常状态 -->
    <div v-else>
      <!-- 筛选和操作 -->
      <div style="display: flex; justify-content: space-between; margin: 20px 0; padding: 20px; background: #f8f9fa; border-radius: 8px;">
        <div style="display: flex; gap: 15px; align-items: center;">
          <div>
            <label style="margin-right: 8px; font-weight: 500;">状态:</label>
            <select v-model="filter.status" style="padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px;">
              <option value="">全部</option>
              <option value="PENDING">待处理</option>
              <option value="PROCESSED">已处理</option>
            </select>
          </div>

          <div>
            <label style="margin-right: 8px; font-weight: 500;">类型:</label>
            <select v-model="filter.type" style="padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px;">
              <option value="">全部</option>
              <option value="SUGGESTION">建议</option>
              <option value="COMPLAINT">投诉</option>
              <option value="PRAISE">表扬</option>
            </select>
          </div>

          <button @click="resetFilter" style="padding: 8px 16px; background: #6c757d; color: white; border: none; border-radius: 4px; cursor: pointer;">
            重置
          </button>
        </div>

        <div>
          <button @click="loadFeedbacks" style="padding: 8px 16px; background: #007BFF; color: white; border: none; border-radius: 4px; cursor: pointer;">
            刷新
          </button>
        </div>
      </div>

      <!-- 反馈列表 -->
      <div style="overflow-x: auto;">
        <table style="width: 100%; border-collapse: collapse; background: white;">
          <thead>
          <tr style="background: #f1f1f1;">
            <th style="padding: 12px; text-align: left; border: 1px solid #dee2e6;">ID</th>
            <th style="padding: 12px; text-align: left; border: 1px solid #dee2e6;">用户ID</th>
            <th style="padding: 12px; text-align: left; border: 1px solid #dee2e6;">类型</th>
            <th style="padding: 12px; text-align: left; border: 1px solid #dee2e6;">内容</th>
            <th style="padding: 12px; text-align: left; border: 1px solid #dee2e6;">状态</th>
            <th style="padding: 12px; text-align: left; border: 1px solid #dee2e6;">提交时间</th>
            <th style="padding: 12px; text-align: left; border: 1px solid #dee2e6;">操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="feedback in filteredFeedbacks" :key="feedback.id"
              :style="{ backgroundColor: feedback.status === 'PENDING' ? '#FFF3CD' : 'transparent' }">
            <td style="padding: 12px; border: 1px solid #dee2e6;">{{ feedback.id }}</td>
            <td style="padding: 12px; border: 1px solid #dee2e6;">{{ feedback.userId }}</td>
            <td style="padding: 12px; border: 1px solid #dee2e6;">
                <span :style="{
                  padding: '4px 8px',
                  borderRadius: '4px',
                  fontSize: '12px',
                  color: feedback.type === 'COMPLAINT' ? '#DC3545' :
                         feedback.type === 'PRAISE' ? '#28A745' : '#007BFF',
                  backgroundColor: feedback.type === 'COMPLAINT' ? '#F8D7DA' :
                                  feedback.type === 'PRAISE' ? '#D4EDDA' : '#D1ECF1'
                }">
                  {{ getTypeText(feedback.type) }}
                </span>
            </td>
            <td style="padding: 12px; border: 1px solid #dee2e6; max-width: 300px;">
              <div style="max-height: 60px; overflow: hidden; text-overflow: ellipsis;">
                {{ feedback.content }}
              </div>
            </td>
            <td style="padding: 12px; border: 1px solid #dee2e6;">
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
            </td>
            <td style="padding: 12px; border: 1px solid #dee2e6;">{{ formatTime(feedback.createdAt) }}</td>
            <td style="padding: 12px; border: 1px solid #dee2e6;">
              <div style="display: flex; gap: 8px;">
                <button
                    @click="viewDetail(feedback.id)"
                    style="padding: 6px 12px; background: #007BFF; color: white; border: none; border-radius: 4px; cursor: pointer;"
                >
                  查看
                </button>
                <button
                    v-if="feedback.status === 'PENDING'"
                    @click="markAsProcessed(feedback.id)"
                    :disabled="processingId === feedback.id"
                    :style="{
                      padding: '6px 12px',
                      background: processingId === feedback.id ? '#cccccc' : '#28A745',
                      color: 'white',
                      border: 'none',
                      borderRadius: '4px',
                      cursor: processingId === feedback.id ? 'not-allowed' : 'pointer'
                    }"
                >
                  {{ processingId === feedback.id ? '处理中...' : '标记处理' }}
                </button>
                <button
                    @click="deleteFeedback(feedback.id)"
                    style="padding: 6px 12px; background: #DC3545; color: white; border: none; border-radius: 4px; cursor: pointer;"
                >
                  删除
                </button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- 空状态 -->
      <div v-if="filteredFeedbacks.length === 0" style="text-align: center; padding: 50px; color: #666;">
        暂无反馈数据
      </div>
    </div>
  </div>
</template>

<script>
import { feedbackAPI } from '@/api/feedback'

export default {
  name: 'FeedbackManagePage',
  data() {
    return {
      feedbacks: [],
      loading: true,
      error: '',
      filter: {
        status: '',
        type: ''
      },
      processingId: null
    }
  },
  computed: {
    filteredFeedbacks() {
      if (!Array.isArray(this.feedbacks)) {
        console.warn('feedbacks不是数组:', this.feedbacks)
        return []
      }
      return this.feedbacks.filter(feedback => {
        const statusMatch = !this.filter.status || feedback.status === this.filter.status
        const typeMatch = !this.filter.type || feedback.type === this.filter.type
        return statusMatch && typeMatch
      })
    }
  },
  mounted() {
    this.loadFeedbacks()
  },
  methods: {
    async loadFeedbacks() {
      this.loading = true
      this.error = ''

      try {
        console.log('正在加载反馈列表...')
        const response = await feedbackAPI.getAllFeedbacks()
        console.log('API响应:', response)

        // 确保是数组
        if (Array.isArray(response)) {
          this.feedbacks = response
        } else {
          console.warn('响应不是数组，转换为数组:', response)
          this.feedbacks = response ? [response] : []
        }

        console.log('加载的反馈数据:', this.feedbacks)
      } catch (error) {
        console.error('加载失败:', error)
        this.error = error.message || '加载失败'

        // 如果是权限错误，提示更友好的信息
        if (error.message.includes('权限不足')) {
          this.error = '权限不足，需要管理员账号才能访问此页面'
        }
      } finally {
        this.loading = false
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
      return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    },

    viewDetail(id) {
      this.$router.push(`/admin/feedback/${id}`)
    },

    async markAsProcessed(id) {
      if (!confirm('确定标记为已处理吗？')) return

      this.processingId = id
      try {
        await feedbackAPI.markAsProcessed(id)
        await this.loadFeedbacks() // 重新加载
      } catch (error) {
        console.error('处理失败:', error)
        alert(error.message || '处理失败')
      } finally {
        this.processingId = null
      }
    },

    async deleteFeedback(id) {
      if (!confirm('确定删除这条反馈吗？')) return

      try {
        await feedbackAPI.deleteFeedback(id)
        await this.loadFeedbacks() // 重新加载
      } catch (error) {
        console.error('删除失败:', error)
        alert(error.message || '删除失败')
      }
    },

    resetFilter() {
      this.filter.status = ''
      this.filter.type = ''
    }
  }
}
</script>