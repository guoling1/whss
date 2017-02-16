<template lang="html">
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
        钱包++代理商系统
        <small>Version 1.0</small>
      </h1>
      <ol class="breadcrumb">
        <li>
          <router-link to="/app/home"><i class="glyphicon glyphicon-home"></i> 主页</router-link>
        </li>
        <!--<li class="active">Dashboard</li>-->
      </ol>
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
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
                    <span>最小可提现金额：1.00 元 <br> 最大可提现金额：{{accountInfo.available}} 元</span>
                    <el-input slot="reference" type="number" v-model="form.amount" placeholder="保留俩位小数"
                              @change="counter">
                      <template slot="append">元</template>
                    </el-input>
                  </el-popover>
                </el-form-item>
                <el-form-item label="到账金额">
                  <el-input v-model="calculate.amount" disabled placeholder="请先输入提现金额">
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
                <el-form-item label="手续费">
                  {{accountInfo.fee}} 元
                </el-form-item>
                <el-form-item label="发送验证码" prop="code">
                  <el-col :span="19">
                    <el-input type="text" v-model="form.code"></el-input>
                  </el-col>
                  <el-col class="line-center" :span="5">
                    <el-button type="text" :disabled="!canSendCode" @click="sendCode">{{sendCodeText}}</el-button>
                  </el-col>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onSubmit">立即提现</el-button>
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
        if (value >= 1 && value <= this.accountInfo.available) {
          callback();
        } else {
          callback(new Error('最小可提现金额1.00元 最大可提现金额' + this.accountInfo.available + '元'));
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
            {pattern: /^[0-9]{1,9}([.][0-9]{1,2})?$/, message: '请输入正确的提现金额', trigger: 'blur'},
            {validator: validateAmount, trigger: 'blur'}
          ],
          code: [
            {required: true, message: '请输入验证码', trigger: 'blur'},
            {min: 6, max: 6, message: '请输入正确的验证码', trigger: 'blur'}
          ]
        },
        timerNum: 60,
        canSendCode: true,
        sendCodeText: '发送验证码'
      }
    },
    methods: {
      counter: function (v) {
        this.$refs['form'].validateField('amount', (valid) => {
          if (valid) {
            this.calculate.amount = '';
          } else {
            this.calculate.amount = (v - this.accountInfo.fee).toFixed(2) + '元';
            return false;
          }
        });
      },
      sendCode: function () {
        if (this.canSendCode) {
          this.canSendCode = false;
          this.$http.post('/api/daili/account/sendVerifyCode').then(res => {
            this.canSendCode = true;
            let timer = setInterval(() => {
              if (this.timerNum < 0) {
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
        this.$refs['form'].validate((valid) => {
          if (valid) {
            this.$http.post('/api/daili/account/withdraw', this.form).then(res => {
              this.$message({
                showClose: true,
                message: '提现申请成功',
                type: 'success'
              });
              this.$router.push('/app/balance_withdrawal');
            }, err => {
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
