// src/api/feedback.ts
import axios from 'axios'

// 创建axios实例
const api = axios.create({
    baseURL: 'http://localhost:8080', // 确保vue.config.js有代理配置
    timeout: 10000,
    withCredentials: true // 关键：允许携带cookie/session
})

// 请求拦截器
api.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json'
    // 注意：不要添加Authorization header，使用session
    return config
}, error => {
    return Promise.reject(error)
})

// 响应拦截器 - 统一处理BaseResponse格式
api.interceptors.response.use(
    response => {
        console.log('API Response:', response.config.url, response.data)

        // 统一处理BaseResponse格式
        const data = response.data
        if (data && typeof data === 'object' && 'code' in data) {
            // 这是BaseResponse格式
            if (data.code === 0 || data.code === 200) {
                return data.data // 返回真正的数据
            } else {
                // 业务错误
                return Promise.reject(new Error(data.message || '请求失败'))
            }
        }

        // 直接返回数据（如果不是BaseResponse格式）
        return data
    },
    error => {
        console.error('API Error:', {
            url: error.config?.url,
            status: error.response?.status,
            data: error.response?.data,
            message: error.message
        })

        // 处理401未授权
        if (error.response?.status === 401) {
            localStorage.removeItem('user')
            window.location.href = '/login'
        }

        // 处理403权限不足
        if (error.response?.status === 403) {
            return Promise.reject(new Error('权限不足，请联系管理员'))
        }

        return Promise.reject(error)
    }
)

// 类型定义
export interface FeedbackRequest {
    content: string
    type: string  // 'SUGGESTION' | 'COMPLAINT' | 'PRAISE'
    userId?: number
}

export interface FeedbackResponse {
    id: number
    userId: number
    userName?: string
    content: string
    type: string
    status: string  // 'PENDING' | 'PROCESSED'
    createdAt: string
    updatedAt: string
}

export interface UserInfo {
    id: number
    username: string
    userAccount: string
    userRole?: number
    [key: string]: any
}

interface LoginStatus {
    loggedIn: boolean
    reason?: string
    user?: any
}

interface TestResult {
    success: boolean
    error?: string
    currentUser?: any
    myFeedbacks?: FeedbackResponse[]
}

// API函数 - 直接返回数据，不返回包装对象
export const feedbackAPI = {
    // 提交反馈
    async submitFeedback(data: FeedbackRequest) {
        const response = await api.post<FeedbackResponse>('/feedback/create', data)
        return response
    },

    // 获取所有反馈（员工用）
    async getAllFeedbacks() {
        const response = await api.get<FeedbackResponse[]>('/feedback/list')
        return Array.isArray(response) ? response : []
    },

    // 获取单个反馈
    async getFeedbackById(id: number) {
        const response = await api.get<FeedbackResponse>(`/feedback/${id}`)
        return response
    },

    // 获取用户的反馈
    async getUserFeedbacks(userId: number) {
        const response = await api.get<FeedbackResponse[]>(`/feedback/user/${userId}`)
        return Array.isArray(response) ? response : []
    },

    // 获取当前登录用户的反馈
    async getMyFeedbacks() {
        const response = await api.get<FeedbackResponse[]>('/feedback/my')
        return Array.isArray(response) ? response : []
    },

    // 更新反馈
    async updateFeedback(id: number, data: Partial<FeedbackRequest>) {
        const response = await api.put<FeedbackResponse>(`/feedback/update/${id}`, data)
        return response
    },

    // 删除反馈
    async deleteFeedback(id: number) {
        const response = await api.delete(`/feedback/delete/${id}`)
        return response
    },

    // 标记为已处理
    async markAsProcessed(id: number) {
        const response = await api.put<FeedbackResponse>(`/feedback/mark-processed/${id}`)
        return response
    }
}

// 用户相关工具函数
export const userUtils = {
    // 获取当前登录用户
    getCurrentUser(): UserInfo | null {
        const userStr = localStorage.getItem('user')
        if (!userStr) {
            console.log('localStorage中没有用户信息')
            return null
        }

        try {
            const user = JSON.parse(userStr)
            console.log('从localStorage获取用户:', user)
            return user
        } catch (e) {
            console.error('解析用户信息失败:', e)
            return null
        }
    },

    // 获取当前用户ID
    getCurrentUserId(): number | null {
        const user = this.getCurrentUser()
        return user?.id || null
    },

    // 检查是否已登录
    isLoggedIn(): boolean {
        return !!this.getCurrentUser()
    },

    // 测试当前用户API
    async testCurrentUserAPI() {
        try {
            const response = await api.get('/user/current')
            console.log('当前用户API返回:', response)
            return response
        } catch (error: any) {
            console.error('获取当前用户失败:', error)
            return null
        }
    },

    // 检查登录状态
    async checkLoginStatus(): Promise<LoginStatus> {
        try {
            const user = this.getCurrentUser()
            if (!user) {
                return { loggedIn: false, reason: 'localStorage中无用户信息' }
            }

            // 调用后端验证API
            const response = await api.get('/user/current')  // 需要确认这个API是否存在
            return { loggedIn: true, user: response }
        } catch (error: any) {
            return { loggedIn: false, reason: 'API验证失败: ' + error.message }
        }
    },

    // 保存用户信息
    saveUser(user: UserInfo) {
        localStorage.setItem('user', JSON.stringify(user))
    },

    // 清除用户信息
    clearUser() {
        localStorage.removeItem('user')
    }
}

// 测试API
export const testAPI = {
    async testAll(): Promise<TestResult> {
        try {
            // 测试当前用户
            const currentUser = await api.get('/user/current')

            // 测试反馈API
            const myFeedbacks = await feedbackAPI.getMyFeedbacks()

            return {
                success: true,
                currentUser,
                myFeedbacks
            }
        } catch (error: any) {
            return {
                success: false,
                error: error.message
            }
        }
    }
}

export default api