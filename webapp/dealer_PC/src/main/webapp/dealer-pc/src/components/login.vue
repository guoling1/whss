<template lang="html">
  <div class="login-box">
    <div class="login-logo">
      <div>钱包++代理商系统</div>
    </div>
    <!-- /.login-logo -->
    <div class="login-box-body">
      <p class="login-box-msg">欢迎登录钱包++代理商系统</p>

      <div class="form-group has-feedback">
        <input type="text" class="form-control" placeholder="账户名" v-model="loginName">
        <span class="glyphicon glyphicon-user form-control-feedback"></span>
      </div>
      <div class="form-group has-feedback">
        <input type="password" class="form-control" placeholder="密码" v-model="pwd">
        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
      </div>
      <div class="row">
        <div class="col-xs-8">
          <!-- 用户协议 记住账号 -->
          <div class="checkbox">
            <label>
              <input type="checkbox" v-model="remember">记住我
            </label>
          </div>
        </div>
        <!-- /.col -->
        <div class="col-xs-4">
          <el-button type="primary" class="btn btn-primary btn-block btn-flat" @click="login" :loading="isLogin">登录
          </el-button>
        </div>
        <!-- /.col -->
      </div>
    </div>
    <!-- /.login-box-body -->
  </div>
</template>

<script lang="babel">
  import store from '../store';
  export default {
    name: 'hello',
    data () {
      return {
        loginName: '',
        pwd: '',
        remember: false,
        isLogin: false
      }
    },
    beforeRouteEnter(to, from, next) {
      const accountInfo = JSON.parse(localStorage.getItem('accountInfo'));
      //next('/app/home');
      next(vm => {
        if (accountInfo) {
          vm.loginName = accountInfo.loginName;
          vm.remember = accountInfo.remember;
        }
      })
    },
    methods: {
      login: function () {
        this.isLogin = true;
        this.$http.post('/daili/login/pc', {
          loginName: this.loginName,
          pwd: this.pwd
        }).then(res => {
          // 如果记住我 那就存储 username remember 到 localStorage
          if (this.remember) {
            localStorage.setItem('accountInfo', JSON.stringify({loginName: this.loginName, remember: this.remember}));
          } else {
            localStorage.setItem('accountInfo', null);
          }
          // 跳转到 home 页
          this.$router.push('/daili/app/home');
          this.isLogin = false;
        }, err => {
          this.isLogin = false;
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
        })
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .login {
    padding: 100px;
  }

  .f-right {
    float: right;
  }
</style>
