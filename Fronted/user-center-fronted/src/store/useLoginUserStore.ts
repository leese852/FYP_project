import { getCurrentUser, logout } from "@/api/user";
import { defineStore } from "pinia";
import { ref } from "vue";

//Pinia 是 Vue 的专属状态管理库，它允许你跨组件或页面共享状态。
// 如果你熟悉组合式 API 的话，
// 你可能会认为可以通过一行简单的 export const state = reactive({})
// 来共享一个全局状态。
export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<any>({
    username: "未登录",
  });

  //远程获取登录用户信息
  async function fetchLoginUser() {
    try{
      const userData = await getCurrentUser();
      loginUser.value = userData;
      console.log("✅ 获取用户信息成功:", userData);
      // if (res.data.code === 0 && res.data.data) {
      //   loginUser.value = res.data.data;
      // }
    }catch(error:any){
      console.log(error.message);
    }
  }
  //单独设置信息，并进行更新
  function setLoginUser(user: any) {
    loginUser.value = user;
  }

  async function logoutUser() {
    const res = await logout();
    setLoginUser({ username: "未登录" });
  }

  return { loginUser, fetchLoginUser, setLoginUser, logoutUser };
});
