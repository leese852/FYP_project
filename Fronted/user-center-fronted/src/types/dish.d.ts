
// 菜品列表
export type dishItem = {
    id: number
    name: string
    price: number
    categoryId: number
    description: string
    imgUrl: string
    flavors?: flavorItem[]
}

// 口味列表
export type flavorItem = {
    id: number
    name: string
    list: string
    dishId: number
}