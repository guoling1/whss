'use strict';

/**
 * Created by administrator on 2016/12/9.
 */

// 引入动画模版 处理验证码
var Animation = _require('animation');
var animation = new Animation();
// 引入http message validate
var validate = _require('validate');
var message = _require('message');
var http = _require('http');
// 引入浏览器特性处理
var browser = _require('browser');
browser.elastic_touch();
// 引入wx_upload
var Upload = _require('upload');
// 定义变量
var merchantName = document.getElementById('merchantName');
var address = document.getElementById('address');
var bankNo = document.getElementById('bankNo');
var sampleShow = document.getElementById('sampleShow');
var sampleHide = document.getElementById('sampleHide');
var username = document.getElementById('username');
var identity = document.getElementById('identity');
var reserveMobile = document.getElementById('reserveMobile');
var code = document.getElementById('code');
var submit = document.getElementById('submit');
var bankText = document.getElementById('bankText');

var upload_bank = new Upload('chooseImage', function () {
  bankText.innerHTML = '图片已上传';
});

// 定义验证码
animation.fourelement({
  url: '/merchantInfo/sendVerifyCode',
  bankName: 'bankNo',
  bankVal: 'bankNo',
  nameName: 'name',
  nameVal: 'username',
  identityName: 'identity',
  identityVal: 'identity',
  phoneName: 'reserveMobile',
  phoneVal: 'reserveMobile',
  btn: 'sendCode'
});

sampleShow.addEventListener('click', function () {
  sampleHide.style.display = 'block';
});

sampleHide.addEventListener('click', function () {
  sampleHide.style.display = 'none';
});

bankNo.addEventListener('blur', function () {
  validate.bankNo(bankNo.value);
});

submit.addEventListener('click', function () {
  var bankPic = upload_bank.getServerId();
  if (validate.name(merchantName.value, '店铺名称') && validate.address(address.value, '店铺地址') && validate.joint({
    bankNo: bankNo.value,
    idCard: identity.value,
    phone: reserveMobile.value,
    code: code.value
  }) && validate.empty(bankPic, '银行卡图片') && validate.name(username.value, '开户姓名')) {
    http.post('/merchantInfo/save', {
      merchantName: merchantName.value,
      address: address.value,
      bankNo: bankNo.value,
      name: username.value,
      identity: identity.value,
      reserveMobile: reserveMobile.value,
      code: code.value,
      bankPic: bankPic
    }, function () {
      window.location.replace("/sqb/addNext");
    });
  }
});
//# sourceMappingURL=material.js.map
