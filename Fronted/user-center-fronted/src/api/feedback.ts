import myAxios from "@/request";
import type { FeedbackRequest, FeedbackResponse } from '@/types/feedback'

export const submitFeedback = async(data: FeedbackRequest) => {
    return await myAxios.request({
        url: '/feedback/create',
        method: 'post',
        data
    })
}

export const getFeedbackList= async()=> {
    return await myAxios.request<FeedbackResponse[]>({
        url: '/feedback/list',
        method: 'get'
    })
}

export const deleteFeedback=async(id: number)=> {
    return await myAxios.request({
        url: `/feedback/delete/${id}`,
        method: 'delete'
    })
}

export const updateFeedback=async(id: number, data: FeedbackRequest)=> {
    return await myAxios.request({
        url: `/feedback/update/${id}`,
        method: 'put',
        data
    })
}

export const getFeedbackById=async(id: number)=> {
    return await myAxios.request<FeedbackResponse>({
        url: `/feedback/${id}`,
        method: 'get'
    })
}