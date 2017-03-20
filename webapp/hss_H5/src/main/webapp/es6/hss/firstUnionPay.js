/**
 * Created by administrator on 2016/12/6.
 */

// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// 引入动画模版 处理验证码
const AnimationCountdown = _require('art-countdown');
let countdown = new AnimationCountdown('sendCode', '重新获取');
// 时间选择器
const DatePicker = _require('datePicker');
new DatePicker('expireDate');
// 引入http message
const http = _require('http');
const validate = _require('validate');
const message = _require('message');

function fmoney(s, n) {
  n = n > 0 && n <= 20 ? n : 2;
  s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
  let l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
  let t = "";
  for (let i = 0; i < l.length; i++) {
    t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
  }
  return t.split("").reverse().join("") + "." + r;
}

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
let sendCode = document.getElementById('sendCode');
let submit = document.getElementById('submit');

let check_cvv = document.getElementById('check_cvv');
let example_cvv = document.getElementById('example_cvv');
let cancel_cvv = document.getElementById('cancel_cvv');

check_cvv.addEventListener('click', function () {
  example_cvv.style.display = 'block';
});

cancel_cvv.addEventListener('click', function () {
  example_cvv.style.display = 'none';
});

let check_validity = document.getElementById('check_validity');
let example_validity = document.getElementById('example_validity');
let cancel_validity = document.getElementById('cancel_validity');

check_validity.addEventListener('click', function () {
  example_validity.style.display = 'block';
});

cancel_validity.addEventListener('click', function () {
  example_validity.style.display = 'none';
});

// 定义信用卡号 正确性校验
let test = function (cardNo) {
  cardNo = cardNo || '';
  let cache = [], cardNoArr = cardNo.split(''),
    temp, cardNoTemp, cardNoArrLen = cardNoArr.length,
    sum = 0, cacheLen;
  /*信用卡校验规则：
   *1、从倒数第二位开始，每相隔一位*2，如果乘积为两位数则数字相加
   *2、得到的乘积与1中未处理的数字相加（除了最后一位），取和向上最近的以0结尾的数字
   *3、用2得到的数字减去2中相加的和，得到的数字等于信用卡最后一位
   */
  if (cardNoArrLen < 1) {
    return false;
  }
  let lastnumber = cardNoArr[cardNoArrLen - 1];
  let cacuIndex = cardNoArrLen - 2;
  let curIndex = cardNoArrLen - 1;
  while (curIndex--) {
    let mod = cacuIndex - curIndex;
    if (mod % 2) {
      cache.push(parseInt(cardNoArr[curIndex]));
    } else {
      cardNoTemp = cardNoArr[curIndex] * 2;

      if (cardNoTemp.toString().length == 1) {
        cache.push(cardNoArr[curIndex] * 2);
      } else {
        temp = cardNoTemp.toString().split('');
        cache.push(parseInt(temp[0], 10) + parseInt(temp[1], 10));
      }
    }
  }
  cacheLen = cache.length;
  while (cacheLen--) {
    sum += cache[cacheLen];
  }
  let ceil = Math.ceil(sum / 10);
  return (ceil * 10 - sum) == lastnumber;
};

let amount = getQueryString('amount');
let channel = getQueryString('channel');
let bankName = '';
let bankCode = document.getElementById('bankCode');
let expireDate = document.getElementById('expireDate');
let cvv2 = document.getElementById('cvv2');
let mobile = document.getElementById('mobile');
let code = document.getElementById('code');
let orderId = '';
// 定义信用卡号校验
bankCode.addEventListener('change', function (e) {
  let ev = e.target;
  console.log(ev.value);
});
// 定义支付
submit.addEventListener('click', function () {
  http.post('/trade/confirmUnionPay', {
    orderId: orderId,
    code: code.value,
  }, function () {
    window.location.replace('/trade/unionPaySuccess/' + orderId);
  })
});
// 定义验证码
sendCode.addEventListener('click', function () {
  if (countdown.check()) {
    let expire = expireDate.value.split('/');
    http.post('/trade/firstUnionPay', {
      amount: amount,
      channel: channel,
      bankName: bankName,
      bankCardNo: bankCode.value,
      expireDate: expire[0] + expire[1],
      cvv2: cvv2.value,
      mobile: mobile.value
    }, function (data) {
      orderId = data.orderId;
      message.prompt_show('验证码发送成功');
      countdown.submit_start();
    })
  }
});

chooseBank.addEventListener('click', function () {
  layer.style.display = 'block';
});

layer_x.addEventListener('click', function () {
  layer.style.display = 'none';
});

// 获取支持的银行卡列表
http.post('/channel/queryChannelSupportBank', {
  channelSign: '301'
}, function (data) {
  for (let i = 0; i < data.length; i++) {
    let list = document.createElement('div');
    list.className = 'choose-box-body-list-bank';
    list.addEventListener('click', function () {
      bankName = data[i].bankName;
      chooseBank.value = data[i].bankName;
      layer.style.display = 'none';
    });
    let logo = document.createElement('div');
    if (data[i].status == 1) {
      logo.className = 'logo ' + data[i].bankCode;
    } else {
      logo.className = 'logo ' + data[i].bankCode + '_NO';
    }
    let detail = document.createElement('div');
    detail.className = 'detail';
    let name = document.createElement('div');
    if (data[i].status == 1) {
      name.className = 'name';
    } else {
      name.className = 'name NO';
    }
    name.innerHTML = data[i].bankName;
    let small = document.createElement('div');
    if (data[i].status == 1) {
      small.className = 'small';
    } else {
      small.className = 'small NO';
    }
    small.innerHTML = '单日限额 ' + fmoney(data[i].dayLimitAmount, 2) + '元';
    detail.appendChild(name);
    detail.appendChild(small);
    list.appendChild(logo);
    list.appendChild(detail);
    layer_body.appendChild(list);
  }
});