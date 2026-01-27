// src/types/order.ts
export interface Order {
    id: number;
    orderId: string;
    userId: number;
    addressId: number;
    status: number;
    totalAmount: number;
    payMethod: string;
    payStatus: number;
    remark?: string;
    cancelReason?: string;
    cancelTime?: string;
    rejectionReason?: string;
    estimatedDeliveryTime?: string;
    deliveryStatus: number;
    deliveryTime?: string;
    packAmount?: number;
    createTime?: string;
    updateTime?: string;
    isDelete: number;
    riderId?: number;
    rideAddress?: string;
    orderComment?: string;
}
