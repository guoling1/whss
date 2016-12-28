<template lang="html">
  <div id="upgrade">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">用户升级规则</div>
    <div style="margin: 0 15px 15px;width: inherit" class="box box-info">
      <form class="form-horizontal">
        <div class="box-body">
          <div class="form-group">
            <label for="channelName" class="col-sm-4 control-label">一级分润比例</label>
            <div class="col-sm-4 middle">
              <input type="text" class="form-control" id="channelName" v-model="channelName">
              <i>%</i>
            </div>
          </div>
          <div class="form-group">
            <label for="thirdCompany" class="col-sm-4 control-label">二级分润比例</label>

            <div class="col-sm-4 middle">
              <input type="text" class="form-control" id="thirdCompany" v-model="thirdCompany">
              <i>%</i>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label">钻石级</label>
          </div>
          <div class="form-group">
            <label for="channelSources" class="col-sm-4 control-label">商户费率</label>

            <div class="col-sm-4 middle">
              <input type="text" class="form-control" id="channelSources" v-model="channelSource">
              <i>%</i>
            </div>
          </div>
          <div class="form-group">
            <label for="channelSource" class="col-sm-4 control-label">付费升级</label>

            <div class="col-sm-4 middle">
              <input type="text" class="form-control" id="channelSource" v-model="channelSource">
              <i>元</i>
            </div>
          </div>
          <div class="form-group">
            <label for="basicTradeRate" class="col-sm-4 control-label">邀请好友</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basicTradeRate" v-model="basicTradeRate">
              <i>个</i>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label">白金级</label>
          </div>
          <div class="form-group">
            <label for="basicTradeRate1" class="col-sm-4 control-label">商户费率</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basicTradeRate1" v-model="basicTradeRate">
              <i>%</i>
            </div>
          </div>
          <div class="form-group">
            <label for="channel" class="col-sm-4 control-label">付费升级</label>

            <div class="col-sm-4 middle">
              <input type="text" class="form-control" id="channel" v-model="channelSource">
              <i>元</i>
            </div>
          </div>
          <div class="form-group">
            <label for="basicTradeRate2" class="col-sm-4 control-label">邀请好友</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basicTradeRate2" v-model="basicTradeRate">
              <i>个</i>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-3 control-label">白银级</label>
          </div>
          <div class="form-group">
            <label for="basicTradeRate1" class="col-sm-4 control-label">商户费率</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basicTradeRate1" v-model="basicTradeRate">
              <i>%</i>
            </div>
          </div>
          <div class="form-group">
            <label for="channel" class="col-sm-4 control-label">付费升级</label>

            <div class="col-sm-4 middle">
              <input type="text" class="form-control" id="channel" v-model="channelSource">
              <i>元</i>
            </div>
          </div>
          <div class="form-group">
            <label for="basicTradeRate2" class="col-sm-4 control-label">邀请好友</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basicTradeRate2" v-model="basicTradeRate">
              <i>个</i>
            </div>
          </div>
        </div>
        <div class="box-footer">
          <div type="submit" class="btn btn-info">保 存</div>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name:"upgrade",
    data(){
      return{

      }
    },
    created: function () {
      this.$http.post('/admin/channel/list')
        .then(function (res) {

        }, function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    },
    methods: {

    },
    computed: {

    }
  }
</script>
<style scoped lang="less">
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }
  .middle{
    position: relative;
  i {
    position: absolute;
    top: 7px;
    right: -23px;
    width: 33px;
    font-style: normal;
  }
  }
</style>
