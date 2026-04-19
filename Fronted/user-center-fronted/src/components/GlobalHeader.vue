<template>
  <a-row :wrap="false" class="global-header">
    <a-col flex="250px">
      <div id="globalHeader">
        <div class="title-bar">
          <img class="logo" src="../assets/logo.png" alt="logo" />
          <div class="title">美食点餐系统</div>
        </div>
      </div>
    </a-col>
    <a-col flex="auto">
      <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
          class="header-menu"
      />
    </a-col>

    <a-col flex="auto">
      <div class="search-bar">
        <a-input-search
            v-model:value="searchValue"
            placeholder="搜索菜品..."
            enter-button
            @search="onSearch"
            class="orange-search"
        />
      </div>
    </a-col>
    <a-col flex="100px">
      <div class="user-login-status">
        <div v-if="isLoggedIn">
          <a-dropdown :trigger="['click']" placement="bottomRight">
            <div class="user-dropdown-trigger">
              <a-avatar
                  v-if="avatarUrl"
                  :src="avatarUrl"
                  class="user-avatar"
              />
              <a-avatar v-else class="user-avatar" :style="{ backgroundColor: '#ff7a1a' }">
                {{ displayName.charAt(0).toUpperCase() }}
              </a-avatar>
            </div>
            <template #overlay>
              <a-menu @click="handleMenuClick">
                <a-menu-item key="profile" class="user-name-item">
                  <UserOutlined /> {{ displayName }}
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item key="info">
                  <UserOutlined /> 个人信息
                </a-menu-item>
                <a-menu-item key="myFeedback">
                  <MessageOutlined /> 我的反馈
                </a-menu-item>
                <a-menu-item key="logout">
                  <LogoutOutlined /> 退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
        <div v-else>
          <a-button type="primary" href="/user/login" class="login-btn">登录</a-button>
        </div>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { computed, h, ref } from "vue";
import {
  HomeOutlined,
  AppstoreOutlined,
  PlusSquareOutlined,
  UserOutlined,
  LogoutOutlined,
  MessageOutlined,
} from "@ant-design/icons-vue";
import { useRouter } from "vue-router";
import { useLoginUserStore } from "@/store/useLoginUserStore";
import { message } from "ant-design-vue";

const loginUserStore = useLoginUserStore();

const searchValue = ref("");
const onSearch = (searchValue: string) => {
  console.log("搜索:", searchValue);
}

const isLoggedIn = computed(() => {
  const user = loginUserStore.loginUser;
  return user && user.id;
});

const displayName = computed(() => {
  const user = loginUserStore.loginUser;
  if (!user) return "用户";
  return user.username || user.userAccount || "用户";
});

const avatarUrl = computed(() => {
  return loginUserStore.loginUser?.avatarUrl || "";
})

const router = useRouter();

// 🔥 修复：处理菜单点击
const handleMenuClick = ({ key }: { key: string }) => {
  console.log("菜单点击:", key);

  if (key === "logout") {
    loginUserStore.logoutUser();
    message.success("已退出登录");
    router.push({ path: "/user/login" });
  } else if (key === "info") {
    router.push({ path: "/user/info" });
  } else if (key === "profile") {
    // 个人信息，可以跳转到个人中心
    router.push({ path: "/user/info" });
  }else if (key === "myFeedback") {  // 🔥 添加我的反馈跳转
    router.push({ path: "/user/feedback/my" });
  }
};

const doMenuClick = ({ key }: { key: string }) => {
  router.push({ path: key });
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
    key: "/order/customeorderlist",
    icon: () => h(AppstoreOutlined),
    label: "訂單列表",
    title: "訂單列表",
  },
  {
    key: "/user/cart",
    icon: () => h(PlusSquareOutlined),
    label: "购物车",
    title: "购物车",
  }
]);
</script>

<style scoped>
.global-header {
  padding: 0 24px;
  height: 64px;
  background: linear-gradient(135deg, #ffffff 0%, #fff8f0 100%);
  border-bottom: 1px solid #ffd9b3;
  align-items: center;
}

.title-bar {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title {
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, #ff7a1a 0%, #e66a0e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.logo {
  height: 40px;
  width: auto;
}

.search-bar {
  margin-top: 0;
  width: 100%;
  max-width: 400px;
}

.search-bar :deep(.ant-input-search .ant-input-group-addon .ant-btn) {
  background: linear-gradient(135deg, #ff7a1a 0%, #e66a0e 100%);
  border-color: #ff7a1a;
  border-radius: 0 20px 20px 0;
}

.search-bar :deep(.ant-input-search .ant-input) {
  border-radius: 20px 0 0 20px;
}

.search-bar :deep(.ant-input-search .ant-input-group-addon .ant-btn:hover) {
  background: linear-gradient(135deg, #ff944d 0%, #ff7a1a 100%);
}

.header-menu {
  background: transparent;
  border-bottom: none;
}

.header-menu :deep(.ant-menu-item-selected) {
  color: #ff7a1a;
  border-bottom-color: #ff7a1a;
}

.header-menu :deep(.ant-menu-item:hover) {
  color: #ff7a1a;
}

.user-login-status {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 64px;
}

.user-dropdown-trigger {
  cursor: pointer;
  display: flex;
  align-items: center;
}

.user-avatar {
  cursor: pointer;
  transition: transform 0.3s;
}

.user-avatar:hover {
  transform: scale(1.05);
}

.login-btn {
  background: linear-gradient(135deg, #ff7a1a 0%, #e66a0e 100%);
  border: none;
  border-radius: 20px;
  padding: 4px 20px;
}

.user-menu .user-name-item {
  color: #ff7a1a;
  font-weight: 600;
}

/* 响应式 */
@media (max-width: 768px) {
  .global-header {
    padding: 0 12px;
  }

  .search-bar {
    max-width: 200px;
  }

  .title {
    font-size: 16px;
  }
}
</style>