/**
 * Created by administrator on 2016/12/8.
 */

// 引入动画模版 处理验证码
const AnimationCountdown = _require('art-countdown');
let countdown = new AnimationCountdown('sendCode', '重新获取');
// 引入http message
const validate = _require('validate');
const message = _require('message');
const http = _require('http');

// 定义变量
let amount = document.getElementById('amount');
let settleAmount = document.getElementById('settleAmount');
let available = document.getElementById('available');

// 获取数据
http.post('/account/info', {}, function (data) {
  amount.innerHTML = data.totalAmount.toFixed(2);
  settleAmount.innerHTML = data.settleAmount.toFixed(2);
  available.innerHTML = data.available.toFixed(2);
});