<template lang="html">
  <div id="dealList">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;    color: #fff;">交易详情</div>
    <div style="margin: 0 15px">
      <div class="box box-primary">
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
      </div>
      <!--<div class="box box-primary">
        <p class="lead">Amount Due 2/22/2014</p>

        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            </tbody></table>
        </div>
      </div>-->
    </div>
    <message></message>
  </div>
  </div>
</template>

<script lang="babel">
  import Message from './Message.vue'
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
      this.$http.post('/admin/queryOrderRecord/orderListAll',{id:this.$route.query.id})
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

  a {
    color: #42b983;
  }

</style>
