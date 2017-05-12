<template>
  <div id="profitAccount">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">公司账户</h3>
        </div>
        <div class="box-body">
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border>
            <el-table-column type="index" width="100" label="序号"></el-table-column>
            <el-table-column prop="userName" label="账户名称"></el-table-column>
            <el-table-column label="账户总额（元）" align="right" header-align="left">
              <template scope="scope">
                <span>{{scope.row.totalAmount|toFix}}</span>
              </template>
            </el-table-column>
            <el-table-column label="待结算金额（元）" align="right" header-align="left">
              <template scope="scope">
                <span>{{scope.row.dueSettleAmount|toFix}}</span>
              </template>
            </el-table-column>
            <el-table-column label="可用余额（元）" align="right" header-align="left">
              <template scope="scope">
                <span>{{scope.row.available|toFix}}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="70">
              <template scope="scope">
                <router-link target="_blank" :to="{path:'/admin/details/profitAccountDet',query:{id:scope.row.id}}" type="text" size="small" v-if="scope.row.totalAmount!=0||scope.row.dueSettleAmount!=0||scope.row.available!=0">明细
                </router-link>
              </template>
            </el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.pageNo"
                           :page-sizes="[10, 20, 50]"
                           :page-size="query.pageSize"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="count">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'profitAccount',
    data(){
      return {
        records: [],
        query:{
          pageNo:1,
          pageSize:10
        },
        count: 0,
        currentPage: 1,
        loading: true
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/queryJkmProfit/accountList', this.query)
          .then(function (res) {
            this.records = res.data.records;
            this.count = res.data.count;
            this.loading = false;
          }, function (err) {
            this.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      //每页条数改变
      handleSizeChange(val) {
        this.query.pageNo = 1;
        this.query.pageSize = val;
        this.getData()
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.query.pageNo = val;
        this.getData()
      }
    }
  }
</script>
<style scoped lang="less">

</style>
