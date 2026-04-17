// src/types/order.ts

// Basic Order interface (matches OrderEntity)
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
    createTime: string;
    updateTime: string;
    isDelete: number;
    riderId?: number;

    // 🔥 添加坐标字段（从订单表获取）
    restaurantLat?: number;
    restaurantLng?: number;
    customerLat?: number;
    customerLng?: number;

    // 骑手信息（从关联表获取）
    rider?: {
        id: number;
        name: string;
        phone: string;
        location?: string;
    };
}

// For order details with items
export interface OrderWithItems extends Order {
    items?: OrderItem[];
    statusLabel?: string;
    formattedTime?: string;
    customerName?: string;
}

// Order item interface
export interface OrderItem {
    id: number;
    orderId: number;
    dishId?: number;
    dishName: string;
    dishFlavor?: string;
    quantity: number;
    price: number;
    subtotal?: number;
}

// For dashboard statistics
export interface OrderStats {
    pendingAcceptance: number;
    accepted: number;
    pendingRefund: number;
    delivering: number;
    completed: number;
    completedOld: number;
    cancelled: number;
    others: number;
}