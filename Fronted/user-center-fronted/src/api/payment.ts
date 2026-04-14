import myAxios from "@/request"

/**
 * 支付请求的参数类型
 */
export interface PaymentRequest{
    orderId:number | string;
    amount: number | string;
    paymentMethod: string;
}

/**
 * 发起支付请求
 * @param data 支付参数
 */
export const processPayment =  (data : PaymentRequest) => {
    return myAxios({
        url: '/api/payments/process',
        method: 'post',
        data: data
    })
}