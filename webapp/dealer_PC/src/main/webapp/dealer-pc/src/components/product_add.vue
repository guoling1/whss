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
                          <el-input slot="reference" placeholder="保留俩位小数" v-model="scope.row.paymentSettleRate">
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
                          <el-input slot="reference" placeholder="保留俩位小数" v-model="scope.row.withdrawSettleFee">
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
                        <el-input placeholder="保留俩位小数" disabled v-model="scope.row.merchantSettleRate">
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
                        <el-input placeholder="保留俩位小数" disabled v-model="scope.row.merchantWithdrawFee">
                          <template slot="append">元/笔</template>
                        </el-input>
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
              <el-button type="primary" @click="onSubmit">保存产品设置</el-button>
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
      this.$http.post('/daili/dealer/getDealerProduct', {
        dealerId: query.dealerId,
        sysType: query.product,
        productId: query.productId
      }).then(res => {
        this.dealerId = query.dealerId;
        this.product = query.product;
        this.productName = res.data.productName;
        this.inviteStatus = res.data.inviteBtn;
        this.inviteCode = res.data.inviteCode;
        this.inviteBoolean = (this.inviteStatus == 2);
        this.tableData = res.data.product.channels;
        this.productId = res.data.product.productId;
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
        inviteBoolean: false,
        inviteStatus: 1,
        inviteCode: '',
        tableData: [],
        formObject: []
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
                }, err => {
                  this.$message({
                    showClose: true,
                    message: err.data.msg,
                    type: 'error'
                  });
                });
              } else {
                done();
              }
            }
          }).then(action => {
            this.$message({
              showClose: true,
              message: '保存成功',
              type: 'success'
            });
            this.$router.push('/daili/app/dealer_list');
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
