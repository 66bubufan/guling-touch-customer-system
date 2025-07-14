# RedissonClient Bean 缺失问题修复说明

## 问题描述

在 Spring Boot 项目中，当 Redis 配置被禁用时（如开发环境中的 `spring.redis.enabled: false`），应用启动时会遇到 `RedissonClient` Bean 缺失的错误。这是因为某些工具类在静态初始化时尝试获取 Spring Bean，而此时 Spring 容器可能还没有完全初始化，或者相关的 Bean 因为配置被禁用而不存在。

## 问题根源

以下工具类在静态初始化时尝试获取 Spring Bean，导致启动失败：

1. `CacheUtils` - 静态初始化时获取 `CacheManager`
2. `JsonUtils` - 静态初始化时获取 `ObjectMapper`
3. `MessageUtils` - 静态初始化时获取 `MessageSource`
4. `ValidatorUtils` - 静态初始化时获取 `Validator`
5. `MailUtils` - 静态初始化时获取 `MailAccount`
6. `DataBaseHelper` - 静态初始化时获取 `DynamicRoutingDataSource`

此外，`lock4j-redisson-spring-boot-starter` 依赖的自动配置类 `RedissonLockAutoConfiguration` 也会尝试获取 `RedissonClient` Bean，导致启动失败。

## 修复方案

将所有工具类的静态初始化改为懒加载模式，确保在 Bean 不可用时不会抛出异常，而是返回合理的默认值或忽略操作。同时，在开发环境中禁用 lock4j 功能。

### 修复的文件

1. **ruoyi-common/src/main/java/com/ruoyi/common/utils/redis/CacheUtils.java**
   - 将 `private static final CacheManager CACHE_MANAGER = SpringUtils.getBean(CacheManager.class);` 改为懒加载
   - 添加异常处理，在 Redis 不可用时返回空集合或 null

2. **ruoyi-common/src/main/java/com/ruoyi/common/utils/JsonUtils.java**
   - 将 `private static final ObjectMapper OBJECT_MAPPER = SpringUtils.getBean(ObjectMapper.class);` 改为懒加载
   - 添加异常处理，在 ObjectMapper 不可用时返回 null

3. **ruoyi-common/src/main/java/com/ruoyi/common/utils/MessageUtils.java**
   - 将 `private static final MessageSource MESSAGE_SOURCE = SpringUtils.getBean(MessageSource.class);` 改为懒加载
   - 添加异常处理，在 MessageSource 不可用时返回消息键本身

4. **ruoyi-common/src/main/java/com/ruoyi/common/utils/ValidatorUtils.java**
   - 将 `private static final Validator VALID = SpringUtils.getBean(Validator.class);` 改为懒加载
   - 添加异常处理，在 Validator 不可用时跳过验证

5. **ruoyi-common/src/main/java/com/ruoyi/common/utils/email/MailUtils.java**
   - 将 `private static final MailAccount ACCOUNT = SpringUtils.getBean(MailAccount.class);` 改为懒加载
   - 添加异常处理，在 MailAccount 不可用时返回 null

6. **ruoyi-common/src/main/java/com/ruoyi/common/helper/DataBaseHelper.java**
   - 将 `private static final DynamicRoutingDataSource DS = SpringUtils.getBean(DynamicRoutingDataSource.class);` 改为懒加载
   - 添加异常处理，在数据源不可用时返回默认的 MySQL 类型

7. **ruoyi-admin/src/main/resources/application-dev.yml**
   - 添加 `lock4j.enabled: false` 配置禁用分布式锁功能
   - 在 `autoconfigure.exclude` 中添加 `RedissonLockAutoConfiguration` 排除项

8. **ruoyi-admin/src/main/resources/application-test.yml**
   - 添加 `lock4j.enabled: false` 配置禁用分布式锁功能
   - 在 `autoconfigure.exclude` 中添加 `RedissonLockAutoConfiguration` 排除项

## 修复效果

修复后，应用在以下情况下都能正常启动：

1. **Redis 被禁用时** - 缓存相关操作会被忽略，不会抛出异常
2. **邮件服务被禁用时** - 邮件相关操作会被忽略，不会抛出异常
3. **Spring 容器未完全初始化时** - 工具类会优雅地处理 Bean 不可用的情况
4. **lock4j 分布式锁被禁用时** - 不会尝试创建 RedissonLockExecutor

## 测试验证

创建了测试类 `RedisDisabledTest` 来验证修复效果：

- 测试 Redis 工具类在 Redis 不可用时的行为
- 测试缓存工具类在 Redis 不可用时的行为
- 测试 JSON 工具类在 Spring 容器未准备好时的行为
- 测试消息工具类在 Spring 容器未准备好时的行为
- 测试验证工具类在 Spring 容器未准备好时的行为
- 测试数据库助手在 Spring 容器未准备好时的行为
- 测试 lock4j 在 Redis 不可用时的行为

## 配置说明

在开发环境中，可以通过以下配置禁用 Redis 和相关功能：

```yaml
spring:
  redis:
    enabled: false
  autoconfigure:
    exclude:
      - org.redisson.spring.starter.RedissonAutoConfiguration
      - org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
      - com.baomidou.lock.spring.boot.autoconfigure.LockAutoConfiguration
      - com.baomidou.lock.spring.boot.autoconfigure.RedissonLockAutoConfiguration

# 禁用分布式锁
lock4j:
  enabled: false
```

## 注意事项

1. 修复后的工具类在 Bean 不可用时会返回合理的默认值，不会影响应用的正常启动
2. 在 Redis 不可用时，缓存相关操作会被忽略，这可能会影响某些功能的性能，但不会导致应用崩溃
3. lock4j 分布式锁功能在开发环境中被禁用，如果需要测试分布式锁功能，请启用 Redis
4. 建议在生产环境中启用 Redis 以获得更好的性能和功能支持 