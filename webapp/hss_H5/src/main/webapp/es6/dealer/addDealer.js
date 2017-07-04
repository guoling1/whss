/**
 * Created by yulong.zhang on 2016/12/6.
 */

// 引入http message validate
const validate = _require('validate');
const message = _require('message');
const http = _require('http');
// 定义变量
let submit = document.getElementById('submit');
let mobile = document.getElementById('mobile');
let username = document.getElementById('name');
let idCard = document.getElementById('idCard');
let belongArea = document.getElementById('belongArea');
let bankAccountName = document.getElementById('bankAccountName');
let bankCard = document.getElementById('bankCard');
let bankReserveMobile = document.getElementById('bankReserveMobile');
let weixinSettleRate = document.getElementById('weixinSettleRate');
let alipaySettleRate = document.getElementById('alipaySettleRate');
let quickSettleRate = document.getElementById('quickSettleRate');
let withdrawSettleFee = document.getElementById('withdrawSettleFee');
submit.addEventListener('click', function () {
  if (validate.joint({
      phone: [mobile.value, bankReserveMobile.value],
      bankNo: bankCard.value
    }) && validate.name(username.value, '代理商姓名')
    && validate.address(belongArea.value, '代理商所在地')
    && validate.name(bankAccountName.value, '银行开户名')
    && validate.empty(alipaySettleRate.value, '支付宝结算费率')
    && validate.empty(weixinSettleRate.value, '微信结算费率')
    && validate.empty(quickSettleRate.value, '无卡快捷结算费率')
    && validate.empty(withdrawSettleFee.value, '提现结算价')) {
    http.post('/dealer/addSecondDealer', {
      mobile: mobile.value, name: username.value, idCard: idCard.value, belongArea: belongArea.value,
      bankAccountName: bankAccountName.value, bankCard: bankCard.value,
      bankReserveMobile: bankReserveMobile.value, weixinSettleRate: weixinSettleRate.value,
      alipaySettleRate: alipaySettleRate.value, quickSettleRate: quickSettleRate.value,
      withdrawSettleFee: withdrawSettleFee.value
    }, function () {
      window.location.href = "/dealer/indexInfo";
    })
  }
});