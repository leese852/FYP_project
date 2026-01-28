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
 * 從當前用戶購物車生成訂單（下單），需要傳入地址 ID
 */
export async function placeOrderFromCart(addressId: number): Promise<boolean> {
    const res = await axios.post(`/api/orders/place?addressId=${addressId}`, null, {
        withCredentials: true,
    });
    return res.data?.code === 0;
}

/**
 * 獲取訂單詳情（按數據庫主鍵 ID 查詢）
 */
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
