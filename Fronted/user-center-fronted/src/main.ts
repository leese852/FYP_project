// 1. 从 Vue 核心库中导入 createApp 函数
import { createApp } from "vue";

// 2. 从 Pinia 导入 createPinia
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";
import Antd from "ant-design-vue";
import "ant-design-vue/dist/reset.css";
import './assets/global.css';  // 🔥 引入全局橙色主题

window.addEventListener('error', e => {
    if (e.message && e.message.includes('ResizeObserver')) {
        e.stopImmediatePropagation();
        return true;
    }
});

const pinia = createPinia();

createApp(App)
    .use(pinia)
    .use(Antd)
    .use(router)
    .mount("#app");