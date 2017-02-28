<template>
  <div id="profitSec">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">二级代理分润</h3>
          <span @click="onload()" download="公司分润" class="btn btn-primary" style="color: #fff;float: right">导出</span>
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
            <el-table-column prop="splitAmount" label="收益金额" align="right" header-align="left"></el-table-column>
            <el-table-column label="操作" width="100">
              <template scope="scope">
                <router-link
                  :to="{path:'/admin/record/profitSecDet',query:{type:records[scope.$index].businessType,id:records[scope.$index].receiptMoneyAccountId,time:records[scope.$index].splitDate}}"
                  v-if="records[scope.$index].splitAmount!=0&&records[scope.$index].businessType!='总额'" type="text" size="small">明细
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
        isMask: false,
        loadUrl: '',
        loadUrl1: '',
      }
    },
    created: function () {
      this.getData();
    },
    methods: {
      onload: function () {
        this.$data.loadUrl = this.loadUrl1;
        this.$data.isMask = true;
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/allProfit/secondProfit', this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loadUrl1 = res.data.ext;
            this.$data.loading = false;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            }
            var total=0;
            for (let i = 0; i < this.$data.records.length; i++) {
              this.$data.records[i].splitAmount = toFix(this.$data.records[i].splitAmount);
              total = toFix(parseFloat(total)+parseFloat(this.$data.records[i].splitAmount))
            }
            if(this.records.length!=0){
              this.records.push({
                businessType:"总额",
                splitAmount:total
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

          return year + "-" + tod(month) + "-" + tod(date);
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
<style scoped lang="less" rel="stylesheet/less">
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
