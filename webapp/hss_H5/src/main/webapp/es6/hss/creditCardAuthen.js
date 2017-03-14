/**
 * Created by administrator on 2016/12/8.
 */

// 引入浏览器特性处理
const http = _require('http');
const message = _require('message');
const browser = _require('browser');
browser.elastic_touch();

function getQueryString(name) {
  let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  let r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]);
  return null;
}

// 定义变量
let ipt = document.getElementById('ipt');
let submit = document.getElementById('submit');

let layer = document.getElementById('layer');
let cancel = document.getElementById('cancel');

cancel.addEventListener('click', function () {
  window.location.href = '/sqb/wallet';
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

submit.addEventListener('click', function () {
  if (ipt.value.length >= 13 && ipt.value.length <= 24 && test(ipt.value)) {
    http.post('/merchantInfo/queryBank', {bankNo: ipt.value}, function (data) {
      if (data != 1) {
        message.prompt_show('请输入正确的信用卡号');
      } else {
        http.post('/wx/creditCardAuthen', {
          creditCard: ipt.value
        }, function () {
          if (getQueryString('card')) {
            layer.style.display = 'block';
          } else {
            window.location.href = '/sqb/authentication';
          }
        })
      }
    })
  } else {
    message.prompt_show('请输入正确的信用卡号');
  }
});
