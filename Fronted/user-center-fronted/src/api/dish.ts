import myAxios from "@/request"
import {dishItem} from "@/types/dish";

/**
 * 获取菜品列表（通用函数）
 * 不传name：获取所有菜品
 * 传name：搜索对应菜品
 * @param name 菜品名称（可选）
 */
// <dishItem[]>
export const getDishList = async (name?:string) => {
    return await myAxios.request({
        url: "/user/dish/list",
        method: "get",
        params: name ? {name} : {},
    });
};

export const getDishListAdmin = async (name?:string) => {
    return await myAxios.request({
        url: "/admin/dish/list",
        method: "get",
        params: name ? {name} : {},
    });
};
// <dishItem>
export const getDishById = async (id:number)=>{
    return await myAxios.request({
        url: `/user/dish/${id}`,
        method: "get",
        // params: {id},
    });
}

export const setOnOff = async(data:any)=> {
    return await myAxios.request({
        url: "/admin/dish/status",
        method: "put",
        headers: {
            'Content-Type': 'application/json'
        },
        data,
    });
}

export const addDish = async(data:any)=>{
    return await myAxios.request({
        url: "/admin/dish/add_dish",
        method: "post",
        headers: {
            'Content-Type': 'application/json'
        },
        data
    })
}

export const updateDish = async(data:any)=>{
    return await myAxios.request({
        url:"/admin/dish/update_dish",
        method:"put",
        data
    })
}

export const deleteDishs = async(data:any)=>{
    return await myAxios.request({
        url:"/admin/dish/deletes",
        method:"delete",
        data
    })
}
export const deleteDish = async(data:any)=>{
    return await myAxios.request({
        url:"/admin/dish/delete",
        method:"post",
        headers: {
            'Content-Type': 'application/json'
        },
        data
    })
}

// 上传菜品图片
export const uploadDishImage = async (file: File) => {
    const formData = new FormData();
    formData.append("file", file);
    return await myAxios.request({
        url: "/common/upload",
        method: "post",
        data: formData,
        headers: {
            "Content-Type": "multipart/form-data",
        },
    });
};

