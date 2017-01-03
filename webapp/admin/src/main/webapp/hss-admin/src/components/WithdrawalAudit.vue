<template lang="html">
  <div id="productAdd">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">审核打款结果</div>
    <div style="margin: 0 15px;width: inherit" class="box box-info">
      <form class="form-horizontal">
        <div class="box-body">
          <div class="form-group">
            <label class="col-sm-6 control-label">账户编号：</label>
            <div class="col-sm-6">1000011</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">用户名称：</label>
            <div class="col-sm-6">老王烟酒</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">用户类型：</label>
            <div class="col-sm-6 middle">商户</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">业务流水号：</label>
            <div class="col-sm-6">10010233331</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">业务类型：</label>
            <div class="col-sm-6 middle">提现</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">打款金额：</label>
            <div class="col-sm-6 middle">100.02元</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">打款人：</label>
            <div class="col-sm-6 middle">王大锤</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">收款银行账号：</label>
            <div class="col-sm-6 middle">12090132326565</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">打款时间：</label>
            <div class="col-sm-6">2016-06-10 20:02:20</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">打款通道：</label>
            <div class="col-sm-6">魔宝支付</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">打款状态：</label>
            <div class="col-sm-6">失败</div>
          </div>
          <div class="form-group">
            <label class="col-sm-6 control-label">失败原因：</label>
            <div class="col-sm-6">银行账号错误</div>
          </div>
          <div class="form-group">
            <label class="col-sm-4 control-label">审核备注：</label>
            <div class="col-sm-5">
              <input type="text" class="form-control" v-model="productName" placeholder="必填">
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-4 control-label"></label>
            <div class="col-sm-5">
              <div class="btn btn-success">打款成功</div>
              <div class="btn btn-info" style="margin: 0 auto;">确认失败 重新打款</div>
              <div class="btn btn-warning">确认失败 解冻</div>
            </div>
          </div>
          <div class="form-group" style="text-align: center;">注意：操作不可逆，请谨慎操作</div>
          <div class="form-group" style="text-align: center;color: #a7a5a5;">审核为“打款成功”将会扣除用户冻结资金，确认失败将会解冻资金归还到可用余额。</div>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="babel">
  export default{
    name:'productAdd',
    data(){
      return{

      }
    },
    created: function () {

    },
    methods:{
      goBack:function () {
        this.$router.push('/admin/record/productList')
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
</style>
