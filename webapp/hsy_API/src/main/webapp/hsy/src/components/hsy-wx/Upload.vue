<template lang="html">
  <div id="upload">
    <div class="process">
      <div class="steps">
        <span class="right"></span>填写资料
      </div>
      <div class="steps active">
        <span>2</span>上传资料
      </div>
      <div class="steps">
        <span>3</span>开始使用
      </div>
    </div>
    <div class="main flexBox flex-box-column">
      <div class="space">
        <div class="left">
          <a href="javascript:void(0);" class="file">
            <div class="file-title">身份证正面照片</div>
            <div class="file-logo">
              <img v-bind:src="$$data.identityFace" v-if="$$data.identityFace&&$$data.identityFace!=''">
            </div>
            <div class="btn" v-upload:identityFace></div>
          </a>
        </div>
        <div class="right">
          <div class="sample-title">示例</div>
          <div class="sample-logo"><img src="../../assets/sample1.png"></div>
        </div>
      </div>
      <div class="space">
        <div class="left">
          <a href="javascript:void(0);" class="file">
            <div class="file-title">身份证背面照片</div>
            <div class="file-logo">
              <img v-bind:src="identityOpposite" v-if="identityOpposite&&identityOpposite!=''">
            </div>
            <div class="btn" v-upload:identityOpposite></div>
          </a>
        </div>
        <div class="right">
          <div class="sample-title">示例</div>
          <div class="sample-logo"><img src="../../assets/sample2.png"></div>
        </div>
      </div>
      <div class="space">
        <div class="left">
          <a href="javascript:void(0);" class="file">
            <div class="file-title">手持身份证正面照片</div>
            <div class="file-logo">
              <img v-bind:src="identityHand" v-if="identityHand&&identityHand!=''">
            </div>
            <div class="btn" v-upload:identityHand></div>
          </a>
        </div>
        <div class="right">
          <div class="sample-title">示例</div>
          <div class="sample-logo"><img src="../../assets/sample3.png"></div>
        </div>
      </div>
      <div class="space">
        <div class="left">
          <a href="javascript:void(0);" class="file">
            <div class="file-title">手持结算卡正面照片</div>
            <div class="file-logo">
              <img v-bind:src="bankHand" v-if="bankHand&&bankHand!=''">
            </div>
            <div class="btn" v-upload:bankHand></div>
          </a>
        </div>
        <div class="right">
          <div class="sample-title">示例</div>
          <div class="sample-logo"><img src="../../assets/sample4.png"></div>
        </div>
      </div>
      <div class="next">
        <div @click="next($event)">提交</div>
      </div>
    </div>
    <div v-show="false">{{$$upload}}</div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'upload',
    data: function () {
      return {
        id: '',//商户id
        // 提交数据 serverId
        identityFacePic: "", // 身份证正面
        identityOppositePic: "", //身份证反面
        identityHandPic: "", //手持身份证正面
        bankHandPic: "", //手持一行卡正面
        // 展示数据 localId
        identityFace: "", // 身份证正面
        identityOpposite: "", //身份证反面
        identityHand: "", //手持身份证正面
        bankHand: "" //手持一行卡正面
      }
    },
    created: function () {
      // 获取用户本地存储数据,如果有,直接赋给data
      let localData = JSON.parse(window.localStorage.getItem('upload'));
      if (localData) {
        this.$store.commit('UPLOAD_CHANGE', {
          localId: {
            identityFace: localData.identityFace,
            identityOpposite: localData.identityOpposite,
            identityHand: localData.identityHand,
            bankHand: localData.bankHand
          },
          serverId: {
            identityFace: localData.identityFacePic,
            identityOpposite: localData.identityOppositePic,
            identityHand: localData.identityHandPic,
            bankHand: localData.bankHandPic
          }
        });
      }
      // id 赋值为链接上的id
      this.$data.id = this.$route.query.id;
    },
    beforeDestroy: function () {
      // 组件销毁前,将用户数据存储到 localStorage
      window.localStorage.setItem('upload', JSON.stringify(this.$data))
    },
    methods: {
      next: function (event) {
        if (this.$data.identityFacePic && this.$data.identityFacePic != '' &&
          this.$data.identityOppositePic && this.$data.identityOppositePic != '' &&
          this.$data.identityHandPic && this.$data.identityHandPic != '' &&
          this.$data.bankHandPic && this.$data.bankHandPic != '') {
          this.$http.post('/merchantInfo/savePic', {
            id: this.$data.id,
            identityFacePic: this.$data.identityFacePic,
            identityOppositePic: this.$data.identityOppositePic,
            identityHandPic: this.$data.identityHandPic,
            bankHandPic: this.$data.bankHandPic
          }).then(function () {
            this.$router.push({path: '/sqb/follow'});
          }, function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          });
        } else {
          console.log('不能有空值');
        }
      }
    },
    computed: {
      $$data: function () {
        return this.$data;
      },
      $$upload: function () {
        let change = this.$store.state.upload.change;
        let localId = this.$store.state.upload.localId;
        let serverId = this.$store.state.upload.serverId;
        console.log(localId);
        console.log(serverId);
        this.$data.identityFacePic = serverId.identityFace;
        this.$data.identityOppositePic = serverId.identityOpposite;
        this.$data.identityHandPic = serverId.identityHand;
        this.$data.bankHandPic = serverId.bankHand;
        this.$data.identityFace = localId.identityFace;
        this.$data.identityOpposite = localId.identityOpposite;
        this.$data.identityHand = localId.identityHand;
        this.$data.bankHand = localId.bankHand;
        return change;
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

  #upload {
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: #f4f5f8;
  }

  .main {
    overflow: auto;
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
        &.right {
          background: url("../../assets/right.png") no-repeat center;
          background-size: 16px 16px;
          border: none;
        }
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

  .space {
    padding: 25px 15px 5px;
    .left {
      float: left;
      .file {
        display: inline-block;
        .flexItem(1);
        width: 100%;
        height: 100%;
        position: relative;
        .btn {
          width: 100%;
          height: 100%;
          opacity: 0;
          position: absolute;
          left: 0;
          top: 0;
        }
      }
    }
    .right {
      float: right;
    }
    .left, .right {
      width: 48%;
      height: 170px;
      background-color: #FFF;
      padding: 8px 5px;
      .file-title {
        font-size: 14px;
        color: #aab2d2;
        line-height: 24px;
        text-align: center;
      }
      .file-logo {
        width: 100%;
        height: 126px;
        background: #f4f5f8 url("../../assets/camera.png") no-repeat center;
        background-size: 37px 32px;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .sample-title {
        font-size: 14px;
        color: #aab2d2;
        line-height: 24px;
        text-align: center;
      }
      .sample-logo {
        width: 100%;
        height: 126px;
        background-color: #f4f5f8;
        img {
          width: 100%;
          height: 100%;
        }
      }
    }
  }

  .next {
    padding: 20px 15px 0;
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
