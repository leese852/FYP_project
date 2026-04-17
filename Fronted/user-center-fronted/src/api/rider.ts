// src/api/rider.ts
import axios from "axios";

export interface RiderLocationResponse {
    lat: number;
    lng: number;
    updateTime: string;
}

/**
 * 获取骑手实时位置
 * @param orderId 订单ID
 * @returns 骑手位置信息
 */
export async function getRiderLocation(orderId: number) {
    try {
        const res = await axios.get(`/api/rider/location/get?orderId=${orderId}`, {
            withCredentials: true
        });
        return res;
    } catch (error) {
        console.error("获取骑手位置失败:", error);
        throw error;
    }
}