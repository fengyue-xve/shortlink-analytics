# 营销活动短链数据分析平台（AI 投放助手）

用户可以创建营销投放活动，为不同活动生成短链接，系统负责短码生成、短链跳转、Redis 缓存、访问限流、访问日志记录、活动维度统计，并提供 AI 投放文案生成能力。

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


## 说明

本项目用于学习 Java 后端开发和 AI 应用后端集成，重点展示短链接生成、Redis 缓存与限流、访问日志统计和大模型 API 调用链路。
