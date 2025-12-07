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

const loginUserStore = useLoginUserStore();
//提交表单

const handleSubmit = async (values: any) => {
  try {
    const res = await userLogin(values);
    console.log("登录响应", res);
    //登录成功，把状态保存到全局状态中
    if (res.data.code === 0 && res.data.data) {
      await loginUserStore.fetchLoginUser();
      message.success("登录成功");
      router.push({
        path: "/",
        replace: true,
      });
    } else {
      message.error("登录失败" + res.data.description);
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
