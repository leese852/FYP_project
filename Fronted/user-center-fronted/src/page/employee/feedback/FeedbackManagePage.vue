<!-- src/page/employee/feedback/FeedbackManagePage.vue -->
<template>
  <div style="padding: 30px; max-width: 1200px; margin: 0 auto;">
    <h1 style="color: #333; border-bottom: 2px solid #2196F3; padding-bottom: 10px;">
      👨‍💼 反馈管理
    </h1>

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
        共 {{ filteredFeedbacks.length }} 条反馈
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
                  style="padding: 6px 12px; background: #28A745; color: white; border: none; border-radius: 4px; cursor: pointer;"
              >
                标记处理
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
</template>

<script>
export default {
  name: 'FeedbackManagePage',
  data() {
    return {
      filter: {
        status: '',
        type: ''
      },
      feedbacks: [
        {
          id: 1,
          userId: 1001,
          type: 'SUGGESTION',
          content: '建议增加更多素食选项，很多顾客有需求',
          status: 'PENDING',
          createdAt: '2024-01-15 14:30:00'
        },
        {
          id: 2,
          userId: 1002,
          type: 'COMPLAINT',
          content: '配送员态度不好，希望加强培训',
          status: 'PROCESSED',
          createdAt: '2024-01-14 10:15:00'
        },
        {
          id: 3,
          userId: 1003,
          type: 'PRAISE',
          content: '菜品味道很好，包装也很用心，会继续光顾',
          status: 'PENDING',
          createdAt: '2024-01-13 18:45:00'
        },
        {
          id: 4,
          userId: 1004,
          type: 'SUGGESTION',
          content: '希望可以开发预约取餐功能，节省等待时间',
          status: 'PROCESSED',
          createdAt: '2024-01-12 09:20:00'
        }
      ]
    }
  },
  computed: {
    filteredFeedbacks() {
      return this.feedbacks.filter(feedback => {
        const statusMatch = !this.filter.status || feedback.status === this.filter.status
        const typeMatch = !this.filter.type || feedback.type === this.filter.type
        return statusMatch && typeMatch
      })
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
      return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    },
    viewDetail(id) {
      this.$router.push(`/admin/feedback/${id}`)
    },
    markAsProcessed(id) {
      if (confirm('确定标记为已处理吗？')) {
        const index = this.feedbacks.findIndex(f => f.id === id)
        if (index !== -1) {
          this.feedbacks[index].status = 'PROCESSED'
          alert('标记成功')
        }
      }
    },
    deleteFeedback(id) {
      if (confirm('确定删除这条反馈吗？')) {
        this.feedbacks = this.feedbacks.filter(f => f.id !== id)
        alert('删除成功')
      }
    },
    resetFilter() {
      this.filter.status = ''
      this.filter.type = ''
    }
  }
}
</script>