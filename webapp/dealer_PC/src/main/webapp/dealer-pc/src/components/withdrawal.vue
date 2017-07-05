<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-13">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">提现</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="demo-ruleForm">
                <el-form-item label="可提现金额">
                  {{accountInfo.available}} 元
                </el-form-item>
                <el-form-item label="到账银行">
                  {{accountInfo.bankName}}
                </el-form-item>
                <el-form-item label="银行卡号">
                  {{accountInfo.bankNo}}
                </el-form-item>
                <el-form-item label="提现金额" prop="amount">
                  <el-popover placement="top" title="提示" width="200" trigger="focus">
                    <span>最小可提现金额：2.00 元 <br> 当前最大可提现金额：{{accountInfo.available}} 元<br> 提现手续费：提现金额小于500元收2元提现手续费。大于等于500元不收提现手续费</span>
                    <el-input slot="reference" type="number" v-model="form.amount" placeholder="保留俩位小数"
                              @change="counter" size="small">
                      <template slot="append">元</template>
                    </el-input>
                  </el-popover>
                </el-form-item>
                <el-form-item label="到账金额">
                  <el-input v-model="calculate.amount" size="small" disabled placeholder="请先输入提现金额">
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
                <el-form-item label="手续费">
                  {{c_fee}}
                </el-form-item>
                <el-form-item label="验证码" prop="code">
                  <el-col :span="19">
                    <el-input type="text" v-model="form.code" size="small"></el-input>
                  </el-col>
                  <el-col class="line-center" :span="5">
                    <el-button type="text" :disabled="!canSendCode" @click="sendCode">{{sendCodeText}}</el-button>
                  </el-col>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="small" @click="onSubmit" :loading="isSubmit" :disabled="isSubmit">
                    立即提现
                  </el-button>
                </el-form-item>
              </el-form>
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
      this.accountInfo = this.$route.query;
    },
    data() {
      let validateAmount = (rule, value, callback) => {
        if (value > 2 && value <= this.accountInfo.available) {
          callback();
        } else {
          callback(new Error('最小可提现金额2.00元 当前最大可提现金额' + this.accountInfo.available + '元'));
        }
      };
      return {
        accountInfo: {},
        calculate: {
          amount: ''
        },
        form: {
          amount: '',
          channel: 101,
          code: ''
        },
        rules: {
          amount: [
            {required: true, type: 'number', message: '请输入提现金额', trigger: 'blur'},
            {pattern: /^[0-9]{1,9}([.][0-9]{1,2})?$/, message: '请输入正确的提现金额,保留俩位小数', trigger: 'blur'},
            {validator: validateAmount, trigger: 'blur'}
          ],
          code: [
            {required: true, message: '请输入验证码', trigger: 'blur'},
            {min: 6, max: 6, message: '请输入正确的验证码', trigger: 'blur'}
          ]
        },
        timerNum: 60,
        canSendCode: true,
        sendCodeText: '发送验证码',
        isSubmit: false,
        fee: '请先输入提现金额'
      }
    },
    methods: {
      counter: function (v) {
        this.$refs['form'].validateField('amount', (valid) => {
          if (valid) {
            this.fee = '请先输入提现金额';
            this.calculate.amount = '';
          } else {
            // 计算手续费
            if (v >= 500) {
              this.fee = 0;
            } else {
              this.fee = 2;
            }
            // 计算到账金额
            this.calculate.amount = (v - this.fee).toFixed(2) + '元';
            return false;
          }
        });
      },
      sendCode: function () {
        if (this.canSendCode) {
          this.canSendCode = false;
          this.$http.post('/daili/account/sendVerifyCode').then(res => {
            let timer = setInterval(() => {
              if (this.timerNum < 0) {
                this.canSendCode = true;
                this.sendCodeText = '重新发送';
                clearInterval(timer);
                return;
              }
              this.sendCodeText = this.timerNum--;
            }, 1000);
          }, err => {
            this.canSendCode = true;
            this.$message({
              showClose: true,
              message: err.data.msg,
              type: 'error'
            });
          })
        }
      },
      onSubmit: function () {
        if (!this.isSubmit) {
          this.$refs['form'].validate((valid) => {
            if (valid) {
              this.isSubmit = true;
              this.$http.post('/daili/account/withdraw', this.form).then(res => {
                this.$message({
                  showClose: true,
                  message: '提现申请成功',
                  type: 'success'
                });
                this.$router.push('/daili/app/balance_withdrawal');
                this.isSubmit = false;
              }, err => {
                this.isSubmit = false;
                this.$message({
                  showClose: true,
                  message: err.data.msg,
                  type: 'error'
                });
              })
            } else {
              console.log('error submit!!');
              return false;
            }
          });
        }
      }
    },
    computed: {
      c_fee: function () {
        if (typeof (this.fee) == 'string') {
          return this.fee;
        }
        return this.fee + '元';
      }
    }
  }
</script>
<style scoped lang="less">
  .form-label {
    margin-bottom: 15px;
  }

  .line-center {
    text-align: center;
  }
</style>
