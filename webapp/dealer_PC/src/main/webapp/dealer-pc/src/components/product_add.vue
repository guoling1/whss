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
              <h3 class="box-title">产品信息设置</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <label class="form-label">产品名称：{{productName}}</label>
            </div>
            <div class="box-body">
              <label class="form-label">产品分润设置</label>
              <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="demo-ruleForm">
                <el-table :data="tableData" border>
                  <el-table-column prop="id" label="通道名称"></el-table-column>
                  <el-table-column prop="author" label="支付结算手续费"></el-table-column>
                  <el-table-column prop="type" label="结算时间"></el-table-column>
                  <el-table-column label="提现结算费" width="180">
                    <template scope="scope">
                      <el-form-item label="代理商手机号" prop="mobile">
                        <el-input v-model="form.mobile"></el-input>
                      </el-form-item>
                    </template>
                  </el-table-column>
                  <el-table-column prop="type" label="商户支付手续费"></el-table-column>
                  <el-table-column prop="type" label="商户提现手续费"></el-table-column>
                </el-table>
              </el-form>
            </div>
            <div class="box-body">
              <label class="form-label">代理商推广码&推广链接</label>
              <br>
              <el-switch v-model="inviteBoolean" @change="switchInvite"
                         on-text="开启"
                         on-color="#13ce66"
                         off-text="关闭"
                         off-color="#ff4949">
              </el-switch>
              <div class="inviteText" v-show="inviteBoolean">
                推广码：{{inviteCode}}
                <br>
                推广链接：https://{{product}}.qianbaojiajia.com/reg?invest={{inviteCode}}
              </div>
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
      this.$http.post('/api/daili/dealer/getDealerProduct', {
        dealerId: query.dealerId,
        sysType: query.product,
        productId: query.productId
      }).then(res => {
        this.product = query.product;
        this.productName = res.data.productName;
        this.inviteStatus = res.data.inviteBtn;
        this.inviteCode = res.data.inviteCode;
        this.inviteBoolean = (this.inviteStatus == 2);
      }, err => {
        console.log(err);
      })
    },
    data() {
      return {
        product: '',
        productName: '',
        inviteBoolean: false,
        inviteStatus: 1,
        inviteCode: '',
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
        }
      }
    },
    methods: {
      switchInvite: function (Boole) {
        if (Boole) {
          this.inviteStatus = 2;
        } else {
          this.inviteStatus = 1;
        }
      },
      onSubmit: function () {
        this.$refs['form'].validate((valid) => {
          console.log(valid);
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
<style lang="less">
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
