<template>
  <div id="achievement">
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">销售业绩统计（好收银）</h3>
          <el-button size="small" class="pull-right">导出</el-button>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>报单员登录名:</label>
              <el-input style="width: 193px" v-model="query.username" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>报单员姓名:</label>
              <el-input style="width: 193px" v-model="query.realname" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>日期:</label>
              <el-date-picker v-model="date" size="small" type="daterange" align="right" placeholder="选择日期范围"
                              :picker-options="pickerOptions" style="width: 193px" :clearable="false" @change="datetimeSelect"
                              :editable="false"></el-date-picker>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border>
            <el-table-column width="62" label="序号" type="index"></el-table-column>
            <el-table-column prop="realname" label="报单员登录名"></el-table-column>
            <el-table-column prop="userName" label="报单员姓名"></el-table-column>
            <el-table-column label="日期">
              <template scope="scope">
                {{scope.row.creatTime|changeDate}}
              </template>
            </el-table-column>
            <el-table-column prop="vaildTradeUserCount" label="当日有效商户数(有一笔5元以上交易)" align="right"></el-table-column>
            <el-table-column prop="tradeCount" label="当日5元以上交易笔数" align="right">

            </el-table-column>
            <el-table-column prop="tradeTotalCount" label="当日名下商户交易总笔数" align="right"></el-table-column>
            <el-table-column prop="tradeTotalAmount" label="当日名下商户交易总额（元）" align="right"></el-table-column>

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
        <div class="box box-info mask el-message-box" v-if="isMask">
          <div class="maskCon">
            <div class="head">
              <div class="title">消息</div>
              <i class="el-icon-close" @click="isMask=false"></i>
            </div>
            <div class="body">
              <div>确定导出列表吗？</div>
            </div>
            <div class="foot">
              <a href="javascript:void(0)" @click="isMask=false" class="el-button el-button--default">取消</a>
              <a :href="'http://'+loadUrl" @click="isMask=false"
                 class="el-button el-button-default el-button--primary ">下载</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'achievement',
    data(){
      return {
        pickerOptions: {
          onPick: function ({maxDate, minDate}) {
            if (maxDate == '' || maxDate == null) {
              this.disabledDate = function (maxDate) {
                return minDate < maxDate.getTime() - 8.64e7 * 30 || minDate.getTime() > maxDate;
              }
            } else {
              this.disabledDate = function () {
              }
            }
          }
        },
        date: '',
        records: [],
        count: 0,
        query: {
          pageNo: 1,
          pageSize: 10,
          realname:'',
          username:'',
          startTime1:'',
          endTime:'',
        },
        loading: true,
        isMask: false,
        loadUrl: '',
      }
    },
    created: function () {
      this.currentDate();
      this.getData()
    },
    methods: {
      datetimeSelect: function (val) {
        if (val == undefined) {
          this.query.startTime1 = '';
          this.query.endTime = '';
        } else {
          let format = val.split(' - ');
          this.query.startTime1 = format[0];
          this.query.endTime = format[1];
        }
      },
      currentDate: function () {
        let time = new Date();
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
            this.$data.query.startSettleDate = str;
          } else {
            this.$data.query.endSettleDate = str;
          }
        }
      },
      reset: function () {
        this.query = {
          pageNo: 1,
          pageSize: 10,
          userNo: "",//商户编号
          userName: "",  //商户名字
          userType: '',
          startSettleDate: "",
          endSettleDate: "",
          checkedStatus: '',
          settleStatus: ''
        };
        this.currentDate()
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/AchievementStatistics/getAchievement', this.$data.query)
          .then(function (res) {
            setTimeout(() => {
              this.loading = false;
              this.records = res.data.records;
            }, 1000)
            this.count = res.data.count;
          }, function (err) {
            setTimeout(() => {
              this.loading = false;
            }, 1000)
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      search: function () {
        this.query.pageNo = 1;
        this.getData()
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
    },
    /*watch: {
      date: function (val, oldVal) {
        if (val != undefined && val[0] != null) {
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
              this.query.startSettleDate = str;
            } else {
              this.query.endSettleDate = str;
            }
          }
        } else {
          this.query.startSettleDate = '';
          this.query.endSettleDate = '';
        }
      }
    }*/
  }
</script>
<style scoped lang="less" rel="stylesheet/less">
  ul {
    padding: 0;
    margin: 0;
  }

  .search {
    margin-bottom: 0;
    label {
      display: block;
      margin-bottom: 0;
    }
  }

  .same {
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }

  .mask {
    z-index: 2020;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.45);

    .maskCon {
      margin: 250px auto;
      text-align: left;
      vertical-align: middle;
      background-color: #fff;
      width: 420px;
      border-radius: 3px;
      font-size: 16px;
      overflow: hidden;
      -webkit-backface-visibility: hidden;
      backface-visibility: hidden;

      .head {
        position: relative;
        padding: 20px 20px 0;

        .title {
          padding-left: 0;
          margin-bottom: 0;
          font-size: 16px;
          font-weight: 700;
          height: 18px;
          color: #333;
        }

        i {
          font-family: element-icons !important;
          speak: none;
          font-style: normal;
          font-weight: 400;
          font-variant: normal;
          text-transform: none;
          vertical-align: baseline;
          display: inline-block;
          -webkit-font-smoothing: antialiased;
          position: absolute;
          top: 19px;
          right: 20px;
          color: #999;
          cursor: pointer;
          line-height: 20px;
          text-align: center;
        }

      }
      .body {
        padding: 30px 20px;
        color: #48576a;
        font-size: 14px;
        position: relative;

        div {
          margin: 0;
          line-height: 1.4;
          font-size: 14px;
          color: #48576a;
          font-weight: 400;
        }
      }
      .foot {
        padding: 10px 20px 15px;
        text-align: right;
      }
    }
  }
</style>
