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
                :picker-options="pickerOptions"
                :clearable="false"
                @change="datetimeSelect"
                :editable="false" size="small">
              </el-date-picker>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column prop="orderNo" label="流水号"></el-table-column>
            <el-table-column prop="changeTime" label="时间">
              <template scope="scope">{{scope.row.changeTime|changeTime}}</template>
            </el-table-column>
            <el-table-column prop="beforeAmount" label="发生前余额（元）" align="right"></el-table-column>
            <el-table-column prop="incomeAmount" label="收入金额（元）" align="right"></el-table-column>
            <el-table-column prop="outAmount" label="支出金额（元）" align="right"></el-table-column>
            <el-table-column prop="afterAmount" label="发生后余额（元）" align="right"></el-table-column>
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
    name: 'profitAccountDet',
    data(){
      return {
        pickerOptions: {
          disabledDate: function (time) {
            return time.getTime() > Date.now() - 8.64e7;
          },
          onPick: function ({maxDate, minDate}) {
            if (maxDate == '' || maxDate == null) {
              this.disabledDate = function (maxDate) {
                return minDate < maxDate.getTime() - 8.64e7 * 30 || minDate.getTime() > maxDate || maxDate > new Date().setTime(new Date().getTime()-24*60*60*1000) || minDate > new Date().setTime(new Date().getTime()-24*60*60*1000);
              }
            } else {
              this.disabledDate= function (time) {
                return time.getTime() > Date.now() - 8.64e7;
              }
            }
          }
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
        currentPage: 1,
        loading: false,
        index: '',
      }
    },
    created: function () {
      this.query.id = this.$route.query.id;
      this.currentDate();
      this.getData();
    },
    methods: {
      getData: function () {
        this.$http.post('/admin/queryJkmProfit/accountDetails', this.query)
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
            })
          })
      },
      datetimeSelect: function (val) {
        if (val == undefined) {
          this.query.startTime = '';
          this.query.endTime = '';
        } else {
          let format = val.split(' - ');
          this.query.startTime = format[0];
          this.query.endTime = format[1];
        }
      },
      currentDate: function () {
        let time = new Date();
        time.setTime(time.getTime()-24*60*60*1000);
        this.date = [time, time];
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
            this.query.startTime = str;
          } else {
            this.query.endTime = str;
          }
        }
      },
      search(){
          this.currentPage = 1;
        this.query.pageNo = 1;
        this.loading = true;
        this.getData();
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.query.pageNo = val;
        this.loading = true;
        this.getData();
      },
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
