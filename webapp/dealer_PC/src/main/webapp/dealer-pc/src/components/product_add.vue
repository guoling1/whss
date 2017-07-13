<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">产品信息设置</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <label class="form-label">产品名称：{{productName}}</label>
            </div>
            <div class="box-body">
              <label class="form-label">产品分润设置</label>
              <el-table :data="tableData" border>
                <el-table-column prop="channelName" label="通道名称" width="180"></el-table-column>
                <el-table-column label="支付结算手续费">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.paymentSettleRate" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'不能为空 保留俩位小数',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span>最小值：{{scope.row.minPaymentSettleRate}} <br> 最大值：{{scope.row.merchantSettleRate}}</span>
                          <el-input slot="reference" size="small" placeholder="保留俩位小数"
                                    v-model="scope.row.paymentSettleRate">
                            <template slot="append">%</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column prop="settleType" label="结算时间" width="100"></el-table-column>
                <el-table-column label="提现结算费">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.withdrawSettleFee" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'不能为空 保留俩位小数',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span>最小值：{{scope.row.minWithdrawSettleFee}} <br> 最大值：{{scope.row.merchantWithdrawFee}}</span>
                          <el-input slot="reference" size="small" placeholder="保留俩位小数"
                                    v-model="scope.row.withdrawSettleFee">
                            <template slot="append">元/笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="商户支付手续费" width="180">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.merchantSettleRate" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'不能为空 保留俩位小数',trigger:'blur'}">
                        <el-input placeholder="保留俩位小数" size="small" disabled v-model="scope.row.merchantSettleRate">
                          <template slot="append">%</template>
                        </el-input>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="商户提现手续费" width="180">
                  <template scope="scope">
                    <el-form :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.merchantWithdrawFee" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'不能为空 保留俩位小数',trigger:'blur'}">
                        <el-input placeholder="保留俩位小数" size="small" disabled v-model="scope.row.merchantWithdrawFee">
                          <template slot="append">元/笔</template>
                        </el-input>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div class="box-body">
              <label class="form-label">合伙人推荐功能</label>
              <br>
              <el-switch v-model="recommendBoolean" @change="switchRecommend"
                         on-text="开启" on-color="#13ce66"
                         off-text="关闭" off-color="#ff4949">
              </el-switch>
              <div class="inviteText" v-show="inviteBoolean">
                开通后，代理商设置的商户终端费率按产品费率执行
              </div>
            </div>
            <div class="box-body">
              <label class="form-label">合伙人推荐分润设置</label>
              <el-table :data="dealerProfits" border>
                <el-table-column prop="channelName" label="通道名称"></el-table-column>
                <el-table-column label="总分润空间">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.profitSpace" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'不能为空 保留俩位小数',trigger:'blur'}">
                        <el-input placeholder="保留俩位小数" size="small" v-model="scope.row.profitSpace">
                          <template slot="append">%</template>
                        </el-input>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div class="box-body">
              <el-button type="primary" size="small" @click="onSubmit">保存产品设置</el-button>
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
    created(){
      let query = this.$route.query;
      this.$http.post('/daili/dealer/getFirstDealerProduct', {
        dealerId: query.dealerId,
        sysType: query.product,
        productId: query.productId
      }).then(res => {
        this.dealerId = query.dealerId;
        this.product = query.product;
        this.productName = res.data.productName;
        this.recommendBoolean = (res.data.recommendBtn == 2);
        this.recommendBtn = res.data.recommendBtn;
        this.tableData = res.data.product.channels;
        this.productId = res.data.product.productId;
        this.dealerProfits = res.data.dealerProfits;
      }, err => {
        console.log(err);
      })
    },
    data() {
      return {
        dealerId: '',
        product: '',
        productId: '',
        productName: '',
        tableData: [],
        formObject: [],
        recommendBoolean: false,
        recommendBtn: 1,
        dealerProfits: []
      }
    },
    methods: {
      createForm: function () {
        let length = this.formObject.length;
        this.formObject.push('form' + length);
        return 'form' + length;
      },
      switchRecommend: function (Boole) {
        if (Boole) {
          this.recommendBtn = 2;
        } else {
          this.recommendBtn = 1;
        }
      },
      onSubmit: function () {
        let valid = true;
        let validMsg = '';
        for (let m = 0; m < this.tableData.length; m++) {
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.tableData[m].paymentSettleRate))) {
            valid = false;
            validMsg = this.tableData[m].channelName + ' 支付结算手续费 未设置';
            break;
          }
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.tableData[m].withdrawSettleFee))) {
            valid = false;
            validMsg = this.tableData[m].channelName + ' 提现结算费 未设置';
            break;
          }
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.tableData[m].merchantSettleRate))) {
            valid = false;
            validMsg = this.tableData[m].channelName + ' 商户支付手续费 未设置';
            break;
          }
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.tableData[m].merchantWithdrawFee))) {
            valid = false;
            validMsg = this.tableData[m].channelName + ' 商户提现手续费 未设置';
            break;
          }
        }
        if (valid) {
          this.$msgbox({
            title: '提示',
            message: '确认修改代理商产品信息？',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            beforeClose: (action, instance, done) => {
              if (action === 'confirm') {
                instance.confirmButtonLoading = true;
                instance.confirmButtonText = '执行中...';
                this.$http.post('/daili/dealer/addOrUpdateDealerProduct', {
                  dealerId: this.dealerId,
                  inviteBtn: this.inviteStatus,
                  product: {
                    productId: this.productId,
                    channels: this.tableData
                  }
                }).then(res => {
                  done();
                  instance.confirmButtonLoading = false;
                  instance.confirmButtonText = '确定';
                }, err => {
                  instance.confirmButtonLoading = false;
                  instance.confirmButtonText = '确定';
                  this.$message({
                    showClose: true,
                    message: err.data.msg,
                    duration: 0,
                    type: 'error'
                  });
                });
              } else {
                done();
              }
            }
          }).then(action => {
            if (action == 'confirm') {
              this.$message({
                showClose: true,
                message: '保存成功',
                type: 'success'
              });
              if (this.$route.query.dealerType == 2) {
                this.$router.push('/daili/app/dealer_list');
              } else {
                this.$router.push('/daili/app/first_dealer_list');
              }
            }
          });
        } else {
          this.$message({
            showClose: true,
            message: validMsg,
            type: 'warning'
          });
          return false;
        }
      }
    }
  }
</script>
<style scoped lang="less">
  .inviteText {
    margin-top: 5px;
    line-height: 30px;
  }

  .form-label {
    margin-bottom: 15px;
  }

  .line-center {
    text-align: center;
  }
</style>
