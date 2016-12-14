<template lang="html">
    <div id="passAdd">
      <h1 v-if="isShow">增加通道</h1>
      <h1 v-else="isShow">修改通道</h1>
      <form action="">
        <label>
          通道名称：
          <input type="text" v-model="channelName" value="">
          <span>例如：华有支付宝</span>
        </label>
        <label>
          收单机构：
          <input type="text" v-model="thirdCompany">
          <span>例如：支付宝、微信、京东钱包、百度钱包</span>
        </label>
        <label>
          渠道来源：
          <input type="text" v-model="channelSource">
          <span>例如：华有</span>
        </label>
        <label>
          支付费率：
          <input type="text" v-model="basicTradeRate">
          <i>%</i>
          <span class="left">例如：0.3%</span>
        </label>
        <label>
          提现费用：
          <input type="text" v-model="basicWithdrawFee">
          <i>元/笔</i>
          <span class="left">例如：0.5元/笔</span>
        </label>
        <label>
          结算时间：
          <select name="" id="" v-model="basicBalanceType">
            <option value="">请选择</option>
            <option value="D0">D0</option>
            <option value="D1">D1</option>
            <option value="T0">T0</option>
            <option value="T1">T1</option>
          </select>
        </label>
      </form>
      <div class="btn btn-success" @click="passAdd" v-if="isShow">添 加</div>
      <div class="btn btn-danger product" @click="passAdd" v-if="!isShow">修改</div>
      <div class="btn btn-success product" @click="" v-if="!isShow" @click="goBack">返回</div>
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
  form{
    label{
      display: block;
      input{
        padding-left: 10px;
        display: inline-block;
        height: 40px;
        margin-right: 10px;
      }
      i{
        font-style: normal;
        position: relative;
        top:0;
        left: -55px;
        width: 33px;
        text-align: right;
        display: inline-block;
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
  }
</style>
