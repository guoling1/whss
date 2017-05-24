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
                  <div class="box-header with-border">
                    <h3 class="box-title">合伙人推荐分润设置</h3>
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
        hsy_tableData: []
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
