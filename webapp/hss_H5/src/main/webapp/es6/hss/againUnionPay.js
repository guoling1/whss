/**
 * Created by administrator on 2016/12/6.
 */

// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// 引入动画模版 处理验证码
const AnimationCountdown = _require('art-countdown');
let countdown = new AnimationCountdown('sendCode', '重新获取');
// 引入http message
const http = _require('http');
const validate = _require('validate');
const message = _require('message');

function getQueryString(name) {
  let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  let r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]);
  return null;
}

// 定义变量
let layer = document.getElementById('layer');
let layer_body = document.getElementById('layer-body');
let layer_x = document.getElementById('layer-x');
let chooseBank = document.getElementById('chooseBank');
let bank = document.getElementById('bank');
let mobile = document.getElementById('mobile');
let sendCode = document.getElementById('sendCode');
let submit = document.getElementById('submit');
let addNew = document.getElementById('addNew');

layer_x.addEventListener('click', function () {
  layer.style.display = 'none';
});

let check_cvv = document.getElementById('check_cvv');
let example_cvv = document.getElementById('example_cvv');
let cancel_cvv = document.getElementById('cancel_cvv');

check_cvv.addEventListener('click', function () {
  example_cvv.style.display = 'block';
});

cancel_cvv.addEventListener('click', function () {
  example_cvv.style.display = 'none';
});

let amount = getQueryString('amount');
let channel = getQueryString('channel');
let cvv2 = document.getElementById('cvv2');
let code = document.getElementById('code');
let orderId = '';
// 选择支付卡
chooseBank.addEventListener('click', function () {
  layer.style.display = 'block';
});
// 定义添加新卡
addNew.addEventListener('click', function () {
  window.location.replace('/trade/firstUnionPayPage?amount=' + amount + '&channel=' + channel);
});
// 是否可支付 或者发送验证码
if (pageData.status == 1) {
  pageData.canPay = true;
}
// 定义支付
submit.addEventListener('click', function () {
  if (pageData.canPay) {
    if (validate.empty(cvv2.value, 'CVV2') &&
      validate.empty(code.value, '验证码')) {
      if (cvv2.value.length == 3) {
        message.load_show('正在支付');
        http.post('/trade/confirmUnionPay', {
          orderId: orderId,
          code: code.value,
        }, function () {
          message.load_hide();
          window.location.replace('/trade/unionPaySuccess/' + orderId);
        })
      } else {
        message.prompt_show('请输入正确的CVV2');
      }
    }
  } else {
    message.prompt_show('请选择可用的银行卡');
  }
});
// 定义验证码
sendCode.addEventListener('click', function () {
  if (pageData.canPay) {
    if (countdown.check()) {
      if (validate.empty(cvv2.value, 'CVV2')) {
        if (cvv2.value.length == 3) {
          message.load_show('正在发送');
          http.post('/trade/againUnionPay', {
            amount: amount,
            channel: channel,
            creditCardId: pageData.creditCardId,
            cvv2: cvv2.value
          }, function (data) {
            orderId = data.orderId;
            message.load_hide();
            message.prompt_show('验证码发送成功');
            countdown.submit_start();
          })
        } else {
          message.prompt_show('请输入正确的CVV2');
        }
      }
    }
  } else {
    message.prompt_show('请选择可用的银行卡');
  }
});

// 获取支持的银行卡列表
http.post('/bankcard/list', {
  creditCardId: pageData.creditCardId,
  channel: channel
}, function (data) {
  for (let i = 0; i < data.length; i++) {
    let list = document.createElement('div');
    if (pageData.creditCardId == data[i].creditCardId) {
      list.className = 'choose-box-body-list-bank active';
    } else {
      list.className = 'choose-box-body-list-bank';
    }
    if (data[i].status == 1) {
      list.addEventListener('click', function () {
        let className = document.getElementsByClassName('choose-box-body-list-bank');
        for (let i = 0; i < className.length; i++) {
          className[i].className = 'choose-box-body-list-bank';
        }
        this.className = 'choose-box-body-list-bank active';
        bank.className = 'val';
        bank.innerHTML = data[i].bankName + ' 尾号' + data[i].shortNo;
        mobile.innerHTML = data[i].mobile;
        pageData.creditCardId = data[i].creditCardId;
        pageData.canPay = true;
        layer.style.display = 'none';
      });
    }
    let logo = document.createElement('div');
    if (data[i].status == 0) {
      logo.className = 'logo ' + data[i].bankCode + '_NO';
    } else {
      logo.className = 'logo ' + data[i].bankCode;
    }
    let info = document.createElement('div');
    if (data[i].status == 0) {
      info.className = 'info NO';
      info.innerHTML = data[i].bankName + ' (' + data[i].shortNo + ')' + ' <span>信用卡 (暂不可用)</span>';
    } else {
      info.className = 'info';
      info.innerHTML = data[i].bankName + ' (' + data[i].shortNo + ')' + ' <span>信用卡</span>';
    }
    list.appendChild(logo);
    list.appendChild(info);
    layer_body.appendChild(list);
  }
});
