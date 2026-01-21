<template>
  <a-layout style="min-height: 100vh">
    <!-- 顶部导航栏 -->
    <a-layout-header class="header">
      <div class="logo">
        <h2 style="color: white; margin: 0">管理系统</h2>
      </div>
      <div style="flex: 1"></div>
      <a-dropdown>
        <a class="ant-dropdown-link" @click.prevent>
          <a-avatar style="background-color: #1890ff">User</a-avatar>
        </a>
        <template #overlay>
          <a-menu>
            <a-menu-item key="1">个人中心</a-menu-item>
            <a-menu-item key="2">退出登录</a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </a-layout-header>

    <a-layout>
      <!-- 侧边栏 -->
      <a-layout-sider v-model:collapsed="collapsed" :trigger="null" collapsible>
        <!-- 菜单 -->
        <a-menu
            v-model:selectedKeys="selectedKeys"
            v-model:openKeys="openKeys"
            theme="dark"
            mode="inline"
            @click="handleMenuClick"
        >
          <a-menu-item key="dashboard">
            <template #icon>
              <dashboard-outlined />
            </template>
            <span>仪表盘</span>
          </a-menu-item>

          <a-menu-item key="menu-management">
            <template #icon>
              <menu-outlined />
            </template>
            <span>菜单管理</span>
          </a-menu-item>

          <a-sub-menu key="system">
            <template #icon>
              <setting-outlined />
            </template>
            <template #title>系统管理</template>
            <a-menu-item key="users">用户管理</a-menu-item>
            <a-menu-item key="roles">角色管理</a-menu-item>
            <a-menu-item key="permissions">权限管理</a-menu-item>
          </a-sub-menu>

          <a-menu-item key="settings">
            <template #icon>
              <tool-outlined />
            </template>
            <span>系统设置</span>
          </a-menu-item>
        </a-menu>
      </a-layout-sider>

      <!-- 主要内容区域 -->
      <a-layout>
        <a-layout-content style="margin: 0 16px">
          <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
            <router-view />
          </div>
        </a-layout-content>

        <!-- 底部 -->
        <a-layout-footer style="text-align: center">
          Ant Design ©2023 Created by Ant UED
        </a-layout-footer>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  DashboardOutlined,
  MenuOutlined,
  SettingOutlined,
  ToolOutlined
} from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const collapsed = ref(false)
const selectedKeys = ref([route.name])
const openKeys = ref([])

// 监听路由变化更新选中的菜单
watch(
    () => route.name,
    (newName) => {
      selectedKeys.value = [newName]
    }
)

const handleMenuClick = ({ key }) => {
  router.push({ name: key })
}
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  padding: 0 24px;
  background: #001529;
}

.logo {
  margin-right: 50px;
}

.header .ant-dropdown-link {
  color: white;
}

.ant-layout-header {
  background: #001529;
  padding: 0;
}

.ant-layout-sider {
  background: #001529;
}

.ant-menu-dark {
  background: #001529;
}
</style>