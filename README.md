# 社区智慧物业报修与工单处理系统

住户报修 → 管理员派单 → 维修工处理 → 住户评价的全流程工单系统，前后端分离。

## 功能

- 住户端：提交报修（含照片）、查看进度、完工评价
- 管理端：工单派发、紧急度分级、多维筛选、统计看板
- 维修端：接单处理、维修记录
- 工单状态机：待派单 → 已派单 → 处理中 → 已完成 → 已评价

## 技术栈

- 后端：Spring Boot + Spring Data JPA + H2（开箱即用，免装数据库）
- 前端：Vue3 + Vite + Element Plus
- 认证：JWT

## 运行

```bash
# 后端（需 JDK 17+）
cd backend && mvn spring-boot:run

# 前端
cd frontend && npm install && npm run dev
```

## 结构

```
backend/   Spring Boot 后端（Controller / Service / Repository / Entity）
frontend/  Vue3 前端（Vite 构建）
```
