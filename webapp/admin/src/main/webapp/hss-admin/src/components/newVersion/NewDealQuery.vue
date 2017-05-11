<template>
  <div id="deal">
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">交易查询</h3>
          <router-link to="/admin/details/deal" class="pull-right btn btn-primary" style="margin-left: 20px" target="_blank">切换旧版</router-link>
          <span @click="_$power(onload,'boss_trade_export')" download="交易记录" class="btn btn-primary" style="float: right">导出</span>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>业务订单号:</label>
              <el-input style="width: 188px" v-model="query.businessOrderNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>交易订单号:</label>
              <el-input style="width: 188px" v-model="query.orderNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>支付流水号:</label>
              <el-input style="width: 188px" v-model="query.sn" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>业务方：</label>
              <el-select style="width: 188px" clearable v-model="appId" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="好收收" value="好收收"></el-option>
                <el-option label="好收银" value="好收银"></el-option>
              </el-select>
            </li>
            <li class="same">
              <label>收款商户编号:</label>
              <el-input style="width: 188px" v-model="markCode" placeholder="请先选择业务方" size="small" :disabled="appId==''"></el-input>
            </li>
            <li class="same">
              <label>收款商户名称:</label>
              <el-input style="width: 188px" v-model="merchantName" placeholder="请先选择业务方" size="small" :disabled="appId==''"></el-input>
            </li>
            <li class="same">
              <label>所属一级代理:</label>
              <el-input style="width: 188px" v-model="query.proxyName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>所属二级代理:</label>
              <el-input style="width: 188px" v-model="query.proxyName1" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>交易日期:</label>
              <el-date-picker
                style="width: 188px"
                v-model="date"
                type="daterange"
                align="right"
                placeholder="选择日期范围"
                :picker-options="pickerOptions" size="small" :clearable="false" :editable="false">
              </el-date-picker>
            </li>
            <li class="same">
              <label>交易金额:</label>
              <div class="form-control price" style="margin-top: -4px;">
                <input type="text" name="date" value="" v-model="query.lessTotalFee">至
                <input type="text" name="date" value="" v-model="query.moreTotalFee">
              </div>
            </li>
            <li class="same">
              <label>交易状态:</label>
              <el-select style="width: 188px" clearable v-model="query.status" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="待支付" value="1">待支付</el-option>
                <el-option label="支付成功" value="4">支付成功</el-option>
                <el-option label="支付失败" value="3">支付失败</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>结算状态:</label>
              <el-select style="width: 188px" clearable v-model="query.settleStatus" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="待结算" value="1">待结算</el-option>
                <el-option label="结算中" value="2">结算中</el-option>
                <el-option label="已结算" value="3">已结算</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>支付方式：</label>
              <el-select style="width: 188px" clearable v-model="query.payType" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="微信支付" value="wechat"></el-option>
                <el-option label="支付宝支付" value="alipay"></el-option>
                <el-option label="快捷支付" value="unionpay"></el-option>
              </el-select>
            </li>
            <li class="same">
              <label>支付渠道：</label>
              <el-select style="width: 188px" clearable v-model="query.payChannelSign" size="small">
                <el-option label="全部" value=""></el-option>
                <el-option :label="channel.channelName" :value="channel.channelTypeSign" v-for="channel in channelList"></el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" height="550" style="font-size: 12px;margin-bottom: 15px;" :data="records" border>
            <el-table-column width="62" label="序号" fixed="left" type="index"></el-table-column>
            <el-table-column prop="appId" label="业务方" min-width="85"></el-table-column>
            <el-table-column label="交易订单号" min-width="112">
              <template scope="scope">
                <span class="td" :data-clipboard-text="scope.row.orderNo" style="cursor: pointer" title="点击复制">{{scope.row.orderNo|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column label="业务订单号" min-width="112">
              <template scope="scope">
                <span class="td" :data-clipboard-text="scope.row.businessOrderNo" style="cursor: pointer" title="点击复制">{{scope.row.businessOrderNo|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column label="支付流水号" min-width="112">
              <template scope="scope">
                <span class="td" :data-clipboard-text="scope.row.sn" style="cursor: pointer" title="点击复制">{{scope.row.sn|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column label="交易日期" width="162">
              <template scope="scope">
                <span>{{scope.row.createTime|changeTime}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="merchantName" label="收款商户名称" min-width="120"></el-table-column>
            <el-table-column prop="markCode" label="收款商户编号" min-width="120"></el-table-column>
            <el-table-column prop="proxyName" label="所属一级" min-width="90"></el-table-column>
            <el-table-column prop="proxyName1" label="所属二级" min-width="110"></el-table-column>
            <el-table-column label="支付金额" align="right" min-width="90">
              <template scope="scope">
                <span>{{scope.row.tradeAmount|toFix}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="payRate" label="手续费率" min-width="90" align="right"></el-table-column>
            <el-table-column label="订单状态" min-width="90">
              <template scope="scope">
                <span>{{scope.row.status|changeStatus}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="settleStatus" :formatter="changeSettleStatus" label="结算状态" min-width="90"></el-table-column>
            <el-table-column prop="payType" label="支付方式" min-width="115"></el-table-column>
            <el-table-column prop="payChannelSigns" label="支付渠道" min-width="115"></el-table-column>
            <el-table-column prop="remark" label="渠道信息" min-width="90"></el-table-column>
            <el-table-column label="操作" width="90" fixed="right">
              <template scope="scope">
                <router-link :to="{path:'/admin/details/newDealDet',query:{orderNo:scope.row.orderNo}}" type="text" target="_blank">详情
                </router-link>
              </template>
            </el-table-column>
          </el-table>
          <ul style="float: left;margin-top: 5px">
            <li>
              <label style="margin-right: 10px;">支付金额</label>
              <span>当页总额：{{pageTotal}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span>统计总额：{{addTotal}}&nbsp;元</span>
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
  import Clipboard from "clipboard"
  export default{
    name: 'deal',
    data(){
      return {
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() < Date.now() - 8.64e7*30||time.getTime() > Date.now();
          }
        },
        query:{
          page:1,
          size:10,
          orderNo:'',
          businessOrderNo:'',
          sn:'',
          startTime: '',
          endTime: '',
          lessTotalFee: '',
          moreTotalFee: '',
          status: '',
          settleStatus:'',
          payType:'',
          proxyName:'',
          proxyName1:'',
          loadUrl: '',
          loadUrl1: '',
          payChannelSign:'',
          appId:''
        },
        appId:'',
        merchantName: '',
        markCode:"",
        channelList:[],
        date: '',
        records: [],
        count: 0,
        total: '',
        loading: true,
        url: '',
        pageTotal: 0,
        addTotal: 0,
        isMask:false,
        loadUrl:'',
        loadUrl1:''
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

      this.$http.post('/admin/channel/list')
        .then(function (res) {
          this.channelList = res.data;
        }, function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        });

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
            this.query.startTime = str;
          } else {
            this.query.endTime = str;
          }
        }
      },
      reset: function () {
        this.appId='';
        this.merchantName='';
        this.markCode='';
        this.query = {
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
            proxyName1:'',
            loadUrl: '',
            loadUrl1: '',
            payChannelSign:''
        };
        this.currentDate()
      },
      getData: function () {
        this.loading = true;
        this.query.appId = this.appId;
        if(this.appId == '好收收'){
          this.query.markCode = this.markCode;
          this.query.merchantName = this.merchantName;
          delete this.query.globalId;
          delete this.query.shortName;
        }else if(this.appId == '好收银'){
          this.query.globalId = this.markCode;
          this.query.shortName = this.merchantName;
          delete this.query.markCode;
          delete this.query.merchantName;
        }else {
          this.query.markCode = '';
          this.query.merchantName = '';
          this.query.globalId = '';
          this.query.shortName = '';
        }
        this.$http.post('/admin/queryOrder/orderList',this.query)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.records = res.data.records;
            },1000)
            this.loadUrl1 = res.data.ext;
            this.count = res.data.count;
            var price=0;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            };
            for (var i = 0; i < res.data.records.length; i++) {
              price = toFix(parseFloat(price)+parseFloat(res.data.records[i].tradeAmount))
              if (res.data.records[i].payRate != null) {
                res.data.records[i].payRate = (parseFloat(res.data.records[i].payRate) * 100).toFixed(2) + '%';
              }
            }
            this.pageTotal = price;
          },function (err) {
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
      onload: function () {
        this.$data.loadUrl = this.loadUrl1;
        this.$data.isMask = true;
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
        this.total = '';
        this.query.page = 1;
        this.getData();
        this.getAddTotal()
      },
      //每页条数改变
      handleSizeChange(val) {
        this.query.page = 1;
        this.query.size = val;
        this.getData();
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.query.page = val;
        this.getData()
      },
      tableFoot(row, index) {
        if (row.proxyName1 === '当页总额'||row.proxyName1 === '筛选条件统计') {
          return {background:'#eef1f6'}
        }
        return '';
      },
      getAddTotal(){
        this.$http.post('/admin/queryOrder/amountCount',this.query)
          .then(res=>{
            this.addTotal = res.data;
          })
          .catch(err=>{
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
      },
      appId: function (val) {
        if(val == ''){
          this.markCode=''
          this.merchantName=''
        }
      }
    },
    filters: {
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
    margin:0;
  }
  .search{
    label{
      display: block;
      margin-bottom: 0;
    }
  }

  .same {
    list-style: none;
    display: inline-block;
    margin: 0 15px 10px 0;
  }
  .btn{
    font-size: 12px;
  }

  .price {
    display: inline-block;
    width: 188px;
    height: 30px;
    border-radius: 4px;
    border-color: #bfcbd9;
    position: relative;
    top: 6px;
    input {
      border: none;
      display: inline-block;
      width: 43%;
      height: 25px;
      position: relative;
      top: -3px;
    }
  }

  .price:hover {
    border-color: #20a0ff;
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
