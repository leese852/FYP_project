import HomePage from "@/page/user/common/HomePage.vue";
import UserLoginPage from "@/page/user/common/UserLoginPage.vue";
import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

import UserRegisterPage from "@/page/user/common/UserRegisterPage.vue";
import DishDetailPage from "@/page/user/dish/DishDetailPage.vue";
import AddressPage from "@/page/user/address/AddressPage.vue";
import UserInfoPage from "@/page/user/common/UserInfoPage.vue";
// import * as path from "node:path";
import AdminLayout from "@/layout/AdminLayout.vue";
import BasicLayout from "@/layout/BasicLayout.vue";

// const routes: Array<RouteRecordRaw
const routes: RouteRecordRaw[] = [
  {
    path: "/",
    component: BasicLayout,
    children:[
      {
        path: "",
        name: "home",
        component: HomePage,
      },
      {
        path: "user/login",
        name: "userLogin",
        component: UserLoginPage,
      },
      {
        path: "user/register",
        name: "userRegister",
        component: UserRegisterPage,
      },
      {
        path: "user/dish/:id",
        name: "dishDetail",
        component: DishDetailPage,
      },
      {
        path: "user/address",
        name: "address",
        component: AddressPage,
      },
      {
        path: "user/info",
        name:"userInfo",
        component: UserInfoPage,
      }
    ]
  },
  {
    path:"/admin",
    component: AdminLayout,
    children: [
      {
        path: "dish/list",
        name: "dishList",
        component: () => import("@/page/employee/dish/index.vue")
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
