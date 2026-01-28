export interface FeedbackRequest {
    content: string
    type: string  // 'SUGGESTION' | 'COMPLAINT' | 'PRAISE'
    userId?: number
}

export interface FeedbackResponse {
    id: number
    userId: number
    userName: string
    content: string
    type: string
    status: string  // 'PENDING' | 'PROCESSED'
    createdAt: string
    updatedAt: string
}