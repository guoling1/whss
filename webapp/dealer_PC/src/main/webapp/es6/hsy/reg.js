/**
 * Created by administrator on 2016/12/8.
 */

// 引入动画模版 处理验证码
const Animation = _require('animation');
const animation = new Animation();
// 引入http message
const validate = _require('validate');
const message = _require('message');
const http = _require('http');
// 定义变量
const mobile = document.getElementById('mobile');
const code = document.getElementById('code');
const submit = document.getElementById('submit');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();

if (!pageData.qrCode || pageData.qrCode == '') {
  document.getElementById('sendCode').addEventListener('click', function () {
    message.prompt_show('您需要扫码才能进行注册');
  })
} else {
  // 定义验证码
  animation.validcode({
    url: '/wx/getCode',
    phoneName: 'mobile',
    phoneVal: 'mobile',
    btn: 'sendCode'
  });
}

// 注册
submit.addEventListener('click', ()=> {
  if (validate.joint({
      phone: mobile.value,
      code: code.value
    })) {
    http.post('/wx/login', {
      mobile: mobile.value,
      code: code.value,
      qrCode: pageData.qrCode
    }, ()=> {
      window.location.replace("/sqb/addInfo");
    })
  }
});