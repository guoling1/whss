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
              <el-table :data="form.dealerRatePolicies" border>
                <el-table-column label="支付方式" width="100px">
                  <template scope="scope">
                    {{scope.row.policyType | payType}}
                  </template>
                </el-table-column>
                <el-table-column label="T1代理商结算价" width="160px">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.paymentSettleRate" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'必填 必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span>最小值：{{scope.row.minPaymentSettleRate}} <br> 最大值：{{scope.row.merchantSettleRate}}</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.dealerTradeRateT1">
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/每笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="T1商户费率">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.withdrawSettleFee" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'必填 必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span>最小值：{{scope.row.minWithdrawSettleFee}} <br> 最大值：{{scope.row.merchantWithdrawFee}}</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMinRateT1">
                            <template slot="prepend">最小值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/每笔</template>
                          </el-input>
                        </el-popover>
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span>最小值：{{scope.row.minWithdrawSettleFee}} <br> 最大值：{{scope.row.merchantWithdrawFee}}</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMinRateT1">
                            <template slot="prepend">最大值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/每笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="D1代理商结算价" width="160px">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.paymentSettleRate" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'必填 必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span>最小值：{{scope.row.minPaymentSettleRate}} <br> 最大值：{{scope.row.merchantSettleRate}}</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.dealerTradeRateD1">
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/每笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="D1商户费率">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.withdrawSettleFee" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'必填 必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span>最小值：{{scope.row.minWithdrawSettleFee}} <br> 最大值：{{scope.row.merchantWithdrawFee}}</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMinRateD1">
                            <template slot="prepend">最小值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/每笔</template>
                          </el-input>
                        </el-popover>
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span>最小值：{{scope.row.minWithdrawSettleFee}} <br> 最大值：{{scope.row.merchantWithdrawFee}}</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMaxRateD1">
                            <template slot="prepend">最大值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/每笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="D0代理商结算价" width="160px">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.paymentSettleRate" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'必填 必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span>最小值：{{scope.row.minPaymentSettleRate}} <br> 最大值：{{scope.row.merchantSettleRate}}</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.dealerTradeRateD0">
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/每笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="D0商户费率">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.withdrawSettleFee" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'必填 必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span>最小值：{{scope.row.minWithdrawSettleFee}} <br> 最大值：{{scope.row.merchantWithdrawFee}}</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMinRateD0">
                            <template slot="prepend">最小值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/每笔</template>
                          </el-input>
                        </el-popover>
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span>最小值：{{scope.row.minWithdrawSettleFee}} <br> 最大值：{{scope.row.merchantWithdrawFee}}</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMaxRateD0">
                            <template slot="prepend">最大值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/每笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <!--<div class="box-body">-->
            <!--<label class="form-label">代理商推广码&推广链接</label>-->
            <!--<br>-->
            <!--<el-switch v-model="inviteBoolean" @change="switchInvite"-->
            <!--on-text="开启" on-color="#13ce66"-->
            <!--off-text="关闭" off-color="#ff4949">-->
            <!--</el-switch>-->
            <!--<div class="inviteText" v-show="inviteBoolean">-->
            <!--推广码：{{inviteCode}}-->
            <!--<br>-->
            <!--推广链接：https://{{product}}.qianbaojiajia.com/reg?invest={{inviteCode}}-->
            <!--</div>-->
            <!--</div>-->
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
//      let query = this.$route.query;
//      this.$http.post('/daili/dealer/getDealerProduct', {
//        dealerId: query.dealerId,
//        sysType: query.product,
//        productId: query.productId
//      }).then(res => {
//        this.dealerId = query.dealerId;
//        this.product = query.product;
//        this.productName = res.data.productName;
//        this.inviteStatus = res.data.inviteBtn;
//        this.inviteCode = res.data.inviteCode;
//        this.inviteBoolean = (this.inviteStatus == 2);
//        this.tableData = res.data.product.channels;
//        this.productId = res.data.product.productId;
//      }, err => {
//        console.log(err);
//      })
    },
    data() {
      return {
        form: {
          dealerId: '',
          dealerRatePolicies: [
            {
              id: 0,
              sn: 1,
              policyType: "wechat",
              dealerTradeRateT1: '',
              merchantMinRateT1: '',
              merchantMaxRateT1: '',
              dealerTradeRateD1: '',
              merchantMinRateD1: '',
              merchantMaxRateD1: '',
              dealerTradeRateD0: '',
              merchantMinRateD0: '',
              merchantMaxRateD0: ''
            }, {
              id: 0,
              sn: 2,
              policyType: "alipay",
              dealerTradeRateT1: '',
              merchantMinRateT1: '',
              merchantMaxRateT1: '',
              dealerTradeRateD1: '',
              merchantMinRateD1: '',
              merchantMaxRateD1: '',
              dealerTradeRateD0: '',
              merchantMinRateD0: '',
              merchantMaxRateD0: ''
            }, {
              id: 0,
              sn: 3,
              policyType: "withdraw",
              dealerTradeRateT1: '',
              merchantMinRateT1: '',
              merchantMaxRateT1: '',
              dealerTradeRateD1: '',
              merchantMinRateD1: '',
              merchantMaxRateD1: '',
              dealerTradeRateD0: '',
              merchantMinRateD0: '',
              merchantMaxRateD0: ''
            }
          ]
        }
      }
    },
    methods: {
      createForm: function () {
        let length = this.formObject.length;
        this.formObject.push('form' + length);
        return 'form' + length;
      },
      switchInvite: function (Boole) {
        if (Boole) {
          this.inviteStatus = 2;
        } else {
          this.inviteStatus = 1;
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
              this.$router.push('/daili/app/dealer_list');
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
    },
    filters: {
      payType: function (v) {
        switch (v) {
          case 'wechat':
            return '微信';
            break;
          case 'alipay':
            return '支付宝';
            break;
          case 'withdraw':
            return '提现手续费';
            break;
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
