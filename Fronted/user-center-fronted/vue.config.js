// 引入 Vue CLI 提供的 defineConfig 工具函数
const { defineConfig } = require("@vue/cli-service");

// 使用 defineConfig 包裹配置对象并导出
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,

  chainWebpack: (config) => {
    // 修复路径中带有括号导致 copy-webpack-plugin 的 ignore glob 失效的问题
    config.plugin("copy").tap((args) => {
      if (args[0] && args[0].patterns && args[0].patterns[0]) {
        args[0].patterns[0].globOptions.ignore = [
          "**/.DS_Store",
          "**/index.html", // 改为通配符忽略，避免绝对路径带有 () 导致正则匹配失败
        ];
      }
      return args;
    });
  },

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
