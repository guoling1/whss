<template lang="html">
  <div id="wallet">
    <div class="wallet">
      <div class="operation" @click="collection($event)">
        <div class="logo collection"></div>
        <div class="operation-title">收款</div>
      </div>
      <div class="operation">
        <div class="logo wallet"></div>
        <div class="operation-title">余额</div>
        <div class="operation-small">￥728.36</div>
      </div>
      <div class="operation">
        <div class="logo card"></div>
        <div class="operation-title">提现</div>
      </div>
    </div>
    <div class="record">
      <div class="group"><span class="money"></span>收款记录</div>
      <div class="group"><span class="card"></span>银行卡</div>
    </div>
    <div class="flexBox flex-box-column">
      <div class="advertisement">

      </div>
      <div class="application">
        <div class="group">
          <div class="logo friend"></div>
          <div class="text">推荐好友</div>
        </div>
        <div class="group">
          <div class="logo upgrade"></div>
          <div class="text">我要升级</div>
        </div>
        <div class="group">
          <div class="logo train"></div>
          <div class="text">火车票</div>
        </div>
        <!--<div class="group">-->
          <!--<div class="logo movie"></div>-->
          <!--<div class="text">电影票</div>-->
        <!--</div>-->
        <div class="group">
          <div class="logo loan"></div>
          <div class="text">贷款申请</div>
        </div>
        <div class="group">
          <div class="logo credit"></div>
          <div class="text">信用卡申请</div>
        </div>
        <div class="group">
          <div class="logo more"></div>
          <div class="text">更多</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'wallet',
    data: function () {
      return {
        msg: '我的钱包'
      }
    },
    methods: {
      collection: function (event) {
        this.$router.push({path: '/sqb/collection'});
      }
    },
    computed: {
      $$data: function () {
        return this.$data;
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

  .wallet {
    width: 100%;
    height: 130px;
    background-color: #4b65a8;
    .operation {
      float: left;
      width: 33.33%;
      height: 130px;
      .logo {
        width: 40px;
        height: 30px;
        margin: 30px auto 8px;
        &.collection {
          background: url("../../assets/shoukuan.png") no-repeat center;
          background-size: 33px 25px;
        }
        &.wallet {
          background: url("../../assets/qb.png") no-repeat center;
          background-size: 30px 30px;
        }
        &.card {
          background: url("../../assets/cardWrite.png") no-repeat center;
          background-size: 36px 25px;
        }
      }
      .operation-title {
        font-size: 15px;
        color: #FFF;
      }
      .operation-small {
        font-size: 11px;
        color: #849ad2;
      }
    }
  }

  .record {
    width: 100%;
    height: 55px;
    background: #FFF url("../../assets/line.png") no-repeat center;
    background-size: 1px 27px;
    span {
      display: inline-block;
      width: 20px;
      height: 20px;
      margin-right: 10px;
      transform: translate3d(0, 4px, 0);
      &.money {
        background: url("../../assets/money.png") no-repeat center;
        background-size: 20px 20px;
      }
      &.card {
        background: url("../../assets/cardGreen.png") no-repeat center;
        background-size: 20px 20px;
      }
    }
    .group {
      float: left;
      width: 50%;
      height: 55px;
      line-height: 55px;
    }
  }

  .advertisement {
    width: 100%;
    height: 105px;
    background: url("../../assets/ad1.png") no-repeat center;
    background-size: 100%;
    border-top: 8px solid #f4f5f8;
    border-bottom: 8px solid #f4f5f8;
  }

  .application {
    .flexItem(1);
    width: 100%;
    height: auto;
    overflow: auto;
    padding: 25px 15px 0;
    .group {
      float: left;
      width: 25%;
      height: 100px;
      .logo {
        width: 30px;
        height: 30px;
        margin: 20px auto 15px;
        &.friend {
          background: url("../../assets/friend.png") no-repeat center;
          background-size: 29px 29px;
        }
        &.upgrade {
          background: url("../../assets/shengji.png") no-repeat center;
          background-size: 30px 27px;
        }
        &.train {
          background: url("../../assets/train.png") no-repeat center;
          background-size: 27px 30px;
        }
        &.movie {
          background: url("../../assets/movie.png") no-repeat center;
          background-size: 27px 27px;
        }
        &.loan {
          background: url("../../assets/loan.png") no-repeat center;
          background-size: 25px 27px;
        }
        &.credit {
          background: url("../../assets/card1.png") no-repeat center;
          background-size: 30px 23px;
        }
        &.more {
          background: url("../../assets/more.png") no-repeat center;
          background-size: 22px 22px;
        }
      }
      .text {
        font-size: 14px;
        color: #7079a0;
        text-align: center;
      }
    }
  }
</style>
