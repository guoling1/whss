<template lang="html">
  <div id="dealDet">
    <div class="box-header with-border" style="margin: 0 0 0 3px;">
      <h3 class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">交易详情</h3>
      <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
    </div>
    <div style="margin: 0 15px">
      <div class="box box-primary">
        <p class="lead"> 收款商户详情</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">商户类型:</th>
              <td>—</td>
              <!--<td>{{record.merchantType|changeMerchantType}}</td>-->
              <th style="text-align: right">商户编号:</th>
              <td>{{record.markCode}}</td>
              <th style="text-align: right">商户名称:</th>
              <td>{{record.merchantName}}</td>
            </tr>
            <tr>
              <th style="text-align: right">注册手机号:</th>
              <td>{{record.mobile}}</td>
              <th style="text-align: right">银行卡号:</th>
              <td>{{record.bankNo}}</td>
              <th style="text-align: right">银行预留手机号:</th>
              <td>{{record.reserveMobile}}</td>
            </tr>
            <tr>
              <th style="text-align: right">分公司名称名称:</th>
              <td>{{record.branchCompany}}</td>
              <th style="text-align: right">分公司编号:</th>
              <td>{{record.companyCode}}</td>
              <th style="text-align: right"></th>
              <td>—</td>
            </tr>
            <tr>
              <th style="text-align: right">一级代理名称:</th>
              <td>{{record.proxyName}}</td>
              <th style="text-align: right">二级代理名称:</th>
              <td>{{record.proxyName1}}</td>
              <th style="text-align: right">推荐人:</th>
              <td>—</td>
            </tr>
            </tbody></table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead"> 交易订单</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">付款人:</th>
              <td>—</td>
              <th style="text-align: right">付款人账户:</th>
              <td>—</td>
              <th style="text-align: right">订单号:</th>
              <td>{{record.orderNo}}</td>
            </tr>
            <tr>
              <th style="text-align: right">交易金额:</th>
              <td>{{record.tradeAmount}}</td>
              <th style="text-align: right">手续费率:</th>
              <td>{{record.payRate}}</td>
              <th style="text-align: right">交易流水号</th>
              <td>{{record.sn}}</td>
            </tr>
            <tr>
              <th style="text-align: right">通道名称:</th>
              <td>{{record.payChannelSigns}}</td>
              <th style="text-align: right">通道费:</th>
              <td>—</td>
              <th style="text-align: right">手续费:</th>
              <td>{{record.poundage}}</td>
            </tr>
            <tr>
              <th style="text-align: right">支付方式:</th>
              <td>{{record.payType}}</td>
              <th style="text-align: right">交易类型:</th>
              <td>{{record.tradeType|changeTradeType}}</td>
              <th style="text-align: right">交易状态:</th>
              <td>{{record.status|changeStatus}}</td>
            </tr>
            <tr>
              <th style="text-align: right">结算状态:</th>
              <td>{{record.settleStatus|changeSettleStatus}}</td>
              <th style="text-align: right">结算周期:</th>
              <td>{{record.settleType}}</td>
              <th style="text-align: right">交易时间:</th>
              <td>{{record.createTime|changeTime}}</td>
            </tr>
            <tr>
              <th style="text-align: right">交易成功时间:</th>
              <td>{{record.paySuccessTime|changeTime}}</td>
              <th style="text-align: right">预计结算时间:</th>
              <td>{{record.settleTime|changeTime}}</td>
              <th style="text-align: right">结算时间:</th>
              <td>{{record.successSettleTime|changeTime}}</td>
            </tr>
            <tr>
              <th style="text-align: right">商品名称:</th>
              <td>{{record.goodsName}}</td>
              <th style="text-align: right">商品描述:</th>
              <td>{{record.goodsDescribe}}</td>
              <th style="text-align: right">备注信息:</th>
              <td>{{record.remark}}</td>
            </tr>
            </tbody></table>
        </div>
      </div>
      <!--<div class="box box-primary">
        <p class="lead"> 支付流水单</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            </tbody>
          </table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead"> 分帐单</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            </tbody>
          </table>
        </div>
      </div>-->
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'dealDet',
    data: function () {
      return {
        record:{},
      }
    },
    created: function(){
      this.record={};
      this.$http.post('/admin/queryOrder/orderListAll',{orderNo:this.$route.query.orderNo})
        .then(function (res) {
          this.record = res.data;
        },function (err) {
          this.record={};
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
    },
    filters: {
      changeMerchantType: function (val) {
        if(val==0){
          return "商户"
        }else if(val==1){
          return "代理商"
        }
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
      changeSettleStatus: function (val) {
        if(val == 2){
          return '结算中'
        }else if(val == 1){
          return '待结算'
        }else if(val == 3){
          return '已结算'
        }
      },
      changeTradeType: function (val) {
        if(val == 1){
          return "支付"
        }else if(val == 2){
          return "充值"
        }else if(val ==3){
          return "提现"
        }
      },
      changeName: function (val) {
        if(val==null){
          return "无"
        }else {
          return val
        }
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .flexItem(@val) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
  }

  .flexBox {
    display: box;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    display: -webkit-flex;
  }
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
  }

  ul {
    list-style-type: none;
    padding: 0;
    background: #fff;
    position: relative;
    left: 70px;
    width: 300px;
  }

  li {
    /*display: inline-block;*/
    margin: 0 10px;
  }
</style>
