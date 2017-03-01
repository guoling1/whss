/**
 * Created by administrator on 2016/12/8.
 */

// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// 定义变量
const color = document.getElementById('color');
const btn = document.getElementById('btn');
const logo = document.getElementById('logo');

// 是否 支行信息
if (pageData.hasBranch == 2) {
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