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

let wxLogo = document.getElementById('wxLogo');
let wxText = document.getElementById('wxText');
let zfbLogo = document.getElementById('zfbLogo');
let zfbText = document.getElementById('zfbText');

if (getQueryString('payType') == 1) {
  wxLogo.style.display = 'inline-block';
  wxText.style.display = 'inline-block';
} else if (getQueryString('payType') == 2) {
  zfbLogo.style.display = 'inline-block';
  zfbText.style.display = 'inline-block';
} else {
  wxLogo.style.display = 'inline-block';
  wxText.style.display = 'inline-block';
  zfbLogo.style.display = 'inline-block';
  zfbText.style.display = 'inline-block';
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