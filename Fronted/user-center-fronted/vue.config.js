// 引入 Vue CLI 提供的 defineConfig 工具函数
const { defineConfig } = require("@vue/cli-service");

// 使用 defineConfig 包裹配置对象并导出
module.exports = defineConfig({
  transpileDependencies: true,
  // lintOnSave: false,

  // 配置開發伺服器代理
  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:8080", // 後端 Spring Boot 地址
        changeOrigin: true,              // 避免 CORS 問題
        pathRewrite: { "^/api": "/api" } // 保持路徑一致
      }
    }
  }
});
