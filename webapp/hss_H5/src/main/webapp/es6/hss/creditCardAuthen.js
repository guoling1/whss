/**
 * Created by administrator on 2016/12/8.
 */

// 引入浏览器特性处理
const http = _require('http');
const message = _require('message');
const browser = _require('browser');
browser.elastic_touch();

// 定义变量
let ipt = document.getElementById('ipt');
let submit = document.getElementById('submit');

submit.addEventListener('click', function () {
  if (ipt.value.length >= 15 && ipt.value.length <= 20) {
    http.post('/merchantInfo/queryBank', {bankNo: ipt.value}, function (data) {
      if (data != 1) {
        message.prompt_show('请输入正确的信用卡号');
      } else {
        http.post('/wx/creditCardAuthen', {
          creditCard: ipt.value
        }, function (data) {
          console.log(data);
        })
      }
    })
  } else {
    message.prompt_show('请输入正确的信用卡号');
  }
});
