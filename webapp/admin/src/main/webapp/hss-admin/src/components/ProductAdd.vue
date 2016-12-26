<template lang="html">
  <div id="productAdd">
    <div v-if="isShow" style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">增加产品</div>
    <div v-else="isShow" style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">产品详情</div>
    <!--<h1 v-if="isShow">增加产品</h1>
    <h1 v-else="isShow">产品详情</h1>-->
    <div style="margin: 0 15px">
      <label for="">
        <p>产品名称：</p>
        <input type="text" v-model="productName">
        <span>例如：快收银2.0</span>
      </label>
      <div class="passAdd">
        <label for="">
          <p>添加通道：</p>
          <table class="table table-bordered" >
            <thead>
            <tr>
              <th>通道名称</th>
              <th>支付结算手续费</th>
              <th>结算时间</th>
              <th>提现结算费</th>
              <th>商户支付手续费</th>
              <th>商户提现手续费</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(product,index) in $$products">
              <td>{{product.channelName}}</td>
              <td><input type="text" v-model="$$channels[index].productTradeRate">%</td>
              <td>T1</td>
              <td><input type="text" v-model="$$channels[index].productWithdrawFee">元/笔</td>
              <td><input type="text" v-model="$$channels[index].productMerchantPayRate">%</td>
              <td><input type="text" v-model="$$channels[index].productMerchantWithdrawFee">元/笔</td>
            </tr>

            </tbody>
          </table>
          <router-link to="/admin/record/passAdd" class="btn btn-primary add">添加通道</router-link>
        </label>
      </div>
      <label for="">
        <p>支付手续费加价限额：</p>
        <input type="text" v-model="limitPayFeeRate">%
        <span>允许一级代理商提高商户的手续费最高限制，例如：0.05%</span>
      </label>
      <label for="">
        <p>提现手续费加价限额：</p>
        <input type="text" v-model="limitWithdrawFeeRate">
        元/笔
        <span>允许一级代理商提高商户的手续费最高限制，例如：0.5元／笔</span>
      </label>
      <label for="">
        <p>商户提现模式：</p>
        <input type="radio" value="HAND" v-model="merchantWithdrawType">手动提现
        <input type="radio" style="margin-left: 20px" value="AUTO" v-model="merchantWithdrawType">逐笔自动提现
      </label>
      <label for="">
        <p>代理商结算模式：</p>
        <input type="radio" value="D0" v-model="dealerBalanceType">D0
        <input type="radio" style="margin-left: 20px" value="D1" v-model="dealerBalanceType">日结
        <input type="radio" style="margin-left: 20px" value="M1" v-model="dealerBalanceType">月结
      </label>
      <div class="btn btn-primary product" @click="productAdd" v-if="isShow">创建产品</div>
      <div class="btn btn-danger product" @click="productAdd" v-if="!isShow">修改</div>
      <div class="btn btn-success product" @click="" v-if="!isShow" @click="geBack">返回</div>
    </div>
  </div>
</template>

<script lang="babel">
  export default{
    name:'productAdd',
    data(){
      return{
        productName:"",
        limitPayFeeRate:"",
        limitWithdrawFeeRate:"",
        merchantWithdrawType:"",
        dealerBalanceType:"",
        channels:[],
        products:[],//后台通道列表
        productMsg:{},
        isShow:true,//true添加，false修改
        productId:'',
        accountId:''
      }
    },
    created: function () {
      this.$http.post('/admin/channel/list',{})
        .then(function(res){
          this.$data.channels.length=res.data.length;
          for(let i=0;i<this.$data.channels.length;i++){
            this.$data.channels[i]={
              productTradeRate:'',
              productWithdrawFee:'',
              productMerchantPayRate:'',
              productMerchantWithdrawFee:''
            }
          }
          this.$data.products=res.data;
          console.log(this.$data.products)
        },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })

      if(this.$route.query.id!=undefined){
        this.$data.isShow = false;
        this.$http.post('/admin/product/list')
          .then(function (res) {
            var data = this.$data.product = res.data[this.$route.query.id];
            console.log(this.$data.product)
            this.$data.productName = data.productName;
            this.$data.limitPayFeeRate = data.limitPayFeeRate;
            this.$data.limitWithdrawFeeRate = data.limitWithdrawFeeRate;
            this.$data.merchantWithdrawType = data.merchantWithdrawType;
            this.$data.dealerBalanceType = data.dealerBalanceType;
            this.$data.productId = data.productId;
            this.$data.accountId = data.accountId;
            for (var i=0;i<data.list.length;i++){
                this.$data.channels[i].productTradeRate=data.list[i].productTradeRate;
                this.$data.channels[i].productWithdrawFee=data.list[i].productWithdrawFee;
                this.$data.channels[i].productMerchantPayRate=data.list[i].productMerchantPayRate;
              this.$data.channels[i].productMerchantWithdrawFee=data.list[i].productMerchantWithdrawFee;
              this.$data.channels[i].id=data.list[i].id;
              this.$data.channels[i].status=data.list[i].status;
            }
          }, function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      }
    },
    methods:{
      productAdd: function () {
        let query = {
          productName:this.$data.productName,
          limitPayFeeRate:this.$data.limitPayFeeRate,
          limitWithdrawFeeRate:this.$data.limitWithdrawFeeRate,
          merchantWithdrawType:this.$data.merchantWithdrawType,
          dealerBalanceType:this.$data.dealerBalanceType,
          basicBalanceType:this.$data.basicBalanceType,
          channels:this.$data.channels
        }
        for(let i= 0;i<this.$data.channels.length;i++){
          query.channels[i].basicChannelId = this.$data.products[i].id;
          query.channels[i].productBalanceType = this.$data.products[i].basicBalanceType;
          query.channels[i].channelTypeSign = this.$data.products[i].channelTypeSign;
        }
        if(this.$data.isShow==true){
          this.$http.post('/admin/product/add',query)
            .then(function(res){
                this.$store.commit('MESSAGE_ACCORD_SHOW', {
                  text: '创建成功'
                })
            },function(err){
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: err.statusMessage
              })
            })
        }else {
          query.productId=this.$data.productId;
          query.accountId = this.$data.accountId;
          query.list=query.channels
          this.$http.post('/admin/product/update',query)
            .then(function(res){
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: '修改成功'
              })
              this.$router.push('/admin/record/productList')
            },function(err){
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: err.statusMessage
              })
            })
        }
      },
      goBack:function () {
        this.$router.push('/admin/record/productList')
      }
    },
    computed: {
      $$products:function () {
        return this.$data.products;
      },
      $$channels: function () {
        return this.$data.channels;
      }
    }
  }
</script>
<style scoped lang="less">
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #fff;
  }
  .productAdd{
    float: right;
    width: 80%;
  }
  label{
    display:block;
    p{
      display: inline-block;
      width: 80px;
    }
    span{
      color: #ccc;
      margin-left: 20px;
    }
  }
  table{
    width: 80%;
    margin:20px 0 0 50px;
    th,td{
      width: 16.6%;
      input{
        width: 70%;
      }
    }

  }
  .add{
    margin:10px 0 10px 50px;
  }
  .product{
    margin:20px 0 0 20px;
    width: 200px;
  }
</style>
