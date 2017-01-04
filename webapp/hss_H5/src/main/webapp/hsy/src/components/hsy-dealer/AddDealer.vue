<template lang="html">
<div id="addDealer">
  <div class="basic">
    <p class="title">基本资料</p>
    <!--<label for="">-->
      <!--<span>代理名称</span>-->
      <!--<input type="text" name="name" placeholder="输入商户名称" v-model="query.name">-->
    <!--</label>-->
    <label for="">
      <span>代理人姓名</span>
      <input type="text" name="name" placeholder="输入代理人真实姓名" v-model="query.name">
    </label>
    <label for="">
      <span>代理人手机</span>
      <input type="text" name="name" placeholder="二级代理手机号，用于登录" v-model="query.mobile">
    </label>
    <label for="">
      <span>所在地</span>
      <input type="text" name="name" placeholder="省/市/区/街道详情" v-model="query.belongArea">
    </label>
    <!--<label for="">-->
      <!--<span></span>-->
      <!--<input type="text" name="name" placeholder="街道详情" v-model="query.name">-->
    <!--</label>-->
  </div>
  <div class="basic">
    <p class="title">结算信息</p>
    <label for="">
      <span>结算卡</span>
      <input type="text" name="name" placeholder="输入分润结算银行卡号" v-model="query.bankCard">
    </label>
    <label for="">
      <span>开户名</span>
      <input type="text" name="name" placeholder="银行开户名称" v-model="query.bankAccountName">
    </label>
    <label for="">
      <span>结算银行</span>
      <input type="text" name="name" placeholder="银行开户预留手机号" v-model="query.bankReserveMobile">
    </label>
  </div>
  <div class="set">
    <p class="title">分润设置</p>
    <ul>
      <li>
        <div class="top">
          <img src="../../assets/zfb.png" alt="" />
          <span>支付宝(D0)</span>
          <div class="">
            <p>我的收单结算价：{{myMsg.alipaySettleRate}}%</p>
            <p>商户收单手续费：{{myMsg.merchantSettleRate}}元</p>
          </div>
        </div>
        <div class="bottom">
          <label for="">
            结算价
            <input type="text" name="name" value="" v-model="query.alipaySettleRate">%
          </label>
          <label for="">
            分润
            <input type="text" name="name" value="">%
          </label>
        </div>
      </li>
      <li>
        <div class="top">
          <img src="../../assets/wx.png" alt="" />
          <span>微信(D0)</span>
          <div class="">
            <p>我的收单结算价：{{myMsg.weixinSettleRate}}%</p>
            <p>商户收单手续费：{{myMsg.merchantSettleRate}}元</p>
          </div>
        </div>
        <div class="bottom">
          <label for="">
            结算价
            <input type="text" name="name" value="" v-model="query.weixinSettleRate">%
          </label>
          <label for="">
            分润
            <input type="text" name="name" value="">%
          </label>
        </div>
      </li>
      <li>
        <div class="top">
          <img src="../../assets/card.png" alt="" />
          <span>无卡快捷(D0)</span>
          <div class="">
            <p>我的收单结算价：{{myMsg.quickSettleRate}}%</p>
            <p>商户收单手续费：{{myMsg.merchantSettleRate}}元</p>
          </div>
        </div>
        <div class="bottom">
          <label for="">
            结算价
            <input type="text" name="name" value="" v-model="query.quickSettleRate">%
          </label>
          <label for="">
            分润
            <input type="text" name="name" value="">%
          </label>
        </div>
      </li>
    </ul>
  </div>
  <div class="price">
    <div class="top">
      <span>提现结算价</span>
      <div class="">
        <input type="text" name="name" value="" v-model="query.withdrawSettleFee">
        <span>元/笔</span>
      </div>
    </div>
    <div class="bottom">
      <span>我的提现结算价：{{myMsg.withdrawSettleFee}}元 / 笔</span>
      <span>商户提现手续费：{{myMsg.merchantWithdrawSettleFee}}元 / 笔</span>
    </div>
  </div>
  <div class="submit" @click="addDealer">
    提交
  </div>
</div>
</template>

<script>
export default {
  name:'addDealer',
  data(){
    return{
      myMsg:{},
      query:{}
    }
  },
  created:function () {
    this.$http.post('/dealer/get')
      .then(function (res) {
        console.log(res)
        this.$data.myMsg = res.data
      },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
      })
  },
  methods:{
    addDealer:function () {
      console.log(this.$data.query)
      this.$http.post('/dealer/addSecondDealer',this.$data.query)
        .then(function(res){
          console.log(res.data)
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: '添加成功'
          })
        },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    }
  },
//  filters: {
//    tofix: function (val) {
//      return val.toFixed(2)
//    }
//  }
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
#addDealer {
  overflow: auto;
  background: #eceff6;
  font-size: 15px;
  color: #272727;
  text-align: left;

.title {
  padding-left: 15px;
  font-size: 14px;
  height: 39px;
  line-height: 39px;
  font-weight: bold;
}

.basic {
  width: 100%;
  background: #fff;
  margin-bottom: 7px;

label {
  display: inline-block;
  padding-left: 15px;
  width: 100%;
  height: 54px;
  line-height: 54px;
  border-top: 1px solid #e8eaf2;

span {
  display: inline-block;
  width: 90px;
}

input {
  width: 65%;
  height: 52px;
  line-height: 52px;
}

}
}
.set {
  width: 100%;
  background: #fff;
  padding: 0 7px;

ul {
  padding-bottom: 19px;

li {
  margin-top: 11px;
  padding: 3px;
  height: 103px;
  background: #f1f4f9;
  border-radius: 2px;

.top {
  font-size: 12px;
  height: 24px;
  line-height: 24px;

img {
  width: 14px;
  height: 14px;
}

p {
  display: inline-block;
  color: #999;
  margin-left: 7px;
}

div {
  float: right;
}

}
.bottom {
  width: 100%;
  height: 74px;
  background: #fff;
  padding: 21px 0;

label {
  margin-left: 18px;
  font-size: 15px;

input {
  width: 53px;
  height: 31px;
  border: 1px solid #d0d7e3;
  border-radius: 2px;
  margin: 0 4px 0 6px;
}

}
}
}
}
}
.price {
  margin-top: 7px;
  padding: 5px 15px 0;
  height: 90px;
  background: #fff;

.top {
  font-size: 15px;
  height: 50px;
  line-height: 50px;
  border-bottom: 1px solid #eceff6;

div {
  float: right;
}

}
.bottom {
  height: 35px;
  line-height: 35px;
  font-size: 12px;
  color: #999;

span {
  margin-right: 5%;
}

}
}
.submit {
  margin: 15px;
  height: 50px;
  line-height: 50px;
  text-align: center;
  font-size: 16px;
  background: #5289fb;
  border-radius: 4px;
  color: #fff;
}

}
</style>
