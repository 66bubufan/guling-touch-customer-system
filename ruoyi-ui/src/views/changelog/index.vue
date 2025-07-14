<template>
  <div class="app-container">
    <el-timeline>
      <el-timeline-item
        v-for="(log, index) in logs"
        :key="index"
        :timestamp="log.date"
        :type="log.type">
        <el-card>
          <h4>{{ log.title }}</h4>
          <p v-for="(item, idx) in log.content" :key="idx">{{ item }}</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script>
export default {
  name: 'Changelog',
  data() {
    return {
      logs: [
        {
          date: '2025-07-14',
          title: 'v1.2.0 发布',
          type: 'success',
          content: [
            '1. 新增批量导入功能',
            '2. 优化系统性能',
            '3. 修复已知问题'
          ]
        },
        {
          date: '2025-07-01',
          title: 'v1.1.0 发布',
          type: 'primary',
          content: [
            '1. 新增数据分析功能',
            '2. 优化用户体验',
            '3. 修复已知问题'
          ]
        }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;

  .el-timeline {
    padding: 20px;
  }

  .el-card {
    h4 {
      margin: 0 0 10px 0;
    }
    p {
      margin: 5px 0;
      color: #606266;
      font-size: 14px;
    }
  }
}
</style>
