<template>
  <div id="profitFir">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">一级代理商分润</h3>
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
              <label>代理商名称:</label>
              <el-input style="width: 120px" v-model="query.firstDealerName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="100" label="序号"></el-table-column>
            <el-table-column prop="firstDealerName" label="一级代理商名称"></el-table-column>
            <el-table-column prop="statisticsDate" label="收益日期"></el-table-column>
            <el-table-column prop="collectMoney" align="right" header-align="left" label="收单收益"></el-table-column>
            <el-table-column prop="withdrawMoney" label="结算收益" align="right" header-align="left"></el-table-column>
            <el-table-column prop="totalMoney" label="收益总额" align="right" header-align="left"></el-table-column>
            <el-table-column label="操作" width="100">
              <template scope="scope">
                <router-link :to="{path:'/admin/record/firProfitDet',query:{id:records[scope.$index].id}}" v-if="records[scope.$index].totalMoney!=0" type="text" size="small">明细
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
    name: 'profitFir',
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
          beginProfitDate:'',
          endProfitDate:'',
          firstDealerName:''
        },
        records: [],
        count: 0,
        total: 0,
        currentPage: 1,
        loading: true,
      }
    },
    created: function () {
      this.$http.post('/admin/profit/firstProfit', this.$data.query)
        .then(function (res) {
          this.$data.records = res.data.records;
          this.$data.count = res.data.count;
          this.$data.total = res.data.totalPage;
          this.$data.loading = false;
          var toFix = function (val) {
            return parseFloat(val).toFixed(2)
          }
          for (let i = 0; i < this.$data.records.length; i++) {
            this.$data.records[i].collectMoney = toFix(this.$data.records[i].collectMoney)
            this.$data.records[i].withdrawMoney = toFix(this.$data.records[i].withdrawMoney)
            this.$data.records[i].totalMoney = toFix(this.$data.records[i].totalMoney)
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
      search(){
        this.$data.query.pageNo = 1;
        this.$data.loading = true;
        this.$http.post('/admin/profit/firstProfit', this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            }
            for (let i = 0; i < this.$data.records.length; i++) {
              this.$data.records[i].collectMoney = toFix(this.$data.records[i].collectMoney)
              this.$data.records[i].withdrawMoney = toFix(this.$data.records[i].withdrawMoney)
              this.$data.records[i].totalMoney = toFix(this.$data.records[i].totalMoney)
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
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.$data.loading = true;
        this.$http.post('/admin/profit/firstProfit', this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            }
            for (let i = 0; i < this.$data.records.length; i++) {
              this.$data.records[i].collectMoney = toFix(this.$data.records[i].collectMoney)
              this.$data.records[i].withdrawMoney = toFix(this.$data.records[i].withdrawMoney)
              this.$data.records[i].totalMoney = toFix(this.$data.records[i].totalMoney)
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
              this.$data.query.beginProfitDate = str;
            }else {
              this.$data.query.endProfitDate = str;
            }
          }
        }else {
          this.$data.query.beginProfitDate = '';
          this.$data.query.endProfitDate = '';
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
