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
export async function getOrderStats(): Promise<{
    total: number;
    byStatus: Record<number, number>;
}> {
    const orders = await getAllOrders();
    const byStatus: Record<number, number> = {};

    orders.forEach(order => {
        byStatus[order.status] = (byStatus[order.status] || 0) + 1;
    });

    return {
        total: orders.length,
        byStatus
    };
}
/**
 * 獲取訂單詳情
 */
// src/api/order.ts
export async function getOrderDetails(id: number): Promise<Order | null> {
    const res = await axios.get(`/api/orders/${id}`, { withCredentials: true });
    return res.data?.data ?? null;
}

export async function getAllOrders(): Promise<Order[]> {
    try {
        // 添加时间戳防止缓存
        const timestamp = new Date().getTime()
        const res = await axios.get(`/api/orders/all?t=${timestamp}`, {
            withCredentials: true
        });
        console.log('📥 获取所有订单API响应:', res.data?.data?.length || 0, '条记录')
        return res.data?.data ?? [];
    } catch (error) {
        console.error('❌ 获取订单失败:', error)
        return []
    }
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
