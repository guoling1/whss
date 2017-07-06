<template lang="html">
  <div id="collection">
    <div class="space">
      <div class="prompt"><span></span>付款给{{$$merchantName}}</div>
      <div>
        <div v-keyboard="{type:keyboard,id:merchantId}"></div>
      </div>
      <div class="remark">和商家确定好金额后输入</div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'collection',
    data: function () {
      return {
        keyboard: 'weixinpay',
        merchantId: '',
        merchantName: ''
      }
    },
    created: function () {
      let GetQueryString = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        return unescape(r[2]);
      };
      let query = this.$route.query;
      this.$data.merchantId = query.merchantId;
      this.$data.merchantName = GetQueryString('merchantName');
      this.$data.keyboard = query.payType;
    },
    computed: {
      $$merchantName: function () {
        return this.$data.merchantName;
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  #collection {
    width: 100%;
    height: 100%;
    background-color: #f4f5f8;
  }

  .space {
    width: 100%;
    height: auto;
    background-color: #FFF;
    padding: 20px 15px;
    .prompt {
      width: 100%;
      height: 50px;
      line-height: 50px;
      text-align: left;
      font-size: 15px;
      color: #4b526d;
      span {
        display: inline-block;
        margin-right: 7px;
        transform: translate3d(0, 3px, 0);
        width: 17px;
        height: 17px;
        background: url("../../assets/payment.png") no-repeat center;
        background-size: 17px 17px;
      }
    }
    .remark {
      height: 40px;
      line-height: 40px;
      font-size: 12px;
      color: #a6abc3;
      text-align: right;
    }
  }
</style>
