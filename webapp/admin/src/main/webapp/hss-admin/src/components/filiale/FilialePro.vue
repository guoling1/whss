<template lang="html">
  <div id="agentAddBase">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border">
          <h3 class="box-title" v-if="isAdd">分公司开通产品</h3>
          <h3 class="box-title" v-if="!isAdd">分公司产品详情</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
        </div>
        <div class="">
          <div class="box-header">
            <h3 class="box-title title2">产品名称：{{records.productName}}</h3>
          </div>
        </div>
        <div class="">
          <div class="box-header">
            <h3 class="box-title title2">产品分润设置：</h3>
          </div>
          <form class="form-horizontal">
            <div class="box-body">
              <div class="form-group">
                <div class="product">
                  <div class="col-xs-10" style="margin-left: 5%">
                    <div class="box box1">
                      <!--<div class="box-header">-->
                      <!--<h3 class="box-title">{{product.productName}}</h3>-->
                      <!--</div>-->
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
                          <tr v-for="channel in channels">
                            <td>{{channel.channelName}}</td>
                            <td><input type="number" name="name" v-model="channel.paymentSettleRate">%</td>
                            <td>{{channel.settleType}}</td>
                            <td><input type="number" name="name" v-model="channel.withdrawSettleFee">元/笔</td>
                            <td><input type="number" name="name" v-model="channel.merchantSettleRate">%</td>
                            <td><input type="number" name="name" v-model="channel.merchantWithdrawFee">元/笔</td>
                          </tr>
                          </tbody>
                        </table>
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
        <!--<div style="position: relative">
          <div class="box-header" style="margin-top: 15px">
            <h3 class="box-title title2">代理商推广码&推广链接：</h3>
          </div>
            <el-radio-group v-model="records.inviteBtn" style="margin-left: 65px">
              <el-radio :label="2" style="display: block">开</el-radio>
              <el-radio :label="1" style="display: block;margin:10px 0 0">关</el-radio>
            </el-radio-group>
          <div style="position: absolute;top: 41px;left: 108px">
            <span style="font-weight: normal;margin-left: 20px">推广码：{{records.inviteCode}}</span>
            <span style="font-weight: normal;margin-left: 20px">推广链接：http://hss.qianbaojiajia.com/reg?invite={{records.inviteCode}}</span>
          </div>

        </div>-->
        <div v-if="records.productName=='好收收'">
          <div class="box-header" style="margin-top: 15px">
            <h3 class="box-title title2">合伙人推荐功能开关：</h3>
          </div>
          <el-radio-group v-model="records.recommendBtn" style="margin-left: 65px">
            <el-radio :label="2" style="display: block">开
              <span style="font-weight: normal;margin-left: 5px">（开通后，代理商设置的商户终端费率按产品费率执行）</span>
            </el-radio>
            <el-radio :label="1" style="display: block;margin:10px 0 0">关</el-radio>
          </el-radio-group>
        </div>
        <div v-if="records.productName=='好收收'">
          <div class="box-header" style="margin-top: 15px">
            <h3 class="box-title title2">合伙人推荐分润设置：</h3>
          </div>
          <form class="form-horizontal" style="width: 90%;margin: 0 auto;">
            <div class="box-body">
              <div class="form-group">
                <div class="product">
                  <div class="col-xs-12">
                    <div class="box box1">
                      <div class="box-body table-responsive no-padding">
                        <table class="table table-hover">
                          <tbody>
                          <tr>
                            <th>通道名称</th>
                            <th style="line-height: 250%">总分润空间</th>
                          </tr>
                          <tr v-for="(dealerProfit,index) in dealerProfits">
                            <td>{{dealerProfit.channelName}}</td>
                            <td><input type="number" name="name" v-model="dealerProfit.profitSpace">%</td>
                          </tr>
                          <tr>
                            <td colspan="2"><span>总分润空间不得大于该通道商户费率与一级代理商结算价之差</span></td>
                          </tr>
                          </tbody>
                        </table>
                      </div>
                    </div>
                  </div>
                  <div class="col-xs-12">
                    <div class="box box1">
                      <div class="box-body table-responsive no-padding">
                        <table class="table table-hover">
                          <tbody>
                          <tr>
                            <td>分润类型</td>
                            <td>金开门分润比例</td>
                            <td>分公司分润比例</td>
                          </tr>
                          <tr>
                            <td>升级费分润</td>
                            <td>{{dealerUpgerdeRate1.bossDealerShareRate}}%</td>
                            <td><input type="number" name="name" v-model="dealerUpgerdeRate1.oemShareRate">%</td>
                          </tr>
                          <tr>
                            <td>收单分润</td>
                            <td>{{dealerUpgerdeRate2.bossDealerShareRate}}%</td>
                            <td><input type="number" name="name" v-model="dealerUpgerdeRate2.oemShareRate">%</td>
                          </tr>
                          </tbody>
                        </table>
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
        <!--<div class="btn btn-primary" @click="goBack" style="margin: 20px 20px 100px 40px;">返回</div>-->
        <div class="btn btn-primary" @click="change" style="margin: 20px 0 100px 40px;" v-if="level==1&&!isAdd">修改</div>
        <div class="btn btn-primary" @click="change" style="margin: 20px 0 100px 40px;" v-if="level==1&&isAdd">开 通</div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  import Message from '../Message.vue'
  export default {
    name: 'agentAddBase',
    data () {
      return {
        isAdd: true,
        records: '',
        channels: '',
        dealerProfits: '',
        dealerUpgerdeRate1: '',
        dealerUpgerdeRate2: '',
        level: 1,
        url: ''
      }
    },
    created: function () {
      if (this.$route.query.productId != 0) {
        this.$data.isAdd = false;
      }
      if (this.$route.query.level == 2) {
        this.$data.level = 2;
      }
      this.$http.get('/admin/dealer/oem/' + this.$route.query.dealerId + '/' + this.$route.query.productId)
        .then(function (res) {
          this.records = res.data;
          this.channels = res.data.product.channels;
          this.dealerProfits = res.data.dealerProfits;
          res.data.dealerUpgerdeRates[0].bossDealerShareRate = (res.data.dealerUpgerdeRates[0].bossDealerShareRate * 100).toFixed(0);
          res.data.dealerUpgerdeRates[1].bossDealerShareRate = (res.data.dealerUpgerdeRates[1].bossDealerShareRate * 100).toFixed(0);
          res.data.dealerUpgerdeRates[0].oemShareRate = (res.data.dealerUpgerdeRates[1].oemShareRate * 100).toFixed(0);
          res.data.dealerUpgerdeRates[1].oemShareRate = (res.data.dealerUpgerdeRates[1].oemShareRate * 100).toFixed(0);
          this.dealerUpgerdeRate1 = res.data.dealerUpgerdeRates[0];
          this.dealerUpgerdeRate2 = res.data.dealerUpgerdeRates[1];
        })
        .catch(function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        });
    },
    methods: {
      goBack: function () {
        this.$router.go(-1)
      },
      //修改
      change: function () {
        if(this.$route.query.product == 'hss'){
         this.$data.records.totalProfitSpace = this.$data.records.totalProfitSpace/100;
         this.$data.records.dealerUpgerdeRates[0].bossDealerShareRate = this.$data.records.dealerUpgerdeRates[0].bossDealerShareRate/100;
         this.$data.records.dealerUpgerdeRates[0].firstDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[0].firstDealerShareProfitRate/100;
         this.$data.records.dealerUpgerdeRates[0].oemShareRate = this.$data.records.dealerUpgerdeRates[0].oemShareRate/100;
         this.$data.records.dealerUpgerdeRates[1].oemShareRate = this.$data.records.dealerUpgerdeRates[1].oemShareRate/100;
         this.$data.records.dealerUpgerdeRates[0].secondDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[0].secondDealerShareProfitRate/100;
         this.$data.records.dealerUpgerdeRates[1].bossDealerShareRate = this.$data.records.dealerUpgerdeRates[1].bossDealerShareRate/100;
         this.$data.records.dealerUpgerdeRates[1].firstDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[1].firstDealerShareProfitRate/100;
         this.$data.records.dealerUpgerdeRates[1].secondDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[1].secondDealerShareProfitRate/100;
         this.$data.records.dealerUpgerdeRates[0] = this.$data.dealerUpgerdeRate1;
         this.$data.records.dealerUpgerdeRates[1] = this.$data.dealerUpgerdeRate2;
         }
        this.records.dealerId = this.$route.query.dealerId;
        this.records.product.channels = this.channels;
        if (this.$route.query.product == "hss") {
          this.$data.url = '/admin/dealer/addOrUpdateHssOem'

        } else {
          this.$data.url = '/admin/dealer/addOrUpdateHsyDealer'
        }
        this.$http.post(this.$data.url, this.$data.records)
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '设置成功'
            })
            this.$data.records.totalProfitSpace = this.$data.records.totalProfitSpace*100;
            this.$data.records.dealerUpgerdeRates[0].bossDealerShareRate = (this.$data.records.dealerUpgerdeRates[0].bossDealerShareRate*100).toFixed(0);
            this.$data.records.dealerUpgerdeRates[0].oemShareRate = this.$data.records.dealerUpgerdeRates[0].oemShareRate*100;
            this.$data.records.dealerUpgerdeRates[1].oemShareRate = this.$data.records.dealerUpgerdeRates[1].oemShareRate*100;
            this.$data.records.dealerUpgerdeRates[0].firstDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[0].firstDealerShareProfitRate*100;
            this.$data.records.dealerUpgerdeRates[0].secondDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[0].secondDealerShareProfitRate*100;
            this.$data.records.dealerUpgerdeRates[1].bossDealerShareRate = this.$data.records.dealerUpgerdeRates[1].bossDealerShareRate*100;
            this.$data.records.dealerUpgerdeRates[1].firstDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[1].firstDealerShareProfitRate*100;
            this.$data.records.dealerUpgerdeRates[1].secondDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[1].secondDealerShareProfitRate*100;
            this.$data.records.dealerUpgerdeRates[0] = this.$data.dealerUpgerdeRate1;
            this.$data.records.dealerUpgerdeRates[1] = this.$data.dealerUpgerdeRate2;
//            this.$message({
//              showClose: true,
//              message: '设置成功',
//              type: 'success'
//            });
//            this.$router.go(-1)
          }, function (err) {
            if(this.$route.query.product == "hss"){
             this.$data.records.totalProfitSpace = this.$data.records.totalProfitSpace*100;
             this.$data.records.dealerUpgerdeRates[0].bossDealerShareRate = (this.$data.records.dealerUpgerdeRates[0].bossDealerShareRate*100).toFixed(0);
             this.$data.records.dealerUpgerdeRates[0].oemShareRate = this.$data.records.dealerUpgerdeRates[0].oemShareRate*100;
             this.$data.records.dealerUpgerdeRates[1].oemShareRate = this.$data.records.dealerUpgerdeRates[1].oemShareRate*100;
             this.$data.records.dealerUpgerdeRates[0].firstDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[0].firstDealerShareProfitRate*100;
             this.$data.records.dealerUpgerdeRates[0].secondDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[0].secondDealerShareProfitRate*100;
             this.$data.records.dealerUpgerdeRates[1].bossDealerShareRate = this.$data.records.dealerUpgerdeRates[1].bossDealerShareRate*100;
             this.$data.records.dealerUpgerdeRates[1].firstDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[1].firstDealerShareProfitRate*100;
             this.$data.records.dealerUpgerdeRates[1].secondDealerShareProfitRate = this.$data.records.dealerUpgerdeRates[1].secondDealerShareProfitRate*100;
             this.$data.records.dealerUpgerdeRates[0] = this.$data.dealerUpgerdeRate1;
             this.$data.records.dealerUpgerdeRates[1] = this.$data.dealerUpgerdeRate2;
             }
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      }
    },
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .alignRight {
    margin-right: 15px;
    text-align: right;
    height: 30px;
    line-height: 30px;
    font-weight: bold;
    margin-bottom: 10px;
  }

  .title2 {
    margin-left: 20px;
  }

  input {
    width: 77%;
    border: none;
    border-bottom: 1px solid #d0d0d0;
  }
</style>
