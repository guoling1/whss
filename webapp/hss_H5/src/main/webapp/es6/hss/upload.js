/**
 * Created by administrator on 2016/12/15.
 */


// 引入http message validate
const validate = _require('validate');
const message = _require('message');
const http = _require('http');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch('upload');
// 引入wx_upload
const Upload = _require('upload');
// 定义变量
let submit = document.getElementById('submit');
let identityFacePic_src = document.getElementById('identityFacePic_src');
let identityOppositePic_src = document.getElementById('identityOppositePic_src');
let identityHandPic_src = document.getElementById('identityHandPic_src');
let bankHandPic_src = document.getElementById('bankHandPic_src');

let ua = navigator.userAgent.toLowerCase();
// 获取本地图片
let getLocalImg = function (id, localId) {
  wx.getLocalImgData({
    localId: localId,
    success: function (res) {
      alert(res.localData);
      document.getElementById(id).setAttribute('src', res.localData);
    }
  });
};

let upload_identityFacePic = new Upload('identityFacePic', function (localId) {
  if (ua.indexOf('iphone') >= 0) {
    getLocalImg('identityFacePic_src', localId);
    return;
  }
  alert(localId);
  document.getElementById('identityFacePic_src').setAttribute('src', localId);
});

let upload_identityOppositePic = new Upload('identityOppositePic', function (localId) {
  if (ua.indexOf('iphone') >= 0) {
    getLocalImg('identityOppositePic_src', localId);
    return;
  }
  document.getElementById('identityOppositePic_src').setAttribute('src', localId);
});

let upload_identityHandPic = new Upload('identityHandPic', function (localId) {
  if (ua.indexOf('iphone') >= 0) {
    getLocalImg('identityHandPic_src', localId);
    return;
  }
  document.getElementById('identityHandPic_src').setAttribute('src', localId);
});

let upload_bankHandPic = new Upload('bankHandPic', function (localId) {
  if (ua.indexOf('iphone') >= 0) {
    getLocalImg('bankHandPic_src', localId);
    return;
  }
  document.getElementById('bankHandPic_src').setAttribute('src', localId);
});

submit.addEventListener('click', function () {
  let identityFacePic = upload_identityFacePic.getServerId();
  let identityOppositePic = upload_identityOppositePic.getServerId();
  let identityHandPic = upload_identityHandPic.getServerId();
  let bankHandPic = upload_bankHandPic.getServerId();
  if (validate.empty(identityFacePic, '身份证正面照片')
    && validate.empty(identityOppositePic, '身份证背面照片')
    && validate.empty(identityHandPic, '手持身份证正面照片')
    && validate.empty(bankHandPic, '手持结算卡正面照片')) {
    http.post('/merchantInfo/savePic', {
      identityFacePic: identityFacePic,
      identityOppositePic: identityOppositePic,
      identityHandPic: identityHandPic,
      bankHandPic: bankHandPic
    }, function () {
      window.location.href = "/sqb/follow";
    })
  }
});