# GitHub版本管理指南

## 概述
系统现已集成GitHub API，可以自动检查您在GitHub仓库发布的最新版本。

## 配置步骤

### 1. 修改GitHub仓库信息
在 `ruoyi-ui/src/views/dashboard/index.vue` 文件中，找到以下配置并替换为您的实际仓库信息：

```javascript
githubRepo: 'your-username/ry-vue-flowable-xg', // 替换为您的GitHub用户名和仓库名
githubApiUrl: 'https://api.github.com/repos/your-username/ry-vue-flowable-xg/releases/latest', // 相应更新API地址
```

### 2. 在GitHub上发布版本

#### 2.1 创建Release
1. 在您的GitHub仓库页面，点击右侧的 "Releases"
2. 点击 "Create a new release" 按钮
3. 填写以下信息：
   - **Tag version**: 版本号，建议格式 `v1.2.4`
   - **Release title**: 版本标题，如 `v1.2.4 - 功能更新`
   - **Describe this release**: 详细的更新说明

#### 2.2 版本号规范
建议遵循语义化版本控制 (SemVer)：
- **主版本号**: 不兼容的API修改 (v2.0.0)
- **次版本号**: 向下兼容的功能性新增 (v1.1.0)
- **修订号**: 向下兼容的问题修正 (v1.0.1)

#### 2.3 更新说明格式
系统会自动解析GitHub Release的描述内容，建议使用以下格式：

```markdown
## 新功能
- 新增了工作台数据可视化功能
- 增加了GitHub版本自动检查

## 改进
- 优化了系统性能，提升响应速度
- 改进了用户界面设计

## 修复
- 修复了商家列表加载缓慢的问题
- 修复了若干已知问题

## 安全
- 加强了数据安全防护机制
```

### 3. 系统检查流程

#### 3.1 自动检查
- 系统会调用GitHub API获取最新版本
- 比较本地版本与GitHub最新版本
- 如有新版本，自动提示用户

#### 3.2 手动检查
- 用户点击"检查更新"按钮
- 30秒内限制重复检查
- 显示详细更新内容和GitHub链接

#### 3.3 降级处理
- 如果GitHub API不可用，自动降级为本地模拟检查
- 确保功能的稳定性和可用性

### 4. 版本发布最佳实践

#### 4.1 发布前准备
1. 确保代码已经测试完毕
2. 更新 `package.json` 中的版本号
3. 更新 `CHANGELOG.md` 文件
4. 提交所有代码变更

#### 4.2 发布步骤
1. 创建并推送版本标签：
   ```bash
   git tag v1.2.4
   git push origin v1.2.4
   ```

2. 在GitHub上创建Release：
   - 选择刚创建的标签
   - 填写详细的更新说明
   - 可以上传附件（如安装包）

#### 4.3 发布后验证
1. 在系统中点击"检查更新"
2. 确认能正确获取最新版本
3. 验证更新说明显示正确

### 5. 高级配置

#### 5.1 自定义API端点
如果使用私有仓库或企业版GitHub，可以修改API地址：
```javascript
githubApiUrl: 'https://api.github.com/repos/your-org/your-repo/releases/latest'
// 或企业版
githubApiUrl: 'https://your-github-enterprise.com/api/v3/repos/your-org/your-repo/releases/latest'
```

#### 5.2 认证配置
对于私有仓库，可能需要添加认证：
```javascript
// 在API请求中添加认证头
headers: {
  'Authorization': 'token your-github-token'
}
```

### 6. 故障排除

#### 6.1 常见问题
- **API请求失败**: 检查网络连接和仓库地址
- **版本比较错误**: 确保版本号格式正确 (v1.2.3)
- **更新说明显示异常**: 检查Release描述格式

#### 6.2 调试方法
1. 打开浏览器开发者工具
2. 查看控制台错误信息
3. 检查网络请求状态

## 示例配置

假设您的GitHub仓库是 `john-doe/my-app`，配置如下：

```javascript
systemVersion: {
  current: 'v1.2.3',
  githubRepo: 'john-doe/my-app',
  githubApiUrl: 'https://api.github.com/repos/john-doe/my-app/releases/latest',
  // ... 其他配置
}
```

## 总结
通过集成GitHub API，您的系统现在可以：
- 自动检查GitHub仓库的最新版本
- 显示详细的更新说明
- 提供直接跳转到GitHub Release页面的链接
- 在API不可用时提供降级方案

这样就实现了版本管理与GitHub仓库的完美集成！
