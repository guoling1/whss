<template lang="html">
  <div id="chargeCode" class="flexBox flex-box-column">
    <div class="content">
      <div class="top">
        <p class="shop">{{subMerName}}</p>

        <p class="price">￥<span>{{amount}}</span></p>
        <i class="left"></i>
        <i class="right"></i>
      </div>
      <div class="bottom">
        <div class="qr" v-qr="{url:payUrl}"></div>

        <div class="bar">
          <img src="../../assets/wxb.png" alt=""/>
          <img src="../../assets/zfbb.png" alt=""/>
          <span>长按或扫描二维码付款</span>
        </div>
      </div>
    </div>
    <div class="explain">
      <p class="refresh">二维码仅支持扫描一次，点击刷新</p>

      <p class="server">由好收银提供服务</p>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'chargeCode',
    data: function () {
      return {
        msg: '收款二维码',
        orderId: '',
        outTradeNo: '',
        payUrl: '',
        subMerName: '',
        amount: ''
      }
    },
    created: function () {
      let query = this.$route.query;
      this.$data.orderId = query.orderId;
      this.$data.outTradeNo = query.outTradeNo;
      this.$data.payUrl = query.payUrl;
      this.$data.subMerName = query.subMerName;
      this.$data.amount = query.amount;
    },
    methods: {
      refresh: function () {
        this.$http.post('/wx/receipt', {
          totalFee: this.$data.amount, //收款金额（小数点后两位，单位元）
          payChannel: '101' //101.微信扫码102.支付宝扫码103.银联
        }).then(function (res) {

        }, function (err) {
          store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
      }
    },
    computed: {
      $$data: function () {
        return this.$data;
      }
    }
  }
</script>

<style scoped lang="less">
  .flexItem(@val) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
  }

  #chargeCode {
    padding: 20px 0;
    width: 100%;
    height: 100%;
    background-color: #4b526d;
  }

  .content {
    .flexItem(1);
    margin: 0 auto;
    position: relative;
    width: 92%;
    height: auto;
    overflow: auto;
    background: #fff;
    box-shadow: 0 0 10px #424964;
    .top {
      position: relative;
      width: 100%;
      height: 140px;
      border-bottom: 1px dashed #666d87;
      color: #4b526d;
      .shop {
        padding: 34px 0 24px;
        font-size: 15px;
      }
      .price {
        font-size: 36px;
        font-weight: bold;
        span {
          font-size: 40px;
        }
      }
      i {
        display: block;
        width: 9px;
        height: 18px;
        background: #424964;
        position: absolute;

        &.left {
          border-radius: 0 9px 9px 0;
          bottom: -9px;
        }
        &.right {
          border-radius: 9px 0 0 9px;
          bottom: -9px;
          right: 0;
        }
      }
    }
    .bottom {
      .qr {
        display: block;
        margin: 58px auto 0;
        width: 180px;
        height: 180px;
      }
      .bar {
        position: absolute;
        bottom: 0;
        width: 100%;
        height: 50px;
        line-height: 50px;
        background: url('../../assets/bg.png') no-repeat bottom;
        background-size: 100% 57px;
        color: #fff;
        font-size: 15px;
        img {
          display: inline-block;
          margin-right: 11px;
          width: 23px;
          height: 23px;
        }
        span {
          display: inline-block;
          vertical-align: middle;
          margin-bottom: 16px;
        }
      }
    }
  }

  .explain {
    height: 100px;
    .refresh {
      line-height: 60px;
      font-size: 12px;
      color: #7984ac;
    }
    .server {
      font-size: 12px;
      color: #596283;
    }
  }
</style>
