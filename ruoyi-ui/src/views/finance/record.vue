<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>交易记录</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleExport">导出</el-button>
        </div>
      </div>
      
      <!-- 搜索条件 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
        <el-form-item label="交易号" prop="tradeNo">
          <el-input
            v-model="queryParams.tradeNo"
            placeholder="请输入交易号"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="交易类型" prop="tradeType">
          <el-select v-model="queryParams.tradeType" placeholder="交易类型" clearable size="small">
            <el-option label="收入" value="1" />
            <el-option label="支出" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易时间">
          <el-date-picker
            v-model="dateRange"
            size="small"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 交易列表 -->
      <el-table v-loading="loading" :data="recordList">
        <el-table-column label="交易号" align="center" prop="tradeNo" />
        <el-table-column label="交易类型" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.tradeType === '1' ? 'success' : 'danger'">
              {{ scope.row.tradeType === '1' ? '收入' : '支出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="交易金额" align="center">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.tradeType === '1' ? '#67C23A' : '#F56C6C' }">
              {{ scope.row.tradeType === '1' ? '+' : '-' }}{{ scope.row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="交易说明" align="center" prop="remark" show-overflow-tooltip />
        <el-table-column label="交易时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" align="center" width="120">
          <template slot-scope="scope">
            <el-button type="text" @click="handleDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>
  </div>
</template>

<script>
import { parseTime } from '@/utils'

export default {
  name: 'FinanceRecord',
  data() {
    return {
      // 遮罩层
      loading: false,
      // 总条数
      total: 0,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tradeNo: undefined,
        tradeType: undefined
      },
      // 交易记录列表
      recordList: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      // 模拟数据，实际项目中应该调用API
      setTimeout(() => {
        this.recordList = [
          {
            tradeNo: '20250714001',
            tradeType: '1',
            amount: '1000.00',
            remark: '代理分成收入',
            createTime: '2025-07-14 10:00:00'
          },
          {
            tradeNo: '20250714002',
            tradeType: '2',
            amount: '500.00',
            remark: '提现',
            createTime: '2025-07-14 11:00:00'
          }
        ]
        this.total = 2
        this.loading = false
      }, 1000)
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleExport() {
      this.$message.success('正在导出数据，请稍候...')
    },
    handleDetail(row) {
      this.$message.success('查看详情：' + row.tradeNo)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}
</style>
