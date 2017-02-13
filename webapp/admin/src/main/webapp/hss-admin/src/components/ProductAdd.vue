<template lang="html">
  <div id="productAdd">
    <div style="margin: 15px;width: inherit" class="box">
      <form class="form-horizontal">
        <div class="box-header">
          <h3 v-if="isShow" class="box-title">增加产品</h3>
          <h3 v-else="isShow" class="box-title">产品详情</h3>
        </div>
        <div class="box-body">
          <div class="form-group">
            <label for="productName" class="col-sm-2 control-label">项目类型</label>
            <div class="col-sm-4">
              <select class="form-control select2 select2-hidden-accessible" tabindex="-1" aria-hidden="true" v-model="productId">
                <option value="">请选择</option>
                <option value="hss">好收收</option>
                <option value="hsy">好收银</option>
              </select>
            </div>
            <div class="col-sm-6 right"></div>
          </div>
          <div class="form-group">
            <label for="productName" class="col-sm-2 control-label">产品名称</label>
            <div class="col-sm-4">
              <input type="text" class="form-control" id="productName" v-model="productName">
            </div>
            <div class="col-sm-6 right">例如：快收银2.0</div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label" style="text-align: left;margin: 0 0 15px 4%;">通道配置</label>
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
                <td><input type="text" v-model="$$channels[index].productTradeRate"> %</td>
                <td>T1</td>
                <td><input type="text" v-model="$$channels[index].productWithdrawFee"> 元/笔</td>
                <td><input type="text" v-model="$$channels[index].productMerchantPayRate"> %</td>
                <td><input type="text" v-model="$$channels[index].productMerchantWithdrawFee"> 元/笔</td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="form-group">
            <label for="limitPayFeeRate" class="col-sm-2 control-label">支付手续费加价限额</label>

            <div class="col-sm-4 middle">
              <input type="text" class="form-control" id="limitPayFeeRate" v-model="limitPayFeeRate">
              <i>%</i>
            </div>
            <div class="col-sm-6 right">允许一级代理商提高商户的手续费最高限制，例如：0.05%</div>
          </div>
          <div class="form-group">
            <label for="limitWithdrawFeeRate" class="col-sm-2 control-label">提现手续费加价限额</label>

            <div class="col-sm-4 middle">
              <input type="text" class="form-control" id="limitWithdrawFeeRate" v-model="limitWithdrawFeeRate">
              <i>元/笔</i>
            </div>
            <div class="col-sm-6 right">允许一级代理商提高商户的手续费最高限制，例如：0.5元／笔</div>
          </div>
          <div class="form-group">
            <label for="" class="col-sm-2 control-label">商户提现模式</label>

            <div class="col-sm-4">
              <select class="form-control" v-model="merchantWithdrawType">
                <option value="">请选择</option>
                <option value="HAND">手动提现</option>
                <option value="AUTO">逐笔自动提现</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label for="" class="col-sm-2 control-label">代理商结算模式</label>

            <div class="col-sm-4 middle">
              <select class="form-control" v-model="dealerBalanceType">
                <option value="">请选择</option>
                <option value="D0">D0</option>
                <option value="D1">日结</option>
                <option value="M1">月结</option>
              </select>
            </div>
          </div>
        </div>
        <div class="box-footer">
          <div type="submit" class="btn btn-default" @click="productAdd" v-if="isShow">创建产品</div>
          <div type="submit" class="btn btn-default" @click="productAdd" v-if="!isShow">修 改</div>
          <div type="submit" class="btn btn-info pull-right" v-if="!isShow" @click="goBack">返 回</div>
        </div>
      </form>
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
              this.$router.push('/admin/record/productList')
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

  table{
    width: 90%;
    margin:20px auto;
  }
  .middle{
    position: relative;
    i{
      position: absolute;
      right: 23px;
      top: 8px;
    }
  }
  .right{
    padding-top: 7px;
  }
  .btn,select{
    font-size: 12px;
  }
</style>
