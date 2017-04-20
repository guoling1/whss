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
let bankCode = '';
let bankCodeBtn = document.getElementById('bankCode');
let expireDate = document.getElementById('expireDate');
let cvv2 = document.getElementById('cvv2');
let mobile = document.getElementById('mobile');
let code = document.getElementById('code');
let orderId = '';
let supportBankCardList = {};
let support = false;
// 是否展示 有效期选择 cvv2填写
let showExpireDate = document.getElementById('showExpireDate');
let showCvv = document.getElementById('showCvv');
if (pageData.showExpireDate == 1) {
  showExpireDate.style.display = 'block';
}
if (pageData.showCvv == 1) {
  showCvv.style.display = 'block';
}
// 定义信用卡号校验
bankCodeBtn.addEventListener('click', function () {
  if (!bankName || bankName == '') {
    message.prompt_show('请先选择所属银行');
  }
});
bankCodeBtn.addEventListener('change', function (e) {
  let ev = e.target;
  if (ev.value.length >= 13 && ev.value.length <= 24 && test(ev.value)) {
    http.post('/merchantInfo/selectBankNameByBankNo', {
      bankNo: ev.value
    }, function (data) {
      if (!data || data == '') {
        support = false;
        message.prompt_show('暂不支持使用储蓄卡');
      } else {
        let allow = false;
        for (let i = 0; i < supportBankCardList.length; i++) {
          if (data == supportBankCardList[i].bankCode && supportBankCardList[i].status == 1) {
            allow = true;
            support = true;
            if (supportBankCardList[i].bankName != bankName) {
              bankName = supportBankCardList[i].bankName;
              bankCode = supportBankCardList[i].bankCode;
              chooseBank.value = supportBankCardList[i].bankName;
            }
          }
        }
        if (!allow) {
          support = false;
          message.prompt_show('暂时不支持该银行的卡');
        }
      }
    })
  } else {
    message.prompt_show('请输入正确的信用卡号');
  }
});
// 定义支付
submit.addEventListener('click', function () {
  if (!support) {
    message.prompt_show('请输入正确的信用卡号');
  } else if ((pageData.showExpireDate == 0 || validate.empty(expireDate.value, '信用卡有效期')) &&
    (pageData.showCvv == 0 || validate.empty(cvv2.value, 'CVV2')) &&
    validate.phone(mobile.value) &&
    validate.empty(code.value, '验证码')) {
    if ((pageData.showCvv == 0 || cvv2.value.length == 3)) {
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
});
// 定义验证码
sendCode.addEventListener('click', function () {
  if (countdown.check()) {
    if (!support) {
      message.prompt_show('请输入正确的信用卡号');
    } else if ((pageData.showExpireDate == 0 || validate.empty(expireDate.value, '信用卡有效期')) &&
      (pageData.showCvv == 0 || validate.empty(cvv2.value, 'CVV2')) &&
      validate.phone(mobile.value)) {
      if ((pageData.showCvv == 0 || cvv2.value.length == 3)) {
        message.load_show('正在发送');
        let expire = expireDate.value.split('/');
        http.post('/trade/firstUnionPay', {
          amount: amount,
          channel: channel,
          bankCode: bankCode,
          bankCardNo: bankCodeBtn.value,
          expireDate: expire[1] + expire[0],
          cvv2: cvv2.value,
          mobile: mobile.value
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
});

// 获取支持的银行卡列表
let bankNameList = [];
http.post('/channel/queryChannelSupportBank', {
  channelSign: channel
}, function (data) {
  supportBankCardList = data;
  for (let i = 0; i < data.length; i++) {
    let bankNameObject = {};
    bankNameObject.label = '<span style="color: #5a5a5a">' + data[i].bankName + '</span><span style="font-size: 14px;color: #5a5a5a"> (单笔限额' + fmoney(data[i].singleLimitAmount, 2) + '元)</span>';
    bankNameObject.value = data[i].bankName + '/' + data[i].bankCode;
    if (data[i].status == 1) {
      bankNameList.push(bankNameObject);
    }
  }
  chooseBank.addEventListener('click', () => {
    weui.picker(bankNameList, {
      onConfirm: (result) => {
        let format = result[0].split('/');
        chooseBank.value = format[0];
        bankName = format[0];
        bankCode = format[1];
        bankCodeBtn.removeAttribute('readonly');
      },
      id: 'chooseBank'
    });
  });
});