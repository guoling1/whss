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
// 引入常用工具
const tools = _require('tools');
let argument = tools.GetUrlArg();
console.log(argument);
// 定义变量
const mobile = document.getElementById('mobile');
const sendCode = document.getElementById('sendCode');
const submit = document.getElementById('submit');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();

sendCode.addEventListener('click', function () {
  if (validate.phone(mobile.value)) {
    if (countdown.check()) {
      http.post('/wx/getLoginCode', {
        mobile: mobile.value
      }, function () {
        message.prompt_show('验证码发送成功');
        countdown.submit_start();
      })
    }
  }
});

// 登录
submit.addEventListener('click', ()=> {
  if (validate.joint({
      phone: mobile.value,
      code: code.value
    })) {
    http.post('/wx/directLogin', {
      mobile: mobile.value,
      code: code.value
    }, ()=> {
      window.location.replace("/sqb/wallet");
    })
  }
});

if (argument.phone) {
  mobile.value = argument.phone;
  sendCode.click();
}