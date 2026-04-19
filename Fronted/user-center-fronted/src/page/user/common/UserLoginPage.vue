<!-- src/page/user/common/UserLoginPage.vue -->
<template>
  <div id="userLoginPage">
    <h2 class="title">用户登录</h2>
    <a-form
        style="max-width: 400px; margin: 0 auto"
        :model="formState"
        name="basic"
        label-align="left"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
        autocomplete="off"
        @finish="handleSubmit"
    >
      <a-form-item
          label="Username"
          name="userAccount"
          :rules="[{ required: true, message: 'Please input your username!' }]"
      >
        <a-input
            v-model:value="formState.userAccount"
            placeholder="请输入账号"
        />
      </a-form-item>

      <a-form-item
          label="Password"
          name="userPassword"
          :rules="[
          { required: true, message: 'Please input your password!' },
          { min: 4, message: '密码长度不能小于8' },
        ]"
      >
        <a-input-password
            v-model:value="formState.userPassword"
            placeholder="请输入密码"
        />
      </a-form-item>

      <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">
        <a-checkbox v-model:checked="formState.remember">
          Remember me
        </a-checkbox>
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
        <a-button type="primary" html-type="submit">Submit</a-button>
      </a-form-item>
    </a-form>

    <!-- 在表单外添加注册链接 -->
    <div style="max-width: 400px; margin: 20px auto; text-align: center">
      <a-typography-text type="secondary">
        还没有账号？
      </a-typography-text>
      <a-typography-link @click="toRegister" style="margin-left: 8px">
        立即注册
      </a-typography-link>

      <div style="margin-top: 15px;">
        <a-typography-link @click="toAdminLogin" type="secondary">
          → 前往管理员登录
        </a-typography-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import { userLogin } from "@/api/user";
import { useLoginUserStore } from "@/store/useLoginUserStore";
import { message } from "ant-design-vue";
import router from "@/router";

interface FormState {
  userAccount: string;
  userPassword: string;
  remember: boolean;
}

const formState = reactive<FormState>({
  userAccount: "",
  userPassword: "",
  remember: true,
});

// 跳转到注册页面
const toRegister = () => {
  router.push("/user/register");
};

// 跳转到管理员登录
const toAdminLogin = () => {
  router.push("/admin/login");
};

const loginUserStore = useLoginUserStore();

// 提交表单
const handleSubmit = async (values: any) => {
  try {
    const res = await userLogin(values);

    // 登录成功，把状态保存到全局状态中
    if (res.data.code === 0 && res.data.data) {
      const userData = res.data.data;

      // 🔥 保存完整的用户信息到 Store（会自动存入 localStorage）
      loginUserStore.setLoginUser(userData);

      message.success("登录成功");
      router.push({
        path: "/",
        replace: true,
      });
    } else {
      console.log('登录失败: code不为0或data为空');
      message.error("登录失败 " + (res.data.description || res.data.message));
    }
  } catch (error: any) {
    message.error(error.message || "网络异常");
  }
  console.log("Success:", values);
};
</script>

<style scoped>
.title {
  margin-top: 30px;
  margin-bottom: 16px;
  color: dimgray;
  text-align: center;
}
</style>