// 引入 Vue CLI 提供的 defineConfig 工具函数
// 它的主要作用是：提供类型提示（在支持 TypeScript/JSX 的编辑器中），
// 并确保配置对象符合 Vue CLI 的规范（虽然 JS 中不强制，但推荐使用）
const { defineConfig } = require("@vue/cli-service");
// 使用 defineConfig 包裹配置对象并导出
// 这样做可以让 VS Code 等编辑器更好地识别配置项（智能提示、跳转等）
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: "false",
});
