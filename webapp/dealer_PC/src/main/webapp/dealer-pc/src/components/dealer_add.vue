<template lang="html">
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
        好收收代理商系统
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
        <div class="col-xs-8">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">添加代理商</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <label class="form-label">基本信息</label>
              <el-form ref="form" :model="form" label-width="120px">
                <el-form-item label="代理商手机号">
                  <el-input v-model="form.mobile"></el-input>
                </el-form-item>
                <el-form-item label="代理名称">
                  <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="登录名">
                  <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="登录密码">
                  <el-col :span="19">
                    <el-input v-model="form.name"></el-input>
                  </el-col>
                  <el-col class="line-center" :span="5">
                    <el-button type="text">修改密码</el-button>
                  </el-col>
                </el-form-item>
                <el-form-item label="联系邮箱">
                  <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="省市区域">
                  <el-col :span="10">
                    <el-select v-model="value" placeholder="请选择">
                      <el-option v-for="item in options"
                                 :label="item.label"
                                 :value="item.value">
                      </el-option>
                    </el-select>
                  </el-col>
                  <el-col class="line-center" :span="2">省</el-col>
                  <el-col :span="10">
                    <el-input v-model="form.name"></el-input>
                  </el-col>
                  <el-col class="line-center" :span="2">市</el-col>
                </el-form-item>
                <el-form-item label="详细地址">
                  <el-input v-model="form.name"></el-input>
                </el-form-item>
              </el-form>
            </div>
            <div class="box-footer">
              <label class="form-label">结算卡信息</label>
              <el-form ref="form" :model="form" label-width="120px">
                <el-form-item label="结算卡号">
                  <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="开户名称">
                  <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="身份证号">
                  <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="开户手机号">
                  <el-input v-model="form.name"></el-input>
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
      this.$http.post('/api/daili/district/findAllProvinces').then(function (res) {
        console.log(res);
      }, function (err) {
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
        item_province: [],
        item_city: [],
      }
    },
    methods: {
      onSubmit: function () {
        console.log(this.form);
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
