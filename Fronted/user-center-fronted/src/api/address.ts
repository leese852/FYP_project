import myAxios from "@/request"

// 获取地址列表
export const getAddressList = async () => {
    return await myAxios.request({
        url: '/user/address/list',
        method: 'get'
    })
}

// 根据ID获取地址
export const getAddressById = async (addressId:number) => {
    return await myAxios.request({
        url: '/user/address/id',
        method: 'get',
        params: { addressId }
    })
}

// 添加地址
export const addAddress = async (data:any) => {
    return await myAxios.request({
        url: '/user/address/add',
        method: 'post',
        data
    })
}

// 更新地址
export const updateAddress = async (data:any) => {
    return await myAxios.request({
        url: '/user/address/update',
        method: 'put',
        data
    })
}

// 删除地址
export const deleteAddress = async (addressId:number) => {
    return await myAxios.request({
        url: '/user/address',
        method: 'delete',
        params: { addressId }
    })
}