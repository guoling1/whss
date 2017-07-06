<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">代理政策</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <el-tabs v-model="activeName2" type="card" @tab-click="handleClick">
                <el-tab-pane label="好收收" name="first">
                  <div class="box-header with-border">
                    <h3 class="box-title">收款及提现结算价</h3>
                  </div>
                  <div class="box-body">
                    <el-table :data="hss_tableData" border
                              v-loading="hss_tableLoading"
                              element-loading-text="数据加载中">
                      <el-table-column prop="channelName" label="通道名称"></el-table-column>
                      <el-table-column label="支付结算手续费">
                        <template scope="scope">
                          {{scope.row.paymentSettleRate}}%
                        </template>
                      </el-table-column>
                      <el-table-column prop="settleType" label="结算时间"></el-table-column>
                      <el-table-column label="提现手续费">
                        <template scope="scope">
                          {{scope.row.withdrawSettleFee}}元/每笔
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                  <div class="box-header with-border" v-if="isShow==1">
                    <h3 class="box-title">合伙人推荐分润设置</h3>
                  </div>
                  <div class="box-body" v-if="isShow==1">
                    <div class="form-group">
                      <div class="product">
                        <div class="col-xs-12">
                          <div class="box box1">
                            <div class="box-body table-responsive no-padding">
                              <table class="table table-hover">
                                <tbody>
                                <tr>
                                  <th>通道名称</th>
                                  <th>总分润空间</th>
                                </tr>
                                <tr v-for="(dealerProfit,index) in dealerProfits">
                                  <td>{{dealerProfit.channelName}}</td>
                                  <td>{{dealerProfit.profitSpace}}%</td>
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
                                  <th>分润类型</th>
                                  <th>金开门分润比例</th>
                                  <th>一级代理商分润比例</th>
                                  <th>二级代理分润比例</th>
                                </tr>
                                <tr>
                                  <td>升级费分润</td>
                                  <td>{{dealerUpgerdeRates1.bossDealerShareRate*100}}%</td>
                                  <td>{{dealerUpgerdeRates1.firstDealerShareProfitRate*100}}%</td>
                                  <td>{{dealerUpgerdeRates1.secondDealerShareProfitRate*100}}%</td>
                                </tr>
                                <tr>
                                  <td>收单分润</td>
                                  <td>{{dealerUpgerdeRates2.bossDealerShareRate*100}}%</td>
                                  <td>{{dealerUpgerdeRates2.firstDealerShareProfitRate*100}}%</td>
                                  <td>{{dealerUpgerdeRates2.secondDealerShareProfitRate*100}}%</td>
                                </tr>
                                <tr>
                                  <td colspan="4">金开门，一级代理，二级代理的比例之和必须等于100%</td>
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
                </el-tab-pane>
                <el-tab-pane label="好收银" name="second">
                  <div class="box-header with-border">
                    <h3 class="box-title">收款及提现结算价</h3>
                  </div>
                  <div class="box-body">
                    <el-table :data="hsy_tableData" border
                              v-loading="hsy_tableLoading"
                              element-loading-text="数据加载中">
                      <el-table-column prop="channelName" label="通道名称"></el-table-column>
                      <el-table-column label="支付结算手续费">
                        <template scope="scope">
                          {{scope.row.paymentSettleRate}}%
                        </template>
                      </el-table-column>
                      <el-table-column prop="settleType" label="结算时间"></el-table-column>
                      <el-table-column label="提现手续费">
                        <template scope="scope">
                          {{scope.row.withdrawSettleFee}}元/每笔
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
  </div>
</template>
<script lang="babel">
  export default {
    name: 'app',
    data() {
      return {
        hss_tableLoading: false,
        hss_tableData: [],
        hsy_tableLoading: false,
        hsy_tableData: [],
        dealerProfits: [],
        dealerUpgerdeRates1: [],
        dealerUpgerdeRates2: [],
        isShow: 2
      }
    },
    created() {
      this.getHssData();
      this.getHsyData();
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      },
      getHssData: function () {
        this.hss_tableLoading = true;
        this.$http.post('/daili/dealer/dealerPolicy', {
          sysType: 'hss'
        }).then(res => {
          this.hss_tableLoading = false;
          this.hss_tableData = res.data.product.channels;
          this.dealerProfits = res.data.dealerProfits;
          if (this.isShow == 1) {
            this.isShow = 1;
            this.dealerUpgerdeRates1 = res.data.dealerUpgerdeRates[0];
            this.dealerUpgerdeRates2 = res.data.dealerUpgerdeRates[1];
          }
        }, err => {
          this.hss_tableLoading = false;
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
        });
      },
      getHsyData: function () {
        this.hsy_tableLoading = true;
        this.$http.post('/daili/dealer/dealerPolicy', {
          sysType: 'hsy'
        }).then(res => {
          this.hsy_tableLoading = false;
          this.hsy_tableData = res.data.product.channels;
        }, err => {
          this.hsy_tableLoading = false;
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
        });
      }
    }
  }
</script>
<style scoped lang="less">
  .screen-top {
    padding-top: 0 !important;
  }

  .screen-item {
    float: left;
    margin-right: 10px;
  }

  .screen-title {
    display: block;
    height: 24px;
    line-height: 24px;
  }
</style>
