<template>
  <div id="profitAccount">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">公司账户</h3>
        </div>
        <div class="box-body">
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="100" label="序号"></el-table-column>
            <el-table-column prop="userName" label="账户名称"></el-table-column>
            <el-table-column prop="totalAmount" label="账户总额（元）" align="right" header-align="left"></el-table-column>
            <el-table-column prop="dueSettleAmount" label="待结算金额（元）" align="right" header-align="left"></el-table-column>
            <el-table-column prop="available" label="可用余额（元）" align="right" header-align="left"></el-table-column>
            <el-table-column label="操作" width="70">
              <template scope="scope">
                <router-link target="_blank" :to="{path:'/admin/details/profitAccountDet',query:{id:records[scope.$index].id}}" type="text" size="small" v-if="records[scope.$index].totalAmount!=0||records[scope.$index].dueSettleAmount!=0||records[scope.$index].available!=0">明细
                </router-link>
              </template>
            </el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" layout="total, prev, pager, next, jumper" :total="count">
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
        total: 0,
        currentPage: 1,
        loading: true,
      }
    },
    created: function () {
      this.$http.post('/admin/queryJkmProfit/accountList', this.$data.query)
        .then(function (res) {
          this.$data.records = res.data.records;
          this.$data.count = res.data.count;
          this.$data.total = res.data.totalPage;
          this.$data.loading = false;
          var toFix = function (val) {
            return parseFloat(val).toFixed(2)
          }
          for (let i = 0; i < this.$data.records.length; i++) {
            this.$data.records[i].totalAmount = toFix(this.$data.records[i].totalAmount)
            this.$data.records[i].dueSettleAmount = toFix(this.$data.records[i].dueSettleAmount)
            this.$data.records[i].available = toFix(this.$data.records[i].available)
          }
        }, function (err) {
          this.$data.loading = false;
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
    },
    methods: {
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.$data.loading = true;
        this.$http.post('/admin/queryJkmProfit/accountList', this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            }
            for (let i = 0; i < this.$data.records.length; i++) {
              this.$data.records[i].totalAmount = toFix(this.$data.records[i].totalAmount)
              this.$data.records[i].dueSettleAmount = toFix(this.$data.records[i].dueSettleAmount)
              this.$data.records[i].available = toFix(this.$data.records[i].available)
            }
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
    },
  }
</script>
<style scoped lang="less">

</style>
