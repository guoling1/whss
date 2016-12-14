<template lang="html">
  <div id="material">
    <div class="process">
      <div class="steps active">
        <span>1</span>填写资料
      </div>
      <div class="steps">
        <span>2</span>上传资料
      </div>
      <div class="steps">
        <span>3</span>开始使用
      </div>
    </div>
    <div class="main flexBox flex-box-column">
      <div class="top">
        <div class="group">
          <div class="name">店铺名</div>
          <input type="text" class="ipt" placeholder="例如某某小卖部" v-model="merchantName">
        </div>
        <div class="group">
          <div class="name">地址</div>
          <input type="text" class="ipt" placeholder="输入店铺地址" v-model="address">
        </div>
      </div>
      <div class="bottom">
        <div class="group">
          <div class="name">结算卡</div>
          <input type="text" class="ipt" placeholder="输入本人名下借记卡号" v-model="bankNo" @blur="bankNoBin($event)">
        </div>
        <div class="group">
          <div class="name">上传</div>
          <a href="javascript:void(0);" class="file">
            {{$$data.cardPhoto}}
            <div v-upload:bank></div>
          </a>

          <div class="btn">查看示例</div>
        </div>
        <div class="group">
          <div class="name">开户名</div>
          <input type="text" class="ipt" placeholder="本人银行卡开户姓名" v-model="name">
        </div>
        <div class="group">
          <div class="name">身份证</div>
          <input type="text" class="ipt" placeholder="输入身份证号" v-model="identity">
        </div>
        <div class="group">
          <div class="name">手机号</div>
          <input type="text" class="ipt" placeholder="开户银行预留手机号" v-model="reserveMobile">

          <div class="btn" v-code:reserveMobile="{mobile:reserveMobile,url:'/merchantInfo/sendVerifyCode'}"></div>
        </div>
        <div class="group">
          <div class="name">验证码</div>
          <input type="text" class="ipt" placeholder="输入短信验证码" v-model="code">
        </div>
        <div class="next">
          <div @click="next($event)">下一步</div>
        </div>
      </div>
    </div>
    <div v-show="false">{{$$upload}}</div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'material',
    data: function () {
      return {
        cardPhoto: '点击上传结算卡正面照',
        id: '',
        merchantName: '',
        address: '',
        bank_pic: '',
        bankNo: '',
        bankBin: false,
        name: '',
        identity: '',
        reserveMobile: '',
        code: ''
      }
    },
    created: function () {
      // 获取用户本地存储数据,如果有,直接赋给data
      let localData = JSON.parse(window.localStorage.getItem('material'));
      if (localData) {
        this.$data.cardPhoto = localData.cardPhoto;
        this.$data.merchantName = localData.merchantName;
        this.$data.address = localData.address;
        this.$data.bank_pic = localData.bank_pic;
        this.$data.bankNo = localData.bankNo;
        this.$data.bankBin = localData.bankBin;
        this.$data.name = localData.name;
        this.$data.identity = localData.identity;
        this.$data.reserveMobile = localData.reserveMobile;
      }
      // id 赋值为链接上的id
      this.$data.id = this.$route.query.id;
    },
    beforeDestroy: function () {
      // 组件销毁前,将用户数据存储到 localStorage
      window.localStorage.setItem('material', JSON.stringify(this.$data))
    },
    methods: {
      bankNoBin: function (event) {
        if (this._$validate.bankNo(this.$data.bankNo)) {
          this.$http.post('/merchantInfo/queryBank', {
            bankNo: this.$data.bankNo//银行卡号
          }).then(function (res) {
            if (res.data == 0) {
              this.$data.bankBin = true;
            } else {
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: '仅支持绑定储蓄卡'
              })
            }
          }, function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          });
        }
      },
      validateBin: function () {
        if (!this.$data.bankBin) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: '仅支持绑定储蓄卡'
          });
          return false;
        }
        return true;
      },
      next: function (event) {
        if (this._$validate.joint({
            name: [{
              data: this.$data.merchantName,
              text: '店铺名称'
            }, {
              data: this.$data.name,
              text: '开户名称'
            }],
            address: this.$data.address,
            bankNo: this.$data.bankNo,
            idCard: this.$data.identity,
            phone: this.$data.reserveMobile,
            code: this.$data.code
          }) && this.validateBin() && this.$data.bank_pic && this.$data.bank_pic != '') {
          this.$http.post('/merchantInfo/save', {
            id: this.$data.id,
            merchantName: this.$data.merchantName, //店铺名
            address: this.$data.address,//地址
            bank_pic: this.$data.bank_pic, //银行卡正面
            bankNo: this.$data.bankNo,//银行卡号
            name: this.$data.name, //商户负责人姓名
            identity: this.$data.identity,//身份证号
            reserveMobile: this.$data.reserveMobile, //手机号
            code: this.$data.code //验证码
          }).then(function () {
            this.$router.push({path: '/sqb/upload', query: {id: this.$data.id}});
          }, function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          });
        }
      }
    },
    computed: {
      $$data: function () {
        return this.$data;
      },
      $$upload: function () {
        let serverId = this.$store.state.upload.serverId;
        this.$data.bank_pic = serverId.bank;
        if (this.$data.bank_pic && this.$data.bank_pic != '') {
          this.$data.cardPhoto = '图片已上传';
        }
        return serverId;
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .flexBox {
    display: box;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    display: -webkit-flex;
  }

  .flexItem(@val) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
  }

  #material {
    width: 100%;
    height: 100%;
    overflow: auto;
  }

  .main {
    padding-bottom: 20px;
  }

  .process {
    width: 100%;
    height: 47px;
    background-color: #61729c;
    .steps {
      float: left;
      width: 33.33%;
      height: 47px;
      line-height: 47px;
      font-size: 16px;
      color: #a7b0d3;
      span {
        display: inline-block;
        width: 16px;
        height: 16px;
        line-height: 16px;
        font-size: 11px;
        color: #a7b0d3;
        border: 1px solid #a7b0d3;
        border-radius: 50%;
        margin-right: 9px;
      }
      &.active {
        color: #FFF;
        span {
          background-color: #FFF;
          color: #61729c;
          border: none;
        }
      }
    }
  }

  .top {
    width: 100%;
    height: auto;
    background-color: #FFF;
    padding-top: 15px;
  }

  .bottom {
    .flexItem(1);
    width: 100%;
    border-top: 8px solid #f4f5f8;
  }

  .group {
    .flexBox();
    width: 100%;
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #edeef5;
    padding: 0 15px;
    .name {
      width: 50px;
      margin-right: 15px;
      font-size: 15px;
      color: #4a5171;
      text-align: left;
    }
    .file {
      display: inline-block;
      .flexItem(1);
      width: 1px;
      line-height: 50px;
      text-align: left;
      font-size: 15px;
      color: #7079a0;
      position: relative;
      div {
        width: 100%;
        height: 100%;
        opacity: 0;
        position: absolute;
        left: 0;
        top: 0;
      }
    }
    .ipt {
      .flexItem(1);
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
    .btn {
      font-size: 12px;
      color: #7079a0;
    }
  }

  .next {
    padding: 26px 15px 0;
    div {
      width: 100%;
      height: 50px;
      line-height: 50px;
      font-size: 16px;
      color: #FFF;
      text-align: center;
      background-color: #7086bd;
      border-radius: 5px;
    }
  }
</style>
