# 营销活动短链数据分析平台（AI 投放助手）

这是一个面向 Java 后端实习投递的差异化项目。用户可以创建营销投放活动，为不同活动生成短链接，系统负责短码生成、短链跳转、Redis 缓存、访问限流、访问日志记录、活动维度统计，并提供 AI 投放文案生成能力。

相比普通 CRUD 管理系统，本项目更强调后端基础设施能力：短码生成、缓存、重定向、限流、访问日志和统计分析；同时保留 Java + AI 应用方向的亮点。

## 技术栈

后端：

- Java 21
- Spring Boot 3.2
- MyBatis-Plus 3.5
- MySQL 8
- Redis
- JWT / BCrypt
- DeepSeek API（可选，未配置时走本地模板兜底）
- Maven

前端：

- Vue 3
- Vite
- Element Plus
- Axios
- Vue Router

## 核心功能

- 用户注册、登录、JWT 登录态校验
- 创建投放活动，为活动创建短链接
- Base62 短码生成
- 短链接列表、搜索、启用/禁用
- `/s/{shortCode}` 短链跳转
- Redis 缓存短码和原始链接映射
- Redis 基于 IP + 短码的简单限流
- 访问日志持久化
- 总链接数、总访问量、今日访问量、Top 链接统计
- 活动维度短链数与访问量统计
- AI 投放助手：根据目标链接、人群和卖点生成推广标题、短文案、渠道建议和风险提示

## 项目结构

```text
shortlink-analytics
├── backend              Spring Boot 后端
├── frontend             Vue 管理台
└── database             MySQL 脚本
```

## 启动步骤

### 1. 初始化数据库

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/init_data.sql
```

默认账号：

```text
admin / 123456
```

### 2. 启动 Redis

默认连接：

```text
localhost:6379
```

### 3. 配置环境变量（可选）

后端默认使用本地 MySQL、Redis 和本地 AI 模板兜底。如果你的环境不同，可以配置：

```powershell
$env:MYSQL_URL="jdbc:mysql://localhost:3306/shortlink_analytics?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai"
$env:MYSQL_USERNAME="root"
$env:MYSQL_PASSWORD="你的MySQL密码"
$env:REDIS_HOST="localhost"
$env:REDIS_PORT="6379"
$env:JWT_SECRET="请替换成至少32字符的随机字符串"
$env:DEEPSEEK_API_KEY="你的DeepSeek API Key"
```

> 未配置 `DEEPSEEK_API_KEY` 时，AI 投放助手会使用本地模板兜底，项目仍可正常演示。

### 4. 启动后端

```powershell
cd backend
mvn spring-boot:run
```

后端地址：

```text
http://localhost:8090
```

### 5. 启动前端

```powershell
cd frontend
npm install
npm run dev
```

前端地址：

```text
http://localhost:5174
```

## 主要接口

| 功能 | 方法 | 路径 |
| --- | --- | --- |
| 登录 | POST | `/api/auth/login` |
| 注册 | POST | `/api/auth/register` |
| 创建活动 | POST | `/api/campaign/create` |
| 活动列表 | GET | `/api/campaign/list` |
| 创建短链接 | POST | `/api/link/create` |
| 查询短链接 | GET | `/api/link/list` |
| 修改短链接 | PUT | `/api/link/update` |
| 禁用短链接 | DELETE | `/api/link/delete/{id}` |
| 统计面板 | GET | `/api/dashboard/stats` |
| AI 投放建议 | POST | `/api/ai/campaign` |
| 短链跳转 | GET | `/s/{shortCode}` |

## 面试介绍 1 分钟版本

这个项目是一个基于 Spring Boot 和 Vue 的营销活动短链接访问分析平台，并加入了 AI 投放助手。用户登录后可以创建投放活动，并为不同活动生成短链接。系统通过 Base62 生成短码，并通过 `/s/{shortCode}` 完成跳转。为了减少数据库查询，短码和原始链接的映射会缓存到 Redis；同时用 Redis 对同一 IP 访问同一短链做简单限流。每次访问都会记录访问日志，并更新短链接访问次数，后台可以查看总链接数、总访问量、今日访问量和 Top 链接等统计数据。

项目还提供 AI 投放助手，根据目标链接、目标人群和核心卖点生成推广文案、渠道建议和风险提示。配置 DeepSeek Key 时调用模型，未配置时使用本地模板兜底，保证项目稳定运行。

## 简历写法参考

营销活动短链数据分析平台（AI 投放助手） | Spring Boot、MyBatis-Plus、MySQL、Redis、JWT、DeepSeek API、Vue

- 设计并实现投放活动管理、短链接创建、短码生成、短链跳转、链接禁用和访问统计等核心功能。
- 使用 Base62 算法根据数据库主键生成短码，保证短码简洁且便于索引查询。
- 使用 Redis 缓存短码到原始链接的映射，降低高频跳转场景下的数据库访问压力。
- 基于 Redis 实现 IP + 短码维度的简单限流，避免同一短链接被异常高频访问。
- 记录短链接访问日志，并支持活动维度统计短链数量、总访问量和 Top 链接，为轻量投放分析提供数据支撑。
- 接入 DeepSeek API 实现 AI 投放助手，可根据目标链接、人群和卖点生成推广文案、渠道建议和风险提示，并提供本地模板兜底方案。

## 可扩展方向

- 使用布隆过滤器拦截不存在的短码，减少缓存穿透。
- 使用消息队列异步写访问日志，提高跳转接口响应速度。
- 对访问日志按天分表或归档，适配更高访问量场景。
- 增加二维码生成、访问来源统计、地域统计、过期自动清理等功能。

## 业务边界

本项目定位是轻量营销活动投放场景，不是复杂广告平台。核心目标是：

```text
创建活动 -> 生成活动短链接 -> 投放渠道使用 -> 记录访问日志 -> 按活动查看效果 -> AI 辅助生成文案
```

这个边界适合 Java 后端实习项目：业务完整、技术点清晰、实现复杂度可控。
