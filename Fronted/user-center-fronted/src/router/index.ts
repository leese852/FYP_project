import HomePage from "@/page/user/HomePage.vue";
import UserLoginPage from "@/page/user/UserLoginPage.vue";
import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import HomeView from "../views/HomeView.vue";
import UserRegisterPage from "@/page/user/UserRegisterPage.vue";
import DishDetailPage from "@/page/user/DishDetailPage.vue";

// const routes: Array<RouteRecordRaw
const routes = [
  {
    path: "/",
    name: "home",
    component: HomePage,
  },
  {
    path: "/user/login",
    name: "userLogin",
    component: UserLoginPage,
  },
  {
    path: "/user/register",
    name: "userRegister",
    component: UserRegisterPage,
  },
  {
    path: "/user/dish/:id",
    name: "dishDetail",
    component: DishDetailPage,
  },

];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
