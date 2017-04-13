/**
 * Created by administrator on 2016/12/6.
 */

// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// 引入http message
const http = _require('http');
const message = _require('message');
const Keyboard = _require('keyboard');
new Keyboard({
  spanId: 'key-span',
  inputId: 'key-input',
  keyboardId: 'keyboard'
});

let know = document.getElementById('know');
let layer = document.getElementById('layer');
let layer_x = document.getElementById('layer-x');
let submit = document.getElementById('submit');

know.addEventListener('click', function () {
  layer.style.display = 'block';
});

layer_x.addEventListener('click', function () {
  layer.style.display = 'none';
});

submit.addEventListener('click', function () {
  layer.style.display = 'none';
});

let know_channel = document.getElementById('know-channel');
let layer_channel = document.getElementById('layer-channel');
let layer_x_channel = document.getElementById('layer-x-channel');
let submit_channel = document.getElementById('submit-channel');

know_channel.addEventListener('click', function () {
  layer_channel.style.display = 'block';
});

layer_x_channel.addEventListener('click', function () {
  layer_channel.style.display = 'none';
});

submit_channel.addEventListener('click', function () {
  layer_channel.style.display = 'none';
});

// 多通道获取
let channelBox = document.getElementById('channelBox');
let channel = document.getElementById('channel');
const checkBusinessRegistration = (code, amount) => {
  return new Promise(function (resolve, reject) {
    message.load_show('正在支付');
    http.post('/wx/checkMerchantInfo1', {
      amount: amount,
      channelTypeSign: code
    }, function () {
      message.load_hide();
      resolve(true);
    });
  });
};
http.post('/channel/list', {}, function (data) {
  for (let i = 0; i < data.length; i++) {
    let group = document.createElement('div');
    group.className = 'channel-group';
    group.onclick = function () {
      let amount = channelBox.getAttribute('payAmount');
      if (amount > 0) {
        checkBusinessRegistration(data[i].channelSign, amount).then(function (check) {
          if (check) {
            message.load_show('正在支付');
            switch (data[i].payMethod) {
              case '快捷':
                http.post('/trade/unionPayRoute', {  // /wx/receipt
                  totalFee: amount,
                  payChannel: data[i].channelSign
                }, function (data) {
                  message.load_hide();
                  window.location.replace(data.url);
                });
                break;
              default:
                http.post('/trade/dcReceipt', {  // /wx/receipt
                  totalFee: amount,
                  payChannel: data[i].channelSign
                }, function (data) {
                  message.load_hide();
                  window.location.replace("/sqb/charge?qrCode=" + encodeURIComponent(data.payUrl) + "&name=" + data.subMerName + "&money=" + data.amount + "&payChannel=" + data[i].channelSign);
                });
                break;
            }
          } else {
            message.load_hide();
          }
        });
      } else {
        message.prompt_show('请输入正确的收款金额');
      }
    };
    let name = document.createElement('div');
    name.className = 'channel-con name big';
    name.innerHTML = data[i].channelName;
    let time = document.createElement('div');
    time.className = 'channel-con';
    time.innerHTML = data[i].settleType + '到账';
    let fee = document.createElement('div');
    fee.className = 'channel-con small';
    fee.innerHTML = (data[i].channelRate * 100).toFixed(2) + '%';
    let amount = document.createElement('div');
    amount.className = 'channel-con right';
    amount.innerHTML = data[i].limitAmount + '元';
    group.appendChild(name);
    group.appendChild(time);
    group.appendChild(fee);
    group.appendChild(amount);
    channel.appendChild(group);
  }
});