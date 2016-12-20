<template lang="html">
  <div id="login" v-if="$$login">
    <div class="login-space">
      <div class="login-title">登录</div>

      <div class="flexBox group">
        <div class="text">用户名</div>
        <input type="text" class="ipt" placeholder="请输入用户名" v-model="username">
      </div>

      <div class="flexBox group">
        <div class="text">密码</div>
        <input type="password" class="ipt" placeholder="请输入密码" v-model="password">
      </div>

      <div class="submit" @click="login">登录</div>
    </div>
    <message></message>
  </div>
</template>

<script lang="babel">
  import Message from './Message.vue'
  export default {
    name: 'login',
    components: {
      Message
    },
    data: function () {
      return {
        msg: '登录',
        login: false,
        username: '',
        password: ''
      }
    },
    created: function () {
      this.$store.commit('LOGIN_SHOW');
    },
    methods: {
      login: function () {
        this.$http.post('/admin/user/login', {
          username: this.$data.username,
          password: this.$data.password
        }).then(function () {
          this.$store.commit('LOGIN_HIDE');
          this.$router.push({path: '/admin/record/deal'})
        }, function (err) {
          console.log(err);
        });
      }
    },
    computed: {
      $$login: function () {
        return this.$store.state.login.ctrl;
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .flexItem(@val) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
  }

  .flexBox {
    display: box;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    display: -webkit-flex;
  }

  #login {
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 9999;
    text-align: center;
    background-color: #f1f1f1;
    padding: 200px 50px;
  }

  .login-space {
    width: 500px;
    height: 330px;
    margin: auto;
    background-color: #FFF;
    border: 1px solid #f5f5f5;
  }

  .login-title {
    height: 80px;
    line-height: 80px;
    font-size: 36px;
    font-weight: bold;
    color: #333;
  }

  .group {
    padding: 10px 15px;
    .text {
      width: 80px;
      height: 50px;
      line-height: 50px;
    }
    .ipt {
      .flexItem(1);
      border: 1px solid #f5f5f5;
      padding-left: 15px;
      font-size: 24px;
    }
  }

  .submit {
    width: 150px;
    height: 50px;
    margin: 20px auto 0;
    line-height: 50px;
    background-color: #f5f5f5;
    border-radius: 3px;
    font-size: 18px;
    color: #333;
    text-align: center;
  }
</style>
