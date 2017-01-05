<template lang="html">
  <div id="invite">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">邀请规则设置</div>
    <div class="box box-info">
      <div class="box-header with-border">
        <h3 class="box-title">商户升级规则设置</h3>
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
                      <div class="box-body table-responsive no-padding">
                        <table class="table table-hover">
                          <tbody>
                          <tr>
                            <th>合伙人等级</th>
                            <th>微信费率</th>
                            <th>支付宝费率</th>
                            <th>无卡费率</th>
                            <th>升级费</th>
                            <th>邀请人数</th>
                          </tr>
                          <tr>
                            <td>0.49%</td>
                            <td>0.49%</td>
                            <td>0.55%</td>
                            <td>无</td>
                            <td>无</td>
                          </tr>
                          <tr >
                            <td><input type="text" name="name">普通</td>
                            <td><input type="text" name="name" v-model="channel.paymentSettleRate">%</td>
                            <td><input type="text" name="name" v-model="channel.paymentSettleRate">%</td>
                            <td><input type="text" name="name" v-model="channel.withdrawSettleFee">%</td>
                            <td><input type="text" name="name" v-model="channel.merchantSettleRate">元</td>
                            <td><input type="text" name="name" v-model="channel.merchantWithdrawFee">人</td>
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
  </div>
</template>

<script lang="babel">
  export default {
    name:"invite",
    data(){
      return{
        query: {
          upgradeRulesList: [
            {
              productId:1,     //产品编码
              name:店员,    //合伙人名称
              type:1,        //合伙人类型 1店员 2店长 3老板
              "promotionNum":20,//须推广人数
              "upgradeCost":168,//升级费用
              "weixinRate":0.49,//微信费率
              "alipayRate":0.49,//支付宝费率
              "fastRate":0.6,
            }
          ]
        }
      }
    },
    created: function () {
        this.$http.post('/admin/channel/list')
          .then(function (res) {

          }, function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
    },
    methods: {

    },
    computed: {

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
  .middle{
    position: relative;
    i {
      position: absolute;
      top: 7px;
      right: -23px;
      width: 33px;
      font-style: normal;
    }
  }
  .right{
    padding-top: 7px;
    span {
      margin-left: 10px;
      color: #ccc;
    }
  }
</style>
