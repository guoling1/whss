<template>
  <div id="profitAccountDet">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">账户明细</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul>
            <li class="same">
              <label>交易日期:</label>
              <el-date-picker
                v-model="date"
                type="daterange"
                align="right"
                placeholder="选择日期范围"
                :picker-options="pickerOptions" size="small">
              </el-date-picker>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column prop="orderNo" label="流水号"></el-table-column>
            <el-table-column prop="changeTime" :formatter="changeTime" label="时间"></el-table-column>
            <el-table-column prop="beforeAmount" label="发生前余额（元）"></el-table-column>
            <el-table-column prop="incomeAmount" label="收入金额（元）"></el-table-column>
            <el-table-column prop="outAmount" label="支出金额（元）"></el-table-column>
            <el-table-column prop="afterAmount" label="发生后余额（元）"></el-table-column>
            <el-table-column label="业务类型">
              <template scope="scope">
                <span v-if="records[scope.$index].appId=='hss'">好收收</span>
                <span v-if="records[scope.$index].appId=='hsy'">好收银</span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注"></el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" layout="total, prev, pager, next, jumper" :total="total">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'profitAccountDet',
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
          startTime:'',
          endTime:'',
          id:''
        },
        records: [],
        count: 0,
        total: 0,
        currentPage: 1,
        loading: false,
        index: '',
      }
    },
    created: function () {
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
      this.$data.query.id = this.$route.query.id;
      this.$http.post('/admin/queryJkmProfit/accountDetails', this.$data.query)
        .then(function (res) {
          this.$data.records = res.data.records;
          this.$data.count = res.data.count;
          this.$data.total = res.data.totalPage;
          this.$data.loading = false;
          /*var changeTime = function (val) {
            if (val == '' || val == null) {
              return ''
            } else {
              val = new Date(val)
              var year = val.getFullYear();
              var month = val.getMonth() + 1;
              var date = val.getDate();

              function tod(a) {
                if (a < 10) {
                  a = "0" + a
                }
                return a;
              }

              return year + "-" + tod(month) + "-" + tod(date);
            }
          }
          for (let i = 0; i < this.$data.records.length; i++) {
            this.$data.records[i].tradeDate = changeTime(this.$data.records[i].tradeDate)
          }*/
        }, function (err) {
          this.$data.loading = false;
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
    },
    methods: {
      //格式化时间
      changeTime: function (row, column) {
        var val=row.changeTime;
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
      search(){
          this.currentPage = 1;
        this.$data.query.pageNo = 1;
        this.$data.loading = true;
        this.$http.post('/admin/queryJkmProfit/accountDetails', this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            var changeTime = function (val) {
              if (val == '' || val == null) {
                return ''
              } else {
                val = new Date(val)
                var year = val.getFullYear();
                var month = val.getMonth() + 1;
                var date = val.getDate();

                function tod(a) {
                  if (a < 10) {
                    a = "0" + a
                  }
                  return a;
                }

                return year + "-" + tod(month) + "-" + tod(date);
              }
            }
            for (let i = 0; i < this.$data.records.length; i++) {
              this.$data.records[i].tradeDate = changeTime(this.$data.records[i].tradeDate)
            }
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.$data.loading = true;
        this.$http.post('/admin/queryJkmProfit/accountDetails', this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            var changeTime = function (val) {
              if (val == '' || val == null) {
                return ''
              } else {
                val = new Date(val)
                var year = val.getFullYear();
                var month = val.getMonth() + 1;
                var date = val.getDate();

                function tod(a) {
                  if (a < 10) {
                    a = "0" + a
                  }
                  return a;
                }

                return year + "-" + tod(month) + "-" + tod(date);
              }
            }
            for (let i = 0; i < this.$data.records.length; i++) {
              this.$data.records[i].tradeDate = changeTime(this.$data.records[i].tradeDate)
            }
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
    },
    watch:{
      date:function (val,oldVal) {
        if(val!=undefined&&val[0]!=null){
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
