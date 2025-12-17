import myAxios from "@/request"
import {dishItem} from "@/types/dish.d";

/**
 * 根据菜品名称搜索菜品
 * @param params
 */
export const getDishByName = async (params: any) => {
    return await myAxios.request<dishItem[]>({
        url: "/user/dish/list",
        method: "get",
    });
};

/**
 * 所有菜品
 *
 */
export const getAllDish = async(name?:string) =>{
    return await myAxios.request<dishItem>({
        url: "/user/dish/list",
        method: "get",
        params: name,
    });
}