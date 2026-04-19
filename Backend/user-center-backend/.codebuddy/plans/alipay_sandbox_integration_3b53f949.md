---
name: alipay_sandbox_integration
overview: 接入支付宝沙盒环境进行电脑网站支付，引导用户获取沙盒密钥并配置；更新后端接口返回支付宝跳转表单；配置异步回调更新订单状态。
todos:
  - id: add-alipay-deps
    content: 修改 pom.xml 引入支付宝 SDK，并更新 application.yml 添加沙盒配置项
    status: completed
  - id: create-alipay-config
    content: 创建 AlipayProperties 和 AlipayConfig 类以实现配置绑定和客户端依赖注入
    status: completed
    dependencies:
      - add-alipay-deps
  - id: update-payment-dto
    content: 在 PaymentResponse 中新增 payHtml 字段携带支付页面代码
    status: completed
  - id: update-payment-service
    content: 改造 PaymentServiceImpl，接入支付宝获取 HTML 表单并完善回调订单状态更新逻辑
    status: completed
    dependencies:
      - create-alipay-config
      - update-payment-dto
  - id: add-notify-controller
    content: 在 PaymentController 中新增 /notify 接口用于接收、验签并处理支付宝异步回调
    status: completed
    dependencies:
      - update-payment-service
  - id: update-frontend-payment
    content: Use [subagent:code-explorer] 查找前端支付调用处，添加支付宝 HTML 表单的自动渲染和跳转逻辑
    status: completed
    dependencies:
      - add-notify-controller
---

## 产品需求

- **核心功能**：在项目中集成支付宝沙盒环境的电脑网站支付（PC端网页跳转支付）。
- **支付流程**：用户确认订单并选择支付宝支付 -> 后端调用支付宝 API 生成支付页面的 HTML 表单 -> 前端接收并自动提交表单跳转至支付宝页面完成付款。
- **回调处理**：支付成功后，支付宝服务器通过公网异步通知（Notify）后端，后端验签无误后，将相关支付记录状态更新为成功，并通知订单模块将订单状态流转为已支付。

## 沙盒配置引导（需手动准备的内容）

由于接入支付宝需要相应的密钥和环境，请提前按照以下步骤准备：

1. **账号注册与登录**：访问并登录[支付宝开放平台](https://open.alipay.com/)，在控制台中找到「沙盒环境」。
2. **获取基础参数**：记录下沙盒环境提供的 `APPID` 以及 `支付宝网关`（通常为 `https://openapi-sandbox.dl.alipaydev.com/gateway.do`）。
3. **配置并获取密钥**：在沙盒应用的安全设置中，获取**应用私钥**（Merchant Private Key）和**支付宝公钥**（Alipay Public Key）。
4. **准备内网穿透（关键）**：因为支付宝需要把支付结果异步通知给你的后端程序，你需要下载并运行内网穿透工具（如 cpolar、ngrok 或 natapp），将你本地的 `8080` 端口映射到公网。获取到形如 `https://xxxx.cpolar.io` 的公网域名后，用于配置后端的异步回调地址（Notify URL）。

## 技术栈与方案

### 后端方案

- **SDK 接入**：通过 `pom.xml` 引入 `com.alipay.sdk:alipay-sdk-java` 依赖。
- **配置管理**：在 `application.yml` 中新增一套支付宝属性（包含 AppID、私钥、支付宝公钥、网关、回调地址等），便于随时替换为正式环境参数。
- **配置类封装**：新增 `AlipayProperties` 绑定 YAML 配置，新增 `AlipayConfig` 根据配置初始化并注入单例 `AlipayClient`，提升性能并规范调用。
- **支付发起逻辑**：改造 `PaymentServiceImpl.processPayment` 方法，若用户选择支付宝（ALIPAY），构建 `AlipayTradePagePayRequest`。将订单 ID 作为 `out_trade_no`，订单金额作为 `total_amount`。调用 SDK 获取返回的 HTML 表单字符串，并将该字符串封装进 `PaymentResponse` 新增的 `payHtml` 字段中返回给前端。
- **异步回调逻辑**：在 `PaymentController` 中新增 `@PostMapping("/notify")` 接口。接收到支付宝的回调参数后，使用 `AlipaySignature.rsaCheckV1` 进行严谨的验签。验签通过并确认交易状态为 `TRADE_SUCCESS` 后，更新对应的 `PaymentRecord` 为 `SUCCESS`，并调用订单服务同步更新订单的支付状态，最后严格遵守支付宝规范返回纯文本 `success`。

### 前端方案

- **表单渲染与跳转**：改造前端发起结账的 API 调用处，若接口返回了 `success: true` 且存在 `payHtml` 字符串，则在页面中动态创建一个隐藏的 `div` 容器，利用 `innerHTML` 注入支付宝返回的代码，并主动调用表单的 `submit()` 方法，实现无感自动跳转。

### 涉及修改的目录结构

```text
project-root/
├── pom.xml                                           # [MODIFY] 添加 alipay-sdk-java 依赖
├── src/main/resources/application.yml                # [MODIFY] 新增支付宝参数的配置模板
├── src/main/java/com/leese/usercenter/mod5/config/
│   ├── AlipayProperties.java                         # [NEW] 支付宝配置属性绑定类
│   └── AlipayConfig.java                             # [NEW] 初始化 AlipayClient 实例
├── src/main/java/com/leese/usercenter/mod5/model/dto/PaymentResponse.java  # [MODIFY] 新增 payHtml 字段
├── src/main/java/com/leese/usercenter/mod5/service/impl/PaymentServiceImpl.java # [MODIFY] 替换模拟逻辑，接入 SDK 获取 HTML 表单和回调处理
├── src/main/java/com/leese/usercenter/mod5/controller/PaymentController.java    # [MODIFY] 新增 /notify 异步回调接口
└── user-center-fronted/src/page/...                  # [MODIFY] 待搜索定位的前端页面，增加 HTML 表单自动跳转逻辑
```

## Agent Extensions

### SubAgent

- **code-explorer**
- Purpose: 搜索前端目录，定位实际调用发起支付请求（如 `/payments/process`）的页面组件或 TypeScript 文件
- Expected outcome: 准确找出触发支付逻辑的前端代码位置，确保能在正确的组件中插入动态渲染支付宝 HTML 表单并提交跳转的逻辑