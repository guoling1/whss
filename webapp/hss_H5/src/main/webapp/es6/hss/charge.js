/**
 * Created by administrator on 2016/12/9.
 */

// 引入http message
const message = _require('message');
const http = _require('http');
// 定义变量
const qr = document.getElementById('qr');
// const refresh = document.getElementById('refresh');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
browser.set_html_size();
let qrImg = new QRCode(qr, {
  text: pageData.payUrl,
  width: 180,
  height: 180,
  colorDark: "#000000",
  colorLight: "#ffffff",
  correctLevel: QRCode.CorrectLevel.H
});

function getQueryString(name) {
  let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  let r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]);
  return null;
}

// 区别通道
let channel = {
  101: 'wx',
  102: 'zfb',
  201: 'wx',
  202: 'zfb',
  1001: 'wx',
  1002: 'zfb'
};

let wxLogo = document.getElementById('wxLogo');
let wxText = document.getElementById('wxText');
let zfbLogo = document.getElementById('zfbLogo');
let zfbText = document.getElementById('zfbText');

if (channel[getQueryString('payChannel')] == 'wx') {
  wxLogo.style.display = 'block';
  wxText.style.display = 'block';
} else if (channel[getQueryString('payChannel')] == 'zfb') {
  zfbLogo.style.display = 'block';
  zfbText.style.display = 'block';
}

// refresh.addEventListener('click', function () {
//   http.post('/trade/dcReceipt', {
//     totalFee: pageData.amount,
//     payChannel: getQueryString('payChannel')
//   }, function (data) {
//     qrImg.makeCode(data.payUrl);
//     message.prompt_show('刷新成功');
//   })
// });