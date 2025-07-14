<template>
  <div class="workbench-container">
    <!-- 基本信息区域 -->
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="platform-info-card" shadow="never">
          <div class="platform-version">
            <div class="info-row">
              <span class="label">平台名称：</span>
              <span class="value">{{ platformInfo.name }}</span>
            </div>
            <div class="info-row">
              <span class="label">当前版本：</span>
              <span class="value">{{ platformInfo.version }}</span>
              <el-tag size="small" type="success" v-if="platformInfo.status === '正常运行'">{{ platformInfo.status }}</el-tag>
            </div>
            <div class="info-row">
              <span class="label">运行状态：</span>
              <span class="value">{{ platformInfo.runningStatus }}</span>
            </div>
            <div class="info-row">
              <span class="label">服务器时间：</span>
              <span class="value">{{ platformInfo.serverTime }}</span>
            </div>
            <div class="info-row">
              <span class="label">在线用户：</span>
              <span class="value">{{ platformInfo.onlineUsers }}人</span>
            </div>
          </div>

          <!-- 统计数据 -->
          <el-row :gutter="20" class="stat-cards">
            <el-col :span="6" v-for="(stat, index) in statistics" :key="index">
              <div class="stat-card">
                <count-to
                  :start-val="0"
                  :end-val="stat.value"
                  :duration="2000"
                  :decimals="stat.decimals || 0"
                  class="stat-number"
                />
                <div class="stat-title">{{ stat.title }}</div>
                <div class="stat-trend" :class="{'up': stat.trend > 0, 'down': stat.trend < 0}">
                  <i :class="stat.trend > 0 ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"></i>
                  {{ Math.abs(stat.trend) }}%
                </div>
              </div>
            </el-col>
          </el-row>

          <!-- 常用功能区 -->
          <div class="common-functions">
            <h3 class="section-title">常用功能</h3>
            <div class="function-list">
              <div class="function-item" v-for="(func, index) in commonFunctions" :key="index" @click="handleFunctionClick(func)">
                <i :class="func.icon"></i>
                <span>{{ func.name }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 系统公告区域 -->
      <el-col :span="8">
        <el-card class="announcement-card" shadow="never">
          <div slot="header" class="card-header">
            <span>系统公告</span>
            <el-button type="text" @click="viewMoreAnnouncements">查看更多</el-button>
          </div>
          <div class="announcement-list">
            <div v-for="(notice, index) in announcements" :key="index" class="announcement-item">
              <div class="notice-title">
                <i class="el-icon-bell"></i>
                <span>{{ notice.title }}</span>
              </div>
              <div class="notice-time">{{ notice.time }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import { getWorkbenchData } from '@/api/dashboard'
import { parseTime } from '@/utils'

export default {
  name: 'Workbench',
  components: {
    CountTo
  },
  data() {
    return {
      platformInfo: {
        name: 'NFC碰一碰获客管理平台',
        version: 'v1.2.0',
        status: '正常运行',
        runningStatus: '正常',
        serverTime: '',
        onlineUsers: 156
      },
      statistics: [
        { title: '总剩余算力', value: 1000000, decimals: 0, trend: 12.5 },
        { title: '消耗算力', value: 311248, decimals: 0, trend: 8.3 },
        { title: '代理数', value: 2, decimals: 0, trend: 0 },
        { title: '客户数', value: 18, decimals: 0, trend: 33.1 }
      ],
      commonFunctions: [
        { name: '代理分成', icon: 'el-icon-s-cooperation' },
        { name: '商户管理', icon: 'el-icon-s-shop' },
        { name: '安装记录', icon: 'el-icon-document' },
        { name: '推广获客', icon: 'el-icon-share' },
        { name: '素材中心', icon: 'el-icon-picture' },
        { name: '财力提现', icon: 'el-icon-money' },
        { name: '代理申请', icon: 'el-icon-user-solid' }
      ],
      announcements: [
        { title: '系统将于今日24:00进行维护升级', time: '2024-01-15 10:30' },
        { title: '新增NFC设备批量导入功能', time: '2024-01-14 16:30' },
        { title: '优化平台数据分析报表', time: '2024-01-13 09:15' }
      ]
    }
  },
  created() {
    this.initData()
    this.startTimeUpdate()
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    async initData() {
      try {
        const res = await getWorkbenchData()
        if (res.code === 200) {
          // 更新数据
          this.updatePageData(res.data)
        }
      } catch (error) {
        console.error('获取工作台数据失败:', error)
      }
    },
    updatePageData(data) {
      // 更新页面数据
      if (data.platformInfo) {
        this.platformInfo = { ...this.platformInfo, ...data.platformInfo }
      }
      if (data.statistics) {
        this.statistics = data.statistics
      }
      if (data.announcements) {
        this.announcements = data.announcements
      }
    },
    startTimeUpdate() {
      this.updateServerTime()
      this.timer = setInterval(this.updateServerTime, 1000)
    },
    updateServerTime() {
      this.platformInfo.serverTime = parseTime(new Date())
    },
    handleFunctionClick(func) {
      // 处理功能点击
      this.$router.push(func.path || '/')
    },
    viewMoreAnnouncements() {
      this.$router.push('/system/notice')
    }
  }
}
</script>

<style lang="scss" scoped>
.workbench-container {
  padding: 15px;
  background: #f0f2f5;

  .platform-info-card {
    margin-bottom: 20px;

    .platform-version {
      margin-bottom: 20px;

      .info-row {
        margin-bottom: 10px;
        line-height: 24px;
        display: flex;
        align-items: center;

        .label {
          color: #606266;
          width: 100px;
        }

        .value {
          color: #303133;
          margin-right: 10px;
        }
      }
    }

    .stat-cards {
      margin: 20px 0;

      .stat-card {
        background: #f8f9fa;
        padding: 15px;
        border-radius: 4px;
        text-align: center;

        .stat-number {
          font-size: 24px;
          color: #303133;
          font-weight: bold;
          margin-bottom: 8px;
        }

        .stat-title {
          color: #606266;
          font-size: 14px;
          margin-bottom: 8px;
        }

        .stat-trend {
          font-size: 12px;
          &.up { color: #67c23a; }
          &.down { color: #f56c6c; }

          i {
            margin-right: 4px;
          }
        }
      }
    }
  }

  .common-functions {
    .section-title {
      font-size: 16px;
      color: #303133;
      margin-bottom: 15px;
      font-weight: bold;
    }

    .function-list {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;

      .function-item {
        display: flex;
        align-items: center;
        cursor: pointer;
        padding: 8px 15px;
        border-radius: 4px;
        transition: all 0.3s;

        &:hover {
          background: #f5f7fa;
        }

        i {
          font-size: 18px;
          margin-right: 8px;
          color: #409EFF;
        }

        span {
          color: #606266;
        }
      }
    }
  }

  .announcement-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .announcement-list {
      .announcement-item {
        padding: 10px 0;
        border-bottom: 1px solid #ebeef5;

        &:last-child {
          border-bottom: none;
        }

        .notice-title {
          display: flex;
          align-items: center;
          margin-bottom: 5px;

          i {
            color: #409EFF;
            margin-right: 8px;
          }

          span {
            color: #303133;
            font-size: 14px;
          }
        }

        .notice-time {
          color: #909399;
          font-size: 12px;
        }
      }
    }
  }
}
</style>