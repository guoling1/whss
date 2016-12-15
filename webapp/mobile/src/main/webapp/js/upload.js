'use strict';

/**
 * Created by administrator on 2016/12/15.
 */

// 引入http message validate
var validate = _require('validate');
var message = _require('message');
var http = _require('http');
// 引入浏览器特性处理
var browser = _require('browser');
browser.elastic_touch('main');
// 引入wx_upload
var Upload = _require('upload');
// 定义变量
var submit = document.getElementById('submit');
var identityFacePic_src = document.getElementById('identityFacePic_src');
var identityOppositePic_src = document.getElementById('identityOppositePic_src');
var identityHandPic_src = document.getElementById('identityHandPic_src');
var bankHandPic_src = document.getElementById('bankHandPic_src');

var upload_identityFacePic = new Upload('identityFacePic', function (localId) {
  document.getElementById('identityFacePic_src').setAttribute('src', localId);
});

var upload_identityOppositePic = new Upload('identityOppositePic', function (localId) {
  document.getElementById('identityOppositePic_src').setAttribute('src', localId);
});

var upload_identityHandPic = new Upload('identityHandPic', function (localId) {
  document.getElementById('identityHandPic_src').setAttribute('src', localId);
});

var upload_bankHandPic = new Upload('bankHandPic', function (localId) {
  document.getElementById('bankHandPic_src').setAttribute('src', localId);
});

submit.addEventListener('click', function () {
  var identityFacePic = upload_identityFacePic.getServerId();
  var identityOppositePic = upload_identityOppositePic.getServerId();
  var identityHandPic = upload_identityHandPic.getServerId();
  var bankHandPic = upload_bankHandPic.getServerId();
  if (validate.empty(identityFacePic, '身份证正面照片') && validate.empty(identityOppositePic, '身份证背面照片') && validate.empty(identityHandPic, '手持身份证正面照片') && validate.empty(bankHandPic, '手持结算卡正面照片')) {
    http.post('/merchantInfo/savePic', {
      identityFacePic: identityFacePic,
      identityOppositePic: identityOppositePic,
      identityHandPic: identityHandPic,
      bankHandPic: bankHandPic
    }, function () {
      window.location.href = "/sqb/follow";
    });
  }
});
//# sourceMappingURL=upload.js.map
