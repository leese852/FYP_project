// src/store/useLoginUserStore.ts
import { getCurrentUser, logout } from "@/api/user";
import { defineStore } from "pinia";
import { ref } from "vue";

// 用户类型定义
interface User {
  id?: number;
  username?: string;
  userAccount?: string;
  avatarUrl?: string;
  userRole?: number;
  email?: string;
  tel?: string;
  gender?: number;
  [key: string]: any;
}

export const useLoginUserStore = defineStore("loginUser", () => {
  // 🔥 初始化时优先从 localStorage 读取用户信息
  const storedUser = localStorage.getItem("user");
  const loginUser = ref<User | any>(
      storedUser ? JSON.parse(storedUser) : { username: "未登录" }
  );

  // 通过 session 获取远程登录用户信息
  async function fetchLoginUser() {
    try {
      const userData = await getCurrentUser();
      console.log("✅ 获取用户信息成功:", userData);
      if (userData.data.code === 0 && userData.data.data) {
        loginUser.value = userData.data.data;
        // 🔥 同步保存到 localStorage
        localStorage.setItem("user", JSON.stringify(loginUser.value));
      }
    } catch (error: any) {
      console.log(error.message, "沒有保存用戶信息");
    }
  }

  // 单独设置信息，并进行更新
  function setLoginUser(user: any) {
    loginUser.value = user;
    // 🔥 保存到 localStorage
    if (user && user.id) {
      localStorage.setItem("user", JSON.stringify(user));
    } else {
      localStorage.removeItem("user");
    }
  }

  // 退出登录
  async function logoutUser() {
    await logout();
    loginUser.value = { username: "未登录" };
    // 🔥 清除 localStorage
    localStorage.removeItem("user");
  }

  return { loginUser, fetchLoginUser, setLoginUser, logoutUser };
});