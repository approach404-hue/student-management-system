# 学生管理系统

## 一、项目介绍

本项目是一个基于 **Spring Boot + Vue 3** 的前后端分离学生管理系统，主要用于管理学生信息。

系统实现了学生信息的增删改查、分页查询、用户注册登录、JWT 登录鉴权、BCrypt 密码加密、ADMIN / USER 角色权限控制、管理员用户管理、路由守卫、404 页面和 Element Plus 后台管理界面。

项目分为前端和后端两部分：

* 前端：Vue 3 + Vue Router + Pinia + Axios + Element Plus + Vite
* 后端：Spring Boot + MyBatis-Plus + MySQL + JWT + BCrypt

---

## 二、功能模块

### 1. 学生管理模块

* 学生列表分页查询
* 按姓名搜索学生
* 按专业搜索学生
* 新增学生
* 修改学生信息
* 删除学生
* Element Plus 表格展示
* Element Plus 分页组件
* Element Plus 弹窗表单
* Element Plus 表单校验

### 2. 用户认证模块

* 用户注册
* 用户登录
* BCrypt 密码加密存储
* 登录成功后返回 JWT token
* 前端保存 token 和用户信息
* 前端 request 请求自动携带 token
* token 无效或过期后自动跳转登录页
* 退出登录

### 3. 权限控制模块

* ADMIN 管理员：可以查询、新增、修改、删除学生
* USER 普通用户：只能查看学生信息
* 前端根据角色隐藏操作按钮
* 后端根据角色拦截接口
* 401：未登录或 token 无效
* 403：已登录但权限不足

### 4. 用户管理模块

* 管理员查看所有用户
* 管理员修改其他用户角色
* 普通用户不能访问用户管理页面
* 用户列表不返回密码字段
* 管理员不能修改自己的角色

### 5. 路由与页面模块

* 登录页
* 注册页
* 学生管理页
* 用户管理页
* 项目说明页
* 404 页面
* 路由守卫
* 已登录用户访问登录 / 注册页时自动跳转学生管理页

---

## 三、技术栈

### 前端技术

* Vue 3
* Vue Router
* Pinia
* Axios
* Element Plus
* Vite

### 后端技术

* Spring Boot
* MyBatis-Plus
* MySQL
* JWT
* BCrypt
* Maven

---

## 四、项目亮点

* 使用 JWT 实现前后端分离登录鉴权
* 使用 BCrypt 对密码进行加密存储
* 使用 Pinia 统一管理登录状态
* 使用 Axios 请求拦截器自动携带 token
* 使用 Axios 响应拦截器统一处理 token 失效
* 使用后端 JwtInterceptor 实现接口权限控制
* 使用 DTO 避免敏感字段 password 返回前端
* 使用 Element Plus 搭建后台管理界面
* 实现 ADMIN / USER 两种角色权限
* 管理员可以在页面中管理用户角色
* 前端和后端都做了权限控制，防止用户绕过前端直接请求后端接口

---

## 五、项目目录结构

### 前端目录结构

```text
student-web
├── src
│   ├── components
│   │   ├── StudentSearch.vue       学生搜索组件
│   │   ├── StudentTable.vue        学生表格组件
│   │   └── StudentDialog.vue       学生新增 / 修改弹窗组件
│   ├── router
│   │   └── index.js                前端路由配置和路由守卫
│   ├── stores
│   │   └── userStore.js            Pinia 用户登录状态管理
│   ├── utils
│   │   └── request.js              Axios 请求封装，自动携带 token，处理 401
│   ├── views
│   │   ├── LoginView.vue           登录页面
│   │   ├── RegisterView.vue        注册页面
│   │   ├── StudentView.vue         学生管理页面
│   │   ├── UserManageView.vue      管理员用户管理页面
│   │   ├── AboutView.vue           项目说明页面
│   │   └── NotFoundView.vue        404 页面
│   ├── App.vue                     项目整体布局
│   └── main.js                     前端入口文件
├── vite.config.js                  Vite 配置，包括 /api 代理
└── package.json                    前端依赖配置
```

### 后端目录结构

```text
student-api
├── src/main/java/com/yujie/studentapi
│   ├── config
│   │   └── WebConfig.java          拦截器配置
│   ├── controller
│   │   ├── StudentController.java  学生接口
│   │   └── UserController.java     用户注册、登录、用户管理接口
│   ├── dto
│   │   ├── LoginResponse.java      登录响应 DTO
│   │   ├── UserResponse.java       用户列表响应 DTO
│   │   └── UpdateRoleRequest.java  修改角色请求 DTO
│   ├── entity
│   │   ├── Student.java            学生实体类
│   │   └── User.java               用户实体类
│   ├── exception
│   │   ├── BusinessException.java  业务异常
│   │   └── GlobalExceptionHandler.java 全局异常处理
│   ├── interceptor
│   │   └── JwtInterceptor.java     JWT 登录鉴权和权限拦截器
│   ├── mapper
│   │   ├── StudentMapper.java      学生数据库操作
│   │   └── UserMapper.java         用户数据库操作
│   ├── service
│   │   ├── StudentService.java     学生业务逻辑
│   │   └── UserService.java        用户业务逻辑
│   ├── utils
│   │   └── JwtUtil.java            JWT 生成和解析工具类
│   └── StudentApiApplication.java  Spring Boot 启动类
├── src/main/resources
│   └── application.yml             后端配置文件
└── pom.xml                         Maven 依赖配置
```

---

## 六、核心流程说明

### 1. 登录流程

```text
用户在前端登录页输入用户名和密码
↓
前端通过 Axios / request 请求后端 /users/login
↓
后端根据用户名查询数据库
↓
使用 BCrypt 校验密码
↓
密码正确后，后端生成 JWT token
↓
后端返回用户信息和 token
↓
前端使用 Pinia 保存 user 和 token
↓
前端跳转到学生管理页面
```

---

### 2. 请求携带 token 流程

```text
用户登录成功后，前端保存 token
↓
前端访问学生接口或用户管理接口
↓
request.js 请求拦截器从 localStorage / userStore 中取出 token
↓
把 token 放入请求头 Authorization
↓
后端 JwtInterceptor 拦截请求
↓
解析并验证 token
↓
验证通过后放行进入 Controller
```

---

### 3. token 失效处理流程

```text
前端请求后端接口
↓
后端发现 token 无效、过期或格式错误
↓
后端返回 HTTP 401
↓
request.js 响应拦截器统一处理 401
↓
前端清除 user 和 token
↓
跳转到登录页
```

---

### 4. 权限校验流程

```text
前端请求受保护接口
↓
后端 JwtInterceptor 解析 token
↓
从 token 中获取当前用户 role
↓
判断当前接口是否需要 ADMIN 权限
↓
权限足够：放行
↓
权限不足：返回 403
```

权限规则：

```text
/students GET 请求：USER 和 ADMIN 都可以访问
/students POST / PUT / DELETE 请求：只有 ADMIN 可以访问
/users 用户管理接口：只有 ADMIN 可以访问
```

---

### 5. 管理员修改用户角色流程

```text
管理员进入用户管理页面
↓
前端请求 GET /users 查询用户列表
↓
后端返回不包含 password 的用户列表
↓
管理员选择某个用户的新角色
↓
前端请求 PUT /users/{id}/role
↓
后端 JwtInterceptor 判断当前用户是否为 ADMIN
↓
UserService 校验角色是否合法
↓
UserService 校验用户是否存在
↓
UserService 校验是否正在修改自己
↓
调用 userMapper.updateById() 更新数据库
↓
返回角色修改成功
```

---

## 七、主要接口说明

### 用户认证接口

| 请求方式 | 接口地址              | 说明   | 权限   |
| ---- | ----------------- | ---- | ---- |
| POST | `/users/register` | 用户注册 | 无需登录 |
| POST | `/users/login`    | 用户登录 | 无需登录 |

### 学生管理接口

| 请求方式   | 接口地址             | 说明     | 权限           |
| ------ | ---------------- | ------ | ------------ |
| GET    | `/students/page` | 分页查询学生 | USER / ADMIN |
| POST   | `/students`      | 新增学生   | ADMIN        |
| PUT    | `/students/{id}` | 修改学生   | ADMIN        |
| DELETE | `/students/{id}` | 删除学生   | ADMIN        |

### 用户管理接口

| 请求方式 | 接口地址               | 说明     | 权限    |
| ---- | ------------------ | ------ | ----- |
| GET  | `/users`           | 查询用户列表 | ADMIN |
| PUT  | `/users/{id}/role` | 修改用户角色 | ADMIN |

---

## 八、核心概念问答

### 1. 这个项目是干什么的？

本项目是一个基于 Spring Boot + Vue 3 的前后端分离学生管理系统，主要用于管理学生信息。系统支持学生列表分页查询、按姓名和专业搜索、新增学生、修改学生、删除学生等功能。

除了学生管理功能外，项目还实现了用户注册登录、JWT 登录鉴权、BCrypt 密码加密、ADMIN / USER 角色权限控制、管理员用户管理、路由守卫、404 页面和 Element Plus 后台管理界面。

---

### 2. 前端使用了哪些技术？

前端主要使用了 Vue 3、Vue Router、Pinia、Axios、Element Plus 和 Vite。

Vue 3 用于构建前端页面和组件；Vue Router 负责页面路由跳转和路由守卫；Pinia 用于统一管理登录用户和 token；Axios 用于向后端发送请求并接收响应；Element Plus 用于搭建后台管理界面；Vite 用于启动前端开发服务器、配置代理和打包项目。

---

### 3. 后端使用了哪些技术？

后端主要使用了 Spring Boot、MyBatis-Plus、MySQL、JWT、BCrypt 和 Maven。

Spring Boot 用于搭建后端接口服务；MyBatis-Plus 用于简化数据库增删改查操作；MySQL 用于保存用户和学生数据；JWT 用于实现前后端分离登录鉴权；BCrypt 用于对用户密码进行加密存储；Maven 用于管理后端依赖。

---

### 4. Axios 在项目里做了什么？

Axios 是前端用来向后端发送 HTTP 请求、接收后端响应的工具。

在本项目中，Axios 被进一步封装成 `request.js`。封装后的 request 主要负责统一请求地址、自动携带 token、处理 token 失效和统一处理接口错误。

例如学生列表查询、新增学生、修改学生、删除学生、查询用户列表、修改用户角色等操作，都是前端通过 request 向后端发送请求完成的。

---

### 5. request 和 Axios 是什么关系？

Axios 是原始请求工具，request 是基于 Axios 封装出来的新请求工具。

以前可以直接写：

```js
axios.get('/api/users')
```

后来项目中封装了 request：

```js
const request = axios.create({
  baseURL: '/api',
  timeout: 5000
})
```

所以现在可以写：

```js
request.get('/users')
```

request 会自动补上 `/api` 前缀，并且在请求发出前自动携带 token。

简单理解：

```text
axios：原始请求工具
request：封装后的请求工具
response / res：后端返回后的响应结果
response.data：后端真正返回的 JSON 数据
```

---

### 6. Axios 的请求拦截器和响应拦截器分别做什么？

请求拦截器是在请求发送到后端之前执行的。

本项目中，请求拦截器主要负责从本地存储中取出 token，并把 token 放入请求头 Authorization 中。

```text
请求发出前
↓
读取 token
↓
添加 Authorization 请求头
↓
发送给后端
```

响应拦截器是在后端响应返回之后执行的。

本项目中，响应拦截器主要负责统一处理 401。如果后端返回 401，说明 token 无效、过期或格式错误，前端会自动清除登录状态并跳转登录页。

```text
后端返回响应
↓
如果是 401
↓
清除 user 和 token
↓
跳转登录页
```

---

### 7. Vite 在项目里做了什么？

Vite 是前端项目的开发和构建工具。

在开发阶段，Vite 负责启动 Vue 项目，提供本地访问地址，例如 `http://localhost:5173`，并支持热更新。修改前端代码后，浏览器页面可以快速刷新。

Vite 还负责配置前端代理。本项目中，前端请求 `/api` 开头的地址时，会通过 Vite 代理转发到后端 Spring Boot 服务，例如转发到 `http://localhost:8080`。

在部署阶段，可以通过 `npm run build` 使用 Vite 将 Vue 项目打包成 `dist` 文件，用于后续部署到 Nginx。

---

### 8. 普通用户和管理员有什么区别？

系统中有两种角色：USER 和 ADMIN。

USER 是普通用户，只能查看学生信息，不能新增、修改、删除学生，也不能访问用户管理页面。

ADMIN 是管理员，可以查看学生信息，也可以新增、修改、删除学生，还可以进入用户管理页面，查看所有用户并修改其他用户的角色。

前端会根据当前用户角色隐藏或显示对应按钮，后端也会通过 JWT 拦截器进行权限校验。即使普通用户绕过前端直接请求后端接口，也会被后端拒绝。

---

### 9. JWT 在项目里做了什么？

JWT 是一种登录凭证格式，不是拦截器本身。

用户登录成功后，后端会生成 JWT token，并返回给前端。前端保存 token，后续请求学生接口或用户管理接口时，会把 token 放到请求头 Authorization 中发送给后端。

后端的 JwtInterceptor 会拦截请求，读取请求头中的 token，并使用 JwtUtil 解析和验证 token。如果 token 正确且未过期，就继续判断用户权限；如果 token 无效、过期或格式错误，就返回 401；如果用户已登录但权限不足，就返回 403。

---

### 10. Pinia 在项目里做了什么？

Pinia 是 Vue 的状态管理工具。

本项目中，Pinia 主要用于统一管理当前登录用户和 token。登录成功后，通过 `userStore.setLoginInfo()` 保存用户信息和 token；退出登录时，通过 `userStore.logout()` 清除登录状态；页面顶部、权限判断和用户信息展示都可以从 userStore 中获取当前登录状态。

使用 Pinia 后，登录状态不再分散在多个页面中直接操作 localStorage，而是集中到 userStore 里统一管理，代码结构更加清晰。

---

### 11. DTO 为什么不直接返回 User 实体类？

数据库中的 User 实体类包含 id、username、password、role 等字段。如果直接把 User 返回给前端，password 字段可能也会被一起返回，存在安全风险。

因此项目中定义了 `UserResponse` DTO，只包含 id、username 和 role，不包含 password。

后端查询到数据库中的 User 后，会将 User 转换成 UserResponse，再返回给前端。这样既能满足前端展示用户信息的需求，又能避免泄露用户密码。

---

### 12. 401 和 403 有什么区别？

401 表示身份认证失败，常见情况包括没有 token、token 格式错误、token 无效或 token 已过期。

403 表示身份认证成功，但是权限不足。比如普通用户已经登录了，但是想访问管理员用户管理接口，或者想新增、修改、删除学生，就会被后端返回 403。

简单理解：

```text
401：你没有有效身份
403：我知道你是谁，但你没有权限做这件事
```

---

### 13. BCrypt 为什么不能直接用 equals 比较密码？

注册时，用户输入的明文密码会被 BCrypt 加密后存入数据库。

例如用户输入：

```text
123456
```

数据库中保存的不是 `123456`，而是 BCrypt 加密后的字符串。

登录时，前端传来的仍然是明文密码，数据库里保存的是加密后的密码，所以不能使用 `equals()` 直接比较。

正确做法是使用：

```java
passwordEncoder.matches(用户输入的明文密码, 数据库中的加密密码)
```

BCrypt 会根据数据库中的加密密码和用户输入的明文密码进行匹配判断，匹配成功才允许登录。

---

## 九、启动方式

### 1. 后端启动

进入后端项目目录：

```bash
cd student-api
```

修改 `application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/student_db
    username: root
    password: 你的数据库密码
```

启动 Spring Boot 项目：

```bash
mvn spring-boot:run
```

后端默认运行地址：

```text
http://localhost:8080
```

---

### 2. 前端启动

进入前端项目目录：

```bash
cd student-web
```

安装依赖：

```bash
npm install
```

启动前端项目：

```bash
npm run dev
```

前端默认运行地址：

```text
http://localhost:5173
```

---

## 十、测试账号

可以根据自己的数据库实际情况创建测试账号。

示例：

```text
管理员账号：
username: admin
password: 123456
role: ADMIN

普通用户账号：
username: user01
password: 123456
role: USER
```

说明：

* 注册的新用户默认是 USER
* 管理员可以在用户管理页面修改其他用户角色
* 管理员不能修改自己的角色

---

## 十一、测试重点

### 1. 登录注册测试

* 用户可以正常注册
* 注册后密码在数据库中是 BCrypt 加密后的字符串
* 用户可以正常登录
* 密码错误时返回错误提示
* 登录成功后前端保存 token 和用户信息

### 2. 学生管理测试

* USER 可以查看学生列表
* USER 看不到新增、编辑、删除按钮
* ADMIN 可以新增学生
* ADMIN 可以修改学生
* ADMIN 可以删除学生
* 分页、搜索、重置功能正常

### 3. 权限测试

* 未登录访问 `/students` 会跳转登录页
* 普通用户访问 `/users` 会被拒绝
* 普通用户直接请求新增、修改、删除接口会返回 403
* token 无效或过期时返回 401
* 已登录用户访问 `/login` 或 `/register` 会自动跳转 `/students`

### 4. 用户管理测试

* ADMIN 可以访问用户管理页面
* ADMIN 可以查看用户列表
* ADMIN 可以修改其他用户角色
* ADMIN 不能修改自己的角色
* USER 无法访问用户管理页面

---

## 十二、后续优化方向

* 接入 Redis，实现 JWT token 黑名单
* 退出登录时将 token 加入 Redis 黑名单
* 增加登录验证码
* 增加登录失败次数限制
* 项目部署到服务器
* Vue 前端打包并部署到 Nginx
* Spring Boot 后端打 jar 包部署
* 增加文件上传功能
* 增加 Excel 导入导出
* 增加日志记录
* 增加更完整的接口文档
* 使用 TypeScript 优化前端类型
* 使用动态路由实现更完整的权限菜单
## Redis token 黑名单功能

### 功能说明

本项目接入 Redis 实现 JWT token 黑名单功能，用于解决 JWT 退出登录后旧 token 在过期前仍然可用的问题。

在未接入 Redis 黑名单之前，用户退出登录时，前端只是删除本地保存的 token，后端并不知道该 token 已经退出。如果旧 token 被再次使用，只要它本身还没有过期，后端仍可能认为它有效。

接入 Redis 黑名单后，用户退出登录时，前端会请求后端 logout 接口，后端会把当前 token 存入 Redis 黑名单，并设置过期时间为 token 剩余有效时间。之后所有受保护接口都会先经过 JwtInterceptor，拦截器会检查当前 token 是否存在于 Redis 黑名单中。如果存在，则返回 401，拒绝访问。

---

### Redis 在本项目中的作用

Redis 主要用于保存临时状态数据。

本项目中 Redis 保存的是：

```text
jwt:blacklist:当前token -> 1
```

其中：

* key：`jwt:blacklist:` + token
* value：`1`，表示该 token 已被拉黑
* 过期时间：token 剩余有效时间

value 的值不是重点，重点是 Redis 中是否存在这个 key。如果存在，说明该 token 已经退出登录，不能继续访问系统。

---

### token 黑名单流程

```text
用户点击退出登录
↓
前端调用 POST /users/logout
↓
request 请求拦截器自动携带 Authorization: Bearer token
↓
后端 UserController.logout() 获取当前 token
↓
JwtUtil.parseToken(token) 解析 token
↓
claims.getExpiration() 获取 token 原本过期时间
↓
过期时间 - 当前时间 = token 剩余有效时间
↓
后端将 token 存入 Redis 黑名单
↓
Redis 设置 TTL，时间为 token 剩余有效时间
↓
前端清除本地 user 和 token
↓
跳转登录页
```

---

### 拦截器校验流程

```text
前端请求受保护接口
↓
JwtInterceptor 获取 Authorization 请求头
↓
截取 Bearer 后面的 token
↓
拼接 Redis 黑名单 key：jwt:blacklist:token
↓
检查 Redis 中是否存在该 key
↓
如果存在：返回 401，提示 token 已退出登录
↓
如果不存在：继续解析 token
↓
token 有效后继续判断用户权限
↓
权限通过后进入 Controller
```

---

### 为什么 Redis 黑名单要设置过期时间？

JWT token 本身已经有过期时间。

例如 token 有效期为 24 小时，用户在登录后 1 小时退出，此时 token 还剩 23 小时有效期。后端会把该 token 加入 Redis 黑名单，并设置 Redis key 的过期时间为 23 小时。

这样做的原因是：

```text
token 到期后本身就已经无效
Redis 没必要永久保存这个黑名单记录
到期后 Redis 自动删除，避免黑名单无限增长
```

Redis 的 `ttl` 命令可以查看某个 key 还剩多少秒过期。

---

### Docker 启动 Redis

本项目开发环境使用 Docker 启动 Redis。

启动命令：

```bash
docker run -d --name redis-dev -p 6379:6379 redis
```

命令说明：

```text
docker run        创建并启动容器
-d                后台运行
--name redis-dev  容器名称为 redis-dev
-p 6379:6379      将本机 6379 端口映射到容器 6379 端口
redis             使用 Redis 镜像
```

查看运行中的容器：

```bash
docker ps
```

进入 Redis 命令行：

```bash
docker exec -it redis-dev redis-cli
```

测试 Redis 是否可用：

```bash
ping
```

如果返回：

```text
PONG
```

说明 Redis 正常运行。

---

### Spring Boot Redis 配置

项目在 `application.properties` 中配置 Redis：

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.database=0
```

因为 Docker 启动 Redis 时已经将容器的 6379 端口映射到本机 6379 端口，所以 Spring Boot 可以通过 `localhost:6379` 连接 Redis。

---

### 相关修改文件

```text
student-api/pom.xml
引入 spring-boot-starter-data-redis 依赖

student-api/src/main/resources/application.properties
配置 Redis 地址和端口

student-api/src/main/java/com/yujie/studentapi/controller/UserController.java
新增 POST /users/logout 接口，将 token 加入 Redis 黑名单

student-api/src/main/java/com/yujie/studentapi/interceptor/JwtInterceptor.java
请求进入 Controller 前，先检查 token 是否在 Redis 黑名单中

student-api/src/main/java/com/yujie/studentapi/common/Result.java
补充 error 方法，用于统一错误响应

student-web/src/App.vue
退出登录时先调用后端 /users/logout，再清除前端登录状态
```

---

### 测试方式

1. 登录系统，获取 token。
2. 使用 token 请求学生列表，应该成功。
3. 调用 `POST /users/logout` 退出登录。
4. 使用同一个旧 token 再次请求学生列表，应该返回 401。
5. 重新登录获取新 token，新 token 应该可以正常访问接口。
6. 使用 Redis 命令查看黑名单 key：

```bash
docker exec -it redis-dev redis-cli
keys jwt:blacklist:*
ttl 你的key
```

如果旧 token 请求接口返回：

```json
{
  "code": 401,
  "message": "token已退出登录，请重新登录",
  "data": null
}
```

说明 Redis token 黑名单功能生效。
