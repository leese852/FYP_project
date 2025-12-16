## 二、使用步骤

下载完源代码并分别在 vscode 和 IDEA 打开项目
记得将数据库打开，并在 IDEA 中连接数据库

前端的話需要在終端輸入 npm install
完成之後你會發現多了 node_modules 文件

## 技术选型

前端：三件套 + vue + 组件库 Ant Design Pro + vant（选一个就好了）

后端：
- java
- spring（依赖注入框架，帮助你管理 Java 对象，集成一些其他的内容）
- springmvc（web 框架，提供接口访问、restful接口等能力）
- mybatis（Java 操作数据库的框架，持久层框架，对 jdbc 的封装）
- mybatis-plus（对 mybatis 的增强，不用写 sql 也能实现增删改查）
- springboot（**快速启动** / 快速集成项目。不用自己管理 spring 配置，不用自己整合各种框架） 
  - 参考：更详细的可以询问ai：可以給我講爲很麽java項目我要用spring和其配件的理由

<!-- - junit 单元测试库 忘了--> 
- mysql 数据库
- 部署：服务器 / 容器（平台）

### Java后端结构

src/main/java/com/yourproject/
├── common/          # 返回结果一致性
├── config/          # 配置文件类
├── controller/      # 控制层 - 接收请求，返回响应  ！！！类似工厂模式的上游，调用service
├── service/         # 服务层 - 业务逻辑处理  ！！！功能代码实际实现的地方
│   └── impl/       # 服务实现类
├── mapper/          # 数据访问层（MyBatis接口）
├── entity or model/ # 实体类，对应数据库表
├── dto/             # 数据传输对象
├── vo/              # 视图对象，用于返回给前端 去敏
├── utils/           # 工具类
└── Application.java # Spring Boot 启动类

src/main/resources/
├── application.yml           # 主配置文件 ！！！
<!-- ├── application-dev.yml       # 开发环境配置 -->
<!-- ├── application-prod.yml      # 生产环境配置 -->
├── mapper/                   # MyBatis XML 映射文件
└── static/                   # 静态资源

- controller 层倾向于对请求参数本身的校验，不涉及业务逻辑本身（越少越好）
  - 职责：接收客户端（如浏览器、App、其他服务）的 HTTP 请求，解析参数，调用相应的 service 层方法，并将处理结果封装成响应（如 JSON、页面等）返回。
- service 层
  - 职责：实现具体的业务逻辑，比如用户注册、订单创建、数据校验、事务管理等。
