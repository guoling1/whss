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
          <ul>
            <li class="same">
              <label>业务订单号:</label>
              <el-input style="width: 130px" v-model="query.businessOrderNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>提现单号:</label>
              <el-input style="width: 130px" v-model="query.orderNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>打款流水号:</label>
              <el-input style="width: 130px" v-model="query.sn" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>账户名称:</label>
              <el-input style="width: 130px" v-model="query.merchantName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>提现时间:</label>
              <el-date-picker
                v-model="date"
                type="daterange"
                align="right"
                placeholder="选择日期范围"
                :picker-options="pickerOptions2" size="small">
              </el-date-picker>
            </li>
            <li class="same">
              <label>提现状态:</label>
              <el-select style="width: 120px" clearable v-model="query.status" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="待支付" value="1">待支付</el-option>
                <el-option label="支付中" value="1">支付中</el-option>
                <el-option label="支付成功" value="4">支付成功</el-option>
                <el-option label="支付失败" value="3">支付失败</el-option>
                <el-option label="提现中" value="3">提现中</el-option>
                <el-option label="提现成功" value="3">提现成功</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" height="583" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column width="62" label="序号" fixed="left">
              <template scope="scope">
                <div>{{scope.$index+1}}</div>
              </template>
            </el-table-column>
            <el-table-column label="业务订单号" min-width="112">
              <template scope="scope">
                <span class="td" :data-clipboard-text="records[scope.$index].businessOrderNo" type="text" size="small"
                      style="cursor: pointer" title="点击复制">{{records[scope.$index].businessOrderNo|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="merchantName" label="账户名称" min-width="120"></el-table-column>
            <el-table-column prop="merchantName" label="用户类型" min-width="120"></el-table-column>
            <el-table-column label="提现单号" min-width="112">
              <template scope="scope">
                <span class="td" :data-clipboard-text="records[scope.$index].orderNo" type="text" size="small"
                      style="cursor: pointer" title="点击复制">{{records[scope.$index].orderNo|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="merchantName" label="提现金额" min-width="120"></el-table-column>
            <el-table-column prop="merchantName" label="手续费" min-width="120"></el-table-column>
            <el-table-column prop="merchantName" label="提现状态" min-width="120"></el-table-column>
            <el-table-column prop="merchantName" label="渠道名称" min-width="120"></el-table-column>
            <el-table-column label="打款流水号" min-width="112">
              <template scope="scope">
                <span class="td" :data-clipboard-text="records[scope.$index].sn" type="text" size="small"
                      style="cursor: pointer" title="点击复制">{{records[scope.$index].sn|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column label="提现时间" width="162">
              <template scope="scope">
                {{scope.row.createTime|changeTime}}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :formatter="changeTime" label="成功时间" width="162">
              <template scope="scope">
                {{scope.row.createTime|changeTime}}
              </template>
            </el-table-column>
          </el-table>
          </el-table>
          <ul style="float: left;margin-top: 5px">
            <li>
              <label style="margin-right: 10px;">提现金额</label>
              <span>当页总额：{{pageTotal}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span>筛选条件统计：{{addTotal}}&nbsp;元</span>
            </li>
            <li>
              <label style="margin-right: 10px;">手续费</label>
              <span>当页总额：{{pageTotal1}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span>筛选条件统计：{{addTotal1}}&nbsp;元</span>
            </li>
          </ul>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.page"
                           :page-sizes="[10, 20, 50]"
                           :page-size="query.size"
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
          page:1,
          size:10,
          orderNo:'',
          businessOrderNo:'',
          sn:'',
          merchantName: '',
          startTime: '',
          endTime: '',
          lessTotalFee: '',
          moreTotalFee: '',
          status: '',
          settleStatus:'',
          payType:'',
          proxyName:'',
          proxyName1:''
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
          this.$data.query.startTime = str;
        } else {
          this.$data.query.endTime = str;
        }
      }
      this.getData()
      this.getAddTotal()
    },
    methods: {
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/queryOrder/orderList',this.$data.query)
          .then(function (res) {
            this.loading = false;
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            var price=0;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            };
            for (var i = 0; i < this.records.length; i++) {
              price = toFix(parseFloat(price)+parseFloat(this.records[i].tradeAmount))
              if (this.records[i].payRate != null) {
                this.records[i].payRate = (parseFloat(this.records[i].payRate) * 100).toFixed(2) + '%';
              }
            }
            this.pageTotal = price;
          },function (err) {
            this.$data.loading = false;
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
        this.$data.query.page = 1;
        this.getData()
        this.getAddTotal()
      },
      //每页条数改变
      handleSizeChange(val) {
        this.$data.query.page = 1;
        this.$data.query.size = val;
        this.getData()
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.page = val;
        this.getData()
      },
      getAddTotal(){
        this.$http.post('/admin/queryOrder/amountCount',this.query)
          .then(res=>{
            this.addTotal = res.data;
          })
          .catch(err=>{
            this.$data.loading = false;
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
  ul {
    padding: 0;
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
