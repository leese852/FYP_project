import myAxios from "@/request";

export const addCart = async (data:any) => {
    return await myAxios.request({
        url: '/user/cart/add',
        method: 'post',
        data: data,
        withCredentials: true // 携带cookie
    })
}

export const getAllCart = async () => {
    return await myAxios.request({
        url: '/user/cart/list',
        method: 'get'
    })
}

export const deleteCart = async (id:any) => {
    return await myAxios.request({
        url: '/user/cart/delete',
        method: 'delete',
        params: { id },
    })
}

// 清空购物车
export const deleteAllCart = async () => {
    return await myAxios.request({
        url: '/user/cart/deleteAll',
        method: 'delete',
    })
}

export const placeOrderFromCart =async (data:any) =>{
    return await myAxios.request({
        url: '/api/orders/place',
        method: 'post',
        data: data,
    })
}
