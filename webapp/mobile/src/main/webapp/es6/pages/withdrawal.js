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
const xx = document.getElementById('xx');
const submit = document.getElementById('submit');
const sendCode = document.getElementById('sendCode');
const code = document.getElementById('code');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// 判断按钮是否可以点击
if (pageData.bookValue == 0) {
  next.setAttribute('disabled', 'true');
}

// 定义验证码
animation.sendcode({
  url: '/wx/getWithDrawCode',
  btn: 'sendCode'
});

next.addEventListener('click', function () {
  layer.style.display = 'block';
  sendCode.click();
});

xx.addEventListener('click', function () {
  layer.style.display = 'none';
});

submit.addEventListener('click', function () {
  if (validate.code(code.value)) {
    message.load_show('正在校验');
    http.post('/wx/withdraw', {
      code: code.value
    }, function () {
      message.load_hide();
      message.toast_show('提现成功');
      setTimeout(function () {
        window.location.replace('/sqb/wallet');
      }, 1000);
    })
  }
});