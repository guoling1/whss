<template lang="html">
  <div id="dealList">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;    color: #fff;">交易详情</div>
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
              <td>{{record.id}}</td>
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
              <th style="text-align: right">交易订单号:</th>
              <td>{{record.orderNo}}</td>
            </tr>
            <tr>
              <th style="text-align: right">交易金额:</th>
              <td>{{record.tradeAmount}}</td>
              <th style="text-align: right">手续费率:</th>
              <td>{{record.payRate}}</td>
              <th style="text-align: right"></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">手续费:</th>
              <td>{{record.poundage}}</td>
              <th style="text-align: right">通道名称:</th>
              <td>{{record.payChannelSign|changePayChannel}}</td>
              <th style="text-align: right">通道费:</th>
              <td>—</td>
            </tr>
            <tr>
              <th style="text-align: right">支付方式:</th>
              <td>{{record.payType|changePayType}}</td>
              <th style="text-align: right">交易类型:</th>
              <td>{{record.tradeType|changeTradeType}}</td>
              <th style="text-align: right">交易状态:</th>
              <td>{{record.status|changeStatus}}</td>
            </tr>
            <tr>
              <th style="text-align: right">结算状态:</th>
              <td>{{record.settleStatus|changeSettleStatus}}</td>
              <th style="text-align: right">结算周期:</th>
              <td>—</td>
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
      <div class="box box-primary">
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
      </div>
    </div>
    <message></message>
  </div>
  </div>
</template>

<script lang="babel">
  import Message from '../Message.vue'
  export default {
    name: 'dealList',
    components: {
      Message
    },
    data: function () {
      return {
        record:{}
      }
    },
    created: function(){
      this.$http.post('/admin/queryOrder/orderListAll',{orderNo:this.$route.query.orderNo})
        .then(function (res) {
          this.$data.record = res.data;
        },function (err) {
          console.log(err)
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
      changeTime: function (val) {
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
          return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
        }
      },
      changePayType: function (val) {
        if(val == "S"){
          return "微信扫码"
        }else if(val == "N"){
          return "微信二维码"
        }else if(val == "H"){
          return "微信H5收银台"
        }else if(val == "B"){
          return "快捷收款"
        }else if(val == "Z"){
          return "支付宝扫码"
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
      changePayChannel: function (val) {
        if(val == 101){
          return '阳光微信扫码'
        }else if(val == 102){
          return '阳光支付宝扫码'
        }else if(val == 103){
          return '阳光银联支付'
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

  a {
    color: #42b983;
  }

</style>
