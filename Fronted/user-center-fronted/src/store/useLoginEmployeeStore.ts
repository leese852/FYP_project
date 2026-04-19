import { defineStore } from "pinia";
import { ref } from "vue";
import { getCurrentEmployee } from "@/api/employee";

export const useLoginEmployeeStore = defineStore("loginEmployee", () => {
  const loginEmployee = ref<any>({
    name: "未登录",
  });

  async function fetchLoginEmployee() {
    try {
      const res = await getCurrentEmployee();
      if (res.data.code === 0 && res.data.data) {
        loginEmployee.value = res.data.data;
      }
    } catch (e) {
      console.error("未能获取管理员状态", e);
    }
  }

  function setLoginEmployee(newEmployee: any) {
    loginEmployee.value = newEmployee;
  }

  return { loginEmployee, fetchLoginEmployee, setLoginEmployee };
});
