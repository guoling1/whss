<template lang="html">
  <div id="agentAdd">
    <div v-if="isShow" style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">新增代理商</div>
    <div v-if="!isShow" style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">代理商资料</div>
    <!--<h1 v-if="isShow">新增代理商</h1>
    <h1 v-if="!isShow">代理商资料</h1>-->
    <div style="margin: 0 10px">
      <div class="box box-info" style="width: 50%">
        <div class="box-header with-border">
          <h3 class="box-title" v-if="isShow">资料填写</h3>
          <h3 class="box-title" v-if="!isShow">基本资料</h3>
        </div>
        <!-- /.box-header -->
        <!-- form start -->
        <form class="form-horizontal">
          <div class="box-body">
            <div class="form-group">
              <label for="mobile" class="col-sm-2 control-label">代理手机</label>

              <div class="col-sm-9">
                <input type="tel" id="mobile" class="form-control" name="mobile" placeholder="代理手机号，用于登录" v-model="$$data.mobile">
              </div>
            </div>
            <div class="form-group">
              <label for="name" class="col-sm-2 control-label">代理名称</label>

              <div class="col-sm-9">
                <input type="text" class="form-control" id="name" name="name" placeholder="输入商户名称" v-model="$$data.name">
              </div>
            </div>
            <div class="form-group">
              <label for="belongArea" class="col-sm-2 control-label">所在地</label>

              <div class="col-sm-9">
                <input type="text" class="form-control" id="belongArea" name="belongArea" placeholder="省/市/街道" v-model="$$data.belongArea">
              </div>
            </div>
            <div class="form-group">
              <label for="bankCard" class="col-sm-2 control-label">结算卡</label>

              <div class="col-sm-9">
                <input type="text" class="form-control" id="bankCard" name="bankCard" placeholder="输入一级代理银行卡号" v-model="$$data.bankCard">
              </div>
            </div>
            <div class="form-group">
              <label for="bankAccountName" class="col-sm-2 control-label">开户名</label>

              <div class="col-sm-9">
                <input type="text" class="form-control" id="bankAccountName" name="bankAccountName" placeholder="银行开户名称" v-model="$$data.bankAccountName">
              </div>
            </div>
            <div class="form-group">
              <label for="bankReserveMobile" class="col-sm-2 control-label">手机号</label>

              <div class="col-sm-9">
                <input type="text" class="form-control" id="bankReserveMobile" name="bankReserveMobile" placeholder="银行开户预留手机号" v-model="$$data.bankReserveMobile">
              </div>
            </div>
          </div>
          <!-- /.box-body -->

        </form>
      </div>
      <div class="product">
        <p>配置产品：</p>
        <label v-for="(product,index) in $$products">
          <input class="check" type="radio" name="name" :value="index" v-model="id">

          <div class="product1">
            <div class="col-xs-12">
              <div class="box">
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
        product: {
          channels: []
        },
        products: [],//所有产品
        id: 0,
        isShow:true
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
                  this.$data.bankReserveMobile = res.data.bankReserveMobile;
                  this.$data.products = [res.data.product];
                  this.$data.products[0].list = res.data.product.channels
                  for(var i= 0; i < this.$data.products[0].list.length; i++){
                      this.$data.products[0].list[i].channelTypeSign = this.$data.products[0].list[i].channelType
                      this.$data.products[0].list[i].productBalanceType = this.$data.products[0].list[i].settleType

                  }
                  console.log(this.$data.products[0])
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
        let query = {
          mobile: this.$data.mobile,
          name: this.$data.name,
          belongArea: this.$data.belongArea,
          bankCard: this.$data.bankCard,
          bankAccountName: this.$data.bankAccountName,
          bankReserveMobile: this.$data.bankReserveMobile,
          product: this.$data.product
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
      change: function () {
        this.$data.product.channels = [];
        this.$data.product.productId = this.$data.products[this.$data.id].productId;
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
        let query = {
          dealerId: this.$route.query.id,
          mobile: this.$data.mobile,
          name: this.$data.name,
          belongArea: this.$data.belongArea,
          bankCard: this.$data.bankCard,
          bankAccountName: this.$data.bankAccountName,
          bankReserveMobile: this.$data.bankReserveMobile,
          product: this.$data.product
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
      }
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

  .agentAdd {
    float: right;
    width: 80%;
  }

  .add {
    border: 1px solid #ccc;
    width: 800px;
    border-top: none;
    font-size: 16px;

    th {
      width: 200px;
      vertical-align: middle;
      text-align: center;
    }
    td input {
      border: none;
      width: 600px;
    }
  }

  .product1 {
    display: inline-block;
    width: 90%;
  }

  input.check {
    position: relative;
    top: -50px;
  }
  .product label{
    width: 90%;
  }
  .product .table {
    input {
      width: 77%;
      border: none;
    }
  }
</style>
