<template>
  <div id="profitCom">
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">分润明细</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary" v-if="isDet">关闭</a>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul style="margin-bottom: 0">
            <li class="same">
              <label>交易订单号:</label>
              <el-input style="width: 188px" v-model="query.orderNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>分润方名称:</label>
              <el-input style="width: 188px" v-model="query.receiptMoneyUserName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same" v-if="isShow">
              <label>业务类型:</label>
              <el-select style="width: 188px" clearable v-model="query.businessType" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="好收收-收款" value="hssPay">好收收-收款</el-option>
                <el-option label="好收收-提现" value="hssWithdraw">好收收-提现</el-option>
                <el-option label="好收收-升级费" value="hssUpgrade">好收收-升级费</el-option>
                <el-option label="好收银-收款" value="hsyPay">好收银-收款</el-option>
              </el-select>
            </li>
            <li class="same" v-if="isShow">
              <label>分润日期:</label>
              <el-date-picker
                style="width: 188px"
                v-model="date"
                type="daterange"
                align="right"
                placeholder="选择日期范围"
                :picker-options="pickerOptions2" size="small">
              </el-date-picker>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border :row-style="tableFoot">
            <el-table-column type="index" width="62" label="序号">
              <template scope="scope">
                <div v-if="records[scope.$index].settleType!='当页总额'&&records[scope.$index].settleType!='筛选条件统计'">{{scope.$index+1}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="splitSn" label="分润流水号" width="218"></el-table-column>
            <el-table-column prop="businessType" label="业务类型"></el-table-column>
            <el-table-column prop="splitDate" label="分润时间" :formatter="changeTime" width="152"></el-table-column>
            <el-table-column prop="orderNo" label="交易订单号" width="218"></el-table-column>
            <el-table-column prop="settleType" label="结算周期"></el-table-column>
            <el-table-column prop="splitTotalAmount" label="分润总额" align="right" :formatter="changeTotal"></el-table-column>
            <el-table-column prop="splitAmount" align="right" header-align="left" label="分润金额" :formatter="changePrice"></el-table-column>
            <el-table-column prop="outMoneyAccountName" label="分润出款账户"></el-table-column>
            <el-table-column prop="receiptMoneyUserName" label="分润方名称"></el-table-column>
            <el-table-column prop="profitType" label="分润方类型" v-if="isShow"></el-table-column>
            <el-table-column prop="remark" label="备注信息"></el-table-column>
          </el-table>
          <ul style="float: left;margin-top: 5px">
            <li>
              <label style="margin-right: 10px;">分润总额</label>
              <span>当页总额：{{pageTotal}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span>统计总额：{{addTotal}}&nbsp;元</span>
            </li>
            <li>
              <label style="margin-right: 10px;">分润金额</label>
              <span>当页总额：{{pageTotal1}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span>统计总额：{{addTotal1}}&nbsp;元</span>
            </li>
          </ul>
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
          businessType:'',
          splitDate:'',
          startTime:'',
          endTime:''
        },
        date:'',
        records: [],
        count: 0,
        total: '',
        price: '',
        loading: true,
        path:'',
        isShow:true,
        totalUrl:'',
        pageTotal:0,
        pageTotal1:0,
        addTotal:0,
        addTotal1:0,
        isDet:true
      }
    },
    created: function () {
      if(this.$route.path=="/admin/record/profitDet"){
        this.$data.path = '/admin/queryProfit/profitDetails';
        this.$data.totalUrl = '/admin/queryProfit/profitAmount'
        this.isDet = false
      }else if(this.$route.path=="/admin/details/profitComDet"){
        this.$data.path = '/admin/allProfit/companyProfitDetail';
        this.$data.totalUrl = '/admin/allProfit/ProfitDetailAmount';
        this.$data.query.accId = this.$route.query.id;
        this.$data.query.splitDate = this.$route.query.time;
        this.$data.query.businessType = this.$route.query.type;
        this.isShow =false
      }else if(this.$route.path=="/admin/details/profitFirDet"){
        this.$data.path = '/admin/allProfit/firstDealerDetail';
        this.$data.totalUrl = '/admin/allProfit/firstDetailAmount';
        this.isShow =false;
        this.$data.query.receiptMoneyAccountId = this.$route.query.id;
        this.$data.query.businessType = this.$route.query.type;
        this.$data.query.splitDate = this.$route.query.time;
      }else if(this.$route.path=="/admin/details/profitSecDet"){
        this.$data.path = '/admin/allProfit/secondDealerDetail';
        this.$data.totalUrl = '/admin/allProfit/secondDetailAmount';
        this.$data.query.receiptMoneyAccountId = this.$route.query.id;
        this.$data.query.splitDate = this.$route.query.time;
        this.$data.query.businessType = this.$route.query.type;
        this.isShow =false
      }
      this.currentDate();
      this.getData();
      this.getAddTotal()
    },
    methods: {
      currentDate: function () {
        let time = new Date();
        this.date = [time,time];
        for (var j = 0; j < this.date.length; j++) {
          var str = this.date[j];
          var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
          for (var i = 0, len = ary.length; i < len; i++) {
            if (ary[i] < 10) {
              ary[i] = '0' + ary[i];
            }
          }
          str = ary[0] + '-' + ary[1] + '-' + ary[2];
          if (j == 0) {
            this.$data.query.startTime = str;
          } else {
            this.$data.query.endTime = str;
          }
        }
      },
      reset: function () {
        this.query = {
          pageNo:1,
          pageSize:10,
          orderNo:'',
          receiptMoneyUserName:'',
          businessType:'',
          splitDate:'',
          startTime:'',
          endTime:''
        };
        this.currentDate()
      },
      getData: function () {
        this.loading = true;
        this.$http.post(this.$data.path, this.$data.query)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.$data.records = res.data.records;
            },1000)
            this.$data.count = res.data.count;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            };
            var total=0,price = 0;
            for (let i = 0; i < res.data.records.length; i++) {
              res.data.records[i].splitTotalAmount = toFix(res.data.records[i].splitTotalAmount);
              res.data.records[i].splitAmount = toFix(res.data.records[i].splitAmount);
              total = toFix(parseFloat(total)+parseFloat(res.data.records[i].splitTotalAmount))
              price = toFix(parseFloat(price)+parseFloat(res.data.records[i].splitAmount))
            }
            this.pageTotal = total;
            this.pageTotal1 = price;
          }, function (err) {
            setTimeout(()=>{
              this.loading = false;
            },1000)
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      getAddTotal(){
        this.$http.post(this.totalUrl,this.query)
          .then(res=>{
            this.addTotal = res.data.splitTotalAmount;
            this.addTotal1 = res.data.splitAmount;
          })
          .catch(err=>{
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      tableFoot(row, index) {
        if (row.settleType === '当页总额'||row.settleType === '筛选条件统计') {
          return {background:'#eef1f6'}
        }
        return '';
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
        if(val!=''){
          val = parseFloat(val).toFixed(2)
        }
        return val;
      },
      changePrice: function (row, colum) {
        var val =row.splitAmount;
        if(val!=''){
          val = parseFloat(val).toFixed(2)
        }
        return val;
      },
      search(){
        this.total = '';
        this.price = '';
        this.$data.query.pageNo = 1;
        this.getData();
        this.getAddTotal()
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
    watch: {
      date: function (val, oldVal) {
        if (val!=undefined&&val[0] != null) {
          for (var j = 0; j < val.length; j++) {
            var str = val[j];
            var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
            for (var i = 0, len = ary.length; i < len; i++) {
              if (ary[i] < 10) {
                ary[i] = '0' + ary[i];
              }
            }
            str = ary[0] + '-' + ary[1] + '-' + ary[2];
            if (j == 0) {
              this.$data.query.startTime = str;
            } else {
              this.$data.query.endTime = str;
            }
          }
        } else {
          this.$data.query.startTime = '';
          this.$data.query.endTime = '';
        }
      }
    }
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
