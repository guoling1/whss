/**
 * Created by administrator on 2016/12/8.
 */

// 定义变量
const color = document.getElementById('color');
const logo = document.getElementById('logo');

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
  logo.className = 'logo ' + pageData.bin;
}