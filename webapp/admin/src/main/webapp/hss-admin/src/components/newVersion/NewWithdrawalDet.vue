<template lang="html">
  <div id="withdrawalDet">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">打款详情</div>
    <div style="margin: 0 15px">
      <!--<div class="box box-primary">
        <p class="lead"></p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">商户类型:</th>
              <td>{{record.merchantType|changeMerchantType}}</td>
              <th style="text-align: right">商户编号:</th>
              <td>{{record.merchantId}}</td>
              <th style="text-align: right">商户名称:</th>
              <td>{{record.subName}}</td>
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
              <th style="text-align: right">所属上级:</th>
              <td>{{record.proxyName}}</td>
              <th style="text-align: right">订单号:</th>
              <td>{{record.orderId}}</td>
              <th style="text-align: right">交易单号:</th>
              <td>{{record.outTradeNo}}</td>
            </tr>
            <tr>
              <th style="text-align: right">交易创建时间:</th>
              <td>{{record.createTime|changeTime}}</td>
              <th style="text-align: right">支付时间:</th>
              <td>{{record.payTime|changeTime}}</td>
              <th style="text-align: right">支付金额:</th>
              <td>{{record.totalFee}}</td>
            </tr>
            <tr>
              <th style="text-align: right">实际所得:</th>
              <td>{{record.realFee}}</td>
              <th style="text-align: right">支付方式:</th>
              <td>{{record.payChannel|changePayChannel}}</td>
              <th style="text-align: right">服务费:</th>
              <td>{{record.serviceFee}}</td>
            </tr>
            <tr>
              <th style="text-align: right">订单状态:</th>
              <td>{{record.orderMessage}}</td>
              <th style="text-align: right">通道名称:</th>
              <td>{{record.channelName}}</td>
              <th style="text-align: right">通道费:</th>
              <td>{{record.channelFee}}</td>
            </tr>
            <tr>
              <th style="text-align: right">结算周期:</th>
              <td>{{record.settlePeriod}}</td>
              <th style="text-align: right">结算状态:</th>
              <td>{{record.settleStatus|changeSettleStatus}}</td>
              <th style="text-align: right">错误信息:</th>
              <td>{{record.errorMessage}}</td>
            </tr>
            </tbody></table>
        </div>
      </div>-->
      <div class="box box-primary">
        <p class="lead"> 收款商户详情</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">商户类型:</th>
              <td>{{record.merchantType|changeMerchantType}}</td>
              <th style="text-align: right">商户编号:</th>
              <td>{{record.merchantId}}</td>
              <th style="text-align: right">商户名称:</th>
              <td>{{record.subName}}</td>
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
              <td></td>
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
              <td></td>
              <th style="text-align: right">付款人账户:</th>
              <td></td>
              <th style="text-align: right">订单号:</th>
              <td>{{record.orderId}}</td>
            </tr>
            <tr>
              <th style="text-align: right">交易单号:</th>
              <td>{{record.outTradeNo}}</td>
              <th style="text-align: right">交易金额:</th>
              <td>{{record.totalFee}}</td>
              <th></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">手续费率:</th>
              <td></td>
              <th style="text-align: right">手续费:</th>
              <td></td>
              <th style="text-align: right">服务费:</th>
              <td>{{record.serviceFee}}</td>
            </tr>
            <tr>
              <th style="text-align: right">通道名称:</th>
              <td>{{record.channelName}}</td>
              <th style="text-align: right">通道费:</th>
              <td>{{record.channelFee}}</td>
              <th style="text-align: right">实际所得:</th>
              <td>{{record.realFee}}</td>
            </tr>
            <tr>
              <th style="text-align: right">支付方式:</th>
              <td>{{record.payChannel|changePayChannel}}</td>
              <th style="text-align: right">交易类型:</th>
              <td></td>
              <th style="text-align: right">交易状态:</th>
              <td>{{record.orderMessage}}</td>
            </tr>
            <tr>
              <th style="text-align: right">结算状态:</th>
              <td>{{record.settleStatus|changeSettleStatus}}</td>
              <th style="text-align: right">结算周期:</th>
              <td>{{record.settlePeriod}}</td>
              <th style="text-align: right">交易时间:</th>
              <td>{{record.createTime|changeTime}}</td>
            </tr>
            <tr>
              <th style="text-align: right">交易成功时间:</th>
              <td>{{record.payTime|changeTime}}</td>
              <th style="text-align: right">预计结算时间:</th>
              <td></td>
              <th style="text-align: right">结算时间:</th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">商品名称:</th>
              <td></td>
              <th style="text-align: right">商品描述:</th>
              <td></td>
              <th style="text-align: right">错误信息:</th>
              <td>{{record.errorMessage}}</td>
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
    </div>
    <message></message>
  </div>
  </div>
</template>

<script lang="babel">
  import Message from '../Message.vue'
  export default {
    name: 'withdrawalDet',
    components: {
      Message
    },
    data: function () {
      return {
        record:{}
      }
    },
    created: function () {
      this.$http.post('/admin/withdraw/withdrawListByContionsAll',{id:this.$route.query.id})
        .then(function (res) {
          console.log(res.data)
          this.$data.record = res.data;
        }, function (err) {
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
      changeSettleStatus: function (val) {
        if(val == 0){
          return '已结算'
        }else if(val == 1){
          return '未结算'
        }
      },
      changePayChannel: function (val) {
        if(val == 101){
          return '微信'
        }else if(val == 102){
          return '支付宝'
        }else if(val == 103){
          return '快捷'
        }
      },
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
