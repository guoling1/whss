<template lang="html">
  <div id="follow">
    <p class="title">您必须关注我们的微信才可以为您提供完整的服务</p>

    <p>1. 查账对账，从公众号便捷访问</p>

    <p>2.“收银”提示，我们将通过微信逐笔通知您收银情况</p>

    <div class="content">
      <img src="../../assets/cord.png" alt="">

      <div class="green">
        <p>长按关注公众号</p>

        <p>公众号ID：kuaishouyinsh</p>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default{
    name: 'follow',
    data(){
      return {
        msg: 'hello vue'
      }
    },
    components: {}
  }
</script>
<style lang="less">
  .flexBox {
    display: box;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    display: -webkit-flex;
  }

  .flexItem(@val) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
  }

  #follow {
    padding: 22px 15px 0 15px;
    width: 100%;
    height: 100%;
    font-size: 15px;
    color: #fff;
    text-align: left;
    background: #4b526d;
    p.title {
      font-size: 15px;
      font-weight: bold;
      border-left: 2px solid #3dae78;
      padding-left: 11px;
      height: 15px;
      margin-bottom: 15px;
    }
    p {
      font-size: 12px;
      height: 23px;

    }

    .content {
      width: 100%;
      margin-top: 40px;
      border-radius: 5px;
      padding-top: 26px;
      background: #fff;
      box-shadow: 0px 0px 10px #000;
      margin-top: 40px;
      img {
        display: -webkit-box;
        width: 303px;
        height: 303px;
        margin: 0 auto;
        border: 1px solid #36b178;
      }
    }
    .green {
      width: 100%;
      height: 74px;
      background: #36b178;
      border-radius: 0 0 5px 5px;
      text-align: center;
      margin-top: 20px;
      padding-top: 1px;
      p:nth-child(1) {
        margin: 17px 0 6px 0;
      }
    }
  }
</style>
