#### 购票系统，使用 JDK17 + SpringBoot + RocketMQ + MySQL + Redis 主要开发
> 使用责任链模式重构请求数据准确性检验，比如：查询购票、购买车票下单以及支付结果回调等业务。
  通过 RocketMQ 延时消息特性，完成用户购票 10 分钟后未支付情况下取消订单功能。
  使用布隆过滤器解决用户名全局唯一带来的缓存穿透问题，使用用户名可复用缓存解决用户名注销不可复用的问题。
  通过 Redis Lua 脚本原子特性，完成用户购票令牌分配，通过令牌限流以应对海量用户购票请求。
  通过订单号和用户信息复合分片算法完成订单数据分库分表，支持订单号和用户查询维度。
  创建订单明细与乘车人的关联表，分库分表规则同订单，完成乘车人账号登录查询本人车票功能。