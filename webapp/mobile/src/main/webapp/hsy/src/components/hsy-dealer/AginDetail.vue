<template lang="html">
  <div id="aginDetail">
    <div class="header">
      <p class="w33" v-if="source=='merchant'">商户名称</p>

      <p class="w33" v-else="source=='merchant'">二级代理名称</p>


      <p class="w21">收单分润</p>

      <p class="w21">提现分润</p>

      <p class="w21">分润总额</p>
    </div>
    <ul>
      <li v-for="info in $$pageInfo">
        <div class="total">
          <p class="w33">{{info.profitDate}}</p>

          <p class="w21">{{info.dayCollectTotalMoney}}</p>

          <p class="w21">{{info.daywithdrawTotalMoney}}</p>

          <p class="red w21">{{info.dayTotalMoney}}</p>
        </div>
        <div class="" v-for="list in info.list">
          <p class="w33" v-if="source=='merchant'">{{list.merchantName}}</p>

          <p class="w33" v-else="source=='merchant'">{{list.dealerName}}</p>

          <p class="w21">{{list.collectMoney}}</p>

          <p class="w21">{{list.withdrawMoney}}</p>

          <p class="w21">{{list.totalMoney}}</p>
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'index',
    data: function () {
      return {
        source: 'merchant',
        pageInfo: []
      }
    },
    created: function () {
      let query = this.$route.query;
      // 根据链接调接口 连接上标明是获取 to商户 or to代理商
      this.$data.source = query.source;
      console.log(this.$data.source)
      if (this.$data.source == 'merchant') {
        // to商户
        this.$http.post('/dealer/profit/toMerchant').then(function (res) {
          this.$data.pageInfo = res.data;
        }, function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        });
      } else {
        // to代理商
        this.$http.post('/dealer/profit/toDealer').then(function (res) {
          this.$data.pageInfo = res.data;
        }, function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
      }
    },
    computed: {
      $$pageInfo: function () {
        return this.$data.pageInfo;
      }
    }
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

  #main {
    overflow: auto;
    background: #eceff6;
  }

  #aginDetail {
    font-size: 12px;
    color: #272727;
    text-align: left;
    overflow: auto;
    background: #eceff6;

  .header {
    background: #fff;
    height: 32px;
    line-height: 32px;
    padding: 0 15px;

  p {
    display: inline-block;
    font-weight: bold;
  }

  }

  .w33 {
    width: 32%;
  }

  .w21 {
    width: 21%;
  }

  ul {

  li {
    margin: 7px 0;
    background: #fff;

  div {
    padding: 0 15px;
    height: 36px;
    line-height: 36px;
    border-bottom: 1px solid #eceff6;

  p {
    display: inline-block;
  }

  &
  .total {
    height: 45px;
    line-height: 45px;
    font-size: 14px;
    font-weight: bold;
  }

  .red {
    color: #fd4545;
  }

  }
  }
  }
  }
</style>
