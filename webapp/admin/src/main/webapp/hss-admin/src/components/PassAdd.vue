<template lang="html">
    <div id="passAdd">
      <div style="margin: 15px;width: inherit" class="box">
        <form class="form-horizontal">
          <div class="box-header">
            <h3 v-if="isShow" class="box-title">增加通道</h3>
            <h3 v-else="isShow" class="box-title">修改通道</h3>
          </div>
          <div class="box-body">
            <div class="form-group">
              <label for="channelName" class="col-sm-2 control-label">通道名称</label>
              <div class="col-sm-4">
                <input type="text" class="form-control" id="channelName" v-model="channelName">
              </div>
              <div class="col-sm-6 right">例如：华有支付宝</div>
            </div>
            <div class="form-group">
              <label for="thirdCompany" class="col-sm-2 control-label">收单机构</label>

              <div class="col-sm-4">
                <input type="text" class="form-control" id="thirdCompany" v-model="thirdCompany">
              </div>
              <div class="col-sm-6 right">例如：支付宝、微信、京东钱包、百度钱包</div>
            </div>
            <div class="form-group">
              <label for="channelSource" class="col-sm-2 control-label">渠道来源</label>

              <div class="col-sm-4">
                <input type="text" class="form-control" id="channelSource" v-model="channelSource">
              </div>
              <div class="col-sm-6 right">例如：华有</div>
            </div>
            <div class="form-group">
              <label for="basicTradeRate" class="col-sm-2 control-label">支付费率</label>

              <div class="col-sm-4 middle">
                <input type="number" class="form-control" id="basicTradeRate" v-model="basicTradeRate">
                <i>%</i>
              </div>
              <div class="col-sm-6 right">例如：0.3%</div>
            </div>
            <div class="form-group">
              <label for="basicWithdrawFee" class="col-sm-2 control-label">提现费用</label>

              <div class="col-sm-4 middle">
                <input type="number" class="form-control" id="basicWithdrawFee" v-model="basicWithdrawFee">
                <i>元/笔</i>
              </div>
              <div class="col-sm-6 right">例如：0.5元/笔</div>
            </div>
            <div class="form-group">
              <label for="basicWithdrawFee" class="col-sm-2 control-label">结算时间</label>
              <div class="col-sm-4">
                <select class="form-control" v-model="basicBalanceType">
                  <option value="">请选择</option>
                  <option value="D0">D0</option>
                  <option value="D1">D1</option>
                  <option value="T0">T0</option>
                  <option value="T1">T1</option>
                </select>
              </div>
              <div class="col-sm-6 right"></div>
            </div>
          </div>
          <div class="box-footer">
            <div type="submit" class="btn btn-default" @click="passAdd" v-if="isShow">添 加</div>
            <div type="submit" class="btn btn-default" @click="passAdd" v-if="!isShow">修 改</div>
            <div type="submit" class="btn btn-info pull-right" v-if="!isShow" @click="goBack">返 回</div>
          </div>
        </form>
      </div>
      <!--<div class="btn btn-success" @click="passAdd" v-if="isShow">添 加</div>-->
      <!--<div class="btn btn-danger product" @click="passAdd" v-if="!isShow">修改</div>-->
      <!--<div class="btn btn-success product" v-if="!isShow" @click="goBack">返回</div>-->
    </div>
</template>

<script lang="babel">
    export default {
      name:"passAdd",
      data(){
        return{
          channelName:"",
          thirdCompany:"",
          channelSource:"",
          basicTradeRate:"",
          basicWithdrawFee:"",
          basicBalanceType:"",
          isShow:true,//true新增
        }
      },
      created: function () {
        if(this.$route.query.id!=undefined){
          this.$data.isShow=false
          this.$http.post('/admin/channel/list')
            .then(function (res) {
              var data= res.data[this.$route.query.id];
              this.$data.channelName=data.channelName;
              this.$data.thirdCompany=data.thirdCompany;
              this.$data.channelSource=data.channelSource;
              this.$data.basicTradeRate=data.basicTradeRate;
              this.$data.basicWithdrawFee=data.basicWithdrawFee;
              this.$data.basicBalanceType=data.basicBalanceType;
              this.$data.id=data.id;
              this.$data.status=data.status;
              this.$data.accountId=data.accountId;
            }, function (err) {
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: err.statusMessage
              })
            })
        }
      },
      methods: {
        passAdd: function () {
          //新增
          if(this.$route.query.id==undefined){
            this.$http.post('/admin/channel/add',this.$data)
              .then(function(res){
                this.$store.commit('MESSAGE_ACCORD_SHOW', {
                  text: "添加成功"
                })
              },function (err) {
                this.$store.commit('MESSAGE_ACCORD_SHOW', {
                  text: "添加失败"
                })
              })
          }else {
            this.$http.post('/admin/channel/update',this.$data)
              .then(function(res){
                this.$store.commit('MESSAGE_ACCORD_SHOW', {
                  text: "修改成功"
                })
                this.$router.push('/admin/record/passList')
              },function (err) {
                this.$store.commit('MESSAGE_ACCORD_SHOW', {
                  text: "修改失败"
                })
              })
          }
        },
        goBack:function () {
          this.$router.push('/admin/record/passList')
        }
      },
      computed: {
        $$data: function () {
          return this.$data;
        }
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

  a {
    color: #42b983;
  }
  .passAdd{
    float: right;
    width: 80%;
  }
  .middle{
    position: relative;
  i{
    font-style: normal;
    position: absolute;
    top: 8px;
    right: 22px;
    width: 33px;
    text-align: right;
    display: inline-block;
  }
  }
  .right{
    padding-top: 7px;
    color: #b0b0b0;
  }
  /*form{
    label{
      display: block;
      input{
        padding-left: 10px;
        display: inline-block;
        height: 40px;
        margin-right: 10px;
      }

      span{
        color: #ccc;
      }
    }
  }
  .left{
    position: relative;
    left: -37px;
  }
  .btn{
    margin:40px 0 0 70px;
    width: 200px;
  }*/
</style>
