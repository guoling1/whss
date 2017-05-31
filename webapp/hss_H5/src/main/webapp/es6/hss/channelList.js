/**
 * Created by administrator on 2016/12/6.
 */

// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// 引入http message
const http = _require('http');
const message = _require('message');


function getQueryString(name) {
  let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  let r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]);
  return null;
}

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
    message.load_show('收款准备中');
    http.post('/wx/checkMerchantInfo1', {
      amount: amount,
      channelTypeSign: code
    }, function () {
      message.load_hide();
      resolve(true);
    });
  });
};
message.load_show('通道获取中');
http.post('/channel/list', {}, function (list) {
  message.load_hide();
  for (let i = 0; i < list.length; i++) {
    let group = document.createElement('div');
    group.className = 'channel-group';
    group.onclick = function () {
      let amount = getQueryString('amount');
      let orderId = getQueryString('id');
      if (amount > 0) {
        checkBusinessRegistration(list[i].channelSign, amount).then(function (check) {
          if (check) {
            message.load_show('收款准备中');
            switch (list[i].payMethod) {
              case '快捷':
                http.post('/trade/unionPayRoute', {  // /wx/receipt
                  orderId: orderId,
                  payChannel: list[i].channelSign
                }, function (data) {
                  message.load_hide();
                  window.location.replace(data.url);
                });
                break;
              default:
                http.post('/trade/dcReceipt', {  // /wx/receipt
                  orderId: orderId,
                  payChannel: list[i].channelSign
                }, function (data) {
                  message.load_hide();
                  window.location.replace("/sqb/charge?qrCode=" + encodeURIComponent(data.payUrl) + "&name=" + data.subMerName + "&money=" + data.amount + "&payChannel=" + list[i].channelSign);
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
    name.innerHTML = list[i].channelName;
    let time = document.createElement('div');
    time.className = 'channel-con small';
    time.innerHTML = list[i].settleType;
    let fee = document.createElement('div');
    // 是否显示推荐
    if (list[i].recommend == 1) {
      // 展示  推荐
      let recommend = document.createElement('span');
      recommend.className = 'recommend';
      name.appendChild(recommend);
    }
    // 5月活动
    if (list[i].channelSign == 601) {
      let now = new Date().getTime();
      // 1492617600000 1492790400000
      if (now >= 1492790400000 && now <= 1496246399000) {
        fee.className = 'channel-con preferential';
        fee.innerHTML = '0.38%+3';
        // 展示 惠
        let hui = document.createElement('span');
        hui.className = 'preferential';
        name.appendChild(hui);
      } else {
        fee.className = 'channel-con';
        fee.innerHTML = (list[i].channelRate * 100).toFixed(2) + '%' + '+' + (list[i].fee / 1).toFixed(0);
      }
    } else {
      fee.className = 'channel-con';
      fee.innerHTML = (list[i].channelRate * 100).toFixed(2) + '%' + '+' + (list[i].fee / 1).toFixed(0);
    }
    let amount = document.createElement('div');
    amount.className = 'channel-con right';
    amount.innerHTML = list[i].limitAmount + '元';
    group.appendChild(name);
    group.appendChild(time);
    group.appendChild(fee);
    group.appendChild(amount);
    channel.appendChild(group);
  }
});