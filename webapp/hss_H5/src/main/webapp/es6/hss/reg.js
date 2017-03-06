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
const mobile = document.getElementById('mobile');
const sendCode = document.getElementById('sendCode');
const code = document.getElementById('code');
const submit = document.getElementById('submit');
const invite = document.getElementById('invite');
const inviteCode = document.getElementById('inviteCode');
const layer = document.getElementById('layer');
const cancel = document.getElementById('cancel');
const login = document.getElementById('login');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();

if (!pageData.qrCode || pageData.qrCode == '') {
  invite.style.display = 'block';
}

if (!pageData.inviteCode || pageData.inviteCode == '') {
  inviteCode.readonly = false;
}

sendCode.addEventListener('click', function () {
  if (validate.phone(mobile.value)) {
    if (countdown.check()) {
      http.post('/wx/getCode', {
        mobile: mobile.value
      }, function (data) {
        if (data === false) {
          layer.style.display = 'block';
          return;
        }
        message.prompt_show('验证码发送成功');
        countdown.submit_start();
      })
    }
  }
});

cancel.addEventListener('click', function () {
  layer.style.display = 'none';
});

login.addEventListener('click', function () {
  window.location.href = '/sqb/login?phone=' + mobile.value;
});

// 注册
submit.addEventListener('click', ()=> {
  if (!pageData.qrCode || pageData.qrCode == '') {
    if (validate.joint({
        phone: mobile.value,
        code: code.value
      })) {
      http.post('/wx/login', {
        mobile: mobile.value,
        code: code.value,
        inviteCode: inviteCode.value
      }, ()=> {
        window.location.replace("/sqb/addInfo");
      })
    }
  } else {
    if (validate.joint({
        phone: mobile.value,
        code: code.value
      })) {
      http.post('/wx/login', {
        mobile: mobile.value,
        code: code.value,
        qrCode: pageData.qrCode,
        inviteCode: inviteCode.value
      }, ()=> {
        window.location.replace("/sqb/addInfo");
      })
    }
  }
});