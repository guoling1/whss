<template lang="html">
  <div id="productAdd">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">审核打款结果</div>
    <div style="margin: 0 15px;width: inherit" class="box box-info">
      <form class="form-horizontal">
        <div class="box-body">
          <div class="form-group">
            <label class="col-sm-6 control-label">账户编号：</label>
            <div class="col-sm-6">{{query.accountId}}</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">用户名称：</label>
            <div class="col-sm-6">{{query.userName}}</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">用户类型：</label>
            <div class="col-sm-6 middle">{{query.userType}}</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">业务流水号：</label>
            <div class="col-sm-6">{{record.orderNo}}</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">业务类型：</label>
            <div class="col-sm-6 middle">提现</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">打款金额：</label>
            <div class="col-sm-6 middle">{{record.amount}}元</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">收款人：</label>
            <div class="col-sm-6 middle">{{record.receiptUserName}}</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">收款银行账号：</label>
            <div class="col-sm-6 middle">{{record.bankCard}}</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">打款时间：</label>
            <div class="col-sm-6">{{record.requestTime}}</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">打款通道：</label>
            <div class="col-sm-6">{{record.playMoneyChannel}}</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">打款状态：</label>
            <div class="col-sm-6">{{record.status}}</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">失败原因：</label>
            <div class="col-sm-6">{{record.message}}</div>
          </div>
          <div class="form-group">
            <label class="col-sm-4 control-label">审核备注：</label>
            <div class="col-sm-5">
              <input type="text" class="form-control" v-model="query.opinionContent" placeholder="必填">
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-4 control-label"></label>
            <div class="col-sm-5">
              <div class="btn btn-success" @click="audit(1)">打款成功</div>
              <div class="btn btn-info" style="margin: 0 auto;" @click="audit(2)">确认失败 重新打款</div>
              <div class="btn btn-warning" @click="audit(3)">确认失败 解冻</div>
            </div>
          </div>
          <div class="form-group" style="text-align: center;">注意：操作不可逆，请谨慎操作</div>
          <div class="form-group" style="text-align: center;color: #a7a5a5;">审核为“打款成功”将会扣除用户冻结资金，确认失败将会解冻资金归还到可用余额。</div>
        </div>
      </form>
    </div>
    <!--选择账号-->
    <div class="box box-info mask" v-if="isMask">
      <div class="box-body" style="text-align: center;font-size: 20px;">
        请选择打款账户
      </div>
      <div class="box-footer clearfix" style="border-top: none">
        <a href="javascript:void(0)" @click="choose(1)" class="btn btn-sm btn-info btn-flat pull-left">D0账户打款</a>
        <a href="javascript:void(0)" @click="choose(2)" class="btn btn-sm btn-default btn-flat pull-right">T1账户打款</a>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default{
    name:'productAdd',
    data(){
      return{
        query:{
          accountId:"",
          userName:"",
          userType:"",
          orderNo:this.$route.query.orderNo,
          sn:this.$route.query.sn,
          tradeType:"提现",
          opinionContent:"",
          opinion:"",
          accountType:""
        },
        isMask: false,
        record: this.$route.query
      }
    },
    created: function () {
      this.$http.post("/admin/order/queryInfoByOrderNo",{orderNo:this.$route.query.orderNo})
        .then(function (res) {
          this.$data.query.accountId = res.data.accountId;
          this.$data.query.userName = res.data.userName;
          this.$data.query.userType = res.data.userType;
        },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    },
    methods:{
      goBack:function () {
        this.$router.push('/admin/record/productList')
      },
      audit: function (val) {
        this.$data.query.opinion = val;
        if(val==2){
          this.$data.isMask = true;
        }else {
          this.$http.post('http://192.168.1.20:8076/order/withdraw/audit',this.$data.query)
            .then(function (res) {
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: "操作成功"
              })
              this.$router.push('/admin/record/newWithdrawalQuery')
            },function (err) {
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: err.statusMessage
              })
            })
        }
      },
      choose: function (val) {
        this.$data.query.accountType = val;
        console.log(this.$data.query)
        this.$http.post('http://192.168.1.20:8076/order/withdraw/audit',this.$data.query)
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: "操作成功"
            })
            this.$data.isMask = false;
          },function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
            this.$data.isMask = false;
          })
      }
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

  a {
    color: #fff;
  }

  .form-group div{
    padding-top: 7px;
  }
  .mask{
    width: 20%;
    position: fixed;
    top: 30%;
    left: 46%;
    box-shadow: 0 0 15px #000;
  }
</style>
