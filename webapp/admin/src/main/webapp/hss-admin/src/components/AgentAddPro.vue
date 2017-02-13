<template lang="html">
  <div id="agentAddBase">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border">
          <h3 class="box-title" v-if="isAdd">新增代理商：产品信息设置</h3>
          <h3 class="box-title" v-if="!isAdd">代理商详情</h3>
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
                  <div class="col-xs-12">
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
                                <td><input type="text" name="name" v-model="channel.paymentSettleRate">%</td>
                                <td>{{channel.settleType}}</td>
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
              </div>
            </div>
          </form>
        </div>
        <div>
          <div class="box-header" style="margin-top: 15px">
            <h3 class="box-title title2">代理商推广码&推广链接：</h3>
          </div>
            <el-radio-group v-model="records.inviteBtn" style="margin-left: 65px">
              <el-radio :label="2" style="display: block">开
                <span style="font-weight: normal;margin-left: 20px">推广码：{{records.inviteCode}}</span>
                <span style="font-weight: normal;margin-left: 20px">推广链接：https://hss.qianbaojiajia.com/reg?invest={{records.inviteCode}}</span></el-radio>
              <el-radio :label="1" style="display: block;margin:10px 0 0">关</el-radio>
            </el-radio-group>
        </div>
        <div>
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
        <div>
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
                            <th>收单总分润空间</th>
                            <th colspan="5" style="text-align: left">
                              <input type="number" style="width: 20%" v-model="records.totalProfitSpace">%
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
                            <td>{{dealerUpgerdeRate1.bossDealerShareRate}}%</td>
                            <td><input type="number" v-model="dealerUpgerdeRate1.firstDealerShareProfitRate">%</td>
                            <td><input type="number" v-model="dealerUpgerdeRate1.secondDealerShareProfitRate">%</td>
                          </tr>
                          <tr>
                            <td>收单分润</td>
                            <td>{{dealerUpgerdeRate2.bossDealerShareRate}}%</td>
                            <td><input type="number" v-model="dealerUpgerdeRate2.firstDealerShareProfitRate">%</td>
                            <td><input type="number" v-model="dealerUpgerdeRate2.secondDealerShareProfitRate">%</td>
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
        <div class="btn btn-default" @click="goBack" style="margin: 0 20px 100px 40px;">
          返回
        </div>
        <div class="btn btn-default" @click="change" style="margin: 0 0 100px;">
          修改
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'agentAddBase',
    data () {
      return {
        isAdd:true,
        records:'',
        channels:'',
        dealerUpgerdeRate1:'',
        dealerUpgerdeRate2:'',
      }
    },
    created: function () {
      if(this.$route.query.productId!=0){
          this.$data.isAdd = false;
      }
      this.$http.get('/admin/dealer/hss/'+this.$route.query.dealerId+'/'+this.$route.query.productId)
        .then(function (res) {
          this.$data.records = res.data;
          this.$data.channels = res.data.product.channels;
          this.$data.dealerUpgerdeRate1 = res.data.dealerUpgerdeRates[0];
          this.$data.dealerUpgerdeRate2 = res.data.dealerUpgerdeRates[1];
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
      create: function () {
        console.log(this.$data.query)
        this.$http.post('/admin/user/addFirstDealer', this.$data.query)
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
//        this.$router.push('/admin/record/agentList')
        this.$router.go(-1)
      },
      //修改
      change: function () {
        this.$data.records.product.channels = this.$data.channels;
        this.$data.records.dealerUpgerdeRates[0] = this.$data.dealerUpgerdeRate1;
        this.$data.records.dealerUpgerdeRates[1] = this.$data.dealerUpgerdeRate2;
        this.$data.records.dealerId = this.$route.query.dealerId
        this.$http.post('/admin/dealer/addOrUpdateHssDealer', this.$data.records)
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '设置成功',
              type: 'success'
            });
            this.$router.push('/admin/record/agentListFir')
          }, function (err) {
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

  .title2{
    margin-left: 20px;
  }
  input {
    width: 77%;
    border: none;
    border-bottom: 1px solid #d0d0d0;
  }
</style>
