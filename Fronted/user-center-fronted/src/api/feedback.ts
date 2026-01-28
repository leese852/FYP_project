import request from '@/utils/request'
import type { FeedbackRequest, FeedbackResponse } from '@/types/feedback'

export function submitFeedback(data: FeedbackRequest) {
    return request({
        url: '/feedback/create',
        method: 'post',
        data
    })
}

export function getFeedbackList() {
    return request<FeedbackResponse[]>({
        url: '/feedback/list',
        method: 'get'
    })
}

export function deleteFeedback(id: number) {
    return request({
        url: `/feedback/delete/${id}`,
        method: 'delete'
    })
}

export function updateFeedback(id: number, data: FeedbackRequest) {
    return request({
        url: `/feedback/update/${id}`,
        method: 'put',
        data
    })
}

export function getFeedbackById(id: number) {
    return request<FeedbackResponse>({
        url: `/feedback/${id}`,
        method: 'get'
    })
}