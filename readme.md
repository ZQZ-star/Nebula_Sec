


# 🛡️ Nebula-Sec 智能数字资产风控审计中台

![JDK](https://img.shields.io/badge/JDK-21-green.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen.svg)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.0-blue.svg)
![Redis](https://img.shields.io/badge/Redis-6.x-red.svg)
![GraalVM](https://img.shields.io/badge/GraalVM-AOT-orange.svg)

## 📖 项目简介

**Nebula-Sec** 是一个基于最新技术栈（`JDK 21` + `Spring Boot 3`）构建的企业级独立数据风控与安全审计中台。

随着企业内部微服务与第三方开放接口的呈指数级增长，传统垂直架构下的权限校验面临高延迟、代码冗余及敏感资产易被非法窃取的痛点。本项目旨在将鉴权分发、高速缓存以及实时的异常调用监控从业务微服务中剥离，提供统一的、无状态的高性能安全防线。

## ✨ 核心架构与技术亮点

* **🔒 无状态安全架构 (Security 6 + JWT + Redis)**
    彻底废弃 Session，构建基于 JWT 的无状态鉴权体系。通过引入 **Redis UUID 黑名单池** 完美解决“JWT 签发后无法主动失效”的行业痛点，并支持 Token 的无感自动续签。
* **🚀 双引擎 ORM 驱动 (JPA + MyBatis-Plus)**
    底层采用双数据引擎：利用 `Spring Data JPA` 处理高度复杂的 RBAC 权限域模型（多对多级联与懒加载）；利用 `MyBatis-Plus` 应对高并发下海量风控审计日志的高速批量插入与聚合查询。
* **🛡️ 智能缓存防护 (Redis + Redisson BloomFilter)**
    针对高频热点资产访问，重写 MyBatis Cache 构建分布式二级缓存。引入**布隆过滤器 (Bloom Filter)** 进行前置请求拦截，从网关层彻底防范“缓存穿透”攻击，并通过逻辑过期策略规避缓存击穿与雪崩。
* **⚡ 虚拟线程异步审计 (JDK 21 Virtual Threads + AOP)**
    结合 Java 21 最新的轻量级虚拟线程特性与 Spring AOP，构建了极低开销的后台异步审计引擎，实现接口调用耗时、操作路径的海量并发落库，且完全不阻塞主业务线程。
* **☁️ 云原生极速启动 (GraalVM Native Image)**
    全面拥抱云原生，利用 GraalVM AOT（预编译）技术，将中台打包为底层操作系统的原生二进制可执行文件，实现 `<100ms` 的极速冷启动与极低的运行内存占用。

## 🛠️ 技术栈清单

| 分类 | 技术组件 | 说明 |
| :--- | :--- | :--- |
| **底层核心** | Java 21, Spring Boot 3.2 | 核心框架，启用虚拟线程 |
| **安全风控** | Spring Security 6, JWT | 鉴权中心与无状态令牌 |
| **持久层** | MyBatis-Plus 3.5, Spring Data JPA | 双引擎 ORM，复杂模型与高速流水的完美结合 |
| **缓存与中间件** | Redis, Redisson | 分布式缓存与布隆过滤器 |
| **数据库** | MySQL 8.x | 核心数据存储 |
| **运维与编译** | GraalVM, Spring Boot Actuator | 原生镜像打包与 JVM 性能监控 |
| **接口文档** | SpringDoc (OpenAPI 3) | 动态 Swagger 接口文档聚合 |

## 📂 项目结构

```text
nebula-sec-center
 ├── src/main/java/com/nebula
 │   ├── annotation    # 自定义注解 (如：@RecordAudit)
 │   ├── aspect        # AOP 切面 (异步审计日志拦截)
 │   ├── config        # 核心配置 (Security, Redis, BloomFilter, 双ORM策略)
 │   ├── controller    # RESTful API 接口层
 │   ├── domain        # 领域模型 (JPA Entity, MyBatis Log, JDK21 Record DTO)
 │   ├── filter        # 核心过滤器 (JWT 签发与黑名单拦截)
 │   ├── mapper        # MyBatis-Plus 数据访问接口
 │   ├── repository    # Spring Data JPA 复杂模型访问接口
 │   ├── service       # 业务逻辑与缓存调度层
 │   └── utils         # 工具类 (JWT 编解码等)
 └── src/main/resources
     ├── application.yml   # 全局配置文件
     └── mapper/           # MyBatis XML 映射文件

```

## 🚀 快速开始

### 1. 环境准备

* **JDK**: `21` 或更高版本
* **MySQL**: `8.0` 或更高版本
* **Redis**: `6.0` 或更高版本
* **Maven**: `3.8+`

### 2. 数据初始化

1. 在 MySQL 中创建数据库 `nebula_sec`。
2. 运行项目根目录下的 `sql/init.sql` 脚本，完成 RBAC 五张核心表与审计日志表的创建，并导入初始测试数据。

### 3. 修改配置

打开 `src/main/resources/application.yml`，修改以下配置为您本地的环境参数：

```yaml
spring:
  datasource:
    username: root
    password: your_mysql_password # 修改为您的数据库密码
  data:
    redis:
      host: 127.0.0.1
      port: 6379

```

### 4. 编译与启动

在项目根目录下执行以下命令：

```bash
mvn clean install
mvn spring-boot:run

```

*(注：若需体验秒级启动的原生镜像，请确保已安装 GraalVM 环境，并执行 `mvn -Pnative native:compile` 进行 AOT 编译)*

## 📄 接口文档与测试

项目启动成功后，可在浏览器中访问内置的 Swagger 接口控制台进行调试：
👉 **在线文档地址**：`http://localhost:8080/swagger-ui/index.html`

**默认测试账号：**

* 管理员账号：`admin`
* 密码：`123`




