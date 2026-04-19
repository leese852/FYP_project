<template>
  <div id="adminLoginPage">
    <div class="login-container">
      <h2 class="title">系统后台管理登录</h2>
      <a-form
        :model="formState"
        name="adminLogin"
        label-align="left"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
        autocomplete="off"
        @finish="handleSubmit"
      >
        <a-form-item
          label="管理员账号"
          name="account"
          :rules="[{ required: true, message: '请输入管理员账号!' }]"
        >
          <a-input
            v-model:value="formState.account"
            placeholder="请输入管理员账号"
            size="large"
          />
        </a-form-item>

        <a-form-item
          label="管理员密码"
          name="password"
          :rules="[{ required: true, message: '请输入管理员密码!' }]"
        >
          <a-input-password
            v-model:value="formState.password"
            placeholder="请输入管理员密码"
            size="large"
          />
        </a-form-item>

        <a-form-item :wrapper-col="{ span: 24 }" style="text-align: center; margin-top: 30px;">
          <a-button type="primary" html-type="submit" size="large" style="width: 100%;">
            登 录
          </a-button>
        </a-form-item>
      </a-form>
      <div style="text-align: center; margin-top: 15px;">
        <a-typography-link @click="toUserLogin">
          返回普通用户登录
        </a-typography-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import { employeeLogin } from "@/api/employee";
import { useLoginEmployeeStore } from "@/store/useLoginEmployeeStore";
import { message } from "ant-design-vue";
import { useRouter } from "vue-router";

const router = useRouter();
const loginEmployeeStore = useLoginEmployeeStore();

interface FormState {
  account: string;
  password: string;
}

const formState = reactive<FormState>({
  account: "",
  password: "",
});

const toUserLogin = () => {
  router.push("/user/login");
};

const handleSubmit = async (values: any) => {
  try {
    const res = await employeeLogin(values);
    if (res.data.code === 0 && res.data.data) {
      // 登录成功，获取专属的 Employee 信息
      await loginEmployeeStore.fetchLoginEmployee();
      
      message.success("管理员登录成功");
      router.push({
        path: "/admin/orders", // 跳转到后台主页
        replace: true,
      });
    } else {
      message.error("登录失败：" + (res.data.description || "账号或密码错误"));
    }
  } catch (error: any) {
    message.error(error.message || "网络异常，登录失败");
  }
};
</script>

<style scoped>
#adminLoginPage {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}

.login-container {
  width: 450px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.title {
  margin-bottom: 30px;
  color: #333;
  text-align: center;
  font-weight: 600;
  font-size: 24px;
}
</style>
