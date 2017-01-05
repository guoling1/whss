<template lang="html">
  <div id="agentAdd">
    <div v-if="isShow" style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">新增代理商</div>
    <div v-if="!isShow" style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">代理商资料</div>
    <div style="margin: 0px 15px 15px;">
      <div class="box box-info">
        <div class="box-header with-border">
          <h3 class="box-title">基本信息</h3>
        </div>
        <form class="form-horizontal">
          <div class="box-body">
            <div class="form-group">
              <label for="mobile" class="col-sm-3 control-label">代理商手机号</label>

              <div class="col-sm-4">
                <input type="tel" id="mobile" class="form-control" name="mobile" placeholder="代理手机号，用于登录" v-model="$$data.mobile">
              </div>
            </div>
            <div class="form-group">
              <label for="name" class="col-sm-3 control-label">代理名称</label>

              <div class="col-sm-4">
                <input type="text" class="form-control" id="name" name="name" placeholder="" v-model="$$data.name">
              </div>
            </div>
            <div class="form-group">
              <label for="belongArea" class="col-sm-3 control-label">所在地</label>

              <div class="col-sm-4">
                <input type="text" class="form-control" id="belongArea" name="belongArea" placeholder="省/市/街道" v-model="$$data.belongArea">
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="box box-info">
        <div class="box-header with-border">
          <h3 class="box-title">结算信息</h3>
        </div>
        <!-- /.box-header -->
        <!-- form start -->
        <form class="form-horizontal">
          <div class="box-body">
            <div class="form-group">
              <label for="bankCard" class="col-sm-3 control-label">结算卡</label>

              <div class="col-sm-4">
                <input type="text" class="form-control" id="bankCard" name="bankCard" placeholder="输入一级代理银行卡号" v-model="$$data.bankCard">
              </div>
            </div>
            <div class="form-group">
              <label for="bankAccountName" class="col-sm-3 control-label">开户名</label>

              <div class="col-sm-4">
                <input type="text" class="form-control" id="bankAccountName" name="bankAccountName" placeholder="银行开户名称" v-model="$$data.bankAccountName">
              </div>
            </div>
            <div class="form-group">
              <label for="idCard" class="col-sm-3 control-label">身份证号</label>

              <div class="col-sm-4">
                <input type="text" class="form-control" id="idCard" name="idCard" placeholder="" v-model="$$data.idCard">
              </div>
            </div>
            <div class="form-group">
              <label for="bankReserveMobile" class="col-sm-3 control-label">开户手机号</label>

              <div class="col-sm-4">
                <input type="text" class="form-control" id="bankReserveMobile" name="bankReserveMobile" placeholder="银行开户预留手机号" v-model="$$data.bankReserveMobile">
              </div>
            </div>
          </div>
          <!-- /.box-body -->
        </form>
      </div>
      <div class="box box-info">
        <div class="box-header with-border">
          <h3 class="box-title">产品分润设置</h3>
        </div>
        <form class="form-horizontal">
          <div class="box-body">
            <div class="form-group">
              <div class="product">
                <label v-for="(product,index) in $$products">
                  <input class="check" type="radio" name="name" :value="index" v-model="id">

                  <div class="product1">
                    <div class="col-xs-12">
                      <div class="box box1">
                        <div class="box-header">
                          <h3 class="box-title">{{product.productName}}</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                          <table class="table table-hover">
                            <tbody>
                            <tr>
                              <th>通道名称</th>
                              <th>支付结算手续费</th>
                              <th>结算时间</th>
                              <th>提现结算费</th>
                              <th>商户支付手续费</th>
                              <th>商户提现手续费</th>
                            </tr>
                            <tr v-for="channel in product.list">
                              <td>{{channel.channelTypeSign|changeName}}</td>
                              <td><input type="text" name="name" v-model="channel.paymentSettleRate">%</td>
                              <td>{{channel.productBalanceType}}</td>
                              <td><input type="text" name="name" v-model="channel.withdrawSettleFee">元/笔</td>
                              <td><input type="text" name="name" v-model="channel.merchantSettleRate">%</td>
                              <td><input type="text" name="name" v-model="channel.merchantWithdrawFee">元/笔</td>
                            </tr>
                            </tbody></table>
                        </div>
                        <!-- /.box-body -->
                      </div>
                      <!-- /.box -->
                    </div>
                  </div>
                </label>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="box box-info">
        <div class="box-header with-border">
          <h3 class="box-title">合伙人推荐功能开关</h3>
        </div>
        <form class="form-horizontal">
          <div class="box-body">
            <div class="form-group">
              <select class="form-control select2 select2-hidden-accessible" style="width: 25%;margin-left: 10%;" tabindex="-1" aria-hidden="true" v-model="recommendBtn">
                <option value="2">开</option>
                <option value="1">关</option>
              </select>
            </div>
          </div>
          <!-- /.box-body -->
        </form>
      </div>
      <div class="box box-info">
        <div class="box-header with-border">
          <h3 class="box-title">合伙人推荐分润设置</h3>
        </div>
        <form class="form-horizontal">
          <div class="box-body">
            <div class="form-group">
              <div class="product">
                <div class="col-xs-12">
                      <div class="box box1">
                        <div class="box-body table-responsive no-padding">
                          <table class="table table-hover">
                            <tbody>
                            <tr>
                              <th>收单总分润空间</th>
                              <th colspan="5">
                                <input type="number" style="width: 20%" v-model="totalProfitSpace">%
                                （总分润空间不可高于0.2%，收单分润需扣除商户升级及推荐的分润成本）
                              </th>
                            </tr>
                            <tr>
                              <td>分润类型</td>
                              <td>金开门分润比例</td>
                              <td>一级代理商分润比例</td>
                              <td>二级代理分润比例</td>
                              <td rowspan="3" style="width: 20%">金开门，一级代理，二级代理的比例之和必须等于100%</td>
                            </tr>
                            <tr>
                              <td>升级费分润</td>
                              <td>20%</td>
                              <td><input type="number" v-model="rate1">%</td>
                              <td><input type="number" v-model="rate2">%</td>
                            </tr>
                            <tr>
                              <td>收单分润</td>
                              <td>20%</td>
                              <td><input type="number" v-model="rate3">%</td>
                              <td><input type="number" v-model="rate4">%</td>
                            </tr>
                            </tbody></table>
                        </div>
                        <!-- /.box-body -->
                      </div>
                      <!-- /.box -->
                    </div>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="btn btn-default" @click="create" v-if="isShow">
        创建代理商
      </div>
      <div class="btn btn-default" @click="change" v-if="!isShow">
        修改
      </div>
      <div class="btn btn-default" @click="goBack" v-if="!isShow">
        返回
      </div>
    </div>

  </div>
</template>

<script lang="babel">
  export default {
    name: 'dale',
    data () {
      return {
        mobile: '',
        name: '',
        belongArea: '',
        bankCard: '',
        bankAccountName: '',
        bankReserveMobile: '',
        idCard:'',
        product: {
          channels: []
        },
        products: [],//所有产品
        id: 0,
        isShow:true,
        recommendBtn:2,
        totalProfitSpace:'',
        rate1:'',
        rate2:'',
        rate3:'',
        rate4:'',
        dealerUpgerdeRates:[
          {
            productId:'',
            type:1,
            firstDealerShareProfitRate:'',
            secondDealerShareProfitRate:'',
            bossDealerShareRate:0.2
          },
          {
            productId:'',
            type:2,
            firstDealerShareProfitRate:'',
            secondDealerShareProfitRate:'',
            bossDealerShareRate:0.2
          }
        ],
      }
    },
    created: function () {
        this.$http.post('/admin/product/list')
          .then(function (res) {
            this.$data.products = res.data;
            for (let i = 0; i < this.$data.products.length; i++) {
              for (let j = 0; j < this.$data.products[i].list.length; j++) {
                this.$data.products[i].list[j].paymentSettleRate = '';
                (this.$data.products[i].list)[j].withdrawSettleFee = '';
                (this.$data.products[i].list)[j].merchantSettleRate = '';
                (this.$data.products[i].list)[j].merchantWithdrawFee = '';
              }
            }
            if(this.$route.query.id !=undefined){
              this.$data.isShow = false;
              this.$http.get('/admin/dealer/'+this.$route.query.id)
                .then(function (res) {
                  this.$data.mobile = res.data.mobile;
                  this.$data.name = res.data.name;
                  this.$data.belongArea = res.data.belongArea;
                  this.$data.bankCard = res.data.bankCard;
                  this.$data.bankAccountName = res.data.bankAccountName;
                  this.$data.idCard = res.data.idCard;
                  this.$data.recommendBtn = res.data.recommendBtn;
                  this.$data.totalProfitSpace = res.data.totalProfitSpace*100;
                  this.$data.bankReserveMobile = res.data.bankReserveMobile;
                  this.$data.products = [res.data.product];
                  this.$data.products[0].list = res.data.product.channels
                  for(var i= 0; i < this.$data.products[0].list.length; i++){
                      this.$data.products[0].list[i].channelTypeSign = this.$data.products[0].list[i].channelType
                      this.$data.products[0].list[i].productBalanceType = this.$data.products[0].list[i].settleType
                  }
                  this.$data.rate1 = res.data.dealerUpgerdeRates[0].firstDealerShareProfitRate*100;
                  this.$data.rate2 = res.data.dealerUpgerdeRates[0].secondDealerShareProfitRate*100;
                  this.$data.rate3 = res.data.dealerUpgerdeRates[1].firstDealerShareProfitRate*100;
                  this.$data.rate4 = res.data.dealerUpgerdeRates[1].secondDealerShareProfitRate*100;
                })
            }
          }, function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
    },
    methods: {
      create: function () {
        this.$data.product.channels = [];
        this.$data.product.productId = this.$data.products[this.$data.id].productId;
        this.$data.dealerUpgerdeRates[0].productId = this.$data.products[this.$data.id].productId;
        this.$data.dealerUpgerdeRates[1].productId = this.$data.products[this.$data.id].productId;
        for (let i = 0; i < this.$data.products[this.$data.id].list.length; i++) {
          this.$data.product.channels.push({
            channelType: this.$data.products[this.$data.id].list[i].channelTypeSign,
            paymentSettleRate: this.$data.products[this.$data.id].list[i].paymentSettleRate,
            settleType: this.$data.products[this.$data.id].list[i].productBalanceType,
            withdrawSettleFee: this.$data.products[this.$data.id].list[i].withdrawSettleFee,
            merchantSettleRate: this.$data.products[this.$data.id].list[i].merchantSettleRate,
            merchantWithdrawFee: this.$data.products[this.$data.id].list[i].merchantWithdrawFee
          })
        }
        this.$data.totalProfitSpace = this.$data.totalProfitSpace/100;
        this.$data.dealerUpgerdeRates[0].firstDealerShareProfitRate= this.$data.rate1/100;
        this.$data.dealerUpgerdeRates[0].secondDealerShareProfitRate= this.$data.rate2/100;
        this.$data.dealerUpgerdeRates[1].firstDealerShareProfitRate= this.$data.rate3/100;
        this.$data.dealerUpgerdeRates[1].secondDealerShareProfitRate= this.$data.rate4/100;
        let query = {
          mobile: this.$data.mobile,
          name: this.$data.name,
          belongArea: this.$data.belongArea,
          bankCard: this.$data.bankCard,
          bankAccountName: this.$data.bankAccountName,
          bankReserveMobile: this.$data.bankReserveMobile,
          product: this.$data.product,
          idCard: this.$data.idCard,
          recommendBtn: this.$data.recommendBtn,
          totalProfitSpace: this.$data.totalProfitSpace,
          dealerUpgerdeRates: this.$data.dealerUpgerdeRates
        };
        this.$http.post('/admin/user/addFirstDealer', query)
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: "添加成功"
            })
            this.$router.push('/admin/record/agentList')
          }, function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      },
      goBack: function () {
        this.$router.push('/admin/record/agentList')
      },
      //修改
      change: function () {
        this.$data.product.channels = [];
        this.$data.product.productId = this.$data.products[this.$data.id].productId;
        this.$data.dealerUpgerdeRates[0].productId = this.$data.products[this.$data.id].productId;
        this.$data.dealerUpgerdeRates[1].productId = this.$data.products[this.$data.id].productId;
        for (let i = 0; i < this.$data.products[this.$data.id].list.length; i++) {
          this.$data.product.channels.push({
            channelType: this.$data.products[this.$data.id].list[i].channelTypeSign,
            paymentSettleRate: this.$data.products[this.$data.id].list[i].paymentSettleRate,
            settleType: this.$data.products[this.$data.id].list[i].productBalanceType,
            withdrawSettleFee: this.$data.products[this.$data.id].list[i].withdrawSettleFee,
            merchantSettleRate: this.$data.products[this.$data.id].list[i].merchantSettleRate,
            merchantWithdrawFee: this.$data.products[this.$data.id].list[i].merchantWithdrawFee
          })
        }
        this.$data.totalProfitSpace = this.$data.totalProfitSpace/100;
        this.$data.dealerUpgerdeRates[0].firstDealerShareProfitRate= this.$data.rate1/100;
        this.$data.dealerUpgerdeRates[0].secondDealerShareProfitRate= this.$data.rate2/100;
        this.$data.dealerUpgerdeRates[1].firstDealerShareProfitRate= this.$data.rate3/100;
        this.$data.dealerUpgerdeRates[1].secondDealerShareProfitRate= this.$data.rate4/100;
        let query = {
          dealerId: this.$route.query.id,
          mobile: this.$data.mobile,
          name: this.$data.name,
          belongArea: this.$data.belongArea,
          bankCard: this.$data.bankCard,
          bankAccountName: this.$data.bankAccountName,
          bankReserveMobile: this.$data.bankReserveMobile,
          product: this.$data.product,
          idCard: this.$data.idCard,
          recommendBtn: this.$data.recommendBtn,
          totalProfitSpace: this.$data.totalProfitSpace,
          dealerUpgerdeRates: this.$data.dealerUpgerdeRates
        };
        this.$http.post('/admin/user/updateDealer', query)
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: "修改成功"
            })
            this.$router.push('/admin/record/agentList')
          }, function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      }
    },
    computed: {
      $$products: function () {
        return this.$data.products
      },
      $$data: function () {
        return this.$data;
      },
    },
    filters: {
      changeName: function (val) {
        if(val == 101){
          val = '阳光微信扫码'
        }else if(val == 102){
          val = '阳光支付宝扫码'
        }else if(val == 103){
          val = '阳光银联支付'
        }
        return val;
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
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
    color: #42b983;
  }
  .box1{
    border-top: 1px solid #d2d6de;
    box-shadow: 0 1px 1px rgba(0,0,0,0.4);
  }
  tr th,tr td{
    text-align: center;
  }

  .middle{
    position: relative;
    i{
      position: absolute;
      top:7px;
      right:0;
    }
  }
  .right{
    padding-top: 7px;
  }
  .product1 {
    display: inline-block;
    width: 95%;
  }

  input.check {
    position: relative;
    top: -50px;
  }
  .product label{
    width: 100%;
    margin-left: 2%;
  }
  .product .table {
    input {
      width: 77%;
      border: none;
      border-bottom: 1px solid #d0d0d0;
    }
  }
</style>
