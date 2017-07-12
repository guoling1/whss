<template lang="html">
  <div id="myMsg">
    <ul>
      <li>
        <span>手机号</span>
        <span class="right">{{dealer.mobile}}</span>
      </li>
      <li>
        <span>名称</span>
        <span class="right">{{dealer.proxyName}}</span>
      </li>
      <li>
        <span>所在地</span>
        <span class="right">{{dealer.belongArea}}</span>
      </li>
      <li>
        <span>支付宝收单结算价</span>
        <span class="right">{{dealer.alipaySettleRate|tofix}}%</span>
      </li>
      <li>
        <span>微信收单结算价</span>
        <span class="right">{{dealer.weixinSettleRate|tofix}}%</span>
      </li>
      <li>
        <span>快捷收单结算价</span>
        <span class="right">{{dealer.quickSettleRate|tofix}}%</span>
      </li>
      <li>
        <span>提现结算价</span>
        <span class="right">{{dealer.withdrawSettleFee}}元</span>
      </li>
    </ul>
    <div class="submit" @click="success">
      确定
    </div>
  </div>
</template>

<script>
  export default {
    name:'myMsg',
    data:function(){
      return {
        dealer:{}
      }
    },
    created: function(){
      this.$http.post('/dealer/get')
        .then(function (res) {
          this.$data.dealer = res.data;
        },function (err) {
          console.log(err)
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    },
    methods: {
      success: function(){
        this.$router.push('/dealer/index')
      }
    },
//    filters: {
//      tofix: function (val) {
//        return val.toFixed(2)
//      }
//    }
  }
</script>

<style lang="less">
  .flexItem(@val) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
  }

</style>
