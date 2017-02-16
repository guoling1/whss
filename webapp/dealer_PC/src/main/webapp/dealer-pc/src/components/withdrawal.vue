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
                <el-form-item label="提现金额">
                  <el-input v-model="form.email" placeholder="保留俩位小数">
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
                <el-form-item label="到账金额">
                  <el-input v-model="form.email" disabled placeholder="请先输入提现金额">
                    <template slot="append">元</template>
                  </el-input>
                </el-form-item>
                <el-form-item label="手续费">
                  {{accountInfo.fee}} 元
                </el-form-item>
                <el-form-item label="发送验证码">
                  <el-col :span="19">
                    <el-input type="text" v-model="form.loginPwd"></el-input>
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
      return {
        form: {
          mobile: '',
          name: '',
          loginName: '',
          loginPwd: '',
          email: '',
          belongProvinceCode: '',
          belongProvinceName: '',
          belongCityCode: '',
          belongCityName: '',
          belongArea: '',
          bankCard: '',
          bankAccountName: '',
          bankReserveMobile: '',
          idCard: ''
        },
        rules: {
          mobile: [
            {required: true, message: '请输入手机号', trigger: 'blur'},
            {pattern: /^1(3|4|5|7|8)\d{9}$/, message: '请输入正确的手机号', trigger: 'blur'}
          ]
        },
        timerNum: 60,
        canSendCode: true,
        sendCodeText: '发送验证码'
      }
    },
    methods: {
      sendCode: function () {
        if (this.canSendCode) {
          this.canSendCode = false;
          this.$http.post('/api/daili/account/sendVerifyCode').then(res => {
            console.log(res);
            this.canSendCode = true;
            let timer = setInterval(() => {
              if (this.timerNum < 0) {
                this.sendCodeText = '重新发送';
                clearInterval(timer);
              }
              this.sendCodeText = this.timerNum--;
            }, 1000);
          }, err => {
            this.canSendCode = true;
            console.log(err);
          })
        }
      },
      onSubmit: function () {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            this.$http.post('/api/daili/dealer/addSecondDealer', this.form).then(res => {
              console.log(res);
            }, err => {
              console.log(err);
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
