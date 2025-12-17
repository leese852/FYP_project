<template>
  <!--  使用栅格布局-->
  <a-row :wrap="false">
    <a-col flex="250px">
      <div id="globalHeader">
        <div class="title-bar">
          <img class="logo" src="../assets/logo.png" alt="logo" />
          <div class="title">用户中心</div>
        </div>
      </div>
    </a-col>
    <a-col flex="auto">
      <a-menu
        v-model:selectedKeys="current"
        mode="horizontal"
        :items="items"
        @click="doMenuClick"
      />
    </a-col>

    <a-col flex="auto">
      <div class="search-bar">
        <a-input-search
            v-model:value="searchValue"
            placeholder="input search text"
            enter-button
            @search="onSearch"
            @pressEnter="searchValue"
        />
      </div>
    </a-col>
    <a-col flex="100px">
      <div class="user-login-status">
        <div v-if="loginUserStore.loginUser.id">
          <a-dropdown size="5" @click="onClick">
            <a class="ant-dropdown-link" @click.prevent>
              {{ loginUserStore.loginUser.username }}
              <DownOutlined />
            </a>
            <template #overlay>
              <a-menu @click="onClick">
                <a-menu-item key="1"><UserOutlined /> 个人信息</a-menu-item>
                <a-menu-item key="2"><LogoutOutlined /> 退出登录</a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
        <div v-else>
          <a-button type="primary" href="/user/login">登录</a-button>
        </div>
      </div>
    </a-col>
  </a-row>
</template>
<script setup lang="ts">
import { h, ref } from "vue";
import {
  HomeOutlined,
  AppstoreOutlined,
  PlusSquareOutlined,
  DownOutlined,
  UserOutlined,
  LogoutOutlined,
} from "@ant-design/icons-vue";
import { useRouter } from "vue-router";
import { useLoginUserStore } from "@/store/useLoginUserStore";

const loginUserStore = useLoginUserStore();
console.log("📊 组件中的 loginUser:", loginUserStore.loginUser);

const searchValue = ref("");
const onSearch = (searchValue : string) => {
  console.log(searchValue);
}

const onClick = ({ key }: { key: string }) => {
  if (key === "2") {
    loginUserStore.logoutUser();
    router.push({
      path: "/user/login",
    });
  }
};
const router = useRouter();
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  });
};



const current = ref(["mail"]);
router.afterEach((to) => {
  current.value = [to.path];
});
const items = ref([
  {
    key: "/",
    icon: () => h(HomeOutlined),
    label: "主页",
    title: "Home",
  },
  {
    key: "/user/login",
    icon: () => h(PlusSquareOutlined),
    label: "用户登录",
    title: "用户登录",
  },
  {
    key: "/user/register",
    icon: () => h(AppstoreOutlined),
    label: "用户注册",
    title: "用户注册",
  },
]);
</script>

<style scoped>
.title-bar {
  display: flex;
  justify-content: center;
}
.title {
  font-size: 16px;
}
.logo {
  margin-top: 15px;
  height: 36px;
}
.search-bar{
  margin-top: 15px;
  width: 500px;
}
</style>
