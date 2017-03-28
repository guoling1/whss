<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">交易查询</h3>
              <!--<a :href="'http://'+this.$data.url" download="交易记录" class="btn btn-primary" style="float: right;color: #fff">导出</a>-->
            </div>
            <!-- /.box-header -->
            <div class="box-body screen-top">
              <div class="screen-item">
                <span class="screen-title">业务订单号</span>
                <el-input v-model="query.businessOrderNo" placeholder="业务订单号" size="small" style="width:220px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">交易订单号</span>
                <el-input v-model="query.orderNo" placeholder="交易订单号" size="small" style="width:220px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">支付流水号</span>
                <el-input v-model="query.sn" placeholder="支付流水号" size="small" style="width:220px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">收款商户名称</span>
                <el-input v-model="query.merchantName" placeholder="收款商户名称" size="small" style="width:220px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">收款商户编号</span>
                <el-input v-model="query.merchantNo" placeholder="收款商户编号" size="small" style="width:220px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">交易状态</span>
                <el-select v-model="query.status" size="small" clearable placeholder="请选择" style="width: 220px">
                  <el-option v-for="item in item_status"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">支付方式</span>
                <el-select v-model="query.payType" size="small" clearable placeholder="请选择" style="width: 220px">
                  <el-option v-for="item in item_payType"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">结算状态</span>
                <el-select v-model="query.settleStatus" size="small" clearable placeholder="请选择" style="width: 220px">
                  <el-option v-for="item in item_settleStatus"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">交易日期</span>
                <el-date-picker size="small"
                                v-model="date"
                                type="daterange"
                                align="right"
                                @change="datetimeSelect"
                                placeholder="选择日期范围"
                                :picker-options="pickerOptions2">
                </el-date-picker>
              </div>
              <div class="screen-item">
                <span class="screen-title">上级代理名称</span>
                <el-input v-model="query.proxyName" placeholder="上级代理名称" size="small" style="width:220px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title"></span>
                <el-button type="primary" size="small" @click="screen">筛选</el-button>
              </div>
            </div>
            <div class="box-body">
              <el-table v-loading.body="tableLoading" height="583" style="font-size: 12px;margin:15px 0" :data="records" border>
                <el-table-column type="index" width="62" label="序号" fixed="left"></el-table-column>
                <el-table-column prop="appId" label="业务方" min-width="85"></el-table-column>
                <el-table-column label="业务订单号" min-width="112">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="records[scope.$index].businessOrderNo" type="text" size="small" style="cursor: pointer" title="点击复制">{{records[scope.$index].businessOrderNo|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="merchantName" label="收款商户名称" min-width="120"></el-table-column>
                <el-table-column prop="proxyName" label="所属一级" min-width="90"></el-table-column>
                <el-table-column prop="proxyName1" label="所属二级" min-width="110"></el-table-column>
                <el-table-column label="交易订单号" min-width="112">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="records[scope.$index].orderNo" type="text" size="small" style="cursor: pointer" title="点击复制">{{records[scope.$index].orderNo|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="支付金额" min-width="90" align="right">
                  <template scope="scope">
                    {{scope.row.tradeAmount | filter_toFix}}
                  </template>
                </el-table-column>
                <el-table-column label="手续费" min-width="80" align="right">
                  <template scope="scope">
                    {{scope.row.poundage | filter_toFix}}
                  </template>
                </el-table-column>
                <el-table-column label="交易状态" min-width="90">
                  <template scope="scope">
                    {{scope.row.status}}
                  </template>
                </el-table-column>
                <el-table-column label="支付流水号" min-width="112">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="records[scope.$index].sn" type="text" size="small"
                      style="cursor: pointer" title="点击复制">{{scope.row.sn|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="payType" label="支付方式" min-width="115"></el-table-column>
                <el-table-column prop="settleStatus" label="结算状态" min-width="90"></el-table-column>
                <el-table-column prop="createTime" label="交易日期" width="162"></el-table-column>
                <el-table-column prop="" label="成功时间" width="162"></el-table-column>
                <el-table-column prop="settleType" label="结算周期"></el-table-column>
              </el-table>
            </div>
            <div class="box-body">
              <el-pagination style="float:right"
                             @size-change="handleSizeChange"
                             @current-change="handleCurrentChange"
                             :current-page="query.page"
                             :page-sizes="[20, 100, 200, 500]"
                             :page-size="query.size"
                             layout="total, sizes, prev, pager, next, jumper"
                             :total="total">
              </el-pagination>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
  </div>
</template>
<script lang="babel">
  import store from '../store'
  import Clipboard from "clipboard"
  export default {
    data () {
      return {
        query:{
          page:1,
          size:10,
          orderNo:'',
          businessOrderNo:'',
          sn:'',
          merchantName: '',
          merchantNo: '',
          startTime: '',
          endTime: '',
          lessTotalFee: '',
          moreTotalFee: '',
          status: '',
          settleStatus:'',
          payType:'',
          proxyName:'',
        },
        records:[],
        item_status:[
          {value: '', label: '全部'},
          {value: '1', label: '待支付'},
          {value: '4', label: '支付成功'},
          {value: '3', label: '支付失败'}
        ],
        item_settleStatus:[
          {value: '', label: '全部'},
          {value: '1', label: '待结算'},
          {value: '2', label: '结算中'},
          {value: '3', label: '已结算'}
        ],
        item_payType:[
          {value: '', label: '全部'},
          {value: 'sm_wechat_jsapi', label: '阳光微信公众号'},
          {value: 'sm_alipay_jsapi', label: '阳光支付宝公众号'},
          {value: 'sm_wechat_code', label: '阳光微信扫码'},
          {value: 'sm_alipay_code', label: '阳光支付宝扫码'},
          {value: 'sm_unionpay', label: '阳光快捷'},
          {value: 'km_wechat_jsapi', label: '卡盟微信公众号'},
          {value: 'km_alipay_jsapi', label: '卡盟支付宝公众号'},
          {value: 'km_wechat_code', label: '卡盟微信扫码'},
          {value: 'km_alipay_code', label: '卡盟支付宝扫码'},
          {value: 'mb_unionpay', label: '摩宝快捷'},
          {value: 'hzyb_wechat', label: '合众易宝微信'},
          {value: 'hzyb_alipay', label: '合众易宝支付宝'},
          {value: 'yijia_wechat', label: '溢+微信'},
          {value: 'yijia_alipay', label: '溢+支付宝'},
        ],
        date: '',
        total:0
      }
    },
    created(){
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
    },
    methods: {
      datetimeSelect: function (val) {
        if(val == undefined){
          this.query.startTime = '';
          this.query.endTime = '';
        }else {
          let format = val.split(' - ');
          this.query.startTime = format[0];
          this.query.endTime = format[1];
        }
      },
      screen: function () {
        this.total = '';
        this.query.page = 1;
        this.getData();
      },
      getData: function () {
        this.tableLoading = true;
        this.$http.post('/daili/tradeQuery/tradeList', this.query).then(res => {
          this.tableLoading = false;
          this.total = res.data.count;
          this.records = res.data.records;
        }, err => {
          this.tableLoading = false;
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
        });
      },
      handleSizeChange(val) {
        this.query.page = 1;
        this.query.size = val;
        this.getData();
      },
      handleCurrentChange(val) {
        this.query.page = val;
        this.getData();
      }
    },
    filters: {
      changeHide: function (val) {
        if(val!=""&&val!=null){
          val = val.replace(val.substring(3,val.length-6),"…");
        }
        return val
      }
    }
  }
</script>
<style scoped lang="less">
  .screen-top {
    padding-top: 0 !important;
  }

  .screen-item {
    float: left;
    margin-right: 10px;
  }

  .screen-title {
    display: block;
    height: 24px;
    line-height: 24px;
  }
</style>
