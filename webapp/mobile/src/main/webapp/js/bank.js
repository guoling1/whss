'use strict';

/**
 * Created by administrator on 2016/12/8.
 */

// 引入浏览器特性处理
var browser = _require('browser');
browser.elastic_touch();
// 定义变量
var color = document.getElementById('color');
var logo = document.getElementById('logo');

// 卡bin和颜色一一对应
var bankCardBin = ['ABC', 'BANKCOMM', 'BOC', 'CCB', 'CEB', 'CGB', 'CIB', 'CITIC', 'CMB', 'CMBC', 'ICBC', 'PINGAN', 'PSBC', 'SHBANK'];
var bankLogoColor = ['green', 'blue', 'red', 'blue', 'red', 'red', 'blue', 'red', 'red', 'red', 'red', 'red', 'green', 'red'];

var HasBank = function HasBank() {
  for (var i = 0; i < bankCardBin.length; i++) {
    if (bankCardBin[i] == pageData.bin) {
      return i;
    }
  }
  return -1;
};

var index = HasBank();

if (index != -1) {
  color.className = 'group ' + bankLogoColor[index];
  logo.className = 'logo ' + pageData.bin;
} else {
  color.className = 'group red';
  logo.className = 'logo DEFAULT';
}
//# sourceMappingURL=bank.js.map
