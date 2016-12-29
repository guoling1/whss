<template lang="html">
  <div id='login'>
    <h1>好收银</h1>

    <h2>HAOSHOUYIN</h2>
    <ul>
      <li class="top flexBox">
        <div class="logo phone"></div>
        <input type="text" placeholder="请输入手机号" v-model="phone">

        <div class="btn" v-code:mobile="{mobile:phone,url:codeUrl}"></div>
      </li>
      <li class="bottom flexBox">
        <div class="logo message"></div>
        <input type="text" placeholder="请输入验证码" v-model="code">
      </li>
    </ul>
    <div class="reg" @click="reg">{{msg}}</div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'login',
    data: function () {
      return {
        phone: '',
        code: '',
        codeUrl: '',
        qrCode: '',
        openId: this.$store.state.users.openId,
        msg: '注册'
      }
    },
    created: function () {
      this.$data.qrCode = this.$route.code;
      if (this.$route.path == "/dealer/login") {
        this.$data.msg = "登 录";
        this.$data.codeUrl = '/dealer/sendVerifyCode'
      } else {
        this.$data.msg = "注 册";
        this.$data.codeUrl = '/wx/getCode'
      }
    },
    methods: {
      reg: function () {
        let loginPath = '',
          toPath = '',
          data = {};
        if (this.$route.path == "/dealer/login") {
          loginPath = '/dealer/login';
          toPath = '/dealer/index';
          data = {
            mobile: this.$data.phone,
            code: this.$data.code
          }
        } else {
          loginPath = '/wx/login';
          toPath = '/sqb/material';
          data = {
            mobile: this.$data.phone,
            code: this.$data.code,
            qrCode: this.$data.qrCode,
            openId: this.$data.openId
          }
        }
        if (this._$validate.joint({
            phone: this.$data.phone,
            code: this.$data.code
          })) {
          this.$http.post(loginPath, data).then(function (res) {
            this.$router.push({
              path: toPath,
              query: {
                id: res.data
              }
            });
          }, function (err) {
            console.log(err);
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          });
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .flexItem(@val) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
  }

  #login {
    overflow: auto;
  }

  h1 {
    margin-top: 41px;
    font-size: 31px;
    color: #7086bd;
    font-weight: bold;
  }

  h2 {
    font-size: 14px;
    font-weight: lighter;
    color: #a1b0d5;
    height: 21px;
    margin-top: 5px;
  }

  ul {
    margin: 41px 0 21px;
    li {
      width: 100%;
      height: 56.5px;
      border-bottom: 1px solid #edeef5;
      padding: 25px 15px 0 20px;
      input {
        .flexItem(1);
        border: none;
        float: left;
        font-size: 16px;
        color: #4a5171;
      }
      ::-webkit-input-placeholder {
        color: #aab2d2;
      }
      :-moz-placeholder {
        color: #aab2d2;
      }
      ::-moz-placeholder {
        color: #aab2d2;
      }
      :-ms-input-placeholder {
        color: #aab2d2;
      }
      .logo {
        float: left;
        width: 35px;
        height: 50px;
        &.phone {
          background: url("../assets/phone.png") no-repeat center;
          background-size: 16px 20px;
        }
        &.message {
          background: url("../assets/message.png") no-repeat center;
          background-size: 21px 15px;
        }
      }
      &.top {
        width: 100%;
        height: 50px;
        padding: 0 15px;
        .btn {
          float: right;
          line-height: 50px;
          font-size: 14px;
          color: #7086bd;
        }
      }
      &.bottom {
        width: 100%;
        height: 50px;
        padding: 0 15px;
      }
    }
  }

  .reg {
    margin: 0 15px;
    height: 50px;
    line-height: 50px;
    background: #7086bd;
    border-radius: 4px;
    font-size: 16px;
    color: #fff;
  }
</style>
