import HomePage from "@/page/user/common/HomePage.vue";
import UserLoginPage from "@/page/user/common/UserLoginPage.vue";
import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import UserRegisterPage from "@/page/user/common/UserRegisterPage.vue";
import DishDetailPage from "@/page/user/dish/DishDetailPage.vue";
import AddressPage from "@/page/user/address/AddressPage.vue";
import UserInfoPage from "@/page/user/common/UserInfoPage.vue";
import AdminLayout from "@/layout/AdminLayout.vue";
import BasicLayout from "@/layout/BasicLayout.vue";
import OrderStaffView from "@/page/order/OrderStaffView.vue";
import OrderCustomerView from "@/page/order/OrderCustomerView.vue";
import OrderRiderView from "@/page/order/OrderRiderView.vue";
import OrderDashboard from '@/page/order/OrderStaffmang.vue'
import StaffOrderManagement from '@/page/order/StaffOrderManagement.vue'
import OrderCancelPage from "@/page/order/OrderCancelPage.vue";
import OrderList from "@/page/order/OrderList.vue";

const routes: RouteRecordRaw[] = [
  {
    path: "/",
    component: BasicLayout,
    children: [
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
        name: "userInfo",
        component: UserInfoPage,
      },
      {
        path: "order/customer/:orderId",
        name: "orderCustomer",
        component: OrderCustomerView,
        props: true,
      },
      {
        path: "order/view",
        name: "orderView",
        component: OrderCustomerView,
      },
      {
        path: "order/rider",
        name: "orderRider",
        component: OrderRiderView,
      },
      {
        path: "order/customeorderlist",
        name: "orderCustomerlist",
        component: OrderList,
      },
      {
        path: "order/cancel",
        name: "orderCancel",
        component: OrderCancelPage,
      },
      {
        path: "user/cart",
        name: "shopping cart",
        component: () => import("@/page/user/cart/ShoppingCart.vue")
      },
      {
        path: "user/feedback",
        name: "userFeedback",
        component: () => import("@/page/user/feedback/FeedbackSubmitPage.vue")
      },
      {
        path: "user/feedback/my",
        name: "myFeedback",
        component: () => import("@/page/user/feedback/MyFeedbackPage.vue")
      },
    ]
  },
  {
    path: "/admin",
    component: AdminLayout,
    children: [
      {
        path: "orders",
        name: "OrderDashboard",
        component: OrderDashboard,
        meta: { requiresAuth: true, requiresAdmin: true }
      },
      {
        path: "orders/staff",
        name: "StaffOrderManagement",
        component: StaffOrderManagement,
        meta: { requiresAuth: true, requiresStaff: true }
      },
      {
        path: "orders/:orderId",
        name: "AdminOrderDetail",  // 修改名稱避免重複
        component: () => import("@/page/order/OrderCustomerView.vue"),  // 使用動態導入
        meta: { requiresAuth: true, requiresAdmin: true},
        props: true
      },
      {
        path: "dish/list",
        name: "dishList",
        component: () => import("@/page/employee/dish/Index.vue")
      },
      {
        path: "dish/add",
        name: "addDish",
        component: () => import("@/page/employee/dish/AddDish.vue")
      },
      {
        path: "dish/update/:id",
        name: "updateDish",
        component: () => import("@/page/employee/dish/Update.vue")
      },
      {
        path: "feedback",
        name: "feedbackManage",
        component: () => import("@/page/employee/feedback/FeedbackManagePage.vue")
      },
      {
        path: "feedback/:id",
        name: "feedbackDetail",
        component: () => import("@/page/employee/feedback/FeedbackDetailPage.vue")
      },
    ]
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;