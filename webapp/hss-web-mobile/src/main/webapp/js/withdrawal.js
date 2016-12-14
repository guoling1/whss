'use strict';

/**
 * Created by administrator on 2016/12/8.
 */

// 引入动画模版 处理验证码
var Animation = _require('animation');
var animation = new Animation();
// 引入http validate message
var validate = _require('validate');
var message = _require('message');
var http = _require('http');
// 定义变量
var next = document.getElementById('next');
var layer = document.getElementById('layer');
var xx = document.getElementById('xx');
var submit = document.getElementById('submit');
var sendCode = document.getElementById('sendCode');
var code = document.getElementById('code');

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
    });
  }
});
//# sourceMappingURL=withdrawal.js.map
