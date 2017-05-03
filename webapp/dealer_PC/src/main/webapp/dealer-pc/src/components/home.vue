<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <table class="table">
        <tbody>
        <tr>
          <td>昨日分润</td>
          <td colspan="2">{{ydayProfit | fix}}元
            <el-button type="text" @click="detail">查询每日历史</el-button>
          </td>
        </tr>
        <tr>
          <td>历史分润总额</td>
          <td colspan="2">{{allProfit | fix}}元</td>
        </tr>
        <tr>
          <td rowspan="2">账户余额</td>
          <td colspan="2">待结算金额 {{duesettleAmount | fix}}元</td>
        </tr>
        <tr>
          <td colspan="2">可提现金额 {{availableAmount | fix}}元
            <el-button type="text" @click="withdrawal">立即体现</el-button>
            <el-button type="text" @click="balance">查看明细</el-button>
          </td>
        </tr>
        <tr style="background-color:#cfcfcf;">
          <td></td>
          <td style="font-weight:bold;">好收收</td>
          <td style="font-weight:bold;">好收银</td>
        </tr>
        <tr>
          <td rowspan="3">昨日商户交易总额</td>
          <td>总额 <span style="float:right;">{{dealerReporthss.ydayMertradeAmount | fix}}元</span></td>
          <td>总额 <span style="float:right;">{{dealerReporthsy.ydayMertradeAmount | fix}}元</span></td>
        </tr>
        <tr>
          <td>直属 <span style="float:right;">{{dealerReporthss.ydayMertradeAmountDir | fix}}元</span></td>
          <td>直属 <span style="float:right;">{{dealerReporthsy.ydayMertradeAmountDir | fix}}元</span></td>
        </tr>
        <tr>
          <td>下级代理 <span style="float:right;">{{dealerReporthss.ydayMertradeAmountSub | fix}}元</span></td>
          <td>下级代理 <span style="float:right;">{{dealerReporthsy.ydayMertradeAmountSub | fix}}元</span></td>
        </tr>
        <tr>
          <td rowspan="2">昨日注册商户</td>
          <td>直属 <span style="float:right;">{{dealerReporthss.ydayregMerNumberDir}}个</span></td>
          <td>直属 <span style="float:right;">{{dealerReporthsy.ydayregMerNumberDir}}个</span></td>
        </tr>
        <tr>
          <td>下级代理 <span style="float:right;">{{dealerReporthss.ydayregMerNumberSub}}个</span></td>
          <td>下级代理 <span style="float:right;">{{dealerReporthsy.ydayregMerNumberSub}}个</span></td>
        </tr>
        <tr>
          <td rowspan="2">昨日审核通过商户</td>
          <td>直属 <span style="float:right;">{{dealerReporthss.ydaycheckMerNumberDir}}个</span></td>
          <td>直属 <span style="float:right;">{{dealerReporthsy.ydaycheckMerNumberDir}}个</span></td>
        </tr>
        <tr>
          <td>下级代理 <span style="float:right;">{{dealerReporthss.ydaycheckMerNumberSub}}个</span></td>
          <td>下级代理 <span style="float:right;">{{dealerReporthsy.ydaycheckMerNumberSub}}个</span></td>
        </tr>
        <tr>
          <td rowspan="2">注册商户总数</td>
          <td>直属 <span style="float:right;">{{dealerReporthss.regMerNumberDir}}个</span></td>
          <td>直属 <span style="float:right;">{{dealerReporthsy.regMerNumberDir}}个</span></td>
        </tr>
        <tr>
          <td>下级代理 <span style="float:right;">{{dealerReporthss.regMerNumberSub}}个</span></td>
          <td>下级代理 <span style="float:right;">{{dealerReporthsy.regMerNumberSub}}个</span></td>
        </tr>
        <tr>
          <td rowspan="2">审核通过商户总数</td>
          <td>直属 <span style="float:right;">{{dealerReporthss.checkMerNumberDir}}个</span></td>
          <td>直属 <span style="float:right;">{{dealerReporthsy.checkMerNumberDir}}个</span></td>
        </tr>
        <tr>
          <td>下级代理 <span style="float:right;">{{dealerReporthss.checkMerNumberSub}}个</span></td>
          <td>下级代理 <span style="float:right;">{{dealerReporthsy.checkMerNumberSub}}个</span></td>
        </tr>
        <tr>
          <td>二维码总数</td>
          <td><span style="float:right;">{{dealerReporthss.qrCodeNumber}}个</span></td>
          <td><span style="float:right;">{{dealerReporthsy.qrCodeNumber}}个</span></td>
        </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>
<script lang="babel">
  import store from '../store'
  export default {
    data () {
      return {
        ydayProfit: 0,
        allProfit: 0,
        duesettleAmount: 0,
        availableAmount: 0,
        dealerReporthss: {
          regMerNumberDir: 0,
          regMerNumberSub: 0,
          checkMerNumberDir: 0,
          checkMerNumberSub: 0,
          qrCodeNumber: 0,
          ydayMertradeAmount: 0,
          ydayMertradeAmountDir: 0,
          ydayMertradeAmountSub: 0,
          ydayregMerNumberDir: 0,
          ydayregMerNumberSub: 0,
          ydaycheckMerNumberDir: 0,
          ydaycheckMerNumberSub: 0
        },
        dealerReporthsy: {
          regMerNumberDir: 0,
          regMerNumberSub: 0,
          checkMerNumberDir: 0,
          checkMerNumberSub: 0,
          qrCodeNumber: 0,
          ydayMertradeAmount: 0,
          ydayMertradeAmountDir: 0,
          ydayMertradeAmountSub: 0,
          ydayregMerNumberDir: 0,
          ydayregMerNumberSub: 0,
          ydaycheckMerNumberDir: 0,
          ydaycheckMerNumberSub: 0
        },
        accountData: ''
      }
    },
    created (){
      this.$http.post('/daili/account/info').then(res => {
        this.accountData = res.data;
      }, err => {
        this.$message({
          showClose: true,
          message: err.data.msg,
          type: 'error'
        });
      });
      this.$http.post('/daili/report/home').then(res => {
        this.ydayProfit = res.data.ydayProfit;
        this.allProfit = res.data.allProfit;
        this.duesettleAmount = res.data.duesettleAmount;
        this.availableAmount = res.data.availableAmount;
        this.dealerReporthss.regMerNumberDir = res.data.dealerReporthss.regMerNumberDir;
        this.dealerReporthss.regMerNumberSub = res.data.dealerReporthss.regMerNumberSub;
        this.dealerReporthss.checkMerNumberDir = res.data.dealerReporthss.checkMerNumberDir;
        this.dealerReporthss.checkMerNumberSub = res.data.dealerReporthss.checkMerNumberSub;
        this.dealerReporthss.qrCodeNumber = res.data.dealerReporthss.qrCodeNumber;
        this.dealerReporthss.ydayMertradeAmount = res.data.dealerReporthss.ydayMertradeAmount;
        this.dealerReporthss.ydayMertradeAmountDir = res.data.dealerReporthss.ydayMertradeAmountDir;
        this.dealerReporthss.ydayMertradeAmountSub = res.data.dealerReporthss.ydayMertradeAmountSub;
        this.dealerReporthss.ydayregMerNumberDir = res.data.dealerReporthss.ydayregMerNumberDir;
        this.dealerReporthss.ydayregMerNumberSub = res.data.dealerReporthss.ydayregMerNumberSub;
        this.dealerReporthss.ydaycheckMerNumberDir = res.data.dealerReporthss.ydaycheckMerNumberDir;
        this.dealerReporthss.ydaycheckMerNumberSub = res.data.dealerReporthss.ydaycheckMerNumberSub;
        this.dealerReporthsy.regMerNumberDir = res.data.dealerReporthsy.regMerNumberDir;
        this.dealerReporthsy.regMerNumberSub = res.data.dealerReporthsy.regMerNumberSub;
        this.dealerReporthsy.checkMerNumberDir = res.data.dealerReporthsy.checkMerNumberDir;
        this.dealerReporthsy.checkMerNumberSub = res.data.dealerReporthsy.checkMerNumberSub;
        this.dealerReporthsy.qrCodeNumber = res.data.dealerReporthsy.qrCodeNumber;
        this.dealerReporthsy.ydayMertradeAmount = res.data.dealerReporthsy.ydayMertradeAmount;
        this.dealerReporthsy.ydayMertradeAmountDir = res.data.dealerReporthsy.ydayMertradeAmountDir;
        this.dealerReporthsy.ydayMertradeAmountSub = res.data.dealerReporthsy.ydayMertradeAmountSub;
        this.dealerReporthsy.ydayregMerNumberDir = res.data.dealerReporthsy.ydayregMerNumberDir;
        this.dealerReporthsy.ydayregMerNumberSub = res.data.dealerReporthsy.ydayregMerNumberSub;
        this.dealerReporthsy.ydaycheckMerNumberDir = res.data.dealerReporthsy.ydaycheckMerNumberDir;
        this.dealerReporthsy.ydaycheckMerNumberSub = res.data.dealerReporthsy.ydaycheckMerNumberSub;
      }, err => {
        console.log(err);
      })
    },
    filters: {
      fix(v = 0){
        if (v) {
          return v.toFixed(2);
        }
        return '0.00';
      }
    },
    methods: {
      detail: function () {
        this.$router.push('/daili/app/profits_detail');
      },
      balance: function () {
        this.$router.push('/daili/app/balance_withdrawal');
      },
      withdrawal: function () {
        this.$router.push({path: '/daili/app/withdrawal', query: this.accountData});
      }
    }
  }
</script>
<style scoped lang="less">
  .table {
    td {
      border: 1px solid #333 !important;
    }
  }

  .top {
    margin-top: 200px;
  }

  .bottom {
    margin-bottom: 200px;
  }
</style>
