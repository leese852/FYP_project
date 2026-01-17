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
        <div v-if="isLoggedIn">
          <a-dropdown size="5" >
            <a class="ant-dropdown-link" @click.prevent>
              {{ displayName }}
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
import {computed, h, ref} from "vue";
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
// 计算属性：判断是否登录
const isLoggedIn = computed(() => {
  const user = loginUserStore.loginUser;
  console.log("🔍 登录状态检查:", user);
  return user && user.id;
});

// 计算属性：显示名称
const displayName = computed(() => {
  const user = loginUserStore.loginUser;
  if (!user) return "用户";

  // 优先显示username，如果为null则显示userAccount
  return user.username || user.userAccount || "用户";
});

const onClick = ({ key }: { key: string }) => {
  if (key === "2") {
    loginUserStore.logoutUser();
    router.push({
      path: "/user/login",
    });
  }else if (key === "1") {
    router.push({
      path: "/user/profile",
    });
  }
};

const router = useRouter();
// 菜单点击事件
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
    key: "/user/address",
    icon: () => h(PlusSquareOutlined),
    label: "地址管理",
    title: "地址管理",
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
