import myAxios from "@/request"
import {dishItem} from "@/types/dish";

/**
 * 获取菜品列表（通用函数）
 * 不传name：获取所有菜品
 * 传name：搜索对应菜品
 * @param name 菜品名称（可选）
 */
export const getDishList = async (name?:string) => {
    return await myAxios.request<dishItem[]>({
        url: "/user/dish/list",
        method: "get",
        params: name ? {name} : {},
    });
};

export const getDishById = async (id:number)=>{
    return await myAxios.request<dishItem>({
        url: `/user/dish/${id}`,
        method: "get",
        // params: {id},
    });
}

