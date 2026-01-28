<template>
  <a-layout style="min-height: 100vh">
    <!-- 顶部导航栏 - 优化 -->
    <a-layout-header class="admin-header">
      <!-- 左侧：Logo和系统名称 -->
      <div class="header-left">
        <div class="logo">
          <img
              v-if="logoUrl"
              :src="logoUrl"
              alt="Logo"
              class="logo-img"
          />
          <div v-else class="logo-placeholder">
            <control-outlined />
          </div>
          <h1 class="system-title" v-show="!collapsed">
            管理系统
          </h1>
        </div>

        <!-- 侧边栏折叠按钮 -->
        <menu-unfold-outlined
            v-if="collapsed"
            class="trigger"
            @click="toggleCollapsed"
        />
        <menu-fold-outlined
            v-else
            class="trigger"
            @click="toggleCollapsed"
        />
      </div>

      <!-- 中间：快捷操作和面包屑 -->
      <div class="header-center">
        <!-- 面包屑导航 -->
        <a-breadcrumb class="breadcrumb">
          <a-breadcrumb-item>首页</a-breadcrumb-item>
          <a-breadcrumb-item v-for="item in breadcrumb" :key="item">
            {{ item }}
          </a-breadcrumb-item>
        </a-breadcrumb>

        <!-- 快捷操作按钮 -->
        <div class="quick-actions" v-show="!isMobile">
          <a-tooltip title="刷新">
            <reload-outlined class="action-icon" @click="handleRefresh" />
          </a-tooltip>
          <a-tooltip title="全屏">
            <fullscreen-outlined class="action-icon" @click="toggleFullscreen" />
          </a-tooltip>
          <a-tooltip title="消息">
            <bell-outlined class="action-icon" :badge-count="messageCount" />
          </a-tooltip>
        </div>
      </div>

      <!-- 右侧：用户信息 -->
      <div class="header-right">
<!--        &lt;!&ndash; 搜索框 &ndash;&gt;-->
<!--        <a-auto-complete-->
<!--            v-model:value="searchValue"-->
<!--            class="search-box"-->
<!--            placeholder="搜索菜单、功能..."-->
<!--            :options="searchOptions"-->
<!--            @select="handleSearchSelect"-->
<!--            @search="handleSearch"-->
<!--        >-->
<!--          <template #prefix>-->
<!--            <search-outlined />-->
<!--          </template>-->
<!--        </a-auto-complete>-->

        <!-- 用户信息下拉菜单 -->
        <a-dropdown placement="bottomRight" :trigger="['click']">
          <div class="user-info">
            <a-avatar
                :size="32"
                :src="userInfo.avatar"
                :style="{ backgroundColor: userInfo.avatar ? 'transparent' : '#1890ff' }"
            >
              {{ userInfo.avatar ? '' : userInfo.name?.charAt(0) }}
            </a-avatar>
            <div class="user-details" v-show="!isMobile">
              <div class="user-name">{{ userInfo.name }}</div>
              <div class="user-role">{{ userInfo.role }}</div>
            </div>
            <down-outlined class="dropdown-icon" />
          </div>

          <template #overlay>
            <a-menu class="user-menu">
              <a-menu-item key="profile" @click="goToProfile">
                <user-outlined />
                个人中心
              </a-menu-item>
              <a-menu-item key="settings" @click="goToSettings">
                <setting-outlined />
                账号设置
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item key="help">
                <question-circle-outlined />
                帮助文档
              </a-menu-item>
              <a-menu-item key="feedback">
                <message-outlined />
                意见反馈
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item key="logout" class="logout-item" @click="handleLogout">
                <logout-outlined />
                退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
    </a-layout-header>

    <a-layout>
      <!-- 侧边栏 - 优化 -->
      <a-layout-sider
          v-model:collapsed="collapsed"
          :trigger="null"
          collapsible
          :width="sidebarWidth"
          :collapsed-width="collapsedSidebarWidth"
          breakpoint="lg"
          @breakpoint="onBreakpoint"
          class="admin-sider"
          :theme="sidebarTheme"
      >
        <!-- 菜单区域 -->
        <div class="menu-container">
          <!-- 菜单标题（折叠时隐藏） -->
          <div class="menu-title" v-show="!collapsed">
            系统菜单
          </div>

          <!-- 菜单列表 -->
          <a-menu
              v-model:selectedKeys="selectedKeys"
              v-model:openKeys="openKeys"
              theme="dark"
              mode="inline"
              :inline-collapsed="collapsed"
              @click="handleMenuClick"
              class="admin-menu"
          >
            <!-- 动态生成菜单 -->
            <template v-for="menu in menuList" :key="menu.path">
              <!-- 如果没有子菜单，渲染普通菜单项 -->
              <a-menu-item v-if="!menu.children" :key="menu.path">
                <template #icon>
                  <component v-if="menu.iconComponent" :is="menu.iconComponent" />
                  <component v-else-if="menu.icon" :is="getIcon(menu.icon)" />
                </template>
                <span>{{ menu.title }}</span>
              </a-menu-item>

<!--              &lt;!&ndash; 如果有子菜单，渲染子菜单 &ndash;&gt;-->
<!--              <a-sub-menu v-else :key="menu.path">-->
<!--                <template #icon>-->
<!--                  <component v-if="menu.iconComponent" :is="menu.iconComponent" />-->
<!--                  <component v-else-if="menu.icon" :is="getIcon(menu.icon)" />-->
<!--                </template>-->
<!--                <template #title>{{ menu.title }}</template>-->
<!--                <a-menu-item-->
<!--                    v-for="child in menu.children"-->
<!--                    :key="child.path"-->
<!--                >-->
<!--                  <span>{{ child.title }}</span>-->
<!--                </a-menu-item>-->
<!--              </a-sub-menu>-->
            </template>
          </a-menu>
        </div>

        <!-- 侧边栏底部 -->
        <div class="sider-footer" v-show="!collapsed">
          <div class="system-info">
            <span>系统版本: v{{ systemVersion }}</span>
            <span class="status-dot" :class="{ online: systemOnline }"></span>
          </div>
        </div>
      </a-layout-sider>

      <!-- 主要内容区域 - 优化 -->
      <a-layout class="main-content">
        <!-- 标签页导航（可选） -->
        <div class="tab-nav" v-if="showTabs">
          <a-tabs
              v-model:activeKey="activeTab"
              type="editable-card"
              hide-add
              @edit="onTabEdit"
          >
            <a-tab-pane
                v-for="tab in tabs"
                :key="tab.key"
                :tab="tab.title"
                :closable="tab.closable"
            />
          </a-tabs>
        </div>

        <!-- 页面内容 -->
        <a-layout-content class="content-wrapper">
          <div class="page-container">
            <!-- 页面标题和操作按钮 -->
            <div class="page-header" v-if="showPageHeader">
              <div class="page-title">
                <h2>{{ pageTitle }}</h2>
                <a-tag v-if="pageTag" :color="pageTag.color">
                  {{ pageTag.text }}
                </a-tag>
              </div>
              <div class="page-actions">
                <slot name="actions"></slot>
                <a-button v-if="showBackButton" @click="goBack">
                  <arrow-left-outlined />
                  返回
                </a-button>
              </div>
            </div>

            <!-- 页面内容 -->
            <div class="page-content">
              <router-view v-slot="{ Component }">
                <transition name="fade-transform" mode="out-in">
                  <component :is="Component" :key="route.fullPath" />
                </transition>
              </router-view>
            </div>
          </div>
        </a-layout-content>

        <!-- 底部信息 -->
        <a-layout-footer class="admin-footer">
          <div class="footer-content">
            <div class="copyright">
              © {{ currentYear }} 管理系统 |
            </div>
          </div>
        </a-layout-footer>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script setup>
import {ref, computed, onMounted, onUnmounted, watch} from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  // 原有图标
  DashboardOutlined,
  MenuOutlined,
  SettingOutlined,
  ToolOutlined,
  // 新增图标
  ControlOutlined,
  MenuUnfoldOutlined,
  MenuFoldOutlined,
  ReloadOutlined,
  FullscreenOutlined,
  BellOutlined,
  SearchOutlined,
  DownOutlined,
  UserOutlined,
  QuestionCircleOutlined,
  MessageOutlined,
  LogoutOutlined,
  AppstoreOutlined,
  TeamOutlined,
  AreaChartOutlined,
  ArrowLeftOutlined,
  // 添加菜单需要的图标
  ShoppingCartOutlined,
  GiftOutlined,
  CoffeeOutlined
} from '@ant-design/icons-vue'

// 可扩展的菜单配置
const menuList = ref([
  {
    // 看router文件配置
    title: '控制台',
    path: '/admin/',
    iconComponent: DashboardOutlined,
  },
  {
    title: '菜品管理',
    path: '/admin/dish/list',
    iconComponent: CoffeeOutlined,
  }
])


const router = useRouter()
const route = useRoute()

// 响应式状态
const collapsed = ref(false)
const selectedKeys = ref([route.name])
const openKeys = ref([])
const isMobile = ref(false)
const searchValue = ref('')
const messageCount = ref(3)
const fullscreen = ref(false)

// 用户信息
const userInfo = ref({
  name: '管理员',
  role: '超级管理员',
  avatar: ''
})

// 系统信息
const systemVersion = ref('1.0.0')
const systemOnline = ref(true)

// 面包屑
const breadcrumb = computed(() => {
  const path = route.path.split('/').filter(p => p)
  return path.map(p => {
    // 这里可以根据路由配置显示更友好的名称
    return p.charAt(0).toUpperCase() + p.slice(1)
  })
})

// 侧边栏配置
const sidebarWidth = computed(() => isMobile.value ? 200 : 256)
const collapsedSidebarWidth = computed(() => isMobile.value ? 0 : 80)
const sidebarTheme = 'dark'

// 页面标题
const pageTitle = computed(() => {
  // 根据路由显示页面标题
  const titles = {
    'dashboard': '仪表盘',
    'menu-management': '菜单管理',
    'system-users': '用户管理',
    // ... 添加更多路由标题映射
  }
  return titles[route.name] || '管理系统'
})

// 搜索选项
const searchOptions = ref([])

// 时间相关
const currentYear = new Date().getFullYear()

// 方法
const toggleCollapsed = () => {
  collapsed.value = !collapsed.value
}

const onBreakpoint = (broken) => {
  isMobile.value = broken
  if (broken) {
    collapsed.value = true
  }
}

const handleMenuClick = ({ key }) => {
  router.push({ path: key })
}

const handleRefresh = () => {
  window.location.reload()
}

const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
    fullscreen.value = true
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen()
      fullscreen.value = false
    }
  }
}

const handleSearch = (value) => {
  // 实现搜索逻辑
  console.log('搜索:', value)
}

const handleSearchSelect = (value) => {
  console.log('选择:', value)
  searchValue.value = ''
}

const goToProfile = () => {
  router.push({ name: 'profile' })
}

const goToSettings = () => {
  router.push({ name: 'settings' })
}

const handleLogout = () => {
  // 实现退出登录逻辑
  console.log('退出登录')
  router.push('/login')
}

const showAbout = () => {
  // 显示关于对话框
}

const goBack = () => {
  router.back()
}

// 监听路由变化
const stopWatch = watch(
    () => route.name,
    (newName) => {
      selectedKeys.value = [newName]
      // 根据路由自动展开对应的菜单
      const parentKey = getParentKey(newName)
      if (parentKey && !openKeys.value.includes(parentKey)) {
        openKeys.value = [parentKey]
      }
    }
)

const getParentKey = (key) => {
  // 根据子菜单key获取父菜单key
  const menuMap = {
    'articles': 'content',
    'categories': 'content',
    'tags': 'content',
    'comments': 'content',
    'user-list': 'user-management',
    'user-groups': 'user-management',
    'user-roles': 'user-management',
    'system-users': 'system',
    'system-roles': 'system',
    'system-permissions': 'system',
    'system-logs': 'system',
    'system-settings': 'system',
    'api-test': 'tools',
    'cache-clear': 'tools',
    'data-backup': 'tools',
  }
  return menuMap[key]
}

// 组件卸载时清理
onUnmounted(() => {
  stopWatch()
})

// 初始化
onMounted(() => {
  // 检测是否移动端
  const checkMobile = () => {
    isMobile.value = window.innerWidth < 992
  }

  checkMobile()
  window.addEventListener('resize', checkMobile)

  // 监听全屏变化
  document.addEventListener('fullscreenchange', () => {
    fullscreen.value = !!document.fullscreenElement
  })
})
</script>

<style scoped>
/* 顶部导航栏样式 */
.admin-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: white;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 10;
  height: 64px;
  position: sticky;
  top: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-img {
  width: 32px;
  height: 32px;
  border-radius: 6px;
}

.logo-placeholder {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #1890ff 0%, #722ed1 100%);
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.system-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
  white-space: nowrap;
}

.trigger {
  font-size: 18px;
  cursor: pointer;
  color: #666;
  transition: color 0.3s;
}

.trigger:hover {
  color: #1890ff;
}

.header-center {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 24px;
}

.breadcrumb {
  font-size: 14px;
}

.quick-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.action-icon {
  font-size: 18px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  padding: 4px;
  border-radius: 4px;
}

.action-icon:hover {
  color: #1890ff;
  background: rgba(24, 144, 255, 0.1);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 24px;
}

.search-box {
  width: 240px;
}

.search-box :deep(.ant-input-affix-wrapper) {
  border-radius: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.3s;
}

.user-info:hover {
  background: rgba(0, 0, 0, 0.025);
}

.user-details {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.user-name {
  font-weight: 500;
  font-size: 14px;
}

.user-role {
  font-size: 12px;
  color: #999;
}

.dropdown-icon {
  font-size: 12px;
  color: #999;
}

.user-menu {
  width: 160px;
}

.user-menu .logout-item {
  color: #ff4d4f;
}

/* 侧边栏样式 */
.admin-sider {
  box-shadow: 2px 0 8px rgba(0, 21, 41, 0.08);
  overflow: auto;
}

.menu-container {
  padding: 16px 0;
}

.menu-title {
  padding: 0 24px 16px;
  color: rgba(255, 255, 255, 0.65);
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.admin-menu {
  border-right: none;
}

.admin-menu :deep(.ant-menu-item),
.admin-menu :deep(.ant-menu-submenu-title) {
  margin: 4px 0;
  height: 40px;
  line-height: 40px;
}

.admin-menu :deep(.ant-menu-item:hover),
.admin-menu :deep(.ant-menu-submenu-title:hover) {
  background: rgba(255, 255, 255, 0.1);
}

.sider-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
}

.system-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: rgba(255, 255, 255, 0.65);
  font-size: 12px;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #ff4d4f;
}

.status-dot.online {
  background: #52c41a;
}

/* 主要内容区域样式 */
.main-content {
  background: #f0f2f5;

}

.tab-nav {
  background: white;
  padding: 0 24px;
  border-bottom: 1px solid #f0f0f0;
}

.content-wrapper {
  padding: 24px;
  overflow: auto;
}

.page-container {
  min-height: calc(100vh - 148px);
  display: flex;
  flex-direction: column;
}

.page-header {
  background: white;
  padding: 16px 24px;
  margin-bottom: 16px;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-title h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #1f1f1f;
}

.page-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-content {
  flex: 1;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
}

/* 过渡动画 */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

/* 底部样式 */
.admin-footer {
  background: transparent;
  padding: 16px 24px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: rgba(0, 0, 0, 0.45);
  font-size: 14px;
}

.footer-content a {
  color: rgba(0, 0, 0, 0.45);
  transition: color 0.3s;
}

.footer-content a:hover {
  color: #1890ff;
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .admin-header {
    padding: 0 16px;
  }

  .header-center {
    display: none;
  }

  .search-box {
    display: none;
  }

  .content-wrapper {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .page-actions {
    width: 100%;
    justify-content: flex-end;
  }

  .footer-content {
    flex-direction: column;
    gap: 8px;
    text-align: center;
  }
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .admin-header {
    background: #141414;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3);
  }

  .page-header,
  .page-content {
    background: #1f1f1f;
    color: rgba(255, 255, 255, 0.85);
  }

  .page-title h2 {
    color: rgba(255, 255, 255, 0.85);
  }

  .footer-content,
  .footer-content a {
    color: rgba(255, 255, 255, 0.45);
  }
}
</style>