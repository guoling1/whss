<template lang="html">
  <div class="login-box">
    <div class="login-logo">
      <div>钱包++管理系统</div>
    </div>
    <!-- /.login-logo -->
    <div class="login-box-body">
      <p class="login-box-msg">欢迎登录钱包++管理系统</p>

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
          <el-button type="primary" class="btn btn-primary btn-block btn-flat" @click="login" :loading="isLogin"
                     :disabled="isLogin">登录
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
        if (!this.isLogin) {
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
            let data = res.data;
            let flag= false;
            let url='';
            sessionStorage.setItem('login', JSON.stringify(res.data));
            // 跳转到 home 页
            for(var i=0;i<data.length;i++){
                if(data[i].url!=null){
                  url = data[i].url;
                  break;
                }else {
                    for(var j=0;j<data[i].children.length;j++){
                      if(data[i].children[j]!=null){
                        url = data[i].children[j].menuUrl;
                        flag = true;
                        break;
                      }
                    }
                }
                if(flag){
                    break
                }
            }

            this.$router.push(url);
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
