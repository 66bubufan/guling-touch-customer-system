<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>商家列表</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleAdd">新增商家</el-button>
      </div>
      
      <el-table v-loading="loading" :data="merchantList">
        <el-table-column label="商家ID" prop="merchantId" align="center" />
        <el-table-column label="商家名称" prop="merchantName" align="center" />
        <el-table-column label="联系人" prop="contact" align="center" />
        <el-table-column label="联系电话" prop="phone" align="center" />
        <el-table-column label="地址" prop="address" align="center" show-overflow-tooltip />
        <el-table-column label="创建时间" prop="createTime" align="center" />
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '1' ? 'success' : 'info'">
              {{ scope.row.status === '1' ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" @click="handleDelete(scope.row)" style="color: #F56C6C">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'MerchantList',
  data() {
    return {
      loading: false,
      merchantList: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      // 这里调用获取商家列表的API
      setTimeout(() => {
        this.merchantList = [
          {
            merchantId: '1',
            merchantName: '测试商家',
            contact: '张三',
            phone: '13800138000',
            address: '北京市朝阳区xxx街道xxx号',
            createTime: '2025-07-14',
            status: '1'
          }
        ]
        this.loading = false
      }, 1000)
    },
    handleAdd() {
      this.$message.success('点击了新增')
    },
    handleEdit(row) {
      this.$message.success('点击了编辑')
    },
    handleDelete(row) {
      this.$confirm('确认删除该商家吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}
</style>
