<template>
  <div id="retrieval">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">提现查询</h3>
          <!--<a :href="'http://'+this.$data.url" download="交易记录" class="btn btn-primary" style="float: right;color: #fff">导出</a>-->
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>业务订单号:</label>
              <el-input style="width: 188px" v-model="query.businessOrderNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>提现单号:</label>
              <el-input style="width: 188px" v-model="query.orderNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>打款流水号:</label>
              <el-input style="width: 188px" v-model="query.sn" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>账户名称:</label>
              <el-input style="width: 188px" v-model="query.userName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>提现时间:</label>
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
              <label>提现状态:</label>
              <el-select style="width: 188px" clearable v-model="query.withdrawStatus" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="提现中" value="提现中">提现中</el-option>
                <el-option label="提现成功" value="提现成功">提现成功</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" height="650" style="font-size: 12px;margin-bottom: 15px;" :data="records" border>
            <el-table-column width="62" label="序号" fixed="left" type="index"></el-table-column>
            <el-table-column label="提现单号" min-width="112px">
              <template scope="scope">
                <span class="td" :data-clipboard-text="scope.row.orderNo" type="text" size="small" style="cursor: pointer" title="点击复制">{{scope.row.orderNo|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="userName" label="账户名称">
              <template scope="scope">
                <router-link :to='{path:"/admin/record/retrievalDet",query:{idd:scope.row.idd,createTimes:scope.row.createTimes,idm:scope.row.idm,businessOrderNo:scope.row.businessOrderNo,orderNo:scope.row.orderNo}}'>{{scope.row.userName}}</router-link>
              </template>
            </el-table-column>
            <el-table-column prop="userType" label="用户类型"></el-table-column>
            <el-table-column label="业务订单号">
              <template scope="scope">
                <span class="td" :data-clipboard-text="scope.row.businessOrderNo" type="text" size="small" style="cursor: pointer" title="点击复制">{{scope.row.businessOrderNo|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="tradeAmount" label="提现金额" align="right">
              <template scope="scope">
                <span>{{scope.row.tradeAmount|toFix}}</span>
              </template>
            </el-table-column>
            <el-table-column label="手续费" align="right">
              <template scope="scope">
                <span>{{scope.row.poundage|toFix}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="withdrawStatus" label="提现状态"></el-table-column>
            <el-table-column prop="payChannelName" label="渠道名称"></el-table-column>
            <el-table-column label="打款流水号" min-width="112">
              <template scope="scope">
                <span class="td" :data-clipboard-text="scope.row.sn" type="text" size="small" style="cursor: pointer" title="点击复制">{{scope.row.sn|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="createTimes" label="提现时间" width="162"></el-table-column>
            <el-table-column prop="updateTimes" label="成功时间" width="162"></el-table-column>
          </el-table>
          </el-table>
          <ul style="float: left;margin-top: 5px">
            <li>
              <label style="margin-right: 10px;">提现金额</label>
              <span>当页总额：{{pageTotal}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span>统计总额：{{addTotal}}&nbsp;元</span>
            </li>
            <li>
              <label style="margin-right: 10px;">手续费</label>
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
  import Clipboard from "clipboard"
  export default{
    name: 'deal',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          orderNo:'',
          businessOrderNo:'',
          sn:'',
          userName: '',
          startTime: '',
          endTime: '',
          withdrawStatus: '',
        },
        pageTotal:0,
        addTotal:0,
        pageTotal1:0,
        addTotal1:0,
        date: '',
        records: [],
        count: 0,
        total: '',
        loading: true,
        url: ''
      }
    },
    created: function () {
      var clipboard = new Clipboard('.td');
      // 复制成功执行的回调，可选
      clipboard.on('success', (e) => {
        this.$message({
          showClose: true,
          message: "复制成功  内容为：" + e.text,
          type: 'success'
        });
      });
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
          this.query.startTime = str;
        } else {
          this.query.endTime = str;
        }
      }
      this.getData();
      this.getAddTotal()
    },
    methods: {
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/order/withdrawList',this.query)
          .then(function (res) {
            this.loading = false;
            this.records = res.data.records;
            this.count = res.data.count;
            var price=0,total=0;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            };
            for (var i = 0; i < this.records.length; i++) {
              price = toFix(parseFloat(price)+parseFloat(this.records[i].tradeAmount));
              total = toFix(parseFloat(total)+parseFloat(this.records[i].poundage));
              if (this.records[i].payRate != null) {
                this.records[i].payRate = (parseFloat(this.records[i].payRate) * 100).toFixed(2) + '%';
              }
            }
            this.pageTotal = price;
            this.pageTotal1 = total;
          },function (err) {
            this.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })

      },
      //格式化hss创建时间
      changeTime: function (row, column) {
        var val = row.createTime;
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
      changeNum: function (row, column) {
        var val = row.tradeAmount;
        if(val!=''){
          return parseFloat(val).toFixed(2);
        }else {
          return val
        }

      },
      changeSettleStatus: function (row, column) {
        var val = row.settleStatus;
        if(val == 2){
          return '结算中'
        }else if(val == 1){
          return '待结算'
        }else if(val == 3){
          return '已结算'
        }
      },
      search(){
        this.query.pageNo = 1;
        this.getData()
        this.getAddTotal()
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
      },
      getAddTotal(){
        this.addTotal = 0;
        this.addTotal1 = 0;
        this.$http.post('/admin/order/withdrawAmount',this.query)
          .then(res=>{
            this.addTotal = res.data.tradeAmount;
            this.addTotal1 = res.data.poundage;
          })
          .catch(err=>{
            this.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          });
      }
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
              this.query.startTime = str;
            } else {
              this.query.endTime = str;
            }
          }
        } else {
          this.query.startTime = '';
          this.query.endTime = '';
        }
      }
    },
    filters: {
      changeHide: function (val) {
        if(val!=""&&val!=null){
          val = val.replace(val.substring(3,val.length-6),"…");
        }
        return val
      },
      changeStatus: function (val) {
        if(val == 1){
          return "待支付"
        }else if(val == 3){
          return "支付失败"
        }else if(val == 4){
          return "支付成功"
        }else if(val == 5){
          return "提现中"
        }else if(val == 6){
          return "提现成功"
        }else if(val == 7){
          return "充值成功"
        }else if(val == 6){
          return "充值失败"
        }
      },
    }
  }
</script>

<style scoped lang="less" rel="stylesheet/less">
  .search {
    label{
      display: block;
      margin-bottom: 0;
    }
  }
  ul{
    padding: 0;
    margin: 0;
  }
  .same {
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }

  .price {
    display: inline-block;
    width: 210px;
    height: 30px;
    border-radius: 4px;
    border-color: #bfcbd9;
    position: relative;
    top: 6px;
    input {
      border: none;
      display: inline-block;
      width: 45%;
      height: 25px;
      position: relative;
      top: -3px;
    }
  }

  .price:hover {
    border-color: #20a0ff;
  }

</style>
