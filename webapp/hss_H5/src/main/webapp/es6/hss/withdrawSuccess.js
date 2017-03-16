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
const tools = _require('tools');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();

let query = tools.GetUrlArg();

let bank = document.getElementById('bank');
let amount = document.getElementById('amount');
let fee = document.getElementById('fee');
let come = document.getElementById('come');
bank.innerHTML = decodeURIComponent(query.toBank);
amount.innerHTML = query.toAmount + '元';
fee.innerHTML = query.toFee + '元';
come.innerHTML = query.toCome + '元';

let complete = document.getElementById('complete');
complete.addEventListener('click', function () {
  window.location.href = '/sqb/wallet';
});