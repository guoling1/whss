<template>
  <div id="profitCom">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">分润明细</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul>
            <li class="same">
              <label>交易订单号:</label>
              <el-input style="width: 120px" v-model="query.orderNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>分润方名称:</label>
              <el-input style="width: 120px" v-model="query.receiptMoneyUserName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>业务类型:</label>
              <el-select style="width: 140px" clearable v-model="query.businessType" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="好收收-收款" value="hssPay">好收收-收款</el-option>
                <el-option label="好收收-提现" value="hssWithdraw">好收收-提现</el-option>
                <el-option label="好收收-升级费" value="hssUpgrade">好收收-升级费</el-option>
                <el-option label="好收银-收款" value="hsyPay">好收银-收款</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="62" label="序号"></el-table-column>
            <el-table-column prop="splitSn" label="分润流水号" width="218"></el-table-column>
            <el-table-column prop="businessType" label="业务类型"></el-table-column>
            <el-table-column prop="splitDate" label="分润时间" :formatter="changeTime" width="152"></el-table-column>
            <el-table-column prop="orderNo" label="交易订单号" width="218"></el-table-column>
            <el-table-column prop="statisticsDate" label="结算周期"></el-table-column>
            <el-table-column prop="statisticsDate" label="结算时间"></el-table-column>
            <el-table-column prop="splitTotalAmount" label="分润总额" align="right" :formatter="changeTotal"></el-table-column>
            <el-table-column prop="outMoneyAccountId" label="分润出款账户"></el-table-column>
            <el-table-column prop="receiptMoneyUserName" label="分润方名称"></el-table-column>
            <el-table-column prop="statisticsDate" label="分润方类型"></el-table-column>
            <el-table-column prop="splitAmount" align="right" header-align="left" label="分润金额" :formatter="changePrice"></el-table-column>
            <el-table-column prop="remark" label="备注信息"></el-table-column>
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
    name: 'profitDet',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          orderNo:'',
          receiptMoneyUserName:'',
          businessType:''
        },
        records: [],
        count: 0,
        total: 0,
        loading: true,
        path:''
      }
    },
    created: function () {
      if(this.$route.path=="/admin/record/profitDet"){
        this.$data.path = '/admin/queryProfit/profitDetails'
      }else if(this.$route.path=="/admin/record/profitComDet"){
        this.$data.path = '/admin/allProfit/companyProfitDetail';
        this.$data.query.accId = this.$route.query.id;
      }else if(this.$route.path=="/admin/record/profitFirDet"){
        this.$data.path = '/admin/allProfit/firstDealerDetail';
        this.$data.query.receiptMoneyAccountId = this.$route.query.id;
      }else if(this.$route.path=="/admin/record/profitSecDet"){
        this.$data.path = '/admin/allProfit/secondDealerDetail';
        this.$data.query.receiptMoneyAccountId = this.$route.query.id;
      }
      this.getData();
    },
    methods: {
      getData: function () {
        this.loading = true;
        this.$http.post(this.$data.path, this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            /*var toFix = function (val) {
             return parseFloat(val).toFixed(2)
             }
             for (let i = 0; i < this.$data.records.length; i++) {
             this.$data.records[i].collectMoney = toFix(this.$data.records[i].collectMoney)
             this.$data.records[i].withdrawMoney = toFix(this.$data.records[i].withdrawMoney)
             this.$data.records[i].totalMoney = toFix(this.$data.records[i].totalMoney)
             }*/
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      //格式化时间
      changeTime: function (row, column) {
        var val=row.splitDate;
        if(val==''||val==null){
          return ''
        }else {
          val = new Date(val)
          var year=val.getFullYear();
          var month=val.getMonth()+1;
          var date=val.getDate();
          var hour=val.getHours();
          var minute=val.getMinutes();
          var second=val.getSeconds();
          function tod(a) {
            if(a<10){
              a = "0"+a
            }
            return a;
          }
          return year+"-"+tod(month)+"-"+tod(date)+" "+tod(hour)+":"+tod(minute)+":"+tod(second);
        }
      },
      //格式化金额
      changeTotal: function (row, colum) {
        var val =row.splitTotalAmount;
        return parseFloat(val).toFixed(2)
      },
      changePrice: function (row, colum) {
        var val =row.splitAmount;
        return parseFloat(val).toFixed(2)
      },
      search(){
        this.$data.query.pageNo = 1;
        this.getData();
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.getData();
      },
      //每页条数改变
      handleSizeChange(val) {
        this.$data.query.pageNo = 1;
        this.$data.query.pageSize = val;
        this.getData()
      },
    },
  }
</script>
<style scoped lang="less">
  ul{
    padding: 0;
  }
  .same{
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }
</style>
