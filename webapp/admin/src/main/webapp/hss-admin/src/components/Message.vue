<template lang="html">
<!--  <transition name="fade">
    <div class="main" v-if="$$message" @click="know">
      <div class="col-md-3 content" v-show="$$accord">
        <div class="box box-danger">
          <div class="box-header with-border">
            <h3 class="box-title">提示</h3>
          </div>
          <div class="box-body">
            {{$$text}}
          </div>
        </div>
      </div>
      <div class="col-md-3 content" v-show="$$delay">
        <div class="box box-danger">
          <div class="box-header with-border">
            <h3 class="box-title">提示</h3>
          </div>
          <div class="box-body">
            {{$$text}}
          </div>
        </div>
      </div>
      &lt;!&ndash;<div class="group" v-show="$$delay">
        <div class="prompt">提示</div>
        <div class="text">{{$$text}}</div>
      </div>
      <div class="group accord" v-show="$$accord">
        <div class="prompt">提示</div>
        <div class="text">{{$$text}}</div>
        <div class="btn" @click="know">我知道了</div>
      </div>&ndash;&gt;
    </div>
  </transition>-->
  <el-dialog title="提示" v-model="$$accord" size="tiny" :show-close="false" :close-on-click-modal="false">
    <span>{{$$text}}</span>
    <span slot="footer" class="dialog-footer">
    <el-button @click="closeAll">关闭窗口</el-button>
    <!--<el-button type="primary" @click="goBack">返回主页</el-button>-->
    </span>
  </el-dialog>
</template>
<script lang="babel">
  import Vue from 'vue';
  export default {
    name: 'message',
    data: function () {
      return {
        delay: false,
        accord: false
      }
    },
    methods: {
      know: function () {
        this.$store.commit('MESSAGE_ACCORD_HIDE');
      },
      goBack:function(){
        this.$store.commit('MESSAGE_ACCORD_HIDE');
        this.$router.push('/admin/record/home')
      },
      closeAll: function () {
        window.close()
      }
    },
    computed: {
      $$message: function () {
        return this.$store.state.message.message
      },
      $$delay: function () {
        return this.$store.state.message.delay
      },
      $$accord: function () {
        return this.$store.state.message.accord
      },
      $$text: function () {
        return this.$store.state.message.text
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .fade-enter-active, .fade-leave-active {
    transition: opacity .2s
  }

  .fade-enter, .fade-leave-active {
    opacity: 0
  }
  i{
    font-style: normal;
  }
  .main {
    width: 100%;
    height: 100%;
    overflow: hidden;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 999999;
    background: rgba(0, 0, 0, 0.5);
  }
  .content{
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate3d(-50%, -50%, 0);
    /*padding: 20px 15px 25px;*/
  }

  .group {
    width: 90%;
    height: auto;
    background-color: #FFF;
    border-radius: 5px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate3d(-50%, -50%, 0);
    padding: 20px 15px 25px;
    .prompt {
      font-size: 20px;
      color: #111;
      font-weight: bold;
      line-height: 30px;
      text-align: center;
    }
    .text {
      font-size: 18px;
      color: #111;
      line-height: 30px;
      text-align: center;
      max-height: 300px;
      overflow: auto;
    }
    &.accord {
      padding-bottom: 50px;
      .btn {
        width: 100%;
        height: 44px;
        border-top: 1px solid #f5f5f5;
        text-align: center;
        line-height: 44px;
        position: absolute;
        left: 0;
        bottom: 0;
      }
    }
  }
</style>
