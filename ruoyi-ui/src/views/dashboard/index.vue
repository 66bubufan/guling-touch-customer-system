<template>
  <div class="dashboard-container">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="banner-content">
        <div class="welcome-info">
          <h1>🎉 欢迎回来！</h1>
          <p class="welcome-subtitle">谷菱碰一碰同城获客系统</p>
          <p class="current-time">{{ currentTime }}</p>
        </div>
        <div class="banner-actions">
          <el-button 
            :type="systemVersion.hasUpdate ? 'danger' : 'success'" 
            size="medium" 
            :icon="systemVersion.hasUpdate ? 'el-icon-warning' : 'el-icon-check'"
            @click="checkUpdates">
            {{ systemVersion.hasUpdate ? '有新版本！' : '已是最新版本' }}
          </el-button>
          <el-button type="info" size="medium" icon="el-icon-document" @click="showUpdateLog">
            查看更新日志
          </el-button>
        </div>
      </div>
    </div>
    
    <!-- 核心数据统计 -->
    <div class="stats-overview">
      <h2 class="section-title">📊 核心数据概览</h2>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stats-card user-stats">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-user-solid"></i>
              </div>
              <div class="card-info">
                <div class="card-value">{{ userStats.total.toLocaleString() }}</div>
                <div class="card-label">用户总数</div>
                <div class="card-trend" :class="userStats.trend > 0 ? 'up' : 'down'">
                  <i :class="userStats.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                  较昨日 {{ userStats.trend > 0 ? '+' : '' }}{{ userStats.trend }}%
                </div>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card merchant-stats">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-office-building"></i>
              </div>
              <div class="card-info">
                <div class="card-value">{{ merchantStats.total.toLocaleString() }}</div>
                <div class="card-label">商家总数</div>
                <div class="card-trend" :class="merchantStats.trend > 0 ? 'up' : 'down'">
                  <i :class="merchantStats.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                  较昨日 {{ merchantStats.trend > 0 ? '+' : '' }}{{ merchantStats.trend }}%
                </div>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card revenue-stats">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-value">¥{{ revenueStats.today.toLocaleString() }}</div>
                <div class="card-label">今日交易额</div>
                <div class="card-trend" :class="revenueStats.trend > 0 ? 'up' : 'down'">
                  <i :class="revenueStats.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                  较昨日 {{ revenueStats.trend > 0 ? '+' : '' }}{{ revenueStats.trend }}%
                </div>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card growth-stats">
            <div class="card-content">
              <div class="card-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-value">{{ growthStats.rate }}%</div>
                <div class="card-label">月度增长率</div>
                <div class="card-trend" :class="growthStats.trend > 0 ? 'up' : 'down'">
                  <i :class="growthStats.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                  较上月 {{ growthStats.trend > 0 ? '+' : '' }}{{ growthStats.trend.toFixed(1) }}%
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据可视化 -->
    <div class="charts-section">
      <h2 class="section-title">📈 数据趋势分析</h2>
      <el-row :gutter="20">
        <el-col :span="16">
          <div class="chart-card main-chart">
            <div class="card-header">
              <h3>💰 交易趋势分析</h3>
              <div class="card-actions">
                <el-radio-group v-model="timeRange" size="small">
                  <el-radio-button label="week">本周</el-radio-button>
                  <el-radio-button label="month">本月</el-radio-button>
                  <el-radio-button label="year">全年</el-radio-button>
                </el-radio-group>
              </div>
            </div>
            <div class="chart-container">
              <div class="chart-placeholder">
                <div class="chart-demo">
                  <div class="chart-bars">
                    <div class="bar" style="height: 60%"></div>
                    <div class="bar" style="height: 80%"></div>
                    <div class="bar" style="height: 45%"></div>
                    <div class="bar" style="height: 90%"></div>
                    <div class="bar" style="height: 75%"></div>
                    <div class="bar" style="height: 95%"></div>
                    <div class="bar" style="height: 85%"></div>
                  </div>
                  <p>📊 交易趋势呈上升状态</p>
                </div>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="chart-card">
            <div class="card-header">
              <h3>🗺️ 商家分布</h3>
              <div class="card-actions">
                <el-dropdown size="small">
                  <span class="el-dropdown-link">
                    按地区 <i class="el-icon-arrow-down el-icon--right"></i>
                  </span>
                </el-dropdown>
              </div>
            </div>
            <div class="chart-container">
              <div class="chart-placeholder">
                <div class="pie-chart-demo">
                  <div class="pie-slice slice1" title="上海 35%"></div>
                  <div class="pie-slice slice2" title="北京 25%"></div>
                  <div class="pie-slice slice3" title="深圳 20%"></div>
                  <div class="pie-slice slice4" title="其他 20%"></div>
                  <div class="pie-center">
                    <div class="pie-total">1,234</div>
                    <div class="pie-label">总商家数</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 操作中心 -->
    <div class="operation-center">
      <el-row :gutter="20">
        <!-- 快捷操作 -->
        <el-col :span="12">
          <div class="operation-card">
            <div class="card-header">
              <h3>⚡ 快捷操作</h3>
            </div>
            <div class="quick-actions-grid">
              <div class="action-item" @click="handleQuickAction('addMerchant')">
                <div class="action-icon merchant">
                  <i class="el-icon-shop"></i>
                </div>
                <div class="action-content">
                  <div class="action-title">新增商家</div>
                  <div class="action-desc">快速注册新商家</div>
                </div>
              </div>
              <div class="action-item" @click="handleQuickAction('viewReport')">
                <div class="action-icon report">
                  <i class="el-icon-data-analysis"></i>
                </div>
                <div class="action-content">
                  <div class="action-title">数据报表</div>
                  <div class="action-desc">查看详细统计</div>
                </div>
              </div>
              <div class="action-item" @click="handleQuickAction('systemSettings')">
                <div class="action-icon settings">
                  <i class="el-icon-setting"></i>
                </div>
                <div class="action-content">
                  <div class="action-title">系统设置</div>
                  <div class="action-desc">配置系统参数</div>
                </div>
              </div>
              <div class="action-item" @click="handleQuickAction('messageCenter')">
                <div class="action-icon message">
                  <i class="el-icon-message"></i>
                </div>
                <div class="action-content">
                  <div class="action-title">消息中心</div>
                  <div class="action-desc">查看系统通知</div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
        
        <!-- 系统动态 -->
        <el-col :span="12">
          <div class="operation-card">
            <div class="card-header">
              <h3>🔔 系统动态</h3>
              <el-link type="primary" @click="viewAllMessages">查看全部</el-link>
            </div>
            <div class="system-messages">
              <div class="message-item" v-for="(message, index) in systemMessages" :key="index">
                <div class="message-indicator" :class="message.type"></div>
                <div class="message-content">
                  <div class="message-title">{{ message.title }}</div>
                  <div class="message-desc">{{ message.description }}</div>
                  <div class="message-time">{{ message.time }}</div>
                </div>
                <div class="message-status" v-if="message.unread">
                  <el-badge is-dot></el-badge>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      timeRange: 'week',
      currentTime: '',
      // 系统版本信息
      systemVersion: {
        current: 'v1.2.4', // 用户当前版本
        buildDate: '2025-07-14',
        createDate: '2024-09-01',
        hasUpdate: true, // 设置为有更新可用
        latestVersion: 'v1.2.5', // 最新版本
        lastCheckTime: null, // 上次检查时间
        checkingUpdate: false, // 是否正在检查更新
        githubRepo: '66bubufan/guling-touch-customer-system', // GitHub仓库地址
        githubApiUrl: 'https://api.github.com/repos/66bubufan/guling-touch-customer-system/releases/latest', // GitHub API地址
        updateLog: [
          {
            version: 'v1.2.5',
            date: '2025-07-14',
            changes: [
              '🚀 新增一键自动更新功能',
              '� GitHub版本管理集成',
              '🎛️ 智能更新检测系统',
              '📋 详细更新日志展示',
              '⚡ 优化系统性能',
              '� 修复版本检测API问题'
            ],
            githubUrl: 'https://github.com/66bubufan/guling-touch-customer-system/releases/tag/v1.2.5'
          },
          {
            version: 'v1.2.3',
            date: '2025-06-20',
            changes: [
              '🎨 优化了用户界面设计',
              '📱 增强了移动端适配',
              '🔧 修复了若干已知问题'
            ]
          },
          {
            version: 'v1.2.0',
            date: '2025-05-15',
            changes: [
              '🚀 新增商家管理模块',
              '💰 完善财务管理功能',
              '📊 增加数据统计报表'
            ]
          },
          {
            version: 'v1.1.0',
            date: '2025-03-10',
            changes: [
              '👥 新增代理管理功能',
              '🔐 优化登录验证机制',
              '📋 完善系统日志记录'
            ]
          },
          {
            version: 'v1.0.0',
            date: '2024-09-01',
            changes: [
              '🎉 系统正式发布',
              '🏗️ 完成基础架构搭建',
              '👤 实现用户管理功能',
              '🛡️ 建立权限控制体系'
            ]
          }
        ]
      },
      // 用户统计数据
      userStats: {
        total: 8888,
        trend: 13
      },
      // 商家统计数据
      merchantStats: {
        total: 666,
        trend: 5
      },
      // 收入统计数据
      revenueStats: {
        today: 12345,
        trend: 8
      },
      // 增长率统计数据
      growthStats: {
        rate: 23.5,
        trend: 2.1
      },
      // 系统消息数据
      systemMessages: [
        {
          title: '系统更新通知',
          description: '系统将在今晚进行例行维护更新',
          time: '10分钟前',
          type: 'info',
          unread: true
        },
        {
          title: '新商家入驻申请',
          description: '有3家新商家提交了入驻申请',
          time: '30分钟前',
          type: 'success',
          unread: true
        },
        {
          title: '数据备份完成',
          description: '今日数据备份已完成',
          time: '1小时前',
          type: 'success',
          unread: false
        },
        {
          title: '服务器性能监控',
          description: '系统运行状态良好',
          time: '2小时前',
          type: 'info',
          unread: false
        }
      ]
    }
  },
  created() {
    this.updateTime()
    // 每分钟更新一次时间
    setInterval(this.updateTime, 60000)
    // 模拟数据刷新
    this.refreshData()
    // 注释掉随机更新状态，使用固定的演示数据
    // this.initUpdateStatus()
  },
  methods: {
    updateTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = now.getMonth() + 1
      const date = now.getDate()
      const hours = String(now.getHours()).padStart(2, '0')
      const minutes = String(now.getMinutes()).padStart(2, '0')
      const day = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][now.getDay()]
      this.currentTime = `${year}年${month}月${date}日 ${hours}:${minutes} ${day}`
    },
    
    // 刷新统计数据
    refreshData() {
      // 这里可以调用API获取实时数据
      // 现在使用模拟数据
      setTimeout(() => {
        // 模拟数据变化
        this.userStats.total += Math.floor(Math.random() * 10)
        this.merchantStats.total += Math.floor(Math.random() * 3)
        this.revenueStats.today += Math.floor(Math.random() * 500)
      }, 2000)
    },

    // 初始化更新状态 - 已禁用，使用固定演示数据
    // initUpdateStatus() {
    //   // 随机设置是否有更新（30%概率有更新）
    //   const hasUpdate = Math.random() < 0.3
    //   this.systemVersion.hasUpdate = hasUpdate
    //   
    //   if (!hasUpdate) {
    //     this.systemVersion.latestVersion = this.systemVersion.current
    //   }
    // },
    
    // 处理快捷操作点击
    handleQuickAction(action) {
      switch(action) {
        case 'addMerchant':
          this.$router.push('/merchant')
          this.$message.success('跳转到商家管理页面')
          break
        case 'viewReport':
          this.$router.push('/finance/overview')
          this.$message.success('跳转到财务概览页面')
          break
        case 'systemSettings':
          this.$router.push('/system/config')
          this.$message.success('跳转到系统设置页面')
          break
        case 'messageCenter':
          this.$message.info('消息中心功能开发中...')
          break
        default:
          this.$message.info('功能开发中...')
      }
    },
    
    // 查看全部消息
    viewAllMessages() {
      this.$message.info('消息中心功能开发中...')
    },

    // 检查系统更新
    checkUpdates() {
      // 如果当前已有检测到的更新，直接显示详情
      if (this.systemVersion.hasUpdate) {
        this.showUpdateDetails()
        return
      }
      
      // 防止频繁检查，30秒内只能检查一次
      const now = Date.now()
      if (this.systemVersion.lastCheckTime && (now - this.systemVersion.lastCheckTime) < 30000) {
        this.$message({
          message: '请稍后再试，检查更新间隔不能少于30秒',
          type: 'warning'
        })
        return
      }
      
      // 如果正在检查中，避免重复检查
      if (this.systemVersion.checkingUpdate) {
        this.$message({
          message: '正在检查更新中，请稍候...',
          type: 'info'
        })
        return
      }
      
      this.systemVersion.checkingUpdate = true
      this.$message({
        message: '正在从GitHub检查更新...',
        type: 'info'
      })
      
      // 从GitHub API获取最新版本
      this.fetchLatestVersionFromGithub()
    },

    // 从GitHub API获取最新版本
    async fetchLatestVersionFromGithub() {
      try {
        const response = await fetch(this.systemVersion.githubApiUrl)
        
        if (!response.ok) {
          throw new Error(`GitHub API请求失败: ${response.status}`)
        }
        
        const releaseData = await response.json()
        const latestVersion = releaseData.tag_name // GitHub release的tag名称
        const releaseDate = new Date(releaseData.published_at).toISOString().split('T')[0]
        const releaseNotes = releaseData.body || '暂无更新说明'
        
        this.systemVersion.checkingUpdate = false
        this.systemVersion.lastCheckTime = Date.now()
        
        // 比较版本号
        const hasUpdate = this.compareVersions(latestVersion, this.systemVersion.current) > 0
        
        if (hasUpdate) {
          this.systemVersion.hasUpdate = true
          this.systemVersion.latestVersion = latestVersion
          
          // 解析GitHub release notes
          const changes = this.parseReleaseNotes(releaseNotes)
          
          // 添加新的更新日志
          this.systemVersion.updateLog.unshift({
            version: latestVersion,
            date: releaseDate,
            changes: changes,
            githubUrl: releaseData.html_url // GitHub release页面链接
          })
          
          this.$confirm(`发现新版本 ${latestVersion}，是否查看更新详情？`, 'GitHub更新检测', {
            confirmButtonText: '查看详情',
            cancelButtonText: '稍后再说',
            type: 'success'
          }).then(() => {
            this.showUpdateDetails()
          })
        } else {
          this.$message({
            message: '当前已是最新版本！',
            type: 'success'
          })
        }
      } catch (error) {
        console.error('检查GitHub更新失败:', error)
        this.systemVersion.checkingUpdate = false
        
        // 如果GitHub API请求失败，降级为本地模拟检查
        this.$message({
          message: 'GitHub连接失败，使用本地检查模式',
          type: 'warning'
        })
        
        setTimeout(() => {
          this.fallbackVersionCheck()
        }, 1000)
      }
    },

    // 版本比较函数
    compareVersions(version1, version2) {
      // 移除v前缀并分割版本号
      const v1parts = version1.replace(/^v/, '').split('.').map(Number)
      const v2parts = version2.replace(/^v/, '').split('.').map(Number)
      
      const maxLength = Math.max(v1parts.length, v2parts.length)
      
      for (let i = 0; i < maxLength; i++) {
        const v1part = v1parts[i] || 0
        const v2part = v2parts[i] || 0
        
        if (v1part > v2part) return 1
        if (v1part < v2part) return -1
      }
      return 0
    },

    // 解析GitHub release notes
    parseReleaseNotes(releaseNotes) {
      // 简单的解析逻辑，您可以根据实际的release notes格式调整
      const lines = releaseNotes.split('\n').filter(line => line.trim())
      const changes = []
      
      for (const line of lines) {
        const trimmedLine = line.trim()
        // 识别常见的更新标记
        if (trimmedLine.startsWith('- ') || trimmedLine.startsWith('* ') || 
            trimmedLine.startsWith('+ ') || trimmedLine.match(/^\d+\./)) {
          changes.push(trimmedLine.replace(/^[-*+]\s*|\d+\.\s*/, ''))
        } else if (trimmedLine.length > 0 && changes.length < 8) {
          // 如果没有标记符号但内容有意义，也加入
          changes.push(trimmedLine)
        }
      }
      
      // 如果没有解析到有效内容，使用默认格式
      if (changes.length === 0) {
        changes.push('🆕 系统功能更新')
        changes.push('🐛 修复已知问题')
        changes.push('⚡ 性能优化改进')
      }
      
      // 限制最多显示8条
      return changes.slice(0, 8)
    },

    // 降级版本检查（当GitHub API不可用时）
    fallbackVersionCheck() {
      this.systemVersion.lastCheckTime = Date.now()
      
      // 模拟版本检查逻辑
      const hasUpdate = Math.random() > 0.85 // 15% 概率有更新
      
      if (hasUpdate) {
        this.systemVersion.hasUpdate = true
        this.systemVersion.latestVersion = 'v1.3.1'
        
        this.systemVersion.updateLog.unshift({
          version: 'v1.3.1',
          date: new Date().toISOString().split('T')[0],
          changes: [
            '🔥 紧急修复了安全漏洞',
            '⚡ 大幅提升系统响应速度',
            '🎯 优化了数据查询性能',
            '🛠️ 修复了多个已知问题'
          ]
        })
        
        this.$confirm('发现新版本 v1.3.1，是否查看更新详情？', '本地更新检测', {
          confirmButtonText: '查看详情',
          cancelButtonText: '稍后再说',
          type: 'success'
        }).then(() => {
          this.showUpdateDetails()
        })
      } else {
        this.$message({
          message: '当前已是最新版本！',
          type: 'success'
        })
      }
    },

    // 执行自动更新
    async completeUpdate() {
      try {
        // 显示更新进度提示
        const loadingMessage = this.$message({
          message: '🚀 正在执行自动更新，请稍候...',
          type: 'info',
          duration: 0,
          showClose: false
        })
        
        // 调用后端API执行更新脚本
        const response = await fetch('/api/system/update', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            // 如果有token认证，添加认证头
            'Authorization': localStorage.getItem('token') ? `Bearer ${localStorage.getItem('token')}` : ''
          },
          timeout: 60000 // 60秒超时
        })
        
        loadingMessage.close()
        
        if (response.ok) {
          const result = await response.json()
          
          if (result.code === 200) {
            // 更新成功
            this.systemVersion.hasUpdate = false
            this.systemVersion.current = this.systemVersion.latestVersion
            this.systemVersion.buildDate = new Date().toISOString().split('T')[0]
            this.systemVersion.lastCheckTime = null
            
            this.$notify({
              title: '✅ 更新成功',
              message: '系统正在后台更新，请等待构建完成后刷新页面查看效果',
              type: 'success',
              duration: 5000,
              position: 'top-right'
            })
            
            // 显示更新进度对话框
            this.$confirm(
              `<div style="text-align: left;">
                <h4 style="color: #67C23A; margin-bottom: 16px;">🎉 更新命令执行成功！</h4>
                <p>系统正在后台执行以下操作：</p>
                <ul style="margin: 12px 0; padding-left: 20px;">
                  <li>📥 拉取最新代码</li>
                  <li>🔨 重新构建前端</li>
                  <li>📦 准备生产环境文件</li>
                </ul>
                <p style="margin-top: 16px; color: #E6A23C;">
                  ⏱️ 预计需要1-3分钟，请耐心等待...
                </p>
                <p style="margin-top: 12px; font-size: 12px; color: #909399;">
                  💡 您可以继续使用系统，更新完成后建议刷新页面
                </p>
              </div>`,
              '更新进度',
              {
                dangerouslyUseHTMLString: true,
                confirmButtonText: '我知道了',
                cancelButtonText: '手动刷新',
                type: 'success',
                callback: (action) => {
                  if (action === 'cancel') {
                    window.location.reload()
                  }
                }
              }
            )
            
          } else {
            throw new Error(result.msg || '更新API返回错误')
          }
        } else {
          const errorText = await response.text()
          throw new Error(`HTTP ${response.status}: ${errorText}`)
        }
        
      } catch (error) {
        console.error('更新失败:', error)
        
        this.$message.closeAll()
        
        // 显示详细错误信息
        this.$confirm(
          `<div style="text-align: left;">
            <h4 style="color: #F56C6C; margin-bottom: 16px;">❌ 自动更新失败</h4>
            <p><strong>错误信息：</strong></p>
            <p style="background: #f5f5f5; padding: 8px; border-radius: 4px; font-family: monospace; font-size: 12px; margin: 8px 0;">
              ${error.message}
            </p>
            <h5 style="margin: 16px 0 8px 0; color: #E6A23C;">🛠️ 解决方案：</h5>
            <ol style="margin: 8px 0; padding-left: 20px; font-size: 14px;">
              <li>请手动双击项目根目录的 <code>update.bat</code> 文件</li>
              <li>或在命令行中执行：<code>git pull origin main</code></li>
              <li>然后重新构建前端：<code>cd ruoyi-ui && npm run build:prod</code></li>
            </ol>
            <p style="margin-top: 16px; font-size: 12px; color: #909399;">
              💡 如果问题持续，请联系技术支持
            </p>
          </div>`,
          '更新失败',
          {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '我知道了',
            cancelButtonText: '查看日志',
            type: 'error',
            callback: (action) => {
              if (action === 'cancel') {
                console.log('更新详细错误:', error)
                this.$message.info('错误详情已输出到浏览器控制台')
              }
            }
          }
        )
      }
    },

    // 显示更新详情
    showUpdateDetails() {
      const latestUpdate = this.systemVersion.updateLog[0]
      const updateContent = latestUpdate.changes.map(item => 
        `<p style="margin: 8px 0; padding-left: 12px;">${item}</p>`
      ).join('')
      
      const githubLink = latestUpdate.githubUrl ? 
        `<p style="margin-top: 16px;">
          <a href="${latestUpdate.githubUrl}" target="_blank" style="color: #409EFF; text-decoration: none;">
            🔗 在GitHub上查看完整更新说明
          </a>
        </p>` : ''
      
      this.$alert(
        `<div style="text-align: left;">
          <h4 style="margin: 0 0 16px 0; color: #409EFF;">🎉 版本 ${latestUpdate.version} 更新内容：</h4>
          ${updateContent}
          ${githubLink}
          <p style="margin-top: 16px; font-size: 12px; color: #909399;">
            发布时间：${latestUpdate.date}
          </p>
        </div>`, 
        '更新详情', 
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '立即更新',
          showCancelButton: latestUpdate.githubUrl ? true : false,
          cancelButtonText: '查看GitHub',
          callback: (action) => {
            if (action === 'confirm') {
              this.$message.success('正在更新系统...')
              // 模拟更新过程
              setTimeout(() => {
                this.completeUpdate()
              }, 2000)
            } else if (action === 'cancel' && latestUpdate.githubUrl) {
              // 打开GitHub页面
              window.open(latestUpdate.githubUrl, '_blank')
            }
          }
        }
      )
    },

    // 显示更新日志
    showUpdateLog() {
      const logContent = this.systemVersion.updateLog.map(log => {
        const githubLink = log.githubUrl ? 
          `<div style="margin-top: 8px;">
            <a href="${log.githubUrl}" target="_blank" style="color: #409EFF; font-size: 12px; text-decoration: none;">
              🔗 查看GitHub Release
            </a>
          </div>` : ''
        
        return `<div style="margin-bottom: 24px; padding: 16px; background: #f8f9fa; border-radius: 8px; border-left: 4px solid #409EFF;">
          <h5 style="margin: 0 0 12px 0; color: #409EFF;">
            📦 ${log.version} 
            <span style="font-size: 12px; color: #909399; font-weight: normal;">(${log.date})</span>
          </h5>
          ${log.changes.map(change => `<p style="margin: 4px 0; font-size: 13px;">${change}</p>`).join('')}
          ${githubLink}
        </div>`
      }).join('')
      
      this.$alert(
        `<div style="text-align: left; max-height: 500px; overflow-y: auto;">
          <h4 style="margin: 0 0 20px 0; color: #409EFF;">📋 系统更新日志</h4>
          <div style="margin-bottom: 16px; padding: 12px; background: #e8f5e8; border-radius: 6px; border-left: 4px solid #67C23A;">
            <strong>🏗️ 系统创建时间：</strong> ${this.systemVersion.createDate}
            <br><strong>📍 当前版本：</strong> ${this.systemVersion.current}
            <br><strong>📦 GitHub仓库：</strong> 
            <a href="https://github.com/${this.systemVersion.githubRepo}" target="_blank" style="color: #409EFF; text-decoration: none;">
              ${this.systemVersion.githubRepo}
            </a>
            <br><strong>🔄 更新状态：</strong> 
            <span style="color: ${this.systemVersion.hasUpdate ? '#E6A23C' : '#67C23A'};">
              ${this.systemVersion.hasUpdate ? '有新版本可用' : '已是最新版本'}
            </span>
          </div>
          ${logContent}
          <div style="margin-top: 16px; font-size: 12px; color: #909399; text-align: center;">
            © 2024-2025 谷菱网络科技有限公司 | 
            <a href="https://github.com/${this.systemVersion.githubRepo}" target="_blank" style="color: #409EFF;">
              GitHub开源项目
            </a>
          </div>
        </div>`, 
        '更新日志', 
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '确定'
        }
      )
    },

    // 显示版本信息 (保留原有功能)
    showVersionInfo() {
      const versionInfo = `
        <div style="text-align: left;">
          <h4 style="margin: 0 0 16px 0; color: #409EFF;">📋 系统版本信息</h4>
          <div style="margin: 8px 0;">
            <strong>当前版本：</strong> 
            <span style="color: #67C23A;">${this.systemVersion.current}</span>
          </div>
          <div style="margin: 8px 0;">
            <strong>构建日期：</strong> ${this.systemVersion.buildDate}
          </div>
          <div style="margin: 8px 0;">
            <strong>创建时间：</strong> ${this.systemVersion.createDate}
          </div>
          <div style="margin: 8px 0;">
            <strong>系统名称：</strong> 谷菱碰一碰同城获客系统
          </div>
          <div style="margin: 8px 0;">
            <strong>技术栈：</strong> Vue.js + Element UI + Spring Boot
          </div>
          <div style="margin: 8px 0;">
            <strong>更新状态：</strong> 
            <span style="color: ${this.systemVersion.hasUpdate ? '#E6A23C' : '#67C23A'};">
              ${this.systemVersion.hasUpdate ? '有新版本可用' : '已是最新版本'}
            </span>
          </div>
          <div style="margin: 16px 0 8px 0; font-size: 12px; color: #909399;">
            © 2024-2025 谷菱网络科技有限公司
          </div>
        </div>
      `
      
      this.$alert(versionInfo, '版本信息', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: calc(100vh - 84px);

  // 通用section标题样式
  .section-title {
    color: #fff;
    font-size: 18px;
    font-weight: 600;
    margin: 0 0 20px 0;
    display: flex;
    align-items: center;
    text-shadow: 0 2px 4px rgba(0,0,0,0.1);
  }

  // 欢迎横幅
  .welcome-banner {
    margin-bottom: 24px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 16px;
    padding: 32px;
    box-shadow: 0 8px 32px rgba(102, 126, 234, 0.3);
    position: relative;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      right: 0;
      width: 200px;
      height: 200px;
      background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
      border-radius: 50%;
      transform: translate(50%, -50%);
    }

    .banner-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      position: relative;
      z-index: 1;

      .welcome-info {
        h1 {
          color: #fff;
          font-size: 32px;
          font-weight: 700;
          margin: 0 0 8px 0;
          text-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .welcome-subtitle {
          color: rgba(255,255,255,0.9);
          font-size: 18px;
          margin: 0 0 12px 0;
          font-weight: 500;
        }

        .current-time {
          color: rgba(255,255,255,0.8);
          font-size: 14px;
          margin: 0;
          font-weight: 400;
        }
      }

      .banner-actions {
        .el-button {
          margin-left: 12px;
          border-radius: 8px;
          font-weight: 500;
          box-shadow: 0 4px 12px rgba(0,0,0,0.15);
          
          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0,0,0,0.2);
          }
        }
      }
    }
  }

  // 统计数据卡片
  .stats-overview {
    margin-bottom: 24px;

    .stats-card {
      background: #fff;
      border-radius: 16px;
      padding: 24px;
      height: 140px;
      box-shadow: 0 4px 20px rgba(0,0,0,0.08);
      border: 1px solid rgba(255,255,255,0.1);
      transition: all 0.3s ease;
      position: relative;
      overflow: hidden;

      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 4px;
        background: linear-gradient(90deg, #667eea, #764ba2);
      }

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 30px rgba(0,0,0,0.12);
      }

      &.user-stats::before {
        background: linear-gradient(90deg, #4fc3f7, #29b6f6);
      }

      &.merchant-stats::before {
        background: linear-gradient(90deg, #66bb6a, #43a047);
      }

      &.revenue-stats::before {
        background: linear-gradient(90deg, #ffa726, #ff9800);
      }

      &.growth-stats::before {
        background: linear-gradient(90deg, #ab47bc, #8e24aa);
      }

      .card-content {
        display: flex;
        align-items: center;
        height: 100%;

        .card-icon {
          width: 64px;
          height: 64px;
          border-radius: 16px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 20px;
          background: linear-gradient(135deg, #667eea, #764ba2);
          box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);

          i {
            font-size: 28px;
            color: #fff;
          }
        }

        .card-info {
          flex: 1;

          .card-value {
            font-size: 28px;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 4px;
            line-height: 1;
          }

          .card-label {
            font-size: 14px;
            color: #7f8c8d;
            margin-bottom: 8px;
            font-weight: 500;
          }

          .card-trend {
            font-size: 12px;
            font-weight: 600;
            display: flex;
            align-items: center;
            
            &.up {
              color: #27ae60;
            }

            &.down {
              color: #e74c3c;
            }

            i {
              margin-right: 4px;
              font-size: 14px;
            }
          }
        }
      }
    }
  }

  // 图表区域
  .charts-section {
    margin-bottom: 24px;

    .chart-card {
      background: #fff;
      border-radius: 16px;
      padding: 24px;
      box-shadow: 0 4px 20px rgba(0,0,0,0.08);
      border: 1px solid rgba(255,255,255,0.1);
      
      &.main-chart {
        background: linear-gradient(135deg, #fff 0%, #f8f9fa 100%);
      }

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 24px;
        padding-bottom: 16px;
        border-bottom: 2px solid #f1f2f6;

        h3 {
          margin: 0;
          font-size: 18px;
          color: #2c3e50;
          font-weight: 600;
        }

        .card-actions {
          .el-radio-group {
            .el-radio-button__inner {
              border-radius: 6px;
              font-weight: 500;
            }
          }
        }
      }

      .chart-container {
        height: 350px;
        
        .chart-placeholder {
          height: 100%;
          background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          flex-direction: column;
          border: 2px dashed #dee2e6;

          .chart-demo {
            text-align: center;
            
            .chart-bars {
              display: flex;
              align-items: end;
              justify-content: center;
              gap: 8px;
              margin-bottom: 16px;
              height: 120px;

              .bar {
                width: 20px;
                background: linear-gradient(135deg, #667eea, #764ba2);
                border-radius: 4px 4px 0 0;
                animation: growUp 1s ease-out;
              }
            }

            p {
              color: #6c757d;
              font-size: 14px;
              margin: 0;
              font-weight: 500;
            }
          }

          .pie-chart-demo {
            position: relative;
            width: 160px;
            height: 160px;
            border-radius: 50%;
            background: conic-gradient(
              #667eea 0% 35%,
              #43a047 35% 60%,
              #ff9800 60% 80%,
              #e0e0e0 80% 100%
            );
            display: flex;
            align-items: center;
            justify-content: center;

            .pie-center {
              width: 80px;
              height: 80px;
              background: #fff;
              border-radius: 50%;
              display: flex;
              flex-direction: column;
              align-items: center;
              justify-content: center;
              box-shadow: 0 2px 8px rgba(0,0,0,0.1);

              .pie-total {
                font-size: 18px;
                font-weight: 700;
                color: #2c3e50;
              }

              .pie-label {
                font-size: 10px;
                color: #7f8c8d;
              }
            }
          }
        }
      }
    }
  }

  // 操作中心
  .operation-center {
    .operation-card {
      background: #fff;
      border-radius: 16px;
      padding: 24px;
      box-shadow: 0 4px 20px rgba(0,0,0,0.08);
      border: 1px solid rgba(255,255,255,0.1);
      height: 500px;

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 24px;
        padding-bottom: 16px;
        border-bottom: 2px solid #f1f2f6;

        h3 {
          margin: 0;
          font-size: 18px;
          color: #2c3e50;
          font-weight: 600;
        }

        .el-link {
          font-weight: 500;
        }
      }

      // 快捷操作网格
      .quick-actions-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 16px;

        .action-item {
          display: flex;
          align-items: center;
          padding: 20px;
          border-radius: 12px;
          background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
          border: 2px solid transparent;
          cursor: pointer;
          transition: all 0.3s ease;

          &:hover {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-color: #667eea;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);

            .action-icon i {
              color: #fff;
            }

            .action-content .action-title,
            .action-content .action-desc {
              color: #fff;
            }
          }

          .action-icon {
            width: 48px;
            height: 48px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 16px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);

            i {
              font-size: 20px;
              color: #fff;
              transition: all 0.3s ease;
            }

            &.merchant {
              background: linear-gradient(135deg, #4fc3f7, #29b6f6);
            }

            &.report {
              background: linear-gradient(135deg, #66bb6a, #43a047);
            }

            &.settings {
              background: linear-gradient(135deg, #ffa726, #ff9800);
            }

            &.message {
              background: linear-gradient(135deg, #ab47bc, #8e24aa);
            }
          }

          .action-content {
            .action-title {
              font-size: 16px;
              font-weight: 600;
              color: #2c3e50;
              margin-bottom: 4px;
              transition: all 0.3s ease;
            }

            .action-desc {
              font-size: 12px;
              color: #7f8c8d;
              transition: all 0.3s ease;
            }
          }
        }
      }

      // 系统消息
      .system-messages {
        max-height: 400px;
        overflow-y: auto;

        .message-item {
          display: flex;
          align-items: center;
          padding: 16px 0;
          border-bottom: 1px solid #f1f2f6;
          transition: all 0.3s ease;

          &:last-child {
            border-bottom: none;
          }

          &:hover {
            background: linear-gradient(135deg, rgba(102, 126, 234, 0.05), rgba(118, 75, 162, 0.05));
            border-radius: 8px;
            padding-left: 12px;
            padding-right: 12px;
          }

          .message-indicator {
            width: 8px;
            height: 8px;
            border-radius: 50%;
            margin-right: 16px;
            flex-shrink: 0;

            &.info {
              background: #3498db;
            }

            &.success {
              background: #27ae60;
            }

            &.warning {
              background: #f39c12;
            }

            &.error {
              background: #e74c3c;
            }
          }

          .message-content {
            flex: 1;

            .message-title {
              font-size: 14px;
              font-weight: 600;
              color: #2c3e50;
              margin-bottom: 4px;
            }

            .message-desc {
              font-size: 12px;
              color: #7f8c8d;
              margin-bottom: 4px;
            }

            .message-time {
              font-size: 11px;
              color: #bdc3c7;
            }
          }

          .message-status {
            margin-left: 12px;
          }
        }
      }
    }
  }
}

// 动画效果
@keyframes growUp {
  from {
    height: 0;
  }
  to {
    height: var(--height);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .dashboard-container {
    padding: 16px;

    .welcome-banner .banner-content {
      flex-direction: column;
      text-align: center;

      .banner-actions {
        margin-top: 20px;
      }
    }

    .charts-section .el-col:first-child {
      margin-bottom: 20px;
    }

    .operation-center {
      .quick-actions-grid {
        grid-template-columns: 1fr;
      }
    }
  }
}
</style>
