<template>
  <div style="padding: 30px; max-width: 1400px; margin: 0 auto; background: #f5f7fa; min-height: 100vh;">
    <!-- 页面标题 -->
    <div style="background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
                padding: 20px 30px;
                border-radius: 12px;
                margin-bottom: 30px;
                box-shadow: 0 4px 12px rgba(24, 144, 255, 0.2);">
      <h1 style="color: white; margin: 0; font-size: 24px; display: flex; align-items: center;">
        <span style="background: white; color: #1890ff; width: 36px; height: 36px; border-radius: 8px;
                    display: inline-flex; align-items: center; justify-content: center; margin-right: 12px;">
          👨‍💼
        </span>
        反馈管理系统
      </h1>
      <p style="color: rgba(255,255,255,0.85); margin: 8px 0 0 48px; font-size: 14px;">
        管理用户反馈信息 | 共 {{ filteredFeedbacks.length }} 条反馈
      </p>
    </div>

    <!-- 筛选和操作区域 -->
    <div style="background: white;
                padding: 24px;
                border-radius: 12px;
                margin-bottom: 24px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.06);">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <div style="display: flex; gap: 20px; align-items: center;">
          <!-- 状态筛选 -->
          <div>
            <label style="display: block; margin-bottom: 8px; font-weight: 500; color: #434343; font-size: 14px;">
              📊 状态筛选
            </label>
            <select v-model="filter.status"
                    style="padding: 10px 16px;
                          border: 1px solid #d9d9d9;
                          border-radius: 8px;
                          width: 140px;
                          background: white;
                          color: #262626;
                          font-size: 14px;
                          transition: all 0.3s;
                          cursor: pointer;">
              <option value="">全部状态</option>
              <option value="PENDING" style="color: #faad14;">待处理</option>
              <option value="PROCESSED" style="color: #52c41a;">已处理</option>
            </select>
          </div>

          <!-- 类型筛选 -->
          <div>
            <label style="display: block; margin-bottom: 8px; font-weight: 500; color: #434343; font-size: 14px;">
              🏷️ 类型筛选
            </label>
            <select v-model="filter.type"
                    style="padding: 10px 16px;
                          border: 1px solid #d9d9d9;
                          border-radius: 8px;
                          width: 140px;
                          background: white;
                          color: #262626;
                          font-size: 14px;
                          transition: all 0.3s;
                          cursor: pointer;">
              <option value="">全部类型</option>
              <option value="SUGGESTION" style="color: #1890ff;">建议</option>
              <option value="COMPLAINT" style="color: #ff4d4f;">投诉</option>
              <option value="PRAISE" style="color: #52c41a;">表扬</option>
            </select>
          </div>

          <!-- 重置按钮 -->
          <div style="margin-top: 28px;">
            <button @click="resetFilter"
                    style="padding: 10px 20px;
                          background: #f0f0f0;
                          color: #595959;
                          border: 1px solid #d9d9d9;
                          border-radius: 8px;
                          cursor: pointer;
                          font-size: 14px;
                          transition: all 0.3s;
                          display: flex;
                          align-items: center;
                          gap: 6px;">
              <span>🔄</span> 重置筛选
            </button>
          </div>
        </div>

        <!-- 刷新按钮 -->
        <div style="margin-top: 28px;">
          <button @click="loadFeedbacks"
                  style="padding: 10px 24px;
                        background: #1890ff;
                        color: white;
                        border: none;
                        border-radius: 8px;
                        cursor: pointer;
                        font-size: 14px;
                        font-weight: 500;
                        transition: all 0.3s;
                        display: flex;
                        align-items: center;
                        gap: 6px;
                        box-shadow: 0 2px 6px rgba(24, 144, 255, 0.3);">
            <span>🔄</span> 刷新数据
          </button>
        </div>
      </div>
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
                    border-top: 4px solid #1890ff;
                    border-radius: 50%;
                    animation: spin 1s linear infinite;
                    margin: 0 auto 20px;"></div>
        <p style="color: #595959; font-size: 16px; margin: 0;">加载反馈数据中...</p>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error"
         style="background: #fff2f0;
                border: 1px solid #ffccc7;
                border-radius: 12px;
                padding: 40px;
                text-align: center;
                margin: 0 0 24px 0;">
      <div style="font-size: 48px; margin-bottom: 16px;">❌</div>
      <p style="color: #ff4d4f; font-size: 18px; font-weight: 500; margin-bottom: 12px;">
        加载失败
      </p>
      <p style="color: #595959; margin-bottom: 24px;">{{ error }}</p>
      <button @click="loadFeedbacks"
              style="padding: 10px 24px;
                    background: #ff4d4f;
                    color: white;
                    border: none;
                    border-radius: 8px;
                    cursor: pointer;
                    font-size: 14px;
                    transition: all 0.3s;">
        点击重试
      </button>
    </div>

    <!-- 正常状态：反馈列表 -->
    <div v-else>
      <div style="background: white;
                  border-radius: 12px;
                  overflow: hidden;
                  box-shadow: 0 2px 12px rgba(0,0,0,0.08);">
        <!-- 表格头部 -->
        <div style="background: #fafafa;
                    padding: 16px 24px;
                    border-bottom: 1px solid #f0f0f0;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;">
          <div>
            <span style="font-weight: 600; color: #262626; font-size: 16px;">
              📋 反馈列表
            </span>
            <span style="margin-left: 12px; color: #8c8c8c; font-size: 14px;">
              共 {{ filteredFeedbacks.length }} 条记录
            </span>
          </div>
          <div style="color: #8c8c8c; font-size: 14px;">
            最后更新: {{ formatTime(new Date()) }}
          </div>
        </div>

        <!-- 表格 -->
        <div style="overflow-x: auto;">
          <table style="width: 100%; border-collapse: collapse;">
            <thead>
            <tr style="background: #fafafa;">
              <th style="padding: 16px; text-align: left; border-bottom: 1px solid #f0f0f0; color: #595959; font-weight: 600; font-size: 14px; min-width: 80px;">ID</th>
              <th style="padding: 16px; text-align: left; border-bottom: 1px solid #f0f0f0; color: #595959; font-weight: 600; font-size: 14px; min-width: 100px;">用户ID</th>
              <th style="padding: 16px; text-align: left; border-bottom: 1px solid #f0f0f0; color: #595959; font-weight: 600; font-size: 14px; min-width: 100px;">类型</th>
              <th style="padding: 16px; text-align: left; border-bottom: 1px solid #f0f0f0; color: #595959; font-weight: 600; font-size: 14px; min-width: 300px;">内容</th>
              <th style="padding: 16px; text-align: left; border-bottom: 1px solid #f0f0f0; color: #595959; font-weight: 600; font-size: 14px; min-width: 120px;">状态</th>
              <th style="padding: 16px; text-align: left; border-bottom: 1px solid #f0f0f0; color: #595959; font-weight: 600; font-size: 14px; min-width: 180px;">提交时间</th>
              <th style="padding: 16px; text-align: left; border-bottom: 1px solid #f0f0f0; color: #595959; font-weight: 600; font-size: 14px; min-width: 200px;">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="feedback in filteredFeedbacks" :key="feedback.id"
                :style="{
                    backgroundColor: feedback.status === 'PENDING' ? '#fff7e6' : 'white',
                    borderBottom: '1px solid #f0f0f0',
                    transition: 'background-color 0.3s'
                  }"
                @mouseenter="$event.currentTarget.style.backgroundColor = feedback.status === 'PENDING' ? '#ffe7ba' : '#fafafa'"
                @mouseleave="$event.currentTarget.style.backgroundColor = feedback.status === 'PENDING' ? '#fff7e6' : 'white'">
              <!-- ID列 -->
              <td style="padding: 16px; color: #8c8c8c; font-size: 14px; font-weight: 500;">
                #{{ feedback.id }}
              </td>

              <!-- 用户ID列 -->
              <td style="padding: 16px; color: #262626; font-size: 14px;">
                  <span style="background: #f0f0f0; padding: 4px 10px; border-radius: 12px; font-weight: 500;">
                    {{ feedback.userId }}
                  </span>
              </td>

              <!-- 类型列 -->
              <td style="padding: 16px;">
                  <span :style="{
                    padding: '6px 12px',
                    borderRadius: '6px',
                    fontSize: '13px',
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
              </td>

              <!-- 内容列 -->
              <td style="padding: 16px; color: #434343; font-size: 14px; line-height: 1.5; max-width: 400px;">
                <div style="max-height: 60px; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;">
                  {{ feedback.content }}
                </div>
              </td>

              <!-- 状态列 -->
              <td style="padding: 16px;">
                  <span :style="{
                    padding: '6px 14px',
                    borderRadius: '12px',
                    fontSize: '13px',
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
              </td>

              <!-- 时间列 -->
              <td style="padding: 16px; color: #8c8c8c; font-size: 13px; font-family: monospace;">
                {{ formatTime(feedback.createdAt) }}
              </td>

              <!-- 操作列 -->
              <td style="padding: 16px;">
                <div style="display: flex; gap: 8px; flex-wrap: wrap;">
                  <!-- 查看按钮 -->
                  <button @click="viewDetail(feedback.id)"
                          style="padding: 6px 12px;
                                  background: #1890ff;
                                  color: white;
                                  border: none;
                                  border-radius: 6px;
                                  cursor: pointer;
                                  font-size: 13px;
                                  font-weight: 500;
                                  transition: all 0.3s;
                                  display: flex;
                                  align-items: center;
                                  gap: 4px;">
                    <span>👁️</span> 查看
                  </button>

                  <!-- 标记处理按钮 -->
                  <button v-if="feedback.status === 'PENDING'"
                          @click="markAsProcessed(feedback.id)"
                          :disabled="processingId === feedback.id"
                          :style="{
                              padding: '6px 12px',
                              background: processingId === feedback.id ? '#d9d9d9' : '#52c41a',
                              color: 'white',
                              border: 'none',
                              borderRadius: '6px',
                              cursor: processingId === feedback.id ? 'not-allowed' : 'pointer',
                              fontSize: '13px',
                              fontWeight: '500',
                              transition: 'all 0.3s',
                              display: 'flex',
                              alignItems: 'center',
                              gap: '4px'
                            }">
                    <span v-if="processingId === feedback.id">⏳</span>
                    <span v-else>✅</span>
                    {{ processingId === feedback.id ? '处理中' : '标记处理' }}
                  </button>

                  <!-- 删除按钮 -->
                  <button @click="deleteFeedback(feedback.id)"
                          style="padding: 6px 12px;
                                  background: #ff4d4f;
                                  color: white;
                                  border: none;
                                  border-radius: 6px;
                                  cursor: pointer;
                                  font-size: 13px;
                                  font-weight: 500;
                                  transition: all 0.3s;
                                  display: flex;
                                  align-items: center;
                                  gap: 4px;">
                    <span>🗑️</span> 删除
                  </button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
        </div>

        <!-- 空状态 -->
        <div v-if="filteredFeedbacks.length === 0"
             style="padding: 80px 40px; text-align: center; color: #bfbfbf;">
          <div style="font-size: 64px; margin-bottom: 16px;">📭</div>
          <p style="font-size: 18px; color: #8c8c8c; margin-bottom: 8px;">
            暂无反馈数据
          </p>
          <p style="color: #bfbfbf; font-size: 14px;">
            还没有用户提交反馈，或筛选条件不匹配
          </p>
        </div>
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
      return date.toLocaleDateString('zh-CN') + ' ' +
          date.toLocaleTimeString('zh-CN', {
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
          })
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

<style>
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 表格行悬停效果 */
tbody tr {
  transition: background-color 0.2s ease;
}

/* 按钮悬停效果 */
button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

/* 筛选框悬停效果 */
select:hover {
  border-color: #40a9ff;
}

/* 重置按钮悬停效果 */
button[style*="background: #f0f0f0"]:hover {
  background: #e0e0e0 !important;
  border-color: #bfbfbf !important;
}

/* 操作按钮悬停效果 */
button[style*="background: #1890ff"]:hover:not(:disabled) {
  background: #40a9ff !important;
}

button[style*="background: #52c41a"]:hover:not(:disabled) {
  background: #73d13d !important;
}

button[style*="background: #ff4d4f"]:hover:not(:disabled) {
  background: #ff7875 !important;
}
</style>