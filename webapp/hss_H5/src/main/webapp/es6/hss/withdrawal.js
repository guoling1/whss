/**
 * Created by administrator on 2016/12/8.
 */

// 引入动画模版 处理验证码
const Animation = _require('animation');
const animation = new Animation();
// 引入http validate message
const validate = _require('validate');
const message = _require('message');
const http = _require('http');
// 定义变量
const next = document.getElementById('next');
const layer = document.getElementById('layer');
const submit = document.getElementById('submit');
const sendCode = document.getElementById('sendCode');
const code = document.getElementById('code');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();

// 定义变量
let amount = document.getElementById('amount');
let bank = document.getElementById('bank');
let ipt = document.getElementById('ipt');
let fee = document.getElementById('fee');
let come = document.getElementById('come');
let mobile = document.getElementById('mobile');
let reFee = '';
let reAcconut = '';
// 获取数据
http.post('/account/info', {}, function (data) {
  amount.innerHTML = data.available.toFixed(2);
  bank.innerHTML = data.bankName + '(' + data.bankNo + ')';
  fee.innerHTML = data.withdrawFee.toFixed(2);
  mobile.innerHTML = data.mobile;
  reFee = data.withdrawFee;
  reAcconut = data.available;
  if (data.available <= data.withdrawFee) {
    next.setAttribute('disabled', 'true');
  }
});

ipt.addEventListener('input', function (e) {
  let ev = e.target;
  let val = ev.value;
  come.innerHTML = (val - reFee) > 0 ? (val - reFee).toFixed(2) : '0.00';
});

// 定义验证码
animation.sendcode({
  url: '/account/sendVerifyCode',
  btn: 'sendCode'
});

next.addEventListener('click', function () {
  // 校验输入的值是否合法
  if (ipt.value > reAcconut) {
    message.prompt_show('可提现金额不足');
    return;
  }
  if (ipt.value <= reFee) {
    message.prompt_show('提现金额必须大于手续费');
    return;
  }
  layer.style.display = 'block';
  sendCode.click();
});

submit.addEventListener('click', function () {
  if (validate.code(code.value)) {
    message.load_show('正在校验');
    http.post('/account/withdraw', {
      amount: ipt.value,
      channel: 101,
      code: code.value
    }, function () {
      message.load_hide();
      window.location.replace('/account/toHssWithdrawSuccess');
    })
  }
});