'use strict';

/**
 * Created by administrator on 2016/12/9.
 */

// 引入http message
var message = _require('message');
var http = _require('http');
// 定义变量
var qr = document.getElementById('qr');
var refresh = document.getElementById('refresh');

var qrImg = new QRCode(qr, {
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
  });
});
//# sourceMappingURL=charge.js.map
