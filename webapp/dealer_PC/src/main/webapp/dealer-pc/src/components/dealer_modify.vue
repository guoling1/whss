<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-13">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">添加代理商</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <label class="form-label">基本信息</label>
              <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="demo-ruleForm">
                <el-form-item label="代理商手机号" prop="mobile">
                  <el-input v-model="form.mobile"></el-input>
                </el-form-item>
                <el-form-item label="代理名称" prop="name">
                  <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="登录名" prop="loginName">
                  <el-input v-model="form.loginName"></el-input>
                </el-form-item>
                <el-form-item label="登录密码">
                  <el-col :span="19">
                    <el-input type="password" v-model="update.loginPwd" :disabled="!modify_pwd"></el-input>
                  </el-col>
                  <el-col class="line-center" :span="5">
                    <el-button type="text" @click="modify">修改密码</el-button>
                  </el-col>
                </el-form-item>
                <el-form-item label="联系邮箱" prop="email">
                  <el-input v-model="form.email"></el-input>
                </el-form-item>
                <el-form-item label="省市区域" prop="belongCityCode">
                  <el-col :span="10">
                    <el-select v-model="form.belongProvinceCode" style="width:100%" placeholder="请选择"
                               @change="province_select">
                      <el-option v-for="item in item_province"
                                 :label="item.aname"
                                 :value="item.code">
                      </el-option>
                    </el-select>
                  </el-col>
                  <el-col class="line-center" :span="2">省</el-col>
                  <el-col :span="10">
                    <el-select v-model="form.belongCityCode" style="width:100%" placeholder="请选择"
                               @change="city_select">
                      <el-option v-for="item in item_city"
                                 :label="item.aname"
                                 :value="item.code">
                      </el-option>
                    </el-select>
                  </el-col>
                  <el-col class="line-center" :span="2">市</el-col>
                </el-form-item>
                <el-form-item label="详细地址" prop="belongArea">
                  <el-input v-model="form.belongArea"></el-input>
                </el-form-item>
                <label class="form-label">结算卡信息</label>
                <el-form-item label="结算卡号" prop="bankCard">
                  <el-input v-model="form.bankCard"></el-input>
                </el-form-item>
                <el-form-item label="开户名称" prop="bankAccountName">
                  <el-input v-model="form.bankAccountName"></el-input>
                </el-form-item>
                <el-form-item label="身份证号" prop="idCard">
                  <el-input v-model="form.idCard"></el-input>
                </el-form-item>
                <el-form-item label="开户手机号" prop="bankReserveMobile">
                  <el-input v-model="form.bankReserveMobile"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onSubmit">创建代理商</el-button>
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
      let query = this.$route.query;
      this.update.dealerId = query.dealerId;
      this.update.loginPwd = '123456';
      if (query.dealerId) {
        this.$http.get('/daili/dealer/' + query.dealerId).then(res => {
          this.form.mobile = res.data.mobile;
          this.form.name = res.data.name;
          this.form.loginName = res.data.loginName;
          this.form.email = res.data.email;
          this.form.belongProvinceCode = res.data.belongProvinceCode;
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
      }
      this.$http.post('/daili/district/findAllProvinces').then(res => {
        this.item_province = res.data;
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
        modify_pwd: false,
        update: {
          loginPwd: '',
          dealerId: ''
        },
        form: {
          mobile: '',
          name: '',
          loginName: '',
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
      modify: function () {
        if (!this.modify_pwd) {
          this.modify_pwd = true;
          this.update.loginPwd = '';
        } else {
          this.$http.post('/daili/dealer/updatePwd', this.update).then(res => {
            this.$message({
              showClose: true,
              message: '密码修改成功',
              type: 'success'
            });
            this.modify_pwd = false;
          }, err => {
            this.$message({
              showClose: true,
              message: err.data.msg,
              type: 'error'
            });
          })
        }
      },
      province_select: function (provinceCode) {
        for (let m = 0; m < this.item_province.length; m++) {
          if (this.item_province[m].code == provinceCode) {
            this.form.belongProvinceName = this.item_province[m].aname;
          }
        }
        this.$http.post('/daili/district/findAllCities', {
          code: provinceCode
        }).then(res => {
          this.item_city = res.data;
          this.form.belongCityCode = res.data[0].code;
          this.form.belongCityName = res.data[0].aname;
        }, err => {
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
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
          if (valid) {
            this.$http.post('/daili/dealer/updateDealer', this.form).then(res => {
              this.$router.push('/daili/app/dealer_list');
              this.$message({
                showClose: true,
                message: '修改代理商成功',
                type: 'success'
              });
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
