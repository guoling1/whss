/**
 * Created by administrator on 2016/12/9.
 */

// 引入http message
const message = _require('message');
const http = _require('http');
// 定义变量
const qr = document.getElementById('qr');
const refresh = document.getElementById('refresh');

let qrImg = new QRCode(qr, {
  text: pageData.payUrl,
  width: 180,
  height: 180,
  colorDark: "#000000",
  colorLight: "#ffffff",
  correctLevel: QRCode.CorrectLevel.H
});

refresh.addEventListener('click', function () {
  http.post('/wx/receipt', {
    totalFee: pageData.amount,
    payChannel: '101'
  }, function (data) {
    qrImg.makeCode(data.payUrl);
    message.prompt_show('刷新成功');
  })
});