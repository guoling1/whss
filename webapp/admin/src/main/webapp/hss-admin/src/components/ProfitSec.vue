<template>
  <div id="profitSec">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">二级代理分润</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul>
            <li class="same">
              <label>交易日期:</label>
              <el-date-picker style="width: 190px" v-model="date" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
              </el-date-picker>
            </li>
            <li class="same">
              <label>上级代理名称:</label>
              <el-input style="width: 120px" v-model="query.proxyName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>代理商名称:</label>
              <el-input style="width: 120px" v-model="query.proxyName1" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>代理商编号:</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>收益类型:</label>
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
            <el-table-column   width="100" label="序号">
              <template scope="scope">
                <div v-if="records[scope.$index].businessType!='总额'">{{scope.$index+1}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="proxyName" label="上级代理商名称"></el-table-column>
            <el-table-column prop="proxyName1" label="二级代理商名称"></el-table-column>
            <el-table-column prop="markCode" label="代理编号"></el-table-column>
            <el-table-column prop="splitDate" :formatter="changeTime" label="收益日期"></el-table-column>
            <el-table-column prop="businessType" label="收益类型"></el-table-column>
            <el-table-column prop="splitTotalAmount" label="收益金额" align="right" header-align="left"></el-table-column>
            <el-table-column label="操作" width="100">
              <template scope="scope">
                <router-link
                  :to="{path:'/admin/record/profitSecDet',query:{id:records[scope.$index].receiptMoneyAccountId}}"
                  v-if="records[scope.$index].totalMoney!=0&&records[scope.$index].businessType!='总额'" type="text" size="small">明细
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
    name: 'profitCom',
    data(){
      return {
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        date:'',
        query:{
          pageNo:1,
          pageSize:10,
          startTime: '',
          endTime: '',
          proxyName:'',
          proxyName1:'',
          markCode:'',
          businessType:''
        },
        records: [],
        count: 0,
        total: 0,
        currentPage: 1,
        loading: true,
      }
    },
    created: function () {
      this.getData();
    },
    methods: {
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/allProfit/secondProfit', this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            }
            var total=0;
            for (let i = 0; i < this.$data.records.length; i++) {
              this.$data.records[i].splitTotalAmount = toFix(this.$data.records[i].splitTotalAmount);
              total = toFix(parseFloat(total)+parseFloat(this.$data.records[i].splitTotalAmount))
            }
            if(this.records.length!=0){
              this.records.push({
                businessType:"总额",
                splitTotalAmount:total
              })
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
      //格式化时间
      changeTime: function (row, column) {
        var val = row.splitDate;
        if (val == '' || val == null) {
          return ''
        } else {
          val = new Date(val)
          var year = val.getFullYear();
          var month = val.getMonth() + 1;
          var date = val.getDate();
          var hour = val.getHours();
          var minute = val.getMinutes();
          var second = val.getSeconds();

          function tod(a) {
            if (a < 10) {
              a = "0" + a
            }
            return a;
          }

          return year + "-" + tod(month) + "-" + tod(date) + " " + tod(hour) + ":" + tod(minute) + ":" + tod(second);
        }
      },
      search(){
        this.$data.query.pageNo = 1;
        this.getData();
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.getData()
      },
      //每页条数改变
      handleSizeChange(val) {
        this.$data.query.pageNo = 1;
        this.$data.query.pageSize = val;
        this.getData()
      },
    },
    watch:{
      date:function (val,oldVal) {
        if(val[0]!=null){
          for(var j=0;j<val.length;j++){
            var str = val[j];
            var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
            for(var i = 0, len = ary.length; i < len; i ++) {
              if(ary[i] < 10) {
                ary[i] = '0' + ary[i];
              }
            }
            str = ary[0] + '-' + ary[1] + '-' + ary[2];
            if(j==0){
              this.$data.query.startTime = str;
            }else {
              this.$data.query.endTime = str;
            }
          }
        }else {
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
