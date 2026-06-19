# SeekIsle（寻屿居）

Spring Cloud 微服务租房平台 — 房源搜索、即时通讯、小程序/管理端双端。

## 技术栈

| 层级 | 技术 |
|------|------|
| 框架 | Spring Boot 3.3.3 + Spring Cloud 2023.0.3 + Spring Cloud Alibaba 2023.0.1.2 |
| 注册/配置 | Nacos 2.2.2 |
| 网关 | Spring Cloud Gateway（Reactor Netty） |
| 服务调用 | OpenFeign |
| ORM | MyBatis Plus 3.5.7 |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis + Redisson 3.29.0 + Caffeine 3.1.8 |
| 消息队列 | RabbitMQ |
| 鉴权 | JWT（JJWT 0.9.1，HS512） |
| 即时通讯 | Jakarta WebSocket |
| 对象存储 | Aliyun OSS 3.15.1 |
| 短信 | Aliyun SMS 2.0.24 |

## 微服务

| 服务 | 端口 | 职责 |
|------|------|------|
| bite-gateway | 18080 | 统一入口、JWT 鉴权、白名单动态刷新 |
| bite-admin | 18081 | 房源 CRUD、用户管理、字典/参数、策略模式筛选 |
| bite-file | 18082 | OSS 前端直传签名 |
| bite-portal | 18083 | C 端 BFF：首页、搜索/详情、登录/注册 |
| bite-chat | 18084 | WebSocket + RabbitMQ 实时聊天 |
| bite-mstemplate | 18085 | 消息模板 |

## 本地运行

### 中间件

```bash
docker compose -p bitehouse -f deploy/dev/app/docker-compose-mid.yml up -d
```

| 服务 | 端口 | 凭据 |
|------|------|------|
| MySQL 8.4 | 3306 | `bitedev` / `bite@123` |
| Redis 7.0 | 6379 | `bite@123` |
| RabbitMQ 3.12 | 5672 / 15672 | `bitejiuyeke` / `bite@123` |
| Nacos 2.2.2 | 8848 | `nacos` / `bite@123` |

### 启动顺序

1. Nacos → 导入 `deploy/dev/res/sql/nacosdata.sql`
2. 按序启动：gateway → admin → file → portal → chat → mstemplate
3. 管理端前端：`npm run dev` → http://localhost:3000

## 架构亮点

- **网关鉴权**：GlobalFilter 拦截所有请求，白名单从 Nacos 动态读取，JWT 解析后 Redis 校验会话
- **策略模式筛选**：房源多条件筛选用策略链 `IHouseFilter` + 排序工厂 `ISortStrategy`
- **多级缓存**：Redis 城市房源映射 `house:list:{cityId}` + 房源详情 `house:{houseId}`，空值防穿透
- **WebSocket 聊天**：`@ServerEndpoint` + ConcurrentHashMap 管理连接，RabbitMQ 异步投递离线消息
- **Redisson 分布式锁**：定时任务房源状态扭转，看门狗自动续期
- **OSS 前端直传**：服务端签发临时 Policy + Signature，前端直接上传

## 前端

- **管理端**：Vue 3 + Vite + Element Plus（`biteHouseAdmin-feature-house`）
- **小程序端**：uni-app（`bitehousemp-develop`）
