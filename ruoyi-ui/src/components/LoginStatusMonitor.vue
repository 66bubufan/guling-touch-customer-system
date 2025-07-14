<template>
  <div class="login-status-monitor" v-if="showMonitor && !isLoginPage">
    <el-alert
      :title="statusMessage"
      :type="statusType"
      :closable="false"
      show-icon
      :description="statusDescription"
    >
      <template slot="title">
        <span>{{ statusMessage }}</span>
        <el-button 
          v-if="showRefreshButton" 
          type="text" 
          size="small" 
          @click="refreshToken"
          :loading="refreshing"
        >
          刷新登录状态
        </el-button>
      </template>
    </el-alert>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import { getInfo } from '@/api/login'

export default {
  name: 'LoginStatusMonitor',
  data() {
    return {
      showMonitor: false,
      statusMessage: '',
      statusType: 'info',
      statusDescription: '',
      showRefreshButton: false,
      refreshing: false,
      checkInterval: null,
      lastCheckTime: null
    }
  },
  computed: {
    isLoginPage() {
      return this.$route.path === '/login' || this.$route.path === '/register'
    }
  },
  mounted() {
    this.startMonitoring()
  },
  beforeDestroy() {
    this.stopMonitoring()
  },
  methods: {
    startMonitoring() {
      // 每5分钟检查一次登录状态
      this.checkInterval = setInterval(() => {
        this.checkLoginStatus()
      }, 5 * 60 * 1000)
      
      // 页面获得焦点时检查
      window.addEventListener('focus', this.checkLoginStatus)
      
      // 初始检查
      this.checkLoginStatus()
    },
    
    stopMonitoring() {
      if (this.checkInterval) {
        clearInterval(this.checkInterval)
        this.checkInterval = null
      }
      window.removeEventListener('focus', this.checkLoginStatus)
    },
    
    async checkLoginStatus() {
      // 如果在登录页面，不进行检查
      if (this.isLoginPage) {
        this.hideStatus()
        return
      }
      
      const token = getToken()
      if (!token) {
        this.showStatus('warning', '未检测到登录状态', '请重新登录系统', false)
        return
      }
      
      try {
        const res = await getInfo()
        if (res.code === 200) {
          this.hideStatus()
          this.lastCheckTime = new Date()
        } else {
          this.showStatus('error', '登录状态异常', '请重新登录系统', true)
        }
      } catch (error) {
        console.error('检查登录状态失败:', error)
        this.showStatus('error', '登录状态检查失败', '网络连接异常，请检查网络后重试', true)
      }
    },
    
    showStatus(type, message, description, showRefresh = false) {
      this.statusType = type
      this.statusMessage = message
      this.statusDescription = description
      this.showRefreshButton = showRefresh
      this.showMonitor = true
    },
    
    hideStatus() {
      this.showMonitor = false
    },
    
    async refreshToken() {
      this.refreshing = true
      try {
        await this.checkLoginStatus()
        this.$message.success('登录状态刷新成功')
      } catch (error) {
        this.$message.error('登录状态刷新失败')
      } finally {
        this.refreshing = false
      }
    }
  }
}
</script>

<style scoped>
.login-status-monitor {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
  max-width: 400px;
  min-width: 300px;
}

.el-alert {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
</style> 