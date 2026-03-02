---
name: ImageUploadFeature
overview: 实现一个简单通用的图片上传与存储功能，使用本地文件系统存储图片，并提供 Web 访问接口。方案包含文件上传接口开发和静态资源映射配置，支持跨域访问，适用于用户头像、菜品图片等多种场景。
todos:
  - id: create-web-mvc-config
    content: 创建 WebMvcConfig 配置类，实现静态资源映射 (/common/download/** -> 本地 uploads 目录)
    status: completed
  - id: create-common-controller
    content: 创建 CommonController 通用接口，实现文件上传与保存逻辑
    status: completed
    dependencies:
      - create-web-mvc-config
  - id: verify-app-yml
    content: 检查 application.yml 文件上传大小限制 (max-file-size)
    status: completed
---

## 功能概述 (Functional Overview)

本方案将实现一个通用的**本地文件存储系统**，主要包含以下功能：

1.  **通用文件上传 (Universal Upload)**

    - 提供一个统一的接口 `/common/upload`，接收前端上传的图片文件。
    - 自动将图片重命名（防止文件名冲突）并保存到服务器根目录下的 `uploads` 文件夹中。
    - 返回图片的访问路径（URL），供前端后续使用。

2.  **静态资源映射 (Static Resource Mapping)**

    - 配置服务器规则，将 `/common/download/**`（或类似路径）映射到本地磁盘的 `uploads` 文件夹。
    - 确保前端可以通过 URL 直接访问和显示已上传的图片。

3.  **跨域支持 (CORS Support)**

    - 配合现有的跨域配置，确保前端（如 React/Vue）可以无障碍地调用上传接口和访问图片资源。

4.  **模块复用 (Reusability)**

    - 设计为通用模块，不仅用于用户头像，未来 `Dish`（菜品）或其他模块均可直接调用此接口上传图片。

## 适用场景

- 用户头像上传
- 菜品图片上传
- 任何需要存储图片并回显的业务场景

## 技术栈

- **Spring Boot Web**: 提供 RESTful API 和静态资源映射能力。
- **Java IO**: 处理文件的读写和目录创建。
- **Spring Configuration**: 配置资源处理器 (`WebMvcConfigurer`)。

## 实现细节

### 核心组件

1.  **CommonController**:

    - 位置：`controller/common` 包
    - 职责：处理 `/common/upload` 请求，保存文件，返回 URL。

2.  **WebMvcConfig**:

    - 位置：`config` 包
    - 职责：实现 `WebMvcConfigurer` 接口，配置 `addResourceHandlers`，将 URL 路径映射到本地文件系统路径。

3.  **CorsConfig** (现有):

    - 职责：继续使用项目中已有的 `CorsFilter` 处理跨域请求，无需重复配置，但需确保其覆盖所有路径。

### 目录结构调整

建议在 `user-center-backend` 项目根目录下自动创建 `uploads` 文件夹，而不是放在 `src` 源码目录中，以避免重新编译时文件丢失。