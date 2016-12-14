'use strict';

// 引入动画模版 处理验证码
var Animation = _require('animation');
var animation = new Animation();
// 引入http message
var vali = _require('validate');
var message = _require('message');
var http = _require('http');
// 定义变量
var mobile = document.getElementById('mobile');
var code = document.getElementById('code');
var submit = document.getElementById('submit');

// 定义验证码
animation.validcode({
    url: '/dealer/sendVerifyCode',
    phoneName: 'mobile',
    phoneVal: 'mobile',
    btn: 'sendCode'
});

// 注册
submit.addEventListener('click', function () {
    if (vali.joint({
        phone: mobile.value,
        code: code.value
    })) {
        http.post('/dealer/ajaxLogin', {
            mobile: mobile.value,
            code: code.value
        }, function (data) {
            window.location.href = data.url;
        });
    }
});
//# sourceMappingURL=dealerLogin.js.map
