/**
 * Created by yulong.zhang on 2016/12/6.
 */


// 引入http message
const message = _require('message');
const http = _require('http');
// 定义变量
let close = document.getElementById('close');
let dealerDetail = document.getElementById('dealerDetail');
close.addEventListener('click', function () {
  dealerDetail.style.display = 'none';
});

function check(dealerId, distributeCount, activateCount) {
  if (dealerId > 0) {
    http.post('/dealer/getMyDealerDetail', {
      dealerId: dealerId
    }, function (data) {
      var dealerDetailHtml = document.getElementById('dealerDetail');
      document.getElementById('proxyName').innerHTML = data.proxyName;
      document.getElementById('mobile').innerHTML = data.mobile;
      document.getElementById('distributeCount').innerHTML = distributeCount;
      document.getElementById('activateCount').innerHTML = activateCount;
      var qrCodeHtml = document.getElementById('qrcode');
      var qrCodeRecords = data.codeRecords;
      for (var qrcode in qrCodeRecords) {
        var div = document.createElement('div');
        div.className = 'row';
        var p1 = document.createElement('p');
        p1.className = 'w35';
        p1.innerHTML = qrCodeRecords[qrcode].distributeDate;
        div.appendChild(p1);
        var p2 = document.createElement('p');
        p2.className = 'w50 middle';
        p2.innerHTML = qrCodeRecords[qrcode].startCode + '至' + qrCodeRecords[qrcode].endCode;
        div.appendChild(p2);
        var p3 = document.createElement('p');
        p3.innerHTML = qrCodeRecords[qrcode].count;
        div.appendChild(p3);
        qrCodeHtml.appendChild(div);
      }
      dealerDetailHtml.style.display = 'block';
    })
  }
}