<template lang="html">
  <div id="myCard">
    <ul>
      <li>
        <span>姓名</span>
        <span class="right">{{dealer.bankAccountName}}</span>
      </li>
      <li>
        <span>结算卡</span>
        <span class="right">{{dealer.bankCard}}</span>
      </li>
      <li>
        <span>手机号</span>
        <span class="right">{{dealer.bankReserveMobile}}</span>
      </li>
    </ul>
    <div class="submit" @click="success">
      确定
    </div>
  </div>
</template>

<script>
  export default {
    name:'myCard',
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
  ul{
    background: #fff;

  li{
    padding: 0 15px;
    height: 50px;
    line-height: 50px;
    text-align: left;
    border-bottom: 1px solid #edeef5;
  .right{
    float: right;
  }
  }
  }
  .submit{
    margin: 15px;
    height: 50px;
    line-height: 50px;
    text-align: center;
    font-size: 16px;
    background: #5289fb;
    border-radius: 4px;
    color: #fff;
  }
</style>
