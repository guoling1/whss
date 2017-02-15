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
              <label class="form-label">产品名称：好收收</label>
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
      this.$http.post('/api/daili/dealer/getDealerProduct').then(res => {
        console.log(res);
      }, err => {
        console.log(err);
      })
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
          ],
          name: [
            {required: true, message: '请输入代理商名称', trigger: 'blur'}
          ],
          loginName: [
            {required: true, message: '请输入代理商登录名', trigger: 'blur'}
          ],
          loginPwd: [
            {required: true, message: '请输入代理商登录密码', trigger: 'blur'}
          ],
          belongCityCode: [
            {required: true, message: '请选择代理商所在省市', trigger: 'blur'}
          ],
          belongArea: [
            {required: true, message: '请选择代理商详细地址', trigger: 'blur'}
          ],
          email: [
            {required: true, message: '请输入邮箱', trigger: 'blur'},
            {type: 'email', message: '请输入正确的邮箱', trigger: 'blur'}
          ],
          bankCard: [
            {required: true, message: '请输入结算卡号', trigger: 'blur'},
            {min: 15, max: 19, message: '请输入正确的结算卡号', trigger: 'blur'}
          ],
          bankAccountName: [
            {required: true, message: '请输入开户名称', trigger: 'blur'}
          ],
          idCard: [
            {required: true, message: '请输入身份证号', trigger: 'blur'}
          ],
          bankReserveMobile: [
            {required: true, message: '请输入手机号', trigger: 'blur'},
            {pattern: /^1(3|4|5|7|8)\d{9}$/, message: '请输入正确的手机号', trigger: 'blur'}
          ]
        },
        item_province: [],
        item_city: []
      }
    },
    methods: {
      province_select: function (provinceCode) {
        for (let m = 0; m < this.item_province.length; m++) {
          if (this.item_province[m].code == provinceCode) {
            this.form.belongProvinceName = this.item_province[m].aname;
          }
        }
        this.$http.post('/api/daili/district/findAllCities', {
          code: provinceCode
        }).then(res => {
          this.item_city = res.data;
          this.form.belongCityCode = res.data[0].code;
          this.form.belongCityName = res.data[0].aname;
        }, err => {
          console.log(err);
        })
      },
      city_select: function (cityCode) {
        for (let n = 0; n < this.item_city.length; n++) {
          if (this.item_city[n].code == cityCode) {
            this.form.belongCityName = this.item_city[n].aname;
          }
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
  .form-label {
    margin-bottom: 15px;
  }

  .line-center {
    text-align: center;
  }
</style>
