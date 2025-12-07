<template>
  <div id="userRegisterPage">
    <h2 class="title">用户注册</h2>
    <a-form
      style="max-width: 400px; margin: 0 auto"
      :model="formState"
      name="basic"
      label-align="left"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      @finish="handleSubmit"
      @finishFailed="handleSubmitFailed"
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

      <a-form-item
        label="Confirm Password"
        name="checkPassword"
        :rules="[
          { required: true, message: 'Please input your password!' },
          { min: 4, message: '密码长度不能小于8' },
        ]"
      >
        <a-input-password
          v-model:value="formState.checkPassword"
          placeholder="请输入确认密码"
        />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
        <a-button type="primary" html-type="submit">Register</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from "vue";
import { userRegister } from "@/api/user";
import { message } from "ant-design-vue";
import router from "@/router";

interface FormState {
  userAccount: string;
  userPassword: string;
  checkPassword: string;
}

const formState = reactive<FormState>({
  userAccount: "",
  userPassword: "",
  checkPassword: "",
});

//提交表单
const handleSubmit = async (values: any) => {
  if (formState.userPassword !== formState.checkPassword) {
    message.error("密码不一致");
    return;
  }
  const res = await userRegister(values);
  // 注册成功，跳转到登录页面
  if (res.data.code === 0 && res.data.data) {
    message.success("注册成功");
    router.push({
      path: "/user/login",
      replace: true,
    });
  } else {
    message.error("注册失败，" + res.data.message);
  }
};
const handleSubmitFailed = (errorInfo: any) => {
  console.log("Failed:", errorInfo);
};
</script>
<style scoped>
userRegisterPage {
}
.title {
  text-align: center;
  color: dimgray;
  margin-bottom: 16px;
  margin-top: 30px;
}
</style>
