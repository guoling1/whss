<template lang="html">
  <div id="collection">
    <div class="space">
      <div class="prompt"><span></span>收款金额</div>
      <div>
        <div v-keyboard="{type:keyboard}"></div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'collection',
    data: function () {
      return {
        keyboard: 'QR',
        msg: '收款'
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
        background: url("../../assets/money.png") no-repeat center;
        background-size: 17px 17px;
      }
    }
  }
</style>
