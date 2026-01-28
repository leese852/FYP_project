// src/api/order.ts
import axios from "axios";
import { Order } from "@/types/order";

/**
 * 獲取當前用戶的訂單列表
 */
export async function getUserOrders(): Promise<Order[]> {
    const res = await axios.get("/api/orders/user", { withCredentials: true });
    return res.data?.data ?? [];
}

/**
 * 獲取訂單詳情
 */
// src/api/order.ts
export async function getOrderDetails(id: number): Promise<Order | null> {
    const res = await axios.get(`/api/orders/${id}`, { withCredentials: true });
    return res.data?.data ?? null;
}


/**
 * 更新訂單狀態
 */
export async function updateOrderStatus(orderId: string, status: number): Promise<boolean> {
    const res = await axios.put(
        `/api/orders/${orderId}/status?status=${status}`,
        null,
        { withCredentials: true }
    );
    return res.data?.code === 0; // 返回 true 表示更新成功
}
