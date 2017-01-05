<template lang="html">
  <div id="invite">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">邀请规则设置</div>
    <div style="margin: 0 15px 15px;width: inherit" class="box box-info">
      <form class="form-horizontal">
        <div class="box-body">
          <div class="form-group">
            <label for="channelName" class="col-sm-3 control-label">一级代理商</label>
            <div class="col-sm-4">
              <input type="text" class="form-control" id="channelName" v-model="channelName">
            </div>
            <div class="col-sm-5 right"></div>
          </div>
          <div class="form-group">
            <label for="thirdCompany" class="col-sm-3 control-label">受邀用户开通费</label>

            <div class="col-sm-4 middle">
              <input type="text" class="form-control" id="thirdCompany" v-model="thirdCompany">
              <i>元</i>
            </div>
            <div class="col-sm-5 right">  默认值：29元</div>
          </div>
          <div class="form-group">
            <label for="channelSources" class="col-sm-3 control-label">一级代理开通费分润</label>

            <div class="col-sm-4 middle">
              <input type="text" class="form-control" id="channelSources" v-model="channelSource">
              <i>元</i>
            </div>
            <div class="col-sm-5 right"></div>
          </div>
          <div class="form-group">
            <label for="channelSource" class="col-sm-3 control-label">二级代理开通费分润</label>

            <div class="col-sm-4 middle">
              <input type="text" class="form-control" id="channelSource" v-model="channelSource">
              <i>元</i>
            </div>
            <div class="col-sm-5 right"></div>
          </div>
          <div class="form-group">
            <label for="basicTradeRate" class="col-sm-3 control-label">邀请人分润比例(一级)</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basicTradeRate" v-model="basicTradeRate">
              <i>%</i>
            </div>
            <div class="col-sm-5 right">需要邀请3人<span>默认值：0.02%(直属金开门的用户)</span></div>
          </div>
          <div class="form-group">
            <label for="basicTradeRate1" class="col-sm-3 control-label">邀请人分润比例(二级)</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basicTradeRate1" v-model="basicTradeRate">
              <i>%</i>
            </div>
            <div class="col-sm-5 right">需要邀请5人<span>默认值：0.03%(直属金开门的用户)</span></div>
          </div>
          <div class="form-group">
            <label for="basicTradeRate2" class="col-sm-3 control-label">邀请人分润比例(三级)</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basicTradeRate2" v-model="basicTradeRate">
              <i>%</i>
            </div>
            <div class="col-sm-5 right">需要邀请10人<span>默认值：0.04%(直属金开门的用户)</span></div>
          </div>
          <div class="form-group">
            <label for="basicWithdrawFee" class="col-sm-3 control-label">邀请人一级代理收单分润比例</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basicWithdrawFee" v-model="basicWithdrawFee">
              <i>%</i>
            </div>
            <div class="col-sm-5 right"></div>
          </div>
          <div class="form-group">
            <label for="basicWithdraw" class="col-sm-3 control-label">邀请人二级代理提现分润</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basicWithdraw" v-model="basicWithdrawFee">
              <i>元/笔</i>
            </div>
            <div class="col-sm-5 right"></div>
          </div>
          <div class="form-group">
            <label for="basicWith" class="col-sm-3 control-label">邀请人二级代理收单分润比例</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basicWith" v-model="basicWithdrawFee">
              <i>%</i>
            </div>
            <div class="col-sm-5 right"></div>
          </div>
          <div class="form-group">
            <label for="basic" class="col-sm-3 control-label">邀请人二级代理结算分润</label>

            <div class="col-sm-4 middle">
              <input type="number" class="form-control" id="basic" v-model="basicWithdrawFee">
              <i>元/笔</i>
            </div>
            <div class="col-sm-5 right"></div>
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
    name:"invite",
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
  .right{
    padding-top: 7px;
    span {
      margin-left: 10px;
      color: #ccc;
    }
  }
</style>
