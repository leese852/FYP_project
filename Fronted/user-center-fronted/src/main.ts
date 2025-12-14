// 1. 从 Vue 核心库中导入 createApp 函数
//    这是 Vue 3 的新 API，用于创建一个 Vue 应用实例
import { createApp } from "vue";

// 2. 从 Pinia（Vue 官方推荐的状态管理库）导入 createPinia
//    Pinia 用于集中管理应用的全局状态（如用户信息、购物车等）
import { createPinia } from "pinia";

// 3. 导入根组件 App.vue
//    这是整个应用的最外层组件，所有页面都嵌套在它内部
import App from "./App.vue";

// 4. 导入路由配置（通常来自 ./router/index.ts 或类似文件）
//    路由负责根据 URL 显示不同的页面组件（如 /home → Home.vue）
import router from "./router";

// 5. 导入 Ant Design Vue 组件库（适用于 Vue 3 的企业级 UI 框架）
//    提供按钮、表格、表单等高质量组件
import Antd from "ant-design-vue";

// 6. 导入 Ant Design Vue 的基础样式重置文件
//    `reset.css` 会统一浏览器默认样式，确保 UI 在不同浏览器表现一致
//    注意：这是 Ant Design Vue 5.x+ 的新写法（旧版用的是 `antd.less` 或 `antd.css`）
import "ant-design-vue/dist/reset.css";

// 7. 创建 Pinia 实例（状态管理容器）
//    后续可以在组件中通过 `useStore()` 访问全局状态
const pinia = createPinia();

// 8. 创建 Vue 应用并链式注册插件，最后挂载到 DOM
createApp(App) // 创建以 App.vue 为根组件的应用实例
  .use(pinia) // 注册 Pinia 状态管理
  .use(Antd) // 注册 Ant Design Vue 组件库（使 <a-button> 等全局可用）
  .use(router) // 注册 Vue Router（启用路由功能）
  .mount("#app"); // 将整个应用挂载到 index.html 中 id="app" 的 DOM 元素上
