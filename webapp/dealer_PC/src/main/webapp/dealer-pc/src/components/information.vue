<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-13">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">注册信息</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <label class="form-label">基本信息</label>
              <el-form ref="form" :model="form" label-width="120px" class="demo-ruleForm">
                <el-form-item label="注册手机号">
                  <el-input v-model="form.mobile" :readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="名称">
                  <el-input v-model="form.name" :readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="登录名">
                  <el-input v-model="form.loginName" :readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="登录密码">
                  <el-col :span="19">
                    <el-input type="password" v-model="form.loginPwd" :readonly="true"></el-input>
                  </el-col>
                  <el-col class="line-center" :span="5">
                    <el-button type="text" @click="change">修改密码</el-button>
                  </el-col>
                </el-form-item>
                <el-form-item label="联系邮箱">
                  <el-input v-model="form.email" :readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="省市区域">
                  <el-col :span="10">
                    <el-input v-model="form.belongProvinceName" :readonly="true"></el-input>
                  </el-col>
                  <el-col class="line-center" :span="2">省</el-col>
                  <el-col :span="10">
                    <el-input v-model="form.belongCityName" :readonly="true"></el-input>
                  </el-col>
                  <el-col class="line-center" :span="2">市</el-col>
                </el-form-item>
                <el-form-item label="详细地址">
                  <el-input v-model="form.belongArea" :readonly="true"></el-input>
                </el-form-item>
                <label class="form-label">结算卡信息</label>
                <el-form-item label="结算卡号">
                  <el-input v-model="form.bankCard" :readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="开户名称">
                  <el-input v-model="form.bankAccountName" :readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="身份证号">
                  <el-input v-model="form.idCard" :readonly="true"></el-input>
                </el-form-item>
                <el-form-item label="开户手机号">
                  <el-input v-model="form.bankReserveMobile" :readonly="true"></el-input>
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
      this.$http.get('/daili/dealer/dealerDetails').then(res => {
        this.form.mobile = res.data.mobile;
        this.form.name = res.data.name;
        this.form.loginName = res.data.loginName;
        this.form.email = res.data.email;
        this.form.belongProvinceCode = res.data.belongProvinceCode;
        this.form.belongProvinceName = res.data.belongProvinceName;
        this.form.belongProvinceName = res.data.belongProvinceName;
        this.form.belongCityCode = res.data.belongCityCode;
        this.form.belongCityName = res.data.belongCityName;
        this.form.belongArea = res.data.belongArea;
        this.form.bankCard = res.data.bankCard;
        this.form.bankAccountName = res.data.bankAccountName;
        this.form.bankReserveMobile = res.data.bankReserveMobile;
        this.form.idCard = res.data.idCard;
      }, err => {
        this.$message({
          showClose: true,
          message: err.data.msg,
          type: 'error'
        });
      })
    },
    data() {
      return {
        form: {
          mobile: '',
          name: '',
          loginName: '',
          loginPwd: '******',
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
        }
      }
    },
    methods: {
      change: function () {
        this.$router.push('/daili/app/change_password');
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
