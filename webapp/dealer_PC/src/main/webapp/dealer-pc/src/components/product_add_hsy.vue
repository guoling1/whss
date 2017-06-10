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
                      <el-form-item prop="row.dealerTradeRateT1" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span v-if="scope.row.policyType!='withdraw'">最小值：{{scope.row.role.dealerTradeRateT1*100 | toFix}} <br> 最大值：不超过T1商户费率的最小值</span>
                          <span v-if="scope.row.policyType=='withdraw'">最小值：{{scope.row.role.dealerTradeRateT1}} <br> 最大值：不超过T1商户提现手费率的最小值</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.dealerTradeRateT1">
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="T1商户费率">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.merchantMinRateT1" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span v-if="scope.row.policyType!='withdraw'">最小值：{{scope.row.role.merchantMinRateT1*100 | toFix}} 并且 大于等于T1代理商的费率</span>
                          <span v-if="scope.row.policyType=='withdraw'">最小值：{{scope.row.role.merchantMinRateT1}} 并且 大于等于T1代理商的提现手续费</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMinRateT1">
                            <template slot="prepend">最小值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                      <el-form-item prop="row.merchantMaxRateT1" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span v-if="scope.row.policyType!='withdraw'">最大值：{{scope.row.role.merchantMaxRateT1*100 | toFix}} 并且 大于等于最小值</span>
                          <span v-if="scope.row.policyType=='withdraw'">最大值：{{scope.row.role.merchantMaxRateT1}} 并且 大于等于最小值</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMaxRateT1">
                            <template slot="prepend">最大值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="D1代理商结算价" width="160px">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.dealerTradeRateD1" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span v-if="scope.row.policyType!='withdraw'">最小值：{{scope.row.role.dealerTradeRateD1*100 | toFix}} <br> 最大值：不超过D1商户费率的最小值</span>
                          <span v-if="scope.row.policyType=='withdraw'">最小值：{{scope.row.role.dealerTradeRateD1}} <br> 最大值：不超过D1商户提现手续费的最小值</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.dealerTradeRateD1">
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="D1商户费率">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.merchantMinRateD1" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span v-if="scope.row.policyType!='withdraw'">最小值：{{scope.row.role.merchantMinRateD1*100 | toFix}} 并且 大于等于D1代理商的费率</span>
                          <span v-if="scope.row.policyType=='withdraw'">最小值：{{scope.row.role.merchantMinRateD1}} 并且 大于等于D1代理商的提现手续费</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMinRateD1">
                            <template slot="prepend">最小值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                      <el-form-item prop="row.merchantMaxRateD1" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span v-if="scope.row.policyType!='withdraw'">最大值：{{scope.row.role.merchantMaxRateD1*100 | toFix}} 并且 大于等于最小值</span>
                          <span v-if="scope.row.policyType=='withdraw'">最大值：{{scope.row.role.merchantMaxRateD1}} 并且 大于等于最小值</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMaxRateD1">
                            <template slot="prepend">最大值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="D0代理商结算价" width="160px">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.dealerTradeRateD0" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span v-if="scope.row.policyType!='withdraw'">最小值：{{scope.row.role.dealerTradeRateD0*100 | toFix}} <br> 最大值：不超过D0商户费率的最小值</span>
                          <span v-if="scope.row.policyType=='withdraw'">最小值：{{scope.row.role.dealerTradeRateD0}} <br> 最大值：不超过D0商户提现手续费的最小值</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.dealerTradeRateD0">
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column label="D0商户费率">
                  <template scope="scope">
                    <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                      <el-form-item prop="row.merchantMinRateD0" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span v-if="scope.row.policyType!='withdraw'">最小值：{{scope.row.role.merchantMinRateD0*100 | toFix}} 并且 大于等于D0代理商的费率</span>
                          <span v-if="scope.row.policyType=='withdraw'">最小值：{{scope.row.role.merchantMinRateD0}} 并且 大于等于D0代理商的提现手续费</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMinRateD0">
                            <template slot="prepend">最小值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                          </el-input>
                        </el-popover>
                      </el-form-item>
                      <el-form-item prop="row.merchantMaxRateD0" style="margin:10px 0 20px 0"
                                    :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                        <el-popover placement="top" title="提示" width="200" trigger="focus">
                          <span v-if="scope.row.policyType!='withdraw'">最大值：{{scope.row.role.merchantMaxRateD0*100 | toFix}} 并且 大于等于最小值</span>
                          <span v-if="scope.row.policyType=='withdraw'">最大值：{{scope.row.role.merchantMaxRateD0}} 并且 大于等于最小值</span>
                          <el-input slot="reference" size="small" placeholder="必填"
                                    v-model="scope.row.merchantMaxRateD0">
                            <template slot="prepend">最大值</template>
                            <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                            <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
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
      let query = this.$route.query;
      this.form.dealerId = query.dealerId;
      // 获取产品校验规则
      this.$http.get('/daili/dealer/dealerRatePolicyDetail/' + query.dealerId).then(res => {
        for (let i = 0; i < res.data.length; i++) {
          res.data[i].role = {};
        }
        this.form.dealerRatePolicies = res.data;
        this.$http.post('/daili/dealer/getProductRatePolicyDetail').then(res => {
          for (let i = 0; i < res.data.length; i++) {
            this.form.dealerRatePolicies[i].role = res.data[i];
          }
          console.log(this.form);
        });
      }, err => {
        console.log(err);
      })
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
              merchantMaxRateD0: '',
              role: {}
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
              merchantMaxRateD0: '',
              role: {}
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
              merchantMaxRateD0: '',
              role: {}
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
        for (let m = 0; m < this.form.dealerRatePolicies.length; m++) {
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.form.dealerRatePolicies[m].dealerTradeRateT1))) {
            valid = false;
            switch (this.form.dealerRatePolicies[m].policyType) {
              case 'wechat':
                validMsg = '微信 T1代理商结算价 未设置';
                break;
              case 'alipay':
                validMsg = '支付宝 T1代理商结算价 未设置';
                break;
              case 'withdraw':
                validMsg = '提现手续费 T1代理商结算价 未设置';
                break;
            }
            break;
          }
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.form.dealerRatePolicies[m].merchantMinRateT1))) {
            valid = false;
            switch (this.form.dealerRatePolicies[m].policyType) {
              case 'wechat':
                validMsg = '微信 T1商户费率 最小值 未设置';
                break;
              case 'alipay':
                validMsg = '支付宝 T1商户费率 最小值 未设置';
                break;
              case 'withdraw':
                validMsg = '提现手续费 T1商户费率 最小值 未设置';
                break;
            }
            break;
          }
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.form.dealerRatePolicies[m].merchantMaxRateT1))) {
            valid = false;
            switch (this.form.dealerRatePolicies[m].policyType) {
              case 'wechat':
                validMsg = '微信 T1商户费率 最大值 未设置';
                break;
              case 'alipay':
                validMsg = '支付宝 T1商户费率 最大值 未设置';
                break;
              case 'withdraw':
                validMsg = '提现手续费 T1商户费率 最大值 未设置';
                break;
            }
            break;
          }
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.form.dealerRatePolicies[m].dealerTradeRateD1))) {
            valid = false;
            switch (this.form.dealerRatePolicies[m].policyType) {
              case 'wechat':
                validMsg = '微信 D1代理商结算价 未设置';
                break;
              case 'alipay':
                validMsg = '支付宝 D1代理商结算价 未设置';
                break;
              case 'withdraw':
                validMsg = '提现手续费 D1代理商结算价 未设置';
                break;
            }
            break;
          }
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.form.dealerRatePolicies[m].merchantMinRateD1))) {
            valid = false;
            switch (this.form.dealerRatePolicies[m].policyType) {
              case 'wechat':
                validMsg = '微信 D1商户费率 最小值 未设置';
                break;
              case 'alipay':
                validMsg = '支付宝 D1商户费率 最小值 未设置';
                break;
              case 'withdraw':
                validMsg = '提现手续费 D1商户费率 最小值 未设置';
                break;
            }
            break;
          }
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.form.dealerRatePolicies[m].merchantMaxRateD1))) {
            valid = false;
            switch (this.form.dealerRatePolicies[m].policyType) {
              case 'wechat':
                validMsg = '微信 D1商户费率 最大值 未设置';
                break;
              case 'alipay':
                validMsg = '支付宝 D1商户费率 最大值 未设置';
                break;
              case 'withdraw':
                validMsg = '提现手续费 D1商户费率 最大值 未设置';
                break;
            }
            break;
          }
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.form.dealerRatePolicies[m].dealerTradeRateD0))) {
            valid = false;
            switch (this.form.dealerRatePolicies[m].policyType) {
              case 'wechat':
                validMsg = '微信 D0代理商结算价 未设置';
                break;
              case 'alipay':
                validMsg = '支付宝 D0代理商结算价 未设置';
                break;
              case 'withdraw':
                validMsg = '提现手续费 D0代理商结算价 未设置';
                break;
            }
            break;
          }
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.form.dealerRatePolicies[m].merchantMinRateD0))) {
            valid = false;
            switch (this.form.dealerRatePolicies[m].policyType) {
              case 'wechat':
                validMsg = '微信 D0商户费率 最小值 未设置';
                break;
              case 'alipay':
                validMsg = '支付宝 D0商户费率 最小值 未设置';
                break;
              case 'withdraw':
                validMsg = '提现手续费 D0商户费率 最小值 未设置';
                break;
            }
            break;
          }
          if (!(/^[0-9]{1,4}([.][0-9]{1,2})?$/.test(this.form.dealerRatePolicies[m].merchantMaxRateD0))) {
            valid = false;
            switch (this.form.dealerRatePolicies[m].policyType) {
              case 'wechat':
                validMsg = '微信 D0商户费率 最大值 未设置';
                break;
              case 'alipay':
                validMsg = '支付宝 D0商户费率 最大值 未设置';
                break;
              case 'withdraw':
                validMsg = '提现手续费 D0商户费率 最大值 未设置';
                break;
            }
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
                this.$http.post('/daili/dealer/saveOrUpdateDealerRatePolicy', this.form).then(res => {
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
      },
      toFix: function (v) {
        return (v / 1).toFixed(2);
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
