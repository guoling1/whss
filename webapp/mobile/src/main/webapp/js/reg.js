'use strict';

/**
 * Created by administrator on 2016/12/8.
 */

// 引入动画模版 处理验证码
var Animation = _require('animation');
var animation = new Animation();
// 引入http message
var validate = _require('validate');
var message = _require('message');
var http = _require('http');
// 定义变量
var mobile = document.getElementById('mobile');
var code = document.getElementById('code');
var submit = document.getElementById('submit');
// 引入浏览器特性处理
var browser = _require('browser');
browser.elastic_touch();
// 定义验证码
animation.validcode({
  url: '/wx/getCode',
  phoneName: 'mobile',
  phoneVal: 'mobile',
  btn: 'sendCode'
});

// 注册
submit.addEventListener('click', function () {
  if (validate.joint({
    phone: mobile.value,
    code: code.value
  })) {
    http.post('/wx/login', {
      mobile: mobile.value,
      code: code.value,
      qrCode: pageData.qrCode
    }, function () {
      window.location.replace("/sqb/addInfo");
    });
  }
});
//# sourceMappingURL=reg.js.map
