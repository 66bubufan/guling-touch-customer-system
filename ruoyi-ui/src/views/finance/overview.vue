<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-header">
            <span>总收入</span>
          </div>
          <div class="card-body">
            <count-to :start-val="0" :end-val="12580" :duration="2000" prefix="¥" class="card-number" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-header">
            <span>待结算</span>
          </div>
          <div class="card-body">
            <count-to :start-val="0" :end-val="3580" :duration="2000" prefix="¥" class="card-number" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-header">
            <span>已提现</span>
          </div>
          <div class="card-body">
            <count-to :start-val="0" :end-val="9000" :duration="2000" prefix="¥" class="card-number" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-header">
            <span>交易笔数</span>
          </div>
          <div class="card-body">
            <count-to :start-val="0" :end-val="156" :duration="2000" class="card-number" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="chart-card" style="margin-top: 20px;">
      <div slot="header" class="clearfix">
        <span>收入趋势</span>
      </div>
      <!-- 这里放图表组件 -->
    </el-card>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
  name: 'FinanceOverview',
  components: {
    CountTo
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.card-header {
  font-size: 14px;
  color: #606266;
  margin-bottom: 15px;
}

.card-body {
  text-align: center;
  padding: 10px 0;

  .card-number {
    font-size: 24px;
    font-weight: bold;
    color: #303133;
  }
}
</style>
