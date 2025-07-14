# 验证码问题修复说明

## 问题描述
在开发环境中，由于 Redis 被禁用，验证码功能无法正常工作：
- 验证码生成时无法存储到 Redis
- 验证码验证时无法从 Redis 获取，导致验证失败

## 解决方案
通过数据库配置直接禁用验证码功能。

### 步骤 1: 执行 SQL 脚本
运行以下 SQL 语句来禁用验证码：

```sql
-- 禁用验证码功能
UPDATE sys_config SET config_value = 'false' WHERE config_key = 'sys.account.captchaEnabled';
```

### 步骤 2: 验证修复效果
1. 重启应用
2. 访问登录页面，应该不会显示验证码输入框
3. 直接输入用户名和密码即可登录

## 恢复验证码功能
如果需要重新启用验证码功能，请执行：

```sql
-- 启用验证码功能
UPDATE sys_config SET config_value = 'true' WHERE config_key = 'sys.account.captchaEnabled';
```

## 注意事项
- 此修复方案适用于开发环境
- 生产环境请确保 Redis 服务正常运行
- 验证码开关通过数据库配置 `sys.account.captchaEnabled` 控制 