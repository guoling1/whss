/**
 * Created by administrator on 2016/12/8.
 */

// 引入浏览器特性处理
const http = _require('http');
const message = _require('message');
const browser = _require('browser');
browser.elastic_touch();
// 定义变量
const color = document.getElementById('color');
const btn = document.getElementById('btn');
const logo = document.getElementById('logo');

// 是否 支行信息
if (pageData.hasBranch == 0) {
  btn.innerHTML = '补充支行信息';
} else {
  btn.innerHTML = '修改支行信息';
}

// 卡bin和颜色一一对应
let bankCardBin = ['ABC', 'BANKCOMM', 'BOC', 'CCB', 'CEB', 'CGB', 'CIB', 'CITIC', 'CMB', 'CMBC', 'ICBC', 'PINGAN', 'PSBC', 'SHBANK'];
let bankLogoColor = ['green', 'blue', 'red', 'blue', 'red', 'red', 'blue', 'red', 'red', 'red', 'red', 'red', 'green', 'red'];

let HasBank = () => {
  for (let i = 0; i < bankCardBin.length; i++) {
    if (bankCardBin[i] == pageData.bin) {
      return i;
    }
  }
  return -1;
};

let index = HasBank();

if (index != -1) {
  color.className = 'group ' + bankLogoColor[index];
  btn.className = 'btn ' + bankLogoColor[index];
  logo.className = 'logo ' + pageData.bin;
} else {
  color.className = 'group red';
  btn.className = 'btn red';
  logo.className = 'logo DEFAULT';
}

// 获取银行卡列表
http.post('/wx/myCardList', {}, function (data) {
  console.log(data);
  for (let i = 0; i < data.length; i++) {
    let group = document.createElement('div');
    // group top-start
    let top = document.createElement('div');
    let logo = document.createElement('div');
    let info = document.createElement('div');
    // group top-end
    // group bottom-start
    let bottom = document.createElement('div');
    let XP = document.createElement('div');
    let xp = document.createElement('div');
    xp.className = 'bank_x';
    XP.appendChild(xp);
    XP.appendChild(xp);
    XP.appendChild(xp);
    XP.appendChild(xp);
    let p = document.createElement('div');
    let word = document.createElement('div');
    word.innerHTML = '123';
    p.appendChild(word);
    let s = document.createElement('div');
    bottom.appendChild(XP);
    bottom.appendChild(XP);
    bottom.appendChild(XP);
    bottom.appendChild(p);
    bottom.appendChild(s);
    // group bottom-end
    // group small-start
    let small = document.createElement('div');
    small.innerHTML = '该卡用于收款后的自动结算与余额提现';
    // group small-end
    group.appendChild(top);
    group.appendChild(bottom);
    group.appendChild(small);
  }
});