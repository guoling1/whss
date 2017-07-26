<template>
  <div id="storeList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">订单查询</h3>
        </div>
        <div class="box-body">
          <el-tabs class="tab" v-model="activeName" type="card">
            <el-tab-pane label="好收收" name="first">
              <!--筛选-->
              <ul class="search">
                <li class="same">
                  <label>订单号:</label>
                  <el-input style="width: 193px" v-model="queryHss.orderNo" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>订单创建时间:</label>
                  <el-date-picker :clearable="false" style="width: 193px" v-model="dateHss" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small" @change="datetimeSelect">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>交易时间:</label>
                  <el-date-picker :clearable="true" style="width: 193px" v-model="dateHss1" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small" @change="datetimeSelect1">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>交易单号:</label>
                  <el-input style="width: 193px" v-model="queryHss.tradeOrderNo" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>商户名称:</label>
                  <el-input style="width: 193px" v-model="queryHss.merchantName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>一级代理:</label>
                  <el-input style="width: 193px" v-model="queryHss.proxyName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>二级代理:</label>
                  <el-input style="width: 193px" v-model="queryHss.proxyName1" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>支付方式：</label>
                  <el-select style="width: 193px" v-model="queryHss.paymentMethod" size="small">
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="微信支付" value="1"></el-option>
                    <el-option label="支付宝支付" value="2"></el-option>
                    <el-option label="快捷支付" value="3"></el-option>
                    <el-option label="QQ钱包" value="4"></el-option>
                    <el-option label="银联扫码" value="5"></el-option>
                  </el-select>
                </li>
                <li class="same">
                  <label>订单状态：</label>
                  <el-select style="width: 193px" clearable v-model="queryHss.status" size="small">
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="待支付" value="1"></el-option>
                    <el-option label="支付成功" value="3"></el-option>
                    <el-option label="支付失败" value="2"></el-option>
                  </el-select>
                </li>
                <li class="same">
                  <el-button type="primary" size="small" @click="search('hss')">筛选</el-button>
                  <el-button type="primary" size="small" @click="reset('hss')">重置</el-button>
                </li>
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom:15px" :data="recordsHss" border>
                <el-table-column type="index" width="62" label="序号" fixed="left"></el-table-column>
                <el-table-column prop="orderNo" label="订单号" min-width="112">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="scope.row.orderNo" style="cursor: pointer" title="点击复制">{{scope.row.orderNo|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="createTimes" label="订单创建时间" width="162"></el-table-column>
                <el-table-column prop="merchantName" label="收款商户" min-width="112"></el-table-column>
                <!--<el-table-column prop="proxyName1" label="分公司"></el-table-column>-->
                <el-table-column prop="proxyName" label="所属一级"></el-table-column>
                <el-table-column prop="proxyName1" label="所属二级"></el-table-column>
                <el-table-column prop="tradeOrderNo" label="交易订单号" min-width="112">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="scope.row.tradeOrderNo" style="cursor: pointer" title="点击复制">{{scope.row.tradeOrderNo|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="sn" label="支付流水号" min-width="112">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="scope.row.sn" style="cursor: pointer" title="点击复制">{{scope.row.sn|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="tradeAmount" label="订单金额" align="right" min-width="90"></el-table-column>
                <el-table-column prop="payRate" label="费率" align="right" min-width="90"></el-table-column>
                <el-table-column prop="poundage" label="手续费" align="right" min-width="90"></el-table-column>
                <el-table-column prop="statusValue" label="订单状态"  min-width="90"></el-table-column>
                <el-table-column prop="paymentMethod" label="支付方式"  min-width="90"></el-table-column>
                <el-table-column prop="paySuccessTimes" label="交易时间" width="162"></el-table-column>
              </el-table>
              <ul style="float: left;margin-top: 5px">
                <li>
                  <label style="margin-right: 10px;">支付金额</label>
                  <span>当页总额：{{pageTotalHss}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
                  <span>统计总额：{{addTotalHss}}&nbsp;元</span>
                </li>
                <li>
                  <label style="margin-right: 10px;">手续费</label>
                  <span>当页总额：{{pageTotalHss1}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
                  <span>统计总额：{{addTotalHss1}}&nbsp;元</span>
                </li>
              </ul>
              <div class="block" style="text-align: right">
                <el-pagination @size-change="handleSizeChangeHss"
                               @current-change="handleCurrentChangeHss"
                               :current-page="queryHss.page"
                               :page-sizes="[10, 20, 50]"
                               :page-size="queryHss.size"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="countHss">
                </el-pagination>
              </div>
            </el-tab-pane>
            <el-tab-pane label="好收银" name="second">
              <ul class="search">
                <li class="same">
                  <label>订单号:</label>
                  <el-input style="width: 188px" v-model="queryHsy.ordernumber" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>交易单号:</label>
                  <el-input style="width: 188px" v-model="queryHsy.orderno" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>商户名称:</label>
                  <el-input style="width: 188px" v-model="queryHsy.merchantName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>店铺名称:</label>
                  <el-input style="width: 188px" v-model="queryHsy.shortName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>一级代理:</label>
                  <el-input style="width: 188px" v-model="queryHsy.proxyName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>二级代理:</label>
                  <el-input style="width: 188px" v-model="queryHsy.proxyName1" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>报单员:</label>
                  <el-input style="width: 188px" v-model="queryHsy.username" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>支付方式：</label>
                  <el-select style="width: 188px" v-model="queryHsy.paymentChannel" size="small">
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="微信支付" value="1"></el-option>
                    <el-option label="支付宝支付" value="2"></el-option>
                    <el-option label="快捷支付" value="3"></el-option>
                    <el-option label="QQ钱包" value="4"></el-option>
                    <el-option label="会员卡" value="5"></el-option>
                    <el-option label="银联扫码" value="6"></el-option>
                  </el-select>
                </li>
                <li class="same">
                  <label>订单状态：</label>
                  <el-select style="width: 188px" clearable v-model="queryHsy.orderstatus" size="small">
                    <el-option label="全部" value=""></el-option>
                    <el-option label="待支付" value="1"></el-option>
                    <el-option label="支付中" value="2"></el-option>
                    <el-option label="支付失败" value="3"></el-option>
                    <el-option label="支付成功" value="4"></el-option>
                    <el-option label="提现中" value="5"></el-option>
                    <el-option label="提现成功" value="6"></el-option>
                    <el-option label="充值成功" value="7"></el-option>
                    <el-option label="充值失败" value="8"></el-option>
                    <el-option label="待提现" value="9"></el-option>
                    <el-option label="提现失败" value="10"></el-option>
                  </el-select>
                </li>
                <li class="same">
                  <label>交易来源:</label>
                  <el-select style="width: 188px" v-model="queryHsy.source" clearable placeholder="请选择" size="small">
                    <el-option label="全部" value=""></el-option>
                    <el-option label="直销" value="1"></el-option>
                    <el-option label="渠道" value="2"></el-option>
                  </el-select>
                </li>
                <li class="same">
                  <el-button type="primary" size="small" @click="search('hsy')">筛选</el-button>
                  <el-button type="primary" size="small" @click="reset('hsy')">重置</el-button>
                </li>
                <li class="same rightBtn">
                  <el-button @click="onloadHsy" type="primary" :loading="isLoading" size="small">导出</el-button>
                </li>
              </ul>
              <el-table v-loading.body="hsyLoading" style="font-size: 12px;margin-bottom:15px" :data="recordsHsy" border>
                <el-table-column type="index" width="62" label="序号"></el-table-column>
                <el-table-column label="订单号" min-width="112">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="scope.row.ordernumber" style="cursor: pointer" title="点击复制">{{scope.row.ordernumber|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="proxyName" label="一级代理" min-width="90"></el-table-column>
                <el-table-column prop="proxyName1" label="二级代理" min-width="90"></el-table-column>
                <el-table-column prop="username" label="报单员"></el-table-column>
                <el-table-column prop="realname" label="报单员姓名" min-width="110"></el-table-column>
                <el-table-column prop="merchantName" label="商户名称" min-width="90"></el-table-column>
                <el-table-column prop="shortName" label="店铺名称"></el-table-column>
                <el-table-column label="交易单号" min-width="112">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="scope.row.orderno" style="cursor: pointer" title="点击复制">{{scope.row.orderno|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="支付流水号" min-width="112">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="scope.row.paysn" style="cursor: pointer" title="点击复制">{{scope.row.paysn|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="paymentChannels" label="支付方式" min-width="155"></el-table-column>
                <el-table-column prop="paysuccesstimes" label="成功时间" min-width="155"></el-table-column>
                <el-table-column prop="amount" label="订单金额" min-width="90" align="right"></el-table-column>
                <el-table-column prop="poundage" label="手续费金额"  min-width="110" align="right"></el-table-column>
                <el-table-column prop="orderstatuss" label="订单状态"></el-table-column>
              </el-table>
              <div class="block" style="text-align: right">
                <el-pagination @size-change="handleSizeChangeHsy"
                               @current-change="handleCurrentChangeHsy"
                               :current-page="queryHsy.page"
                               :page-sizes="[10, 20, 50]"
                               :page-size="queryHsy.size"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="countHsy">
                </el-pagination>
              </div>
            </el-tab-pane>
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
                  <a :href="'http://'+loadUrlHsy" @click="isMask=false"
                     class="el-button el-button-default el-button--primary ">下载</a>
                </div>
              </div>
            </div>
          </el-tabs>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
  </div>
</template>

<script lang="babel">
  import Clipboard from "clipboard"
  export default{
    name: 'storeList',
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
        isMask: false,
        activeName: 'first', //选项卡选中第一个
        url: '/admin/queryOrder/queryOrderList',
        queryHss: {
          page: 1,
          size: 10,
          orderNo: '',
          tradeOrderNo: "",
          merchantName: '',
          proxyName: '',
          proxyName1: '',
          paymentMethod: '',
          status: '',
          startTime: '',
          endTime: '',
          paySuccessTime: '',
          paySuccessTime1: ''
        },
        pageTotalHss: 0,
        pageTotalHss1: 0,
        addTotalHss: 0,
        addTotalHss1: 0,
        dateHss: '',
        dateHss1: '',
        queryHsy: {
          page: 1,
          size: 10,
          ordernumber:'',
          orderno:'',
          merchantName:'',
          shortName:'',
          proxyName:'',
          proxyName1:'',
          username:'',
          paymentChannel:'',
          orderstatus:'',
          source:''
        },
        recordsHss: [],
        recordsHsy: [],
        countHss: 0,
        countHsy: 0,
        loading: false,
        hsyLoading: false,
        isLoading: false,
        loadUrlHsy:''
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
      this.dateHss = [time,time];
      for (var j = 0; j < this.dateHss.length; j++) {
        var str = this.dateHss[j];
        var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
        for (var i = 0, len = ary.length; i < len; i++) {
          if (ary[i] < 10) {
            ary[i] = '0' + ary[i];
          }
        }
        str = ary[0] + '-' + ary[1] + '-' + ary[2];
        if (j == 0) {
          this.queryHss.startTime = str;
        } else {
          this.queryHss.endTime = str;
        }
      }
      this.getDataHss();
      this.getDataHsy()
      this.getAddTotalHss();
    },
    methods: {
      datetimeSelect: function (val) {
        let format = val.split(' - ');
        this.queryHss.startTime = format[0];
        this.queryHss.endTime = format[1];
      },
      datetimeSelect1: function (val) {
        let format = val.split(' - ');
        this.queryHss.paySuccessTime = format[0];
        this.queryHss.paySuccessTime1 = format[1];
      },
      onloadHsy: function () {
        this.isLoading = true
        this.$http.post('/admin/queryOrder/downLoadHsyOrder',this.queryHsy)
          .then(res=>{
          this.isLoading = false;
          this.loadUrlHsy = res.data[0].url;
          this.isMask = true;
        })
        .catch(err=>{
          this.isLoading = false;
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
      },
      reset: function (val) {
        if(val == 'hss'){
          let time = new Date();
          this.dateHss = [time,time];
          for (var j = 0; j < this.dateHss.length; j++) {
            var str = this.dateHss[j];
            var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
            for (var i = 0, len = ary.length; i < len; i++) {
              if (ary[i] < 10) {
                ary[i] = '0' + ary[i];
              }
            }
            str = ary[0] + '-' + ary[1] + '-' + ary[2];
            if (j == 0) {
              this.queryHss.startTime = str;
            } else {
              this.queryHss.endTime = str;
            }
          }
          this.dateHss1 = '';
          this.queryHss = {
            page:1,
            size:10,
            orderNo:'',
            tradeOrderNo:"",
            merchantName:'',
            proxyName:'',
            proxyName1:'',
            paymentMethod:'',
            status:'',
            paySuccessTime: '',
            paySuccessTime1: ''
          }
        }else if(val == 'hsy'){
          this.dateHsy='';
          this.dateHsy1='';
          this.queryHsy = {
            page: 1,
            size: 10,
            ordernumber:'',
            orderno:'',
            merchantName:'',
            shortName:'',
            proxyName:'',
            proxyName1:'',
            username:'',
            paymentChannel:'',
            orderstatus:'',
            source:''
          }
        }
      },
      getDataHss: function () {
        this.loading = true;
        this.$http.post('/admin/queryOrder/queryOrderList',this.queryHss)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.recordsHss = res.data.records;
              var toFix = function (val) {
                return parseFloat(val).toFixed(2)
              };
              var total=0,total1=0;
              for (let i = 0; i < res.data.records.length; i++) {
                if(res.data.records[i].payRate!=null){
                  res.data.records[i].payRate = toFix(res.data.records[i].payRate*100)+"%";
                }
                if(res.data.records[i].tradeAmount!=null){
                  total = toFix(parseFloat(total)+parseFloat(res.data.records[i].tradeAmount));
                }
                if(res.data.records[i].poundage!=null){
                  total1 = toFix(parseFloat(total1)+parseFloat(res.data.records[i].poundage))

                }
              }
              this.pageTotalHss = total;
              this.pageTotalHss1 = total1;
            },1000)
            this.countHss = res.data.count;
          }, function (err) {
            setTimeout(()=>{
              this.loading = false;
            },1000)
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      getAddTotalHss(){
        this.$http.post('/admin/queryOrder/getOrderCount',this.queryHss)
          .then(res=>{
            this.addTotalHss = res.data.totalPayment;
            this.addTotalHss1 = res.data.totalPoundage;
          })
          .catch(err=>{
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      getDataHsy: function () {
        this.hsyLoading = true;
        this.$http.post('/admin/queryOrder/queryHsyOrderList',this.queryHsy)
          .then(function (res) {
            setTimeout(()=>{
              this.hsyLoading = false;
              this.recordsHsy = res.data.records;
            },1000)
            this.countHsy = res.data.count;
          }, function (err) {
            setTimeout(()=>{
              this.hsyLoading = false;
            },1000)
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      search(val){
        if(val == 'hss'){
          this.queryHss.page = 1;
          this.getDataHss();
          this.getAddTotalHss();
        }else if(val == 'hsy'){
          this.queryHsy.page = 1;
          this.getDataHsy();
        }
      },
      //当前页改变时
      handleCurrentChangeHss(val) {
        this.queryHss.page = val;
        this.getDataHss()
      },
      handleCurrentChangeHsy(val) {
        this.queryHsy.page = val;
        this.getDataHsy()
      },
      handleSizeChangeHss: function (val) {
        this.queryHss.page = 1;
        this.queryHss.size = val;
        this.getDataHss()
      },
      handleSizeChangeHsy: function (val) {
        this.queryHsy.page = 1;
        this.queryHsy.size = val;
        this.getDataHsy()
      }
    },
  }
</script>
<style scoped lang="less" rel="stylesheet/less">
  ul {
    padding: 0;
    margin:0;
  }
  .search{
    margin-bottom:0;
    label{
      display: block;
      margin-bottom: 0;
    }
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
