---
name: dish-image-upload-url
overview: 将菜品图片存储方案从 Base64/byte[] 改造为 URL 字符串方案（与头像上传一致），涉及后端3个Java类、前端2个组件（AddDish.vue改造 + EditDish.vue新建），以及 DishDetailPage.vue 的图片渲染方式更新。
todos:
  - id: backend-field-change
    content: 修改后端 DishDTO、Dish、DishVO 三处 imgUrl 字段类型 byte[] 改为 String
    status: completed
  - id: frontend-api
    content: 在 dish.ts 中新增 uploadDishImage 函数
    status: completed
  - id: adddish-update
    content: 改造 AddDish.vue 和 Update.vue 图片上传逻辑为 URL 方案
    status: completed
    dependencies:
      - frontend-api
  - id: detail-page-update
    content: 改造 DishDetailPage.vue 图片渲染从 Base64 改为直接使用 URL
    status: completed
    dependencies:
      - backend-field-change
---

## 用户需求

将菜品（Dish）图片存储方式从 Base64 字节数组改造为 URL 字符串方案，与头像上传保持一致。

## 产品概述

菜品图片上传采用"两步上传"：先调 `/common/upload` 获取图片 URL，再将 URL 随表单一起提交保存。前端展示时直接用 URL 渲染，不再拼接 Base64 前缀。

## 核心功能

- **后端字段改造**：`DishDTO`、`Dish` 实体、`DishVO` 三处 `imgUrl` 字段类型从 `byte[]` 改为 `String`
- **AddDish.vue 改造**：上传图片时调用 `/common/upload`，获取 URL 写入 `formState.imgUrl`，本地即时预览；提交时直接传 URL 字符串
- **Update.vue（编辑菜品）改造**：加载已有菜品时直接用 URL 渲染图片；更换图片时同样走上传接口；提交时传 URL 字符串
- **DishDetailPage.vue 改造**：图片渲染从 `` `data:image/jpeg;base64,${dish.imgUrl}` `` 改为直接 `:src="dish.imgUrl"`；推荐菜品区域图片渲染同步修正
- **dish.ts API 新增**：新增 `uploadDishImage` 函数，复用 `/common/upload` 接口

## 技术栈

- **前端**：Vue 3 Composition API + TypeScript，Ant Design Vue，复用已有 `myAxios` 请求封装
- **后端**：Spring Boot + MyBatis-Plus，仅改字段类型，无逻辑改动

## 实现思路

与头像上传完全一致的两步模式：

1. 用户选择图片 → 前端即时 `URL.createObjectURL` 本地预览
2. 同时调用 `POST /common/upload`（multipart/form-data）→ 返回服务器 URL → 写入 `formState.imgUrl`
3. 表单提交时 `imgUrl` 已是字符串 URL，直接传给后端

**关键决策**：

- 后端 `imgUrl` 字段类型由 `byte[]` 改为 `String`，`BeanUtils.copyProperties` 在 `addDish`/`updateDish`/`getDishById`/`convertToVOList` 中均无需任何逻辑改动，字段名相同自动映射
- 数据库 `dish.imgUrl` 列类型需同步从 `BLOB/MEDIUMBLOB` 改为 `VARCHAR(500)`（DDL 变更由用户自行执行）
- `Update.vue` 编辑页回显时直接将后端返回的 URL 字符串赋值给 `formState.imgUrl`，不再需要 `getImageUrl` 函数做 Base64 转换

## 实施注意事项

- `uploadDishImage` 函数与 `uploadAvatar` 逻辑完全一致，都调用 `/common/upload`，直接复用接口，仅函数名不同以区分语义
- `AddDish.vue` 上传图片采用"上传立即触发接口"而非"提交时才上传"模式，上传失败应回滚本地预览并提示用户
- `Update.vue` 中 `getImageUrl` 辅助函数在改造后可直接删除，回显逻辑简化为直接赋值
- `DishDetailPage.vue` 第 195 行推荐菜品区域同样有 Base64 渲染，需一并修正
- 后端三个 Java 文件仅改字段类型，`@AllArgsConstructor` 的有参构造参数顺序会变化，但项目中均用 `BeanUtils.copyProperties` 或 setter 赋值，无影响

## 目录结构

```
后端：
src/main/java/com/leese/usercenter/
├── model/dto/DishDTO.java          # [MODIFY] imgUrl: byte[] → String
├── model/entity/Dish.java          # [MODIFY] imgUrl: byte[] → String
└── model/vo/DishVO.java            # [MODIFY] imgUrl: byte[] → String

前端：
src/
├── api/dish.ts                     # [MODIFY] 新增 uploadDishImage 函数，调用 /common/upload
├── page/employee/dish/
│   ├── AddDish.vue                 # [MODIFY] 图片上传改为调 uploadDishImage，本地预览 + URL 写入 formState
│   └── Update.vue                  # [MODIFY] 回显改为直接用 URL，上传改为调 uploadDishImage，删除 getImageUrl
└── page/user/dish/
    └── DishDetailPage.vue          # [MODIFY] 图片渲染从 Base64 拼接改为直接 :src="dish.imgUrl"，推荐菜品区同步修正
```