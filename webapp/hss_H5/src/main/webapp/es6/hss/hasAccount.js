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

// 获取数据
http.post('/daili/account/info', {}, function (data) {
  console.log(data);
});