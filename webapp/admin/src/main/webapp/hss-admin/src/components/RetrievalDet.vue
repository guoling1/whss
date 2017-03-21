<template lang="html">
  <div id="storeAudit">
    <div class="box-header with-border" style="margin: 0 0 0 3px;">
      <h3 class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">提现详情</h3>
    </div>
    <div style="margin: 0 15px">
      <div class="box box-primary" v-if="isStore">
        <p class="lead">提现商户信息</p>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">商户名称：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">商户编号：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">商户注册手机号：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">商户注册时间：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">商户所在地：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">商户详细地址：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">一级代理名称：<span>{{msg.markCode2}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">一级代理编号：<span>{{msg.proxyName1}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">二级代理名称：<span>{{msg.markCode2}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">二级代理编号：<span>{{msg.proxyName1}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
      </div>
      <div class="box box-primary" v-else>
        <p class="lead">提现代理商信息</p>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">代理名称：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">代理编号：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">代理注册手机号：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">代理注册时间：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">代理所在地：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">代理详细地址：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">一级代理名称：<span>{{msg.markCode2}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">一级代理编号：<span>{{msg.proxyName1}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
      </div>
      <div class="box box-primary">
        <p class="lead">交易信息</p>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">提现单号：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">结算周期：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">交易发起时间：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">结算状态：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">交易金额：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">结算时间：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">手续费率：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">付款人：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">手续费：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">付款人账户：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">交易类型：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">收款账户编号：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">交易状态：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">收款商户名称：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">交易成功时间：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">商品名称：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">支付方式：<span>{{msg.mobile}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">商品描述：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
      </div>
      <div class="box box-primary" style="overflow: hidden">
        <p class="lead">打款流水</p>
        <div style="width: 80%;margin: 0 0 15px 15px;">
          <template>
            <el-table :data="rateInfo" border style="width: 100%">
              <el-table-column prop="channelName" label="打款流水号"></el-table-column>
              <el-table-column prop="merchantRate" label="打款金额（元）"></el-table-column>
              <el-table-column prop="time" label="打款发起时间"></el-table-column>
              <el-table-column prop="withdrawMoney" label="打款状态"></el-table-column>
              <el-table-column prop="entNet" label="打款完成时间"></el-table-column>
              <el-table-column prop="remarks" label="打款渠道"></el-table-column>
              <el-table-column prop="remarks" label="渠道信息"></el-table-column>
            </el-table>
          </template>
        </div>
      </div>
      <div class="box box-primary" style="overflow: hidden">
        <p class="lead">分润流水</p>
        <div style="width: 80%;margin: 0 0 15px 15px;">
          <template>
            <el-table :data="rateInfo" border style="width: 100%">
              <el-table-column prop="channelName" label="分润流水号"></el-table-column>
              <el-table-column prop="merchantRate" label="分润金额（元）"></el-table-column>
              <el-table-column prop="time" label="分润收款方名称"></el-table-column>
              <el-table-column prop="withdrawMoney" label="收款方类型"></el-table-column>
              <el-table-column prop="entNet" label="结算周期"></el-table-column>
              <el-table-column prop="remarks" label="结算时间"></el-table-column>
              <el-table-column prop="remarks" label="分润总额"></el-table-column>
              <el-table-column prop="remarks" label="备注"></el-table-column>
            </el-table>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'storeAudit',
    data () {
      return {
        isStore: true,
        id: '',
        msg: {
          id: '',
          merchantName: '',
          identity: '',
          address: '',
          bankNo: '',
          mobile: '',
          identityFacePic: '',
          identityOppositePic: '',
          identityHandPic: '',
          bankHandPic: '',
          proxyName1: '',
          proxyName: '',
          reserveMobile: '',
          createTime: '',
          proxyNameYQ: '',
          proxyNameYQ1: '',
        },
        rateInfo: []
      }
    },
    created: function () {
      this.$data.id = this.$route.query.id;
      this.$http.post('/admin/QueryMerchantInfoRecord/getAll', {id: this.$data.id})
        .then(function (res) {
          this.$data.msg = res.data.list[0];
        }, function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
    },
    methods: {
      //修改名称
      reset: function () {
        this.$prompt('请输入新名称', '修改上报名称', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => {
          this.$http.post('/admin/changeMerchantName/change', {id: this.$route.query.id, merchantChangeName: value})
            .then(function (res) {
              this.$data.msg.merchantChangeName = value;
              this.$message({
                showClose: true,
                type: 'success',
                message: '修改成功'
              });
            })
            .catch(function (err) {
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消修改'
          });
        });
      },
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less" rel="stylesheet/less">
  .box-primary {
    padding-bottom: 15px;
  }

  .label {
    color: #333;
    span, a {
      font-weight: normal;
    }
    a {
      color: #20a0ff;
    }
  }
</style>
