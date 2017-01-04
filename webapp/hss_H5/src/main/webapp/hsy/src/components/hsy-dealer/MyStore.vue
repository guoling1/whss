<template lang="html">
  <div id="myStore">
    <div class="header">

      <p class="w33">店铺名</p>

      <p class="w21">二维码编号</p>

      <p class="w21">注册时间</p>

      <p class="w21">商户状态</p>
    </div>
    <ul>
      <li v-for="info in $$pageInfo">
        <div>
          <p class="w33">{{info.merchantName}}</p>

          <p class="w21">{{info.code}}</p>

          <p class="w21">{{info.registerDate}}</p>

          <p class="w21">{{info.status|status}}</p>
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
        pageInfo: []
      }
    },
    created: function () {
      this.$http.post('/dealer/getMyMerchants').then(function (res) {
        console.log(res)
        this.$data.pageInfo = res.data;
      }, function (err) {
        this.$store.commit('MESSAGE_ACCORD_SHOW', {
          text: err.statusMessage
        })
      });
    },
    computed: {
      $$pageInfo: function () {
        return this.$data.pageInfo;
      }
    },
    filters: {
      status: function (val) {
        if(val==0){
          val = '待审核'
        }else if(val==1){
          val = '审核通过'
        }else if(val==2){
          val = '审核失败'
        }else if(val==3){
          val = '禁用'
        }
        return val;
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

  #myStore {
    font-size: 12px;
    color: #272727;
    text-align: left;
  }

  .header {
    background: #fff;
    height: 32px;
    line-height: 32px;
    padding: 0 15px;
  p {
    display: inline-block;
    font-weight: bold;
    font-size: 14px;
  }
  }

  .w33 {
    width: 32%;
  }

  .w21 {
    width: 21%;
  }

  ul {
  margin-top: 7px;
  li {
    background: #fff;
  div {
    padding: 0 15px;
    height: 36px;
    line-height: 36px;
    border-bottom: 1px solid #eceff6;
  p {
    display: inline-block;
  }

  .red {
    color: #fd4545;
  }
  }
  }
  }
</style>
