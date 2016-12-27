<template lang="html">
  <div id="prompt">
    <img src="../../assets/book.png" alt="">
    <p>您需要先提交资料</p>
    <p class="small">审核通过后才可使用</p>
    <div class="btn">完善资料</div>
  </div>
</template>

<script lang="babel">
  export default{
    name:'follow',
    data(){
      return{
        msg:'hello vue'
      }
    },
    components:{

    }
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

  #prompt {
    padding: 89px 58px 0;
    width: 100%;
    height: 100%;
    background: #FFF;
    img{
      display: block;
      width: 76px;
      height: 86px;
      margin:0 auto;
      margin-bottom: 42px;
    }
    p{
      font-size: 22px;
      color: #aab2d3;
    }
    p.small{
      font-size: 15px;
      color: #aab2d3;
      margin:13px 0 43px;
    }
    .btn{
      width: 100%;
      height: 50px;
      line-height: 50px;
      font-size: 16px;
      color: #fff;
      background: #7086bd;
      border-radius: 4px;
    }
  }
</style>
