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
    createTime: string;  // Changed from optional to required
    updateTime: string;  // Changed from optional to required
    isDelete: number;
    riderId?: number;
    // rideAddress?: string;   // This might be in OrderVO but not OrderEntity
    // orderComment?: string;  // This might be in OrderVO but not OrderEntity
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