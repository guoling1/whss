<template lang="html">
  <div id="success">
    <div class="space">
      <div class="info"></div>
      <div class="text">付款成功</div>
    </div>
    <div class="group">
      <div class="left">交易流水号</div>
      <div class="right">{{$$id.firstId}}<span>{{$$id.lastId}}</span></div>
    </div>
    <div class="group">
      <div class="left">付款金额</div>
      <div class="right">{{$$amount}}元</div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'success',
    data: function () {
      return {
        id: '',
        amount: ''
      }
    },
    created: function () {
      let params = this.$route.params;
      this.$data.id = params.id;
      this.$data.amount = params.amount;
    },
    computed: {
      $$id: function () {
        let firstId = this.$data.id.substr(0, this.$data.id.length - 6);
        let lastId = this.$data.id.substr(-6);
        return {
          firstId: firstId,
          lastId: lastId
        };
      },
      $$amount: function () {
        let amount = this.$data.amount;
        return (amount / 1).toFixed(2);
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  #success {
    width: 100%;
    height: 100%;
    background-color: #f4f5f8;
  }

  .space {
    width: 100%;
    padding: 60px 0;
    background-color: #FFF;
    border-bottom: 1px solid #e8eaf3;
    .info {
      width: 64px;
      height: 64px;
      margin: auto;
      background: url("../../assets/success.png") no-repeat center;
      background-size: 64px 64px;
    }
    .text {
      font-size: 18px;
      color: #5b648d;
      margin-top: 25px;
    }
  }

  .group {
    width: 100%;
    height: 52px;
    line-height: 52px;
    background-color: #FFF;
    border-bottom: 1px solid #e8eaf3;
    padding: 0 15px;
    .left {
      float: left;
      text-align: left;
      font-size: 15px;
      color: #8c93b2;
    }
    .right {
      float: right;
      text-align: right;
      font-size: 15px;
      color: #5a638c;
      span {
        color: #fd4f4f;
      }
    }
  }
</style>
