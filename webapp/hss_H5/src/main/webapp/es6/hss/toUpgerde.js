/**
 * Created by administrator on 2017/1/9.
 */

// 引入http message
const message = _require('message');
const http = _require('http');
// 引入浏览器特性处理
const browser = _require('browser');
browser.set_html_size();
// 定义变量
let toWhere = true;
const payToUp = document.getElementById('payToUp');
const payToCheck = document.getElementById('payToCheck');
const pushToUp = document.getElementById('pushToUp');
const pushToCheck = document.getElementById('pushToCheck');
const toUp = document.getElementById('toUp');
const layer = document.getElementById('layer');
// 记录付款金额
let oldHtml = toUp.innerHTML;

payToUp.addEventListener('click', function () {
  toWhere = true;
  payToCheck.className = 'check active';
  pushToCheck.className = 'check';
  toUp.innerHTML = oldHtml;
});

pushToUp.addEventListener('click', function () {
  toWhere = false;
  payToCheck.className = 'check';
  pushToCheck.className = 'check active';
  toUp.innerHTML = '推广好友';
});

toUp.addEventListener('click', function () {
  if (toWhere) {
    // to pay
    window.location.href = '/sqb/toBuy/' + pageData.level;
  } else {
    // to push
    layer.style.display = 'block';
  }
});

layer.addEventListener('click', function () {
  layer.style.display = 'none';
});

new QRCode(qr, {
  text: pageData.shareUrl,
  colorDark: "#000000",
  colorLight: "#ffffff",
  correctLevel: QRCode.CorrectLevel.H
});